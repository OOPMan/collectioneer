package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc._

trait PropertyValueSetDAO:
  def createPropertyValueSets(propertyValueSets: Seq[traits.entity.PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int]

  def createOrUpdatePropoertyValueSets(propertyValueSets: Seq[traits.entity.PropertyValueSet])(implicit session: DBSession = AutoSession): Array[Int]
