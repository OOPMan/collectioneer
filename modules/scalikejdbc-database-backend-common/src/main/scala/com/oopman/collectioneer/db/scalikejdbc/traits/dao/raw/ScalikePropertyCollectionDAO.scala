package com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikePropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession): Seq[Int]
  def createOrUpdatePropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession): Seq[Int]
  def deletePropertyCollections(propertyCollections: Seq[PropertyCollection])(implicit session: DBSession): Seq[Int]
  def getAllMatchingPropertyPKs(propertyPKs: Seq[UUID])(implicit session: DBSession): Seq[PropertyCollection]
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): Seq[PropertyCollection]
