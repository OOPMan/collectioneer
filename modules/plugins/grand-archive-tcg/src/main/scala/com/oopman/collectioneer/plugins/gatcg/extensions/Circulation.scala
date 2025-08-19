package com.oopman.collectioneer.plugins.gatcg.extensions

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.plugins.gatcg.Models
import com.oopman.collectioneer.plugins.gatcg.properties.{CirculationProperties, CommonProperties}

import java.util.UUID

object Circulation:
  extension (circulation: Models.Circulation)

    def asCollection: Collection = Collection(
      pk = UUID.nameUUIDFromBytes (s"GATCG-circulation-${circulation.edition_id}-${circulation.uuid}".getBytes),
      virtual = true,
      propertyValues = Map(
        CoreProperties.name -> PropertyValue(textValues = circulation.name ++: Nil),
        CommonProperties.isGATCGCollection -> PropertyValue (booleanValues = true :: Nil),
        CommonProperties.isGATCGCirculation -> PropertyValue(booleanValues = true :: Nil),
        CirculationProperties.foil -> PropertyValue(booleanValues = circulation.foil.getOrElse(false) :: Nil),
        CirculationProperties.population -> PropertyValue(intValues = circulation.population :: Nil),
        CirculationProperties.populationOperator -> PropertyValue(textValues = circulation.population_operator :: Nil)
      )
    )