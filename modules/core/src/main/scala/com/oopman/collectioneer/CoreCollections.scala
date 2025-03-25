package com.oopman.collectioneer

import com.oopman.collectioneer.db.entity.projected
import com.oopman.collectioneer.db.traits.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.given

import java.util.UUID

private object CoreCollectionUUIDs:
  val root = "2c18873e-3cda-4002-96d9-86f5676875a3"
  val commonProperties = "709113ad-6ca0-4d08-b9f4-a526d81da549"
  val commonPropertiesOfProperties = "4a3dfbf0-3d68-4604-a471-5c4451a3ee5a"

enum CoreCollections(val collection: Collection):
  /**
   * This Collection exists to root the tree of Collections modelled by the data
   */
  case root extends CoreCollections(projected.Collection(
    pk = CoreCollectionUUIDs.root,
    propertyValues = Map(
      CoreProperties.name ->
        projected.PropertyValue(textValues = List("Root")),
      CoreProperties.description ->
        projected.PropertyValue(textValues = List("The Root Collection under which all other Collections should reside"))
    )
  ))
  /**
   * CommonProperties encapsulates those Properties that are always common to all Collections:
   *
   * 1. name (1+ values required)
   * 2. description (0+ values required)
   */
  case commonProperties extends CoreCollections(projected.Collection(
    pk = CoreCollectionUUIDs.commonProperties,
    propertyValues = Map(
      CoreProperties.name ->
        projected.PropertyValue(textValues = List("Common Properties")),
      CoreProperties.description ->
        projected.PropertyValue(textValues = List("A Collection of Properties automatically available to all other Collections")
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