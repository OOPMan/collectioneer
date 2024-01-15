package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection
import scalikejdbc.*

trait PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession = AutoSession): Array[Int]
  def createOrUpdatePropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession = AutoSession): Array[Int]