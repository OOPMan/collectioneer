package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

trait RelationshipCollection extends traits.entity.raw.RelationshipCollection:
  val relationship: Relationship
  val collection: Collection
