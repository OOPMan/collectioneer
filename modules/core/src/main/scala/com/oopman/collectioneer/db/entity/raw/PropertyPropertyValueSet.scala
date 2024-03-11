package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.{entity, traits}
import scalikejdbc._

import java.util.UUID
case class PropertyPropertyValueSet
(
  propertyPK: UUID = UUID.randomUUID(),
  propertyValueSetPK: UUID = UUID.randomUUID(),
  index: Int = 0,
  relationship: traits.entity.raw.PropertyPropertyValueSetRelationship = traits.entity.raw.PropertyPropertyValueSetRelationship.propertyInPropertyValueSet
) extends traits.entity.raw.PropertyPropertyValueSet

object PropertyPropertyValueSet extends SQLSyntaxSupport[PropertyPropertyValueSet]:
  override val schemaName = Some("public")
  override val tableName = "property__property_value_set"
  val ppvs1 = PropertyPropertyValueSet.syntax("ppvs1")
  val ppvs2 = PropertyPropertyValueSet.syntax("ppvs2")

  def apply(ppvs: ResultName[PropertyPropertyValueSet])(rs: WrappedResultSet) =
    new PropertyPropertyValueSet(
      propertyPK = UUID.fromString(rs.string(ppvs.propertyPK)),
      propertyValueSetPK = UUID.fromString(rs.string(ppvs.propertyValueSetPK)),
      index = rs.int(ppvs.index),
      relationship = traits.entity.raw.PropertyPropertyValueSetRelationship.valueOf(rs.string(ppvs.relationship))
    )
