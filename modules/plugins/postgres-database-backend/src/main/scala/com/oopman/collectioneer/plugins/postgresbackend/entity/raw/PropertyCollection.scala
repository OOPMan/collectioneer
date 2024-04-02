package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw

object PropertyCollection:
  def propertyCollectionSeqToBatchUpsertSeq(propertyCollections: Seq[raw.PropertyCollection]): Seq[Seq[Any]] =
    propertyCollections.map(pc => Seq(
      pc.propertyPK.toString,
      pc.collectionPK.toString,
      pc.index,
      pc.propertyCollectionRelationshipType.toString
    ))
