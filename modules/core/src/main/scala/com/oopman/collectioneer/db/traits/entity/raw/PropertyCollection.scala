package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

enum PropertyCollectionRelationship(val dbValue: String):
  case PropertyOfCollection extends PropertyCollectionRelationship("property_of_collection")
  case CollectionOfPropertiesOfProperty extends PropertyCollectionRelationship("collection_of_properties_of_property")

trait PropertyCollection:
  val propertyPK: UUID
  val collectionPK: UUID
  val propertyValueSetPK: UUID
  val index: Int
  val relationship: PropertyCollectionRelationship
  val created: ZonedDateTime
  val modified: ZonedDateTime

object PropertyCollection:
  def propertyCollectionSeqToBatchInsertSeqSeq(propertyCollections: Seq[PropertyCollection]): Seq[Seq[Any]] =
    propertyCollections.map(pc => Seq(
      pc.propertyPK.toString,
      pc.collectionPK.toString,
      pc.propertyValueSetPK.toString,
      pc.index,
      pc.relationship.dbValue
    ))

  def propertyCollectionSeqToBatchUpsertSeqSeq(propertyCollections: Seq[PropertyCollection]): Seq[Seq[Any]] =
    propertyCollections.map(pc => Seq(
      pc.propertyPK.toString,
      pc.collectionPK.toString,
      pc.propertyValueSetPK.toString,
      pc.index,
      pc.relationship.dbValue,
      pc.modified
    ))