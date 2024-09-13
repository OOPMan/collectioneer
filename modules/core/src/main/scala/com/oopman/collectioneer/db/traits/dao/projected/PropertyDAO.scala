package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.traits.entity.projected.Property

import java.util.UUID

trait PropertyDAO:
  def createProperties(properties: Seq[Property]): Array[Int]
  def createOrUpdateProperties(properties: Seq[Property]): Array[Int]
  def getAll: List[Property]
  def getAllMatchingPKs(propertyPKs: Seq[UUID]): List[Property]
  def getAllMatchingPropertyValues(comparisons: Seq[Comparison]): List[Property]
