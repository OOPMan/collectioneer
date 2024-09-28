package com.oopman.collectioneer.plugins.postgresbackend.dao.projected

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.entity.projected.PropertyValue
import com.oopman.collectioneer.db.scalikejdbc.traits
import com.oopman.collectioneer.db.traits.entity.projected.{Collection as ProjectedCollection, Property as ProjectedProperty}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollectionRelationshipType.PropertyOfCollection
import com.oopman.collectioneer.db.traits.entity.raw.{Collection, PropertyCollection, PropertyCollectionRelationshipType}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

import java.util.UUID

object CollectionDAOImpl extends traits.dao.projected.ScalikeCollectionDAO:
  private def performCreateOrUpdateCollectionsOperation
  (collections: Seq[ProjectedCollection],
   performCreateOrUpdateCollections: Seq[Collection] => Array[Int],
   performCreateOrUpdateProperties: Seq[ProjectedProperty] => Array[Int],
   performCreateOrUpdatePropertyCollections: Seq[PropertyCollection] => Array[Int])
  (implicit session: DBSession = AutoSession): Array[Int] =
    val propertyValues = collections.flatMap(collection => collection.propertyValues.map {
      case propertyValue: entity.projected.PropertyValue => propertyValue.copy(collection = propertyValue.collection.copy(pk = collection.pk))
    })
    val distinctCollections = collections.distinctBy(_.pk)
    val collectionPKPropetiesListSeq = collections.map(collection => (collection.pk, collection.properties ++ collection.propertyValues.map(_.property)))
    val distinctProperties = collectionPKPropetiesListSeq.flatMap(_._2).distinctBy(_.pk)
    // TODO: Use a for expression here
    val propertyCollections = collectionPKPropetiesListSeq.flatMap((collectionPK, properties) => properties.map(property => entity.raw.PropertyCollection(propertyPK = property.pk, collectionPK = collectionPK)))
    val distinctPropertyCollections = propertyCollections
      .distinctBy(propertyCollection => (propertyCollection.propertyPK, propertyCollection.collectionPK))
      .zipWithIndex
      .map((propertyCollection, index) => propertyCollection.copy(index=index))
    performCreateOrUpdateCollections(distinctCollections)
    performCreateOrUpdateProperties(distinctProperties)
    performCreateOrUpdatePropertyCollections(distinctPropertyCollections)
    postgresbackend.dao.projected.PropertyValueDAOImpl.updatePropertyValues(propertyValues)
    // TODO: Return a more useful result?
    Array.empty

  def createCollections(collections: Seq[ProjectedCollection])(implicit session: DBSession = AutoSession): Array[Int] =
    performCreateOrUpdateCollectionsOperation(
      collections,
      postgresbackend.dao.raw.CollectionDAOImpl.createCollections,
      postgresbackend.dao.projected.PropertyDAOImpl.createProperties,
      postgresbackend.dao.raw.PropertyCollectionDAOImpl.createPropertyCollections,
    )

  def createOrUpdateCollections(collections: Seq[ProjectedCollection])(implicit session: DBSession = AutoSession): Array[Int] =
    performCreateOrUpdateCollectionsOperation(
      collections,
      postgresbackend.dao.raw.CollectionDAOImpl.createOrUpdateCollections,
      postgresbackend.dao.projected.PropertyDAOImpl.createOrUpdateProperties,
      postgresbackend.dao.raw.PropertyCollectionDAOImpl.createOrUpdatePropertyCollections,
    )

  def getAll(implicit session: DBSession = AutoSession): List[ProjectedCollection] = ???

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[ProjectedCollection] =
    val collections = postgresbackend.dao.raw.CollectionDAOImpl.getAllMatchingPKs(collectionPKs)
    val propertiesByCollection = postgresbackend.dao.projected.PropertyDAOImpl.getAllRelatedByPropertyCollection(
      collectionPKs = collectionPKs,
      propertyCollectionRelationshipTypes = PropertyCollectionRelationshipType.PropertyOfCollection :: Nil
    )
    val propertyValues = postgresbackend.dao.projected.PropertyValueDAOImpl.getPropertyValuesByCollectionUUIDs(collectionPKs)
    val propertyValuesMap = propertyValues.groupBy(_.collection.pk)
    val results = for {
      collection <- collections
    } yield {
      val (relatedProperties, properties) = propertiesByCollection.getOrElse(collection.pk, Nil).partition(_._1)
      entity.projected.Collection(
        pk = collection.pk,
        virtual = collection.virtual,
        deleted = collection.deleted,
        created = collection.modified,
        modified = collection.modified,
        properties = properties.map(_._2),
        relatedProperties = relatedProperties.map(_._2),
        propertyValues = propertyValuesMap.getOrElse(collection.pk, Nil)
      )
    }
    results

  def getAllRelatedMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession = AutoSession): List[ProjectedCollection] = ???
