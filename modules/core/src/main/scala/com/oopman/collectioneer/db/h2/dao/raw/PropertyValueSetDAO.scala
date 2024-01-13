package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.h2.queries.raw
import com.oopman.collectioneer.db.h2.queries.raw.PropertyValueSetQueries
import scalikejdbc.*

import java.sql.Connection

object PropertyValueSetDAO extends traits.dao.raw.PropertyValueSetDAO:
  def createPropertyValueSets(propertyValueSets: Seq[traits.entity.PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int] =
    PropertyValueSetQueries
      .insert
      .batch(traits.entity.PropertyValueSet.propertyValueSetSeqToBatchInsertUpsertSeqSeq(propertyValueSets): _*)
      .apply()

  def createOrUpdatePropoertyValueSets(propertyValueSets: Seq[traits.entity.PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int] =
    raw.PropertyValueSetQueries
      .upsert
      .batch(traits.entity.PropertyValueSet.propertyValueSetSeqToBatchInsertUpsertSeqSeq(propertyValueSets): _*)
      .apply()


