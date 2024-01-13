package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.{traits, entity, DBConnectionProvider}

class PropertyValueSetDAO(val dbProvider: DBConnectionProvider, db: traits.DatabaseBackend):

  def createPropertyValueSets(propertyValueSets: Seq[traits.entity.PropertyValueSet]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyValueSetDAO.createPropertyValueSets(propertyValueSets) }

  def createOrUpdatePropoertyValueSets(propertyValueSets: Seq[traits.entity.PropertyValueSet]): Array[Int] =
    dbProvider() localTx { implicit session => db.dao.raw.PropertyValueSetDAO.createOrUpdatePropoertyValueSets(propertyValueSets) }
