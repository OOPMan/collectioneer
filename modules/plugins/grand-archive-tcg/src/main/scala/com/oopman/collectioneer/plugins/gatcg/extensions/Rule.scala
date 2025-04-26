package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{CommonProperties, RuleProperties}

import java.time.LocalDate
import java.util.UUID

object Rule:
  extension (rule: Models.Rule)
    def asCollection: Collection = Collection(
      pk = UUID.nameUUIDFromBytes(s"GATCG-rule-${rule.title}-${rule.description}-${rule.date_added}".getBytes),
      virtual = true,
      propertyValues = Map(
        CoreProperties.name -> PropertyValue(textValues = rule.title :: Nil),
        CoreProperties.description -> PropertyValue(textValues = rule.description :: Nil),
        CommonProperties.isGATCGRule -> PropertyValue(booleanValues = true :: Nil),
        RuleProperties.dateAdded -> PropertyValue(textValues = rule.date_added :: Nil)
      )
    )


