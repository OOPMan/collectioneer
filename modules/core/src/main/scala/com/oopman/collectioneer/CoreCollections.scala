package com.oopman.collectioneer

import java.util.UUID

enum CoreCollections
(
  val uuid: UUID,
  val properties: List[CoreProperties],
  val propertyPropertyValueSets: Map[CoreProperties, String]
) {
  /**
   * CommonProperties encapsulates those Properties that are always common to all Collections:
   *
   * 1. name (1+ values required)
   * 2. description (0+ values required)
   */
  case CommonProperties extends CoreCollections(
    UUID.fromString("709113ad-6ca0-4d08-b9f4-a526d81da549"),
    List(CoreProperties.name, CoreProperties.description),
    Map(
      CoreProperties.name -> "7a235eff-99d1-4412-accd-23066d5298a1",
      CoreProperties.description -> "6030f26b-88b5-43a2-b5ec-db3349777808"
    )
  )
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
  case CommonPropertiesOfProperties extends CoreCollections(
    UUID.fromString("4a3dfbf0-3d68-4604-a471-5c4451a3ee5a"),
    List(CoreProperties.default_value, CoreProperties.min_values, CoreProperties.max_values),
    Map()
  )
}