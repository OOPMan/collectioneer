package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.{CoreProperties, given}

val name = "Grand Archive TCG"
val description = "An anime TCG with western game design"

val GATCGRootCollection = Collection(
  pk = "b3192f7b-d4d6-4510-ba5c-aa1b60ab3982",
  virtual = true,
  propertyValues = Map(
    CoreProperties.name -> PropertyValue(textValues = List(name)),
    CoreProperties.description -> PropertyValue(textValues = List(description)),
    // TODO: Add properties for links
  )
)