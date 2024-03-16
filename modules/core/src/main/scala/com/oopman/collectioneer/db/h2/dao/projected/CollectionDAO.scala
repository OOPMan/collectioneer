package com.oopman.collectioneer.db.h2.dao.projected

import com.oopman.collectioneer.db.{entity, h2, traits}
import scalikejdbc.*

import java.util.UUID

object CollectionDAO extends traits.dao.projected.CollectionDAO:
  def createCollections(collections: Seq[traits.entity.projected.Collection])(implicit session: DBSession = AutoSession): Array[Int] =
//    val propertyValueSets = collections.flatMap(_.propertyValueSets)
//    val properties =
//      propertyValueSets.flatMap(_.properties) ++
//      propertyValueSets.flatMap(_.propertyValues.map(_.property))
//    val propertyValues = propertyValueSets.flatMap(pvs => pvs.propertyValues.map {
//      case pv: entity.projected.PropertyValue => pv.copy(propertyValueSet = pv.propertyValueSet.copy(pk = pvs.pk))
//    })

    // Insert rows
//    h2.dao.raw.CollectionDAO.createCollections(collections.distinctBy(_.pk))
//    h2.dao.raw.PropertyDAO.createProperties(properties.distinctBy(_.pk))
//    h2.dao.raw.PropertyValueSetDAO.createPropertyValueSets(propertyValueSets)
//    h2.dao.projected.PropertyValueDAO.updatePropertyValues(propertyValues)
    Array.empty

  def createOrUpdateCollections(collections: Seq[traits.entity.projected.Collection])(implicit session: DBSession = AutoSession): Array[Int] = ???

  def getAll(implicit session: DBSession = AutoSession): List[traits.entity.projected.Collection] = ???

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[traits.entity.projected.Collection] = ???

