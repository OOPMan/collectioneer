package com.oopman.collectioneer.db.h2.dao.projected

import com.oopman.collectioneer.db.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.projected.Property
import com.oopman.collectioneer.db.{entity, h2, traits}
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
    h2.dao.raw.PropertyDAO.createOrUpdateProperties(distinctProperties)
    val collections = properties.map(property => entity.raw.Collection(pk=property.pk))
    h2.dao.raw.CollectionDAO.createOrUpdateCollections(collections)
    val propertyValues = properties.flatMap(property => property.propertyValues.map {
      case propertyValue: entity.projected.PropertyValue => propertyValue.copy(collection = Collection(pk = property.pk))
    })
    h2.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues)
    val propertyCollections = propertyValues
      .groupBy(_.collection.pk)
      .flatMap((_, propertyValues) => propertyValues.zipWithIndex.map((propertyValue, index) => entity.raw.PropertyCollection(
        propertyPK = propertyValue.property.pk,
        collectionPK = propertyValue.collection.pk,
        index = index,
        propertyCollectionRelationshipType = traits.entity.raw.PropertyCollectionRelationshipType.CollectionOfPropertiesOfProperty
      )))
      .toSeq
    h2.dao.raw.PropertyCollectionDAO.createOrUpdatePropertyCollections(propertyCollections)

  def getAll()(implicit session: DBSession = AutoSession): List[traits.entity.projected.Property] = ???
