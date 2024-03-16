package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

trait Relationship extends traits.entity.raw.Relationship:
  val collection: Collection
  val relatedCollection: Collection
