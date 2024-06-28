package com.oopman.collectioneer.plugins.postgresbackend.dao.projected

import com.oopman.collectioneer.db.scalikejdbc.traits.dao.projected.ScalikePropertyDAO
import com.oopman.collectioneer.db.traits.entity.projected.Property as ProjectedProperty
import com.oopman.collectioneer.db.traits.entity.raw.{Collection, Property, PropertyCollection, PropertyCollectionRelationshipType}
import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

object PropertyDAOImpl extends ScalikePropertyDAO:
  private def performCreateOrUpdatePropertiesOperation
  (properties: Seq[ProjectedProperty],
   performCreateOrUpdateProperties: Seq[Property] => Array[Int],
   performCreateOrUpdateCollections: Seq[Collection] => Array[Int],
   performCreateOrUpdatePropertyCollections: Seq[PropertyCollection] => Array[Int])
  (implicit session: DBSession = AutoSession): Array[Int] =
    val distinctProperties = ProjectedProperty.collectProperties(properties)
    val collections = distinctProperties.map(p => entity.raw.Collection(pk = p.pk))
    val propertyValues = properties.flatMap(property => property.propertyValues.map {
      case propertyValue: entity.projected.PropertyValue => propertyValue.copy(collection = propertyValue.collection.copy(pk = property.pk))
    })
    val propertyCollections = propertyValues
      .groupBy(_.collection.pk)
      .flatMap((_, propertyValues) => propertyValues.zipWithIndex.map((propertyValue, index) => entity.raw.PropertyCollection(
        propertyPK = propertyValue.property.pk,
        collectionPK = propertyValue.property.pk,
        index = index,
        propertyCollectionRelationshipType = PropertyCollectionRelationshipType.CollectionOfPropertiesOfProperty
      )))
      .toSeq
    performCreateOrUpdateProperties(distinctProperties)
    performCreateOrUpdateCollections(collections)
    postgresbackend.dao.projected.PropertyValueDAOImpl.updatePropertyValues(propertyValues)
    performCreateOrUpdatePropertyCollections(propertyCollections)

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

  def getAll(implicit session: DBSession = AutoSession): List[ProjectedProperty] = ???
