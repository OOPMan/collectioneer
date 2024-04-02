package com.oopman.collectioneer.plugins.postgresbackend.dao.projected

import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

import java.util.UUID

object CollectionDAO extends traits.dao.projected.CollectionDAO:
  def createCollections(collections: Seq[traits.entity.projected.Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    val propertyValues = collections.flatMap(collection => collection.propertyValues.map {
      case propertyValue: entity.projected.PropertyValue => propertyValue.copy(collection = propertyValue.collection.copy(pk = collection.pk))
    })
    val distinctCollections = collections.distinctBy(_.pk)
    val collectionPKPropetiesListSeq = collections.map(collection => (collection.pk, collection.properties ++ collection.propertyValues.map(_.property)))
    val distinctProperties = collectionPKPropetiesListSeq.flatMap(_._2).distinctBy(_.pk)
    val propertyCollections = collectionPKPropetiesListSeq.flatMap((collectionPK, properties) => properties.map(property => entity.raw.PropertyCollection(propertyPK = property.pk, collectionPK = collectionPK)))
    // TODO: Generate index values for PropertyCollections
    val distinctPropertyCollections = propertyCollections.distinctBy(propertyCollection => (propertyCollection.propertyPK, propertyCollection.collectionPK))
    postgresbackend.dao.raw.CollectionDAO.createCollections(distinctCollections)
    postgresbackend.dao.raw.PropertyDAO.createProperties(distinctProperties)
    postgresbackend.dao.raw.PropertyCollectionDAO.createPropertyCollections(distinctPropertyCollections)
    postgresbackend.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues)
    // TODO: Return a more useful result?
    Array.empty

  def createOrUpdateCollections(collections: Seq[traits.entity.projected.Collection])(implicit session: DBSession = AutoSession): Array[Int] =
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
    postgresbackend.dao.raw.CollectionDAO.createOrUpdateCollections(distinctCollections)
    postgresbackend.dao.raw.PropertyDAO.createOrUpdateProperties(distinctProperties)
    postgresbackend.dao.raw.PropertyCollectionDAO.createOrUpdatePropertyCollections(distinctPropertyCollections)
    postgresbackend.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues)
    // TODO: Return a more useful result?
    Array.empty

  def getAll(implicit session: DBSession = AutoSession): List[traits.entity.projected.Collection] = ???

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[traits.entity.projected.Collection] = ???

