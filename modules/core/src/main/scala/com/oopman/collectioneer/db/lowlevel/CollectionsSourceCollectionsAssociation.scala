package com.oopman.collectioneer.db.lowlevel

case class CollectionsSourceCollectionsAssociation
(
  collectionId: Long,
  sourceCollectionId: Long,
  index: Int = 0,
  sourceType: String = "ITEMS"
)
