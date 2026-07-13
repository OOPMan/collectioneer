package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{CommonProperties, SetProperties}

import java.util.UUID

object Set:
  extension (set: Models.Set)
    def asCollection: Collection = Collection(
      pk = UUID.nameUUIDFromBytes(s"GATCG-set-${set.id}-${set.prefix}-${set.name}-${set.language}".getBytes),
      virtual = true,
      propertyValues = Map(
        CoreProperties.name -> PropertyValue(stringValues = set.name :: Nil),
        CommonProperties.isGATCGCollection -> PropertyValue (booleanValues = true :: Nil),
        CommonProperties.isGATCGSet -> PropertyValue(booleanValues = true :: Nil),
        SetProperties.prefix -> PropertyValue(stringValues = set.prefix :: Nil),
        SetProperties.language -> PropertyValue(stringValues = set.language :: Nil),
      )
    )


