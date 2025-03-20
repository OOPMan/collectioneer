package com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.traits.entity.raw.Property
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikePropertyDAO:
  def createProperties(properties: Seq[Property])(implicit session: DBSession): Seq[Int]
  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession): Seq[Int]
  def getAll(implicit session: DBSession): Seq[Property]
  def getAllMatchingPKs(propertyPKs: Seq[UUID])(implicit session: DBSession): Seq[Property]
  def getAllMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession): Seq[Property]