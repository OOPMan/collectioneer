package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipCollection

import java.time.ZonedDateTime
import java.util.UUID

case class RelationshipCollection
(
  relationshipPK: UUID = UUID.randomUUID(),
  collectionPK: UUID = UUID.randomUUID(),
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends traits.entity.raw.RelationshipCollection:

  def rawCopyWith(relationshipPK: UUID = relationshipPK,
                  collectionPK: UUID = collectionPK,
                  index: Int = index,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): RelationshipCollection =
    copy(relationshipPK = relationshipPK, collectionPK = collectionPK, index = index, created = created, modified = modified)
