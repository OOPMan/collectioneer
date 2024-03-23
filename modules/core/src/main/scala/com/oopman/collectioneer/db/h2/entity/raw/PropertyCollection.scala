package com.oopman.collectioneer.db.h2.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw.PropertyCollection

object PropertyCollection:
  def propertyCollectionListToBatchUpsertSeqList(propertyCollections: Seq[PropertyCollection]): Seq[Seq[Any]] =
    propertyCollections.map(pc => Seq(
      pc.propertyPK.toString,
      pc.collectionPK.toString,
      pc.index,
      pc.propertyCollectionRelationshipType.toString
    ))
