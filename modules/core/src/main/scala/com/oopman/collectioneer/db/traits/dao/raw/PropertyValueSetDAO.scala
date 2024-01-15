package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.PropertyValueSet
import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc.*

trait PropertyValueSetDAO:
  def createPropertyValueSets(propertyValueSets: Seq[PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int]

  def createOrUpdatePropoertyValueSets(propertyValueSets: Seq[PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int]
