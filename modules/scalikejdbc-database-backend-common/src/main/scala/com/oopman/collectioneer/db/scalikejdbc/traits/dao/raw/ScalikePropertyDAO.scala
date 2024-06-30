package com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL
import com.oopman.collectioneer.db.traits.entity.raw.Property
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikePropertyDAO:
  def createProperties(properties: Seq[Property])(implicit session: DBSession): Array[Int]
  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession): Array[Int]
  def getAll(implicit session: DBSession): List[Property]
  def getAllMatchingPKs(propertyPKs: Seq[UUID])(implicit session: DBSession): List[Property]
  def getAllMatchingPropertyValues(comparisons: Seq[PropertyValueQueryDSL.Comparison])(implicit session: DBSession): List[Property]