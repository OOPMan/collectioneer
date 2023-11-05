package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.queries.h2
import scalikejdbc._

object PropertyValueSetDAO:
  def createPropertyValueSets(propertyValueSets: List[entity.PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyValueSetQueries
      .insert
      .batch(Nil) // TODO: Replace with real value
      .apply()

  def createOrUpdatePropoertyValueSets(propertyValueSets: List[entity.PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyValueSetQueries
      .upsert
      .batch(Nil) // TODO: Replace with real value
      .apply()

class PropertyValueSetDAO(val connectionPoolName: String):
  def createPropertyValueSets(propertyValueSets: List[entity.PropertyValueSet]): Array[Int] = NamedDB(connectionPoolName) localTx { implicit session =>
    PropertyValueSetDAO.createPropertyValueSets(propertyValueSets)
  }

  def createOrUpdatePropoertyValueSets(propertyValueSets: List[entity.PropertyValueSet]): Array[Int] = NamedDB(connectionPoolName) localTx { implicit session =>
    PropertyValueSetDAO.createOrUpdatePropoertyValueSets(propertyValueSets)
  }

