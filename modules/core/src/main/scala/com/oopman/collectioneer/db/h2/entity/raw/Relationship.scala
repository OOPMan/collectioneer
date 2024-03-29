package com.oopman.collectioneer.db.h2.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw.Relationship

object Relationship:
  def relationshipSeqToBatchUpsertSeq(relationships: Seq[Relationship]): Seq[Seq[Any]] =
    relationships.map(relationship => Seq(
      relationship.pk.toString,
      relationship.collectionPK.toString,
      relationship.relatedCollectionPK.toString,
      relationship.relationshipType.toString,
      relationship.index
    ))
