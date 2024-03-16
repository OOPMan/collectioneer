package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits

trait PropertyCollection extends traits.entity.raw.PropertyCollection:
  val collection: Collection
  val property: Property
    
