package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.db.h2.queries.raw
import com.oopman.collectioneer.db.h2.queries.raw.PropertyValueSetQueries
import com.oopman.collectioneer.db.traits.entity.PropertyValueSet
import scalikejdbc.*

import java.sql.Connection

object PropertyValueSetDAO:
  def createPropertyValueSets(propertyValueSets: Seq[PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int] =
    PropertyValueSetQueries
      .insert
      .batch(traits.entity.PropertyValueSet.propertyValueSetSeqToBatchInsertUpsertSeqSeq(propertyValueSets): _*)
      .apply()

  def createOrUpdatePropoertyValueSets(propertyValueSets: Seq[PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int] =
    raw.PropertyValueSetQueries
      .upsert
      .batch(traits.entity.PropertyValueSet.propertyValueSetSeqToBatchInsertUpsertSeqSeq(propertyValueSets): _*)
      .apply()

class PropertyValueSetDAO(val dbProvider: () => DBConnection):
  def this(connectionPoolName: String) =
    this(() => NamedDB(connectionPoolName))

  def this(connection: Connection, autoclose: Boolean = false) =
    this(() => DB(connection).autoClose(autoclose))

  def createPropertyValueSets(propertyValueSets: Seq[PropertyValueSet]): Array[Int] =
    dbProvider() localTx { implicit session =>  PropertyValueSetDAO.createPropertyValueSets(propertyValueSets) }

  def createOrUpdatePropoertyValueSets(propertyValueSets: Seq[PropertyValueSet]): Array[Int] =
    dbProvider() localTx { implicit session => PropertyValueSetDAO.createOrUpdatePropoertyValueSets(propertyValueSets) }

