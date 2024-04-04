package com.oopman.collectioneer.plugins.postgresbackend.dao.projected

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.scalikejdbc.traits
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

import java.util.UUID

object CollectionDAOImpl extends traits.dao.projected.ScalikeCollectionDAO:
  def createCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    val propertyValues = collections.flatMap(collection => collection.propertyValues.map {
      case propertyValue: entity.projected.PropertyValue => propertyValue.copy(collection = propertyValue.collection.copy(pk = collection.pk))
    })
    val distinctCollections = collections.distinctBy(_.pk)
    val collectionPKPropetiesListSeq = collections.map(collection => (collection.pk, collection.properties ++ collection.propertyValues.map(_.property)))
    val distinctProperties = collectionPKPropetiesListSeq.flatMap(_._2).distinctBy(_.pk)
    val propertyCollections = collectionPKPropetiesListSeq.flatMap((collectionPK, properties) => properties.map(property => entity.raw.PropertyCollection(propertyPK = property.pk, collectionPK = collectionPK)))
    // TODO: Generate index values for PropertyCollections
    val distinctPropertyCollections = propertyCollections.distinctBy(propertyCollection => (propertyCollection.propertyPK, propertyCollection.collectionPK))
    postgresbackend.dao.raw.CollectionDAOImpl.createCollections(distinctCollections)
    postgresbackend.dao.raw.PropertyDAOImpl.createProperties(distinctProperties)
    postgresbackend.dao.raw.PropertyCollectionDAOImpl.createPropertyCollections(distinctPropertyCollections)
    postgresbackend.dao.projected.PropertyValueDAOImpl.updatePropertyValues(propertyValues)
    // TODO: Return a more useful result?
    Array.empty

  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    // TODO: De-duplicate wotj above function
    val propertyValues = collections.flatMap(collection => collection.propertyValues.map {
      case propertyValue: entity.projected.PropertyValue => propertyValue.copy(collection = propertyValue.collection.copy(pk = collection.pk))
    })
    val distinctCollections = collections.distinctBy(_.pk)
    val collectionPKPropetiesListSeq = collections.map(collection => (collection.pk, collection.properties ++ collection.propertyValues.map(_.property)))
    val distinctProperties = collectionPKPropetiesListSeq.flatMap(_._2).distinctBy(_.pk)
    val propertyCollections = collectionPKPropetiesListSeq.flatMap((collectionPK, properties) => properties.map(property => entity.raw.PropertyCollection(propertyPK = property.pk, collectionPK = collectionPK)))
    // TODO: Generate index values for PropertyCollections
    val distinctPropertyCollections = propertyCollections.distinctBy(propertyCollection => (propertyCollection.propertyPK, propertyCollection.collectionPK))
    postgresbackend.dao.raw.CollectionDAOImpl.createOrUpdateCollections(distinctCollections)
    postgresbackend.dao.raw.PropertyDAOImpl.createOrUpdateProperties(distinctProperties)
    postgresbackend.dao.raw.PropertyCollectionDAOImpl.createOrUpdatePropertyCollections(distinctPropertyCollections)
    postgresbackend.dao.projected.PropertyValueDAOImpl.updatePropertyValues(propertyValues)
    // TODO: Return a more useful result?
    Array.empty

  def getAll(implicit session: DBSession = AutoSession): List[Collection] = ???

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[Collection] = ???

