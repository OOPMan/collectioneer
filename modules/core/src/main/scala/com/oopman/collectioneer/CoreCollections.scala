package com.oopman.collectioneer

import com.oopman.collectioneer.db.entity.projected
import com.oopman.collectioneer.db.traits.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.given

import java.util.UUID

private object CoreCollectionUUIDs:
  val root = "2c18873e-3cda-4002-96d9-86f5676875a3"
  val properties = "6efb8b19-a234-46ce-bfd5-8a64dbc682aa"
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
   * This Collection exists to root a parallel, hidden tree of Collections used to group Properties. This will be used
   * by user interfaces that manage the creation of Collections, allowing for Properties to be added from groups described
   * by the Collections that reside below this Collection
   */
  case properties extends CoreCollections(projected.Collection(
    pk = CoreCollectionUUIDs.properties,
    propertyValues = Map(
      CoreProperties.name -> projected.PropertyValue(textValues = "Properties" :: Nil),
      CoreProperties.description ->
        projected.PropertyValue(textValues = "A secondary root Collection. Collections beneath this one are used to group Properties" :: Nil)
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
   * CommonPropertiesOfProperties encapsulates those Properties that are always common to all Properties
   */
  case commonPropertiesOfProperties extends CoreCollections(projected.Collection(
    pk = CoreCollectionUUIDs.commonPropertiesOfProperties,
    properties = List(
      CoreProperties.defaultValue,
      CoreProperties.minValue,
      CoreProperties.minValues,
      CoreProperties.maxValue,
      CoreProperties.maxValues,
      CoreProperties.visible
    ).map(_.property),
    propertyValues = Map(
      CoreProperties.name ->
        projected.PropertyValue(textValues = "Properties of Properties" :: Nil),
      CoreProperties.description ->
        projected.PropertyValue(textValues = "A Collection of Properties that are only for use as Properties of other Properties" :: Nil)
    )
  ))