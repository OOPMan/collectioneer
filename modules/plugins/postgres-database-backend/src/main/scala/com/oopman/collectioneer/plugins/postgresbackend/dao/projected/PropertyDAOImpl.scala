package com.oopman.collectioneer.plugins.postgresbackend.dao.projected

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.scalikejdbc.entity.Utils
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.projected.ScalikePropertyDAO
import com.oopman.collectioneer.db.traits.entity.projected.Property as ProjectedProperty
import com.oopman.collectioneer.db.traits.entity.raw.{Collection, Property, PropertyCollection, PropertyCollectionRelationshipType}
import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.plugins.postgresbackend
import com.oopman.collectioneer.plugins.postgresbackend.entity.projected.PropertyValue.generatePropertyValueData
import scalikejdbc.*

import java.util.UUID

object PropertyDAOImpl extends ScalikePropertyDAO:
  private def performCreateOrUpdatePropertiesOperation
  (properties: Seq[ProjectedProperty],
   performCreateOrUpdateProperties: Seq[Property] => Array[Int],
   performCreateOrUpdateCollections: Seq[Collection] => Array[Int],
   performCreateOrUpdatePropertyCollections: Seq[PropertyCollection] => Array[Int])
  (implicit session: DBSession = AutoSession): Array[Int] =
    val distinctProperties = ProjectedProperty.collectProperties(properties)
    val collections = distinctProperties.map(p => entity.raw.Collection(pk = p.pk))
    val propertyValues = distinctProperties.flatMap(property => property.propertyValues.map {
      case propertyValue: entity.projected.PropertyValue => propertyValue.copy(collection = propertyValue.collection.copy(pk = property.pk))
    })
    // TODO: Find a way to link Collections to root? May not be desirable actually?
    val propertyCollectionsA = distinctProperties.map(p => entity.raw.PropertyCollection(
      propertyPK = p.pk,
      collectionPK = p.pk,
      propertyCollectionRelationshipType = PropertyCollectionRelationshipType.CollectionOfPropertiesOfProperty
    ))
    val propertyCollectionsB = propertyValues
      .groupBy(_.collection.pk)
      .flatMap((_, propertyValues) => propertyValues.zipWithIndex.map((propertyValue, index) => entity.raw.PropertyCollection(
        propertyPK = propertyValue.property.pk,
        collectionPK = propertyValue.collection.pk,
        index = index
      )))
      .toSeq
    performCreateOrUpdateProperties(distinctProperties)
    performCreateOrUpdateCollections(collections)
    performCreateOrUpdatePropertyCollections(propertyCollectionsA ++ propertyCollectionsB)
    postgresbackend.dao.projected.PropertyValueDAOImpl.updatePropertyValues(propertyValues)
    Array.empty

  def createProperties(properties: Seq[ProjectedProperty])(implicit session: DBSession = AutoSession) =
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
  def createOrUpdateProperties(properties: Seq[ProjectedProperty])(implicit session: DBSession = AutoSession) =
    performCreateOrUpdatePropertiesOperation(
      properties,
      postgresbackend.dao.raw.PropertyDAOImpl.createOrUpdateProperties,
      postgresbackend.dao.raw.CollectionDAOImpl.createOrUpdateCollections,
      postgresbackend.dao.raw.PropertyCollectionDAOImpl.createOrUpdatePropertyCollections,
    )

  def getAll(implicit session: DBSession = AutoSession): List[ProjectedProperty] =
    val propertyPKs = postgresbackend.queries.raw.PropertyQueries
      .allPKs
      .map(rs => UUID.fromString(rs.string("pk")))
      .list
      .apply()
    getAllMatchingPKs(propertyPKs)

  def getAllMatchingPKs(propertyPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[ProjectedProperty] =
    val propertyValueDataList = postgresbackend.queries.projected.PropertyValueQueries
      .propertyValuesByParentPropertyPKs()
      .bind(session.connection.createArrayOf("varchar", propertyPKs.toArray))
      .map { rs => (
        UUID.fromString(rs.string("parent_property_pk")),
        UUID.fromString(rs.string("child_property_pk")),
        generatePropertyValueData(rs)
      )}
      .list
      .apply()
    val propertyUUIDs = propertyValueDataList
      .map((_, childPropertyPK, _) => childPropertyPK)
      .appendedAll(propertyPKs)
      .distinct
    val propertiesMap = postgresbackend.dao.raw.PropertyDAOImpl.getAllMatchingPKs(propertyUUIDs).map(p => (p.pk, p)).toMap
    val propertyValueDataMap = propertyValueDataList
      .groupBy(_._1)
      .map((parentPropertyPK, propertyValueDataList) => (
        parentPropertyPK,
        propertyValueDataList.map((_, childPropertyPK, propertyValueData) => (childPropertyPK, propertyValueData)).toMap
      ))
    val result = for {
      parentPropertyPK <- propertyPKs
      property <- propertiesMap.get(parentPropertyPK)
    } yield {
      val propertyValues = for {
        (childPropertyPK, propertyValueData) <- propertyValueDataMap.getOrElse(property.pk, Map())
        childProperty <- propertiesMap.get(childPropertyPK)
      } yield entity.projected.PropertyValue(
        property = entity.projected.Property(
          pk = childProperty.pk,
          propertyName = childProperty.propertyName,
          propertyTypes = childProperty.propertyTypes,
          deleted = childProperty.deleted,
          created = childProperty.created,
          modified = childProperty.modified,
          propertyValues = Nil
        ),
        collection = entity.projected.Collection(pk = parentPropertyPK),
        textValues = propertyValueData.textValues,
        byteValues = propertyValueData.byteValues,
        smallintValues = propertyValueData.smallintValues,
        intValues = propertyValueData.intValues,
        bigintValues = propertyValueData.bigintValues,
        numericValues = propertyValueData.numericValues,
        floatValues = propertyValueData.floatValues,
        doubleValues = propertyValueData.doubleValues,
        booleanValues = propertyValueData.booleanValues,
        dateValues = propertyValueData.dateValues,
        timeValues = propertyValueData.timeValues,
        timestampValues = propertyValueData.timestampValues,
        uuidValues = propertyValueData.uuidValues,
        jsonValues = propertyValueData.jsonValues
      )
      entity.projected.Property(
        pk = property.pk,
        propertyName = property.propertyName,
        propertyTypes = property.propertyTypes,
        deleted = property.deleted,
        created = property.created,
        modified = property.modified,
        propertyValues = propertyValues.toList
      )
    }
    result.toList

  def getAllMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession): List[ProjectedProperty] =
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
  (implicit session: DBSession = AutoSession): Map[UUID, List[ProjectedProperty]] =
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
  (implicit session: DBSession = AutoSession): Map[UUID, List[(Boolean, ProjectedProperty)]] =
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

  def inflateRawProperties(properties: Seq[Property])(implicit session: DBSession): List[ProjectedProperty] = ???
