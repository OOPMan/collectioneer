package com.oopman.collectioneer.db.traits.entity.raw

import java.util.UUID

enum PropertyPropertyValueSetRelationship:
  case propertyInPropertyValueSet extends PropertyPropertyValueSetRelationship
  case propertyValueSetOfProperty extends PropertyPropertyValueSetRelationship

trait PropertyPropertyValueSet:
  val propertyPK: UUID
  val propertyValueSetPK: UUID
  val index: Int
  val relationship: PropertyPropertyValueSetRelationship

object PropertyPropertyValueSet:
  // TODO: This function, and functions like it, need to be moved as the parameters supplied to queries may vary based on the backing database
  def propertyPropertyValueSetSeqToBatchUpsertSeqSeq(propertyPropertyValueSets: Seq[PropertyPropertyValueSet]): Seq[Seq[Any]] =
    propertyPropertyValueSets.map(ppvs => Seq(
      ppvs.propertyPK.toString,
      ppvs.propertyValueSetPK.toString,
      ppvs.index,
      ppvs.relationship.toString
    ))