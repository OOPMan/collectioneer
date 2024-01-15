package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.PropertyValueSet
import com.oopman.collectioneer.db.{DBConnectionProvider, entity, traits}

class PropertyValueSetDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):

  def createPropertyValueSets(propertyValueSets: Seq[PropertyValueSet]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyValueSetDAO.createPropertyValueSets(propertyValueSets) }

  def createOrUpdatePropoertyValueSets(propertyValueSets: Seq[PropertyValueSet]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyValueSetDAO.createOrUpdatePropoertyValueSets(propertyValueSets) }
