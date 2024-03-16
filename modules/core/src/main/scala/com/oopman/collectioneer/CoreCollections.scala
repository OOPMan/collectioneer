package com.oopman.collectioneer

import com.oopman.collectioneer.db.entity.projected
import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.given

import java.util.UUID

private object CoreCollectionUUIDs:
  val commonProperties = "709113ad-6ca0-4d08-b9f4-a526d81da549"
  val commonPropertiesOfProperties = "4a3dfbf0-3d68-4604-a471-5c4451a3ee5a"

enum CoreCollections(val collection: Collection):
  /**
   * CommonProperties encapsulates those Properties that are always common to all Collections:
   *
   * 1. name (1+ values required)
   * 2. description (0+ values required)
   */
  case commonProperties extends CoreCollections(projected.Collection(
    pk = CoreCollectionUUIDs.commonProperties,
    propertyValues = List(
      PropertyValue(
        property = CoreProperties.name.property,
        varcharValues = List("Common Properties")
      ),
      PropertyValue(
        property = CoreProperties.description.property,
        varcharValues = List("A Collection of Properties automatically available to all other Collections")
      )
    )
  ))
  /**
   * CommonPropertiesOfProperties encapsulates those Properties that are always common to all Properties:
   *
   * 1. default_value (0+ values required)
   * 2. min_values (1 value required)
   * 3. max_values (1 value required)
   *
   * As these three Properties reside at a baseline level, the number of values required is hard-coded into the
   * application, since trying to represent this using the data model would recurse infinitely.
   */
  case commonPropertiesOfProperties extends CoreCollections(projected.Collection(
    pk = CoreCollectionUUIDs.commonPropertiesOfProperties,
    properties = List(
      CoreProperties.defaultValue,
      CoreProperties.minValues,
      CoreProperties.maxValues
    ).map(_.property)
  ))