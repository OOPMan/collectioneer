package com.oopman.collectioneer.db.scalikejdbc.traits.dao.projected

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.traits.entity.projected.Property
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikePropertyDAO:
  def createProperties(properties: Seq[Property])(implicit session: DBSession): Array[Int]
  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession): Array[Int]
  def getAll(implicit session: DBSession): List[Property]
  def getAllMatchingPKs(propertyPKs: Seq[UUID])(implicit session: DBSession): List[Property]
  def getAllMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession): List[Property]

