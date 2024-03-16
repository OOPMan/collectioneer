package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection
import scalikejdbc.DBSession

import java.util.UUID

trait PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession): Array[Int]
  def createOrUpdatePropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession): Array[Int]
  def deletePropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession): Array[Int]
  def getAllMatchingPropertyPKs(propertyPKs: Seq[UUID])(implicit session: DBSession): List[PropertyCollection]
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): List[PropertyCollection]
