package com.oopman.collectioneer.db.traits.dao.raw

import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection

import java.util.UUID

trait PropertyCollectionDAO:
  def createPropertyCollections(propertyCollections: Seq[PropertyCollection]): Seq[Int]
  def createOrUpdatePropertyCollections(propertyCollections: Seq[PropertyCollection]): Seq[Int]
  def deletePropertyCollections(propertyCollections: Seq[PropertyCollection]): Seq[Int]
  def getAllMatchingPropertyPKs(propertyPKs: Seq[UUID]): Seq[PropertyCollection]
  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID]): Seq[PropertyCollection]
