package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.traits.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.Property as RawProperty

import java.util.UUID

trait PropertyDAO:
  def createProperties(properties: Seq[Property]): Seq[Int]
  def createOrUpdateProperties(properties: Seq[Property]): Seq[Int]
  def getAll: Seq[Property]
  def getAllMatchingPKs(propertyPKs: Seq[UUID]): Seq[Property]
  def getAllMatchingPropertyValues(comparisons: Seq[Comparison]): Seq[Property]
  def inflateRawProperties(properties: Seq[RawProperty]): Seq[Property]
