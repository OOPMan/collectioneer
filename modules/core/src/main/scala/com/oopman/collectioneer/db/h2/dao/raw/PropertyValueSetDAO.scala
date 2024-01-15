package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.h2.queries.raw
import com.oopman.collectioneer.db.h2.queries.raw.PropertyValueSetQueries
import com.oopman.collectioneer.db.traits.entity
import com.oopman.collectioneer.db.traits.entity.raw.PropertyValueSet
import scalikejdbc.*

import java.sql.Connection

object PropertyValueSetDAO extends traits.dao.raw.PropertyValueSetDAO:
  def createPropertyValueSets(propertyValueSets: Seq[PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int] =
    PropertyValueSetQueries
      .insert
      .batch(entity.raw.PropertyValueSet.propertyValueSetSeqToBatchInsertUpsertSeqSeq(propertyValueSets): _*)
      .apply()

  def createOrUpdatePropoertyValueSets(propertyValueSets: Seq[PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int] =
    raw.PropertyValueSetQueries
      .upsert
      .batch(entity.raw.PropertyValueSet.propertyValueSetSeqToBatchInsertUpsertSeqSeq(propertyValueSets): _*)
      .apply()


