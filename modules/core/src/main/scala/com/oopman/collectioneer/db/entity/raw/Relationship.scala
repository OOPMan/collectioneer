package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits
import com.oopman.collectioneer.db.traits.entity.raw.Relationship

import java.time.ZonedDateTime
import java.util.UUID

case class Relationship
(
  pk: UUID = UUID.randomUUID(),
  collectionPK: UUID,
  relatedCollectionPK: UUID,
  relationshipType: traits.entity.raw.RelationshipType = traits.entity.raw.RelationshipType.ChildOf,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
) extends traits.entity.raw.Relationship:

  def rawCopyWith(pk: UUID = pk,
                  collectionPK: UUID = collectionPK,
                  relatedCollectionPK: UUID = relatedCollectionPK,
                  index: Int = index,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): Relationship =
    copy(
      pk = pk, 
      collectionPK = collectionPK, 
      relatedCollectionPK = relatedCollectionPK, 
      relationshipType = relationshipType, 
      index = index, 
      created = created, 
      modified = modified
    )
