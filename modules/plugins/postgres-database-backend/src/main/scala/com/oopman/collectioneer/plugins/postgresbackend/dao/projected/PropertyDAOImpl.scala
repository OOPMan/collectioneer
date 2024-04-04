package com.oopman.collectioneer.plugins.postgresbackend.dao.projected

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.entity.projected.Collection
import com.oopman.collectioneer.db.scalikejdbc.traits
import com.oopman.collectioneer.db.traits.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollectionRelationshipType
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

object PropertyDAOImpl extends traits.dao.projected.ScalikePropertyDAO:
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
  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession) =
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
        propertyCollectionRelationshipType = PropertyCollectionRelationshipType.CollectionOfPropertiesOfProperty
      )))
      .toSeq
    postgresbackend.dao.raw.PropertyDAOImpl.createOrUpdateProperties(distinctProperties)
    postgresbackend.dao.raw.CollectionDAOImpl.createOrUpdateCollections(collections)
    postgresbackend.dao.projected.PropertyValueDAOImpl.updatePropertyValues(propertyValues)
    postgresbackend.dao.raw.PropertyCollectionDAOImpl.createOrUpdatePropertyCollections(propertyCollections)

  def getAll(implicit session: DBSession = AutoSession): List[Property] = ???
