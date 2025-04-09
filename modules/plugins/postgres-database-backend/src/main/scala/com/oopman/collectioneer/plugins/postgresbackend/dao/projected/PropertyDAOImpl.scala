package com.oopman.collectioneer.plugins.postgresbackend.dao.projected

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.scalikejdbc.entity.Utils
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.projected.ScalikePropertyDAO
import com.oopman.collectioneer.db.traits.entity.projected.Property as ProjectedProperty
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollectionRelationshipType
import com.oopman.collectioneer.db.traits.entity.{raw, projected}
import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

import java.util.UUID

object PropertyDAOImpl extends ScalikePropertyDAO:
  private def performCreateOrUpdatePropertiesOperation
  (properties: Seq[ProjectedProperty],
   performCreateOrUpdateProperties: Seq[raw.Property] => Seq[Int],
   performCreateOrUpdateCollections: Seq[raw.Collection] => Seq[Int],
   performCreateOrUpdatePropertyCollections: Seq[raw.PropertyCollection] => Seq[Int])
  (implicit session: DBSession = AutoSession): Seq[Int] =
    val relatedProperties = ProjectedProperty.deduplicateProperties(properties.flatMap(_.propertyValues.keys))
    val existingRelatedProperties = postgresbackend.dao.raw.PropertyDAOImpl.getAllMatchingPKs(relatedProperties.map(_.pk))
    val relatedPropertiesToCreate = relatedProperties.diff(existingRelatedProperties)
    val distinctProperties = properties.distinct
    val collectionsAndPropertyCollections = for (property <- distinctProperties) yield (
      entity.raw.Collection(
        pk = property.pk,
        virtual = true,
        deleted = property.deleted,
        created = property.created,
        modified = property.modified
      ),
      entity.raw.PropertyCollection(
        propertyPK = property.pk,
        collectionPK = property.pk,
        propertyCollectionRelationshipType = PropertyCollectionRelationshipType.CollectionOfPropertiesOfProperty
      )
    )
    val (collections, propertyCollectionsA) = collectionsAndPropertyCollections.unzip
    val propertyValuesAndPropertyCollections = for {
      parentProperty <- properties
      ((property, propertyValue), index) <- parentProperty.propertyValues.zipWithIndex
    } yield (
      (property.pk, parentProperty.pk, propertyValue),
      entity.raw.PropertyCollection(
        propertyPK = property.pk,
        collectionPK = parentProperty.pk,
        index = index
      )
    )
    val (propertyValues, propertyCollectionsB) = propertyValuesAndPropertyCollections.unzip
    performCreateOrUpdateProperties(relatedPropertiesToCreate)
    performCreateOrUpdateProperties(properties)
    performCreateOrUpdateCollections(collections)
    performCreateOrUpdatePropertyCollections(propertyCollectionsA ++ propertyCollectionsB)
    postgresbackend.dao.projected.PropertyValueDAOImpl.updatePropertyValues(propertyValues)
    Nil

  def createProperties(properties: Seq[ProjectedProperty])(implicit session: DBSession = AutoSession): Seq[Int] =
    performCreateOrUpdatePropertiesOperation(
      properties,
      postgresbackend.dao.raw.PropertyDAOImpl.createProperties,
      postgresbackend.dao.raw.CollectionDAOImpl.createCollections,
      postgresbackend.dao.raw.PropertyCollectionDAOImpl.createPropertyCollections,
    )

  /**
   * Creates/Updates Projected Properties along with all nested Properties and PropertyValues
   *
   * 1. Collect distinct Properties in a recursive fashion
   * 2. Create Properties
   * 3. Create Collections for each Property
   * 4. Create PropertyValues associated with each Property/Collection combination
   * 5. Create PropertyCollections for each PropertyValue
   *
   * @param properties
   * @param session
   * @return
   */
  def createOrUpdateProperties(properties: Seq[ProjectedProperty])(implicit session: DBSession = AutoSession): Seq[Int] =
    performCreateOrUpdatePropertiesOperation(
      properties,
      postgresbackend.dao.raw.PropertyDAOImpl.createOrUpdateProperties,
      postgresbackend.dao.raw.CollectionDAOImpl.createOrUpdateCollections,
      postgresbackend.dao.raw.PropertyCollectionDAOImpl.createOrUpdatePropertyCollections,
    )

  def getAll(implicit session: DBSession = AutoSession): Seq[ProjectedProperty] =
    val propertyPKs = postgresbackend.queries.raw.PropertyQueries
      .allPKs
      .map(rs => UUID.fromString(rs.string("pk")))
      .list
      .apply()
    getAllMatchingPKs(propertyPKs)

  def getAllMatchingPKs(propertyPKs: Seq[UUID])(implicit session: DBSession = AutoSession): Seq[ProjectedProperty] =
    val properties = postgresbackend.dao.raw.PropertyDAOImpl.getAllMatchingPKs(propertyPKs)
    inflateRawProperties(properties)

  def getAllMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession): Seq[ProjectedProperty] =
    val result = postgresbackend.PropertyValueQueryDSLSupport
      .comparisonsToSQL(comparisons)
      .map { (comparisonSQL, parameters) =>
        val propertyPKs = postgresbackend.queries.raw.PropertyQueries
          .innerJoiningPropertyCollection(s"($comparisonSQL)", "collection_pk", PropertyCollectionRelationshipType.CollectionOfPropertiesOfProperty, selectColumnExpression = "p.pk")
          .bind(parameters*)
          .map(rs => UUID.fromString(rs.string("pk")))
          .list
          .apply()
        getAllMatchingPKs(propertyPKs)
      }
    result.getOrElse(getAll)

  // TODO: This has an issue: It doesn't recursively obtain the Properties for each Collection
  def getAllByPropertyCollection
  (collectionPKs: Seq[UUID], propertyPKs: Seq[UUID], propertyCollectionRelationshipTypes: Seq[PropertyCollectionRelationshipType])
  (implicit session: DBSession = AutoSession): Map[UUID, Seq[ProjectedProperty]] =
    val bindings = Seq(
      session.connection.createArrayOf("varchar", collectionPKs.toArray),
      session.connection.createArrayOf("varchar", propertyCollectionRelationshipTypes.map(_.toString).toArray)
    ) ++ (
      if propertyPKs.nonEmpty
      then Seq(session.connection.createArrayOf("varchar", propertyPKs.toArray))
      else Nil
    )
    val collectionPKsByPropertyPK = postgresbackend.queries.raw.PropertyQueries
      .allByPropertyCollection("p.pk", includePropertiesFilter = propertyPKs.nonEmpty)
      .bind(bindings*)
      .map(rs => (
        UUID.fromString(rs.string("pk")),
        Utils.resultSetArrayToListOf[UUID](rs, "collection_pks")
      ))
      .list
      .apply()
      .toMap
    val properties = getAllMatchingPKs(collectionPKsByPropertyPK.keys.toSeq)
    val collectionPKPropertyPairs = for {
      property <- properties
      collectionPK <- collectionPKsByPropertyPK.getOrElse(property.pk, Nil)
    } yield (collectionPK, property)
    val result = for {
      (collectionPK, collectionPKPropertyPairsForCollection) <- collectionPKPropertyPairs.groupBy(_._1)
    } yield (collectionPK, collectionPKPropertyPairsForCollection.map(_._2))
    result
    
  def getAllRelatedByPropertyCollection
  (collectionPKs: Seq[UUID], propertyPKs: Seq[UUID], propertyCollectionRelationshipTypes: Seq[PropertyCollectionRelationshipType])
  (implicit session: DBSession = AutoSession): Map[UUID, Seq[(Boolean, ProjectedProperty)]] =
    val bindings = Seq(
      session.connection.createArrayOf("varchar", collectionPKs.toArray),
      session.connection.createArrayOf("varchar", propertyCollectionRelationshipTypes.map(_.toString).toArray)
    ) ++ (
      if propertyPKs.nonEmpty
      then Seq(session.connection.createArrayOf("varchar", propertyPKs.toArray))
      else Nil
    )
    val initialData = postgresbackend.queries.raw.PropertyQueries
      .allByRelatedPropertyCollection("p.pk", includePropertiesFilter = propertyPKs.nonEmpty)
      .bind(bindings*)
      .map(rs => (
        UUID.fromString(rs.string("top_level_collection_pk")),
        UUID.fromString(rs.string("pk")),
        rs.boolean("is_inherited")
      ))
      .list
      .apply()
    val retrievedPropertyPKs = initialData.map(_._2).distinct
    val properties = getAllMatchingPKs(retrievedPropertyPKs).map(p => (p.pk, p)).toMap
    val result = for {
      (collection_pk, property_pk, is_inherited) <- initialData
      property <- properties.get(property_pk)
    } yield (collection_pk, (is_inherited, property))
    for {
      (collection_pk, groupedTuples) <- result.groupBy(_._1)
    } yield (collection_pk, groupedTuples.map(_._2))

  /**
   * Inflates a Sequence of raw Property objects. This is done by retrieving all PropertyValue data for the provided
   * Property objects and then executing an additional load of raw Property objects included in the PropertyValue data
   *
   * @param properties
   * @param session
   * @return
   */
  def inflateRawProperties(properties: Seq[raw.Property])(implicit session: DBSession): Seq[ProjectedProperty] =
    val propertyPKs = properties.map(_.pk)
    val propertyValueDataList = postgresbackend.queries.projected.PropertyValueQueries
      .propertyValuesByParentPropertyPKs()
      .bind(session.connection.createArrayOf("varchar", propertyPKs.toArray))
      .map { rs =>
        (
          UUID.fromString(rs.string("parent_property_pk")),
          UUID.fromString(rs.string("child_property_pk")),
          postgresbackend.entity.projected.PropertyValue.generatePropertyValueData(rs)
        )
      }
      .list
      .apply()
    val childPropertyUUIDs = propertyValueDataList
      .map((_, childPropertyPK, _) => childPropertyPK)
      .diff(propertyPKs)
      .distinct
    val childProperties = postgresbackend.dao.raw.PropertyDAOImpl.getAllMatchingPKs(childPropertyUUIDs)
    val allProperties = properties ++ childProperties
    val propertiesMap = allProperties.map(p => p.pk -> p).toMap

    val propertyPKtoPropertyValuesMap =
      for
        (parentPropertyPK, propertyValueDataList) <- propertyValueDataList.groupBy(_._1)
        parentProperty <- propertiesMap.get(parentPropertyPK)
      yield
        val propertyValues =
          for
            (_, childPropertyPK, propertyValue) <- propertyValueDataList
            childProperty <- propertiesMap.get(childPropertyPK)
          yield childProperty -> propertyValue
        parentPropertyPK -> propertyValues.toMap
    for
      property <- properties
    yield entity.projected.Property(
      pk = property.pk,
      propertyName = property.propertyName,
      propertyTypes = property.propertyTypes,
      deleted = property.deleted,
      created = property.created,
      modified = property.modified,
      propertyValues = propertyPKtoPropertyValuesMap.getOrElse(property.pk, Map.empty)
    )
