package com.oopman.collectioneer.db.entity

case class ItemsCollectionsAssociation
(
  itemId: Long,
  collectionId: Long,
  index: Int = 0,
  associationType: String = "COLLECTION_OF"
)
