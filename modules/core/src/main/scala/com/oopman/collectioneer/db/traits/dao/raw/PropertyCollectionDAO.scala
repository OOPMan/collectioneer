package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits
import scalikejdbc._

trait PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[traits.entity.PropertyCollection])(implicit session: DBSession = AutoSession): Array[Int]
  def createOrUpdatePropertyCollections(propertyCollections: Seq[traits.entity.PropertyCollection])(implicit session: DBSession = AutoSession): Array[Int]