package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw.Relationship

object Relationship:
  def relationshipSeqToBatchUpsertSeq(relationships: Seq[Relationship]): Seq[Seq[Any]] =
    relationships.map(relationship => Seq(
      relationship.pk,
      relationship.collectionPK,
      relationship.relatedCollectionPK,
      relationship.relationshipType.toString,
      relationship.index
    ))
