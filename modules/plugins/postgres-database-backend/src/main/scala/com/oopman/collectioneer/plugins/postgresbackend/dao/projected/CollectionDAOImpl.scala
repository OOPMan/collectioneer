package com.oopman.collectioneer.plugins.postgresbackend.dao.projected

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.scalikejdbc.traits
import com.oopman.collectioneer.db.traits.entity.projected.Collection as ProjectedCollection
import com.oopman.collectioneer.db.traits.entity.raw.{Collection, PropertyCollection, PropertyCollectionRelationshipType}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

import java.util.UUID

object CollectionDAOImpl extends traits.dao.projected.ScalikeCollectionDAO:
  private def performCreateOrUpdateCollectionsOperation
  (collections: Seq[ProjectedCollection],
   performCreateOrUpdateCollections: Seq[Collection] => Seq[Int],
   performCreateOrUpdatePropertyCollections: Seq[PropertyCollection] => Seq[Int])
  (implicit session: DBSession = AutoSession): Seq[Int] =
    val propertyValues =
      for
        collection <- collections
        (property, propertyValue) <- collection.propertyValues
      yield (property.pk, collection.pk, propertyValue)
    val indexedPropertiesByCollection =
      for collection <- collections
      yield collection -> (collection.properties ++ collection.propertyValues.keys).distinct.zipWithIndex
    val propertyCollections =
      for
       (collection, indexedProperties) <- indexedPropertiesByCollection
       (property, index) <- indexedProperties
      yield entity.raw.PropertyCollection(
        propertyPK = property.pk,
        collectionPK = collection.pk,
        index = index
      )
    performCreateOrUpdateCollections(collections.distinct)
    performCreateOrUpdatePropertyCollections(propertyCollections)
    postgresbackend.dao.projected.PropertyValueDAOImpl.updatePropertyValues(propertyValues)
    // TODO: Return a more useful result?
    Nil

  def createCollections(collections: Seq[ProjectedCollection])(implicit session: DBSession = AutoSession): Seq[Int] =
    performCreateOrUpdateCollectionsOperation(
      collections,
      postgresbackend.dao.raw.CollectionDAOImpl.createCollections,
      postgresbackend.dao.raw.PropertyCollectionDAOImpl.createPropertyCollections,
    )

  def createOrUpdateCollections(collections: Seq[ProjectedCollection])(implicit session: DBSession = AutoSession): Seq[Int] =
    performCreateOrUpdateCollectionsOperation(
      collections,
      postgresbackend.dao.raw.CollectionDAOImpl.createOrUpdateCollections,
      postgresbackend.dao.raw.PropertyCollectionDAOImpl.createOrUpdatePropertyCollections,
    )

  def getAll(properties: Seq[UUID] = Nil)(implicit session: DBSession = AutoSession): Seq[ProjectedCollection] = ???

  def getAllMatchingPKs(collectionPKs: Seq[UUID], propertyPKs: Seq[UUID] = Nil)(implicit session: DBSession = AutoSession): Seq[ProjectedCollection] =
    val collections = postgresbackend.dao.raw.CollectionDAOImpl.getAllMatchingPKs(collectionPKs)
    inflateRawCollections(collections, propertyPKs)

  def getAllMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession = AutoSession): Seq[Collection] = ???
  def getAllRelatedMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession = AutoSession): Seq[ProjectedCollection] = ???

  def inflateRawCollections(collections: Seq[Collection], propertyPKs: Seq[UUID] = Nil)(implicit session: DBSession): Seq[ProjectedCollection] =
    val collectionPKs = collections.map(_.pk)
    // TODO: These properties are Projected rather than Raw. We should implement this method on the Raw PropertyDAOImpl
    val propertiesByCollection = postgresbackend.dao.projected.PropertyDAOImpl.getAllRelatedByPropertyCollection(
      collectionPKs = collectionPKs,
      propertyPKs = propertyPKs,
      propertyCollectionRelationshipTypes = PropertyCollectionRelationshipType.PropertyOfCollection :: Nil
    )
    val propertyValues = postgresbackend.dao.projected.PropertyValueDAOImpl.getPropertyValuesByCollectionUUIDs(collectionPKs, propertyPKs)
    val propertiesMap =
      for
        (collectionPK, properties) <- propertiesByCollection
        (relatedProperty, property) <- properties.distinct
      yield property.pk -> property

    val propertyValuesMap = propertyValues.groupBy(_._2)
    for(collection <- collections) yield {
      val (relatedProperties, properties) = propertiesByCollection.getOrElse(collection.pk, Nil).partition(_._1)
      val propertyValues =
        for
          (propertyPK, _, _, propertyValue) <- propertyValuesMap.getOrElse(collection.pk, Nil)
          property <- propertiesMap.get(propertyPK)
        yield property -> propertyValue
      entity.projected.Collection(
        pk = collection.pk,
        virtual = collection.virtual,
        deleted = collection.deleted,
        created = collection.modified,
        modified = collection.modified,
        properties = properties.map(_._2),
        relatedProperties = relatedProperties.map(_._2),
        propertyValues = propertyValues.toMap
      )
    }
