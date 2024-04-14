package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.Property

import java.util.UUID

trait PropertyDAO:
  def createProperties(properties: Seq[Property]): Array[Int]
  def createOrUpdateProperties(properties: Seq[Property]): Array[Int]
  def getAll: List[Property]
  def getAllMatchingPKs(propertyPKs: Seq[UUID]): List[Property]