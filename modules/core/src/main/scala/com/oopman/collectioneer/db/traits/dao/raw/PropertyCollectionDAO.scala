package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection

import java.util.UUID

trait PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[PropertyCollection]): Array[Int]
  def createOrUpdatePropertyCollections(propertyCollections: Seq[PropertyCollection]): Array[Int]
  def deletePropertyCollections(propertyCollections: Seq[PropertyCollection]): Array[Int]
  def getAllMatchingPropertyPKs(propertyPKs: Seq[UUID]): List[PropertyCollection]
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID]): List[PropertyCollection]
