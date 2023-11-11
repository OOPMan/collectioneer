package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.queries.h2
import scalikejdbc.*

import java.sql.Connection

object PropertyValueSetDAO:
  def createPropertyValueSets(propertyValueSets: List[entity.PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyValueSetQueries
      .insert
      .batch(entity.PropertyValueSet.propertyValueSetListToBatchInsertUpsertSeqList(propertyValueSets): _*)
      .apply()

  def createOrUpdatePropoertyValueSets(propertyValueSets: List[entity.PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int] =
    h2.raw.PropertyValueSetQueries
      .upsert
      .batch(entity.PropertyValueSet.propertyValueSetListToBatchInsertUpsertSeqList(propertyValueSets): _*)
      .apply()

class PropertyValueSetDAO(val dbProvider: () => DBConnection):
  def this(connectionPoolName: String) =
    this(() => NamedDB(connectionPoolName))

  def this(connection: Connection, autoclose: Boolean = false) =
    this(() => DB(connection).autoClose(autoclose))

  def createPropertyValueSets(propertyValueSets: List[entity.PropertyValueSet]): Array[Int] =
    dbProvider() localTx { implicit session =>  PropertyValueSetDAO.createPropertyValueSets(propertyValueSets) }

  def createOrUpdatePropoertyValueSets(propertyValueSets: List[entity.PropertyValueSet]): Array[Int] =
    dbProvider() localTx { implicit session => PropertyValueSetDAO.createOrUpdatePropoertyValueSets(propertyValueSets) }

