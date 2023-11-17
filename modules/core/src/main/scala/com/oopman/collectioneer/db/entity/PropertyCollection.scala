package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

enum PropertyCollectionRelationship:
  case PropertyOfCollection extends PropertyCollectionRelationship
  case CollectionOfPropertiesOfProperty extends PropertyCollectionRelationship

trait PropertyCollection:
  val propertyPK: UUID
  val collectionPK: UUID
  val propertyValueSetPK: UUID
  val index: Int
  val relationship: PropertyCollectionRelationship
  val created: ZonedDateTime
  val modified: ZonedDateTime

object PropertyCollection:
  def propertyCollectionListToBatchInserSeqList(propertyCollections: Seq[PropertyCollection]): Seq[Seq[Any]] =
    propertyCollections.map(pc => Seq(
      pc.propertyPK.toString,
      pc.collectionPK.toString,
      pc.propertyValueSetPK.toString,
      pc.index,
      pc.relationship.toString
    ))

  def propertyCollectionListToBatchUpsertSeqList(propertyCollections: Seq[PropertyCollection]): Seq[Seq[Any]] =
    propertyCollections.map(pc => Seq(
      pc.propertyPK.toString,
      pc.collectionPK.toString,
      pc.propertyValueSetPK.toString,
      pc.index,
      pc.relationship.toString,
      pc.modified
    ))