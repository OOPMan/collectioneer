package com.oopman.collectioneer.plugins.postgresbackend.dao.projected

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.entity.projected.PropertyValue
import com.oopman.collectioneer.db.scalikejdbc.traits
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.raw.{Property, PropertyCollection}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

import java.util.UUID

object CollectionDAOImpl extends traits.dao.projected.ScalikeCollectionDAO:
  private def performCreateOrUpdateCollectionsOperation
  (collections: Seq[Collection], performCreateOrUpdateCollections: Seq[Collection] => Array[Int],
   performCreateOrUpdateProperties: Seq[Property] => Array[Int],
   performCreateOrUpdatePropertyCollections: Seq[PropertyCollection] => Array[Int],
   performUpdatePropertyValues: Seq[PropertyValue] => Seq[Boolean])
  (implicit session: DBSession = AutoSession): Array[Int] =
    val propertyValues = collections.flatMap(collection => collection.propertyValues.map {
      case propertyValue: entity.projected.PropertyValue => propertyValue.copy(collection = propertyValue.collection.copy(pk = collection.pk))
    })
    val distinctCollections = collections.distinctBy(_.pk)
    val collectionPKPropetiesListSeq = collections.map(collection => (collection.pk, collection.properties ++ collection.propertyValues.map(_.property)))
    val distinctProperties = collectionPKPropetiesListSeq.flatMap(_._2).distinctBy(_.pk)
    val propertyCollections = collectionPKPropetiesListSeq.flatMap((collectionPK, properties) => properties.map(property => entity.raw.PropertyCollection(propertyPK = property.pk, collectionPK = collectionPK)))
    // TODO: Generate index values for PropertyCollections
    val distinctPropertyCollections = propertyCollections.distinctBy(propertyCollection => (propertyCollection.propertyPK, propertyCollection.collectionPK))
    performCreateOrUpdateCollections(distinctCollections)
    performCreateOrUpdateProperties(distinctProperties)
    performCreateOrUpdatePropertyCollections(distinctPropertyCollections)
    performUpdatePropertyValues(propertyValues)
    // TODO: Return a more useful result?
    Array.empty

  def createCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    performCreateOrUpdateCollectionsOperation(
      collections,
      postgresbackend.dao.raw.CollectionDAOImpl.createCollections,
      postgresbackend.dao.raw.PropertyDAOImpl.createProperties,
      postgresbackend.dao.raw.PropertyCollectionDAOImpl.createPropertyCollections,
      postgresbackend.dao.projected.PropertyValueDAOImpl.updatePropertyValues
    )

  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    performCreateOrUpdateCollectionsOperation(
      collections,
      postgresbackend.dao.raw.CollectionDAOImpl.createOrUpdateCollections,
      postgresbackend.dao.raw.PropertyDAOImpl.createOrUpdateProperties,
      postgresbackend.dao.raw.PropertyCollectionDAOImpl.createOrUpdatePropertyCollections,
      postgresbackend.dao.projected.PropertyValueDAOImpl.updatePropertyValues
    )

  def getAll(implicit session: DBSession = AutoSession): List[Collection] = ???

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[Collection] = ???

