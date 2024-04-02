package com.oopman.collectioneer.plugins.postgresbackend.dao.projected

import com.oopman.collectioneer.db.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.projected.Property
import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

object PropertyDAO extends traits.dao.projected.PropertyDAO:
  def createProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession) = ???

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
  def createOrUpdateProperties(properties: Seq[traits.entity.projected.Property])(implicit session: DBSession = AutoSession) =
    val distinctProperties = Property.collectProperties(properties)
    val propertyValues = properties.flatMap(property => property.propertyValues.map {
      case propertyValue: entity.projected.PropertyValue => propertyValue.copy(collection = propertyValue.collection.copy(pk = property.pk))
    })
    val collections = propertyValues.map(_.collection).distinctBy(_.pk)
    val propertyCollections = propertyValues
      .groupBy(_.collection.pk)
      .flatMap((_, propertyValues) => propertyValues.zipWithIndex.map((propertyValue, index) => entity.raw.PropertyCollection(
        propertyPK = propertyValue.property.pk,
        collectionPK = propertyValue.collection.pk,
        index = index,
        propertyCollectionRelationshipType = traits.entity.raw.PropertyCollectionRelationshipType.CollectionOfPropertiesOfProperty
      )))
      .toSeq
    postgresbackend.dao.raw.PropertyDAO.createOrUpdateProperties(distinctProperties)
    postgresbackend.dao.raw.CollectionDAO.createOrUpdateCollections(collections)
    postgresbackend.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues)
    postgresbackend.dao.raw.PropertyCollectionDAO.createOrUpdatePropertyCollections(propertyCollections)

  def getAll()(implicit session: DBSession = AutoSession): List[traits.entity.projected.Property] = ???
