package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{CommonProperties, ReferenceProperties}

import java.util.UUID

object Reference:
  extension (reference: Models.Reference)
    def asCollection: Collection = Collection(
      pk = UUID.nameUUIDFromBytes(s"GATCG-reference-${reference.name}-${reference.slug}-${reference.kind}-${reference.direction}".getBytes),
      virtual = true,
      propertyValues = Map(
        CoreProperties.name -> PropertyValue(stringValues = reference.name :: Nil),
        CommonProperties.isGATCGCollection -> PropertyValue (booleanValues = true :: Nil),
        CommonProperties.isGATCGReference -> PropertyValue(booleanValues = true :: Nil),
        ReferenceProperties.slug -> PropertyValue(stringValues = reference.slug :: Nil),
        ReferenceProperties.kind -> PropertyValue(stringValues = reference.kind :: Nil),
        ReferenceProperties.direction -> PropertyValue(stringValues = reference.direction :: Nil),
      )
    )


