package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.entity.projected.Property
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.traits.entity.projected
import com.oopman.collectioneer.db.traits.entity.projected.HasProperty
import com.oopman.collectioneer.given

private object SetCardPropertiesUUIDs:
  val primaryEditionUID = "83518c85-6c62-4a56-8fc2-d57076860d8b"

object SetCardProperties:
  def properties: Array[projected.Property] = values.map(_.property)

enum SetCardProperties(val property: projected.Property):
  case primaryEditionUID extends SetCardProperties(Property(
    pk = SetCardPropertiesUUIDs.primaryEditionUID,
    propertyName = "Primary Edition UID",
    propertyTypes = PropertyType.text :: Nil,
    propertyValues = PropertyValues.singleValue ++ PropertyValues.invisibleGATCGProperty
  )) with HasProperty
