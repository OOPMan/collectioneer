package com.oopman.collectioneer.db.h2.dao.projected

import com.oopman.collectioneer.db.{entity, h2, traits}
import scalikejdbc.*

import java.util.UUID

object CollectionDAO extends traits.dao.projected.CollectionDAO:
  def createCollections(collections: Seq[traits.entity.projected.Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    val properties =
      collections.flatMap(_.properties) ++
      collections.flatMap(_.propertyValues.map(_.property))
    val propertyValues = collections.flatMap(collection => collection.propertyValues.map {
      case propertyValue: entity.projected.PropertyValue => propertyValue.copy(collection = propertyValue.collection.copy(pk = collection.pk))
    })
    h2.dao.raw.CollectionDAO.createCollections(collections.distinctBy(_.pk))
    h2.dao.raw.PropertyDAO.createProperties(properties.distinctBy(_.pk))
    h2.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues)
    // TODO: Return a more useful result?
    Array.empty

  def createOrUpdateCollections(collections: Seq[traits.entity.projected.Collection])(implicit session: DBSession = AutoSession): Array[Int] = ???

  def getAll(implicit session: DBSession = AutoSession): List[traits.entity.projected.Collection] = ???

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[traits.entity.projected.Collection] = ???

