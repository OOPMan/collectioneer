package com.oopman.collectioneer.db.traits.dao.projected

import com.oopman.collectioneer.db.traits.entity.projected.Property

trait PropertyDAO:
  def createProperties(properties: Seq[Property]): Array[Int]

  def createOrUpdateProperties(properties: Seq[Property]): Array[Int]

  def getAll: List[Property]

