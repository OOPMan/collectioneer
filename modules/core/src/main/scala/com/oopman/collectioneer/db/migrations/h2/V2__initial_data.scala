package com.oopman.collectioneer.db.migrations.h2

import com.oopman.collectioneer.db.entity.PropertyTypes
import com.oopman.collectioneer.{CoreCollections, CoreProperties}
import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}

/**
 *
 */
class V2__initial_data extends BaseJavaMigration:
  override def migrate(context: Context): Unit =
    val connection = context.getConnection
    // Insert Property rows
    val insertProperties = connection.prepareStatement(
      "INSERT INTO properties(pk, property_name, property_type) VALUES (?, ?, ?)"
    )
    for
      coreProperty <- CoreProperties.values
    do
      val uuid = coreProperty.uuid.toString
      val propertyName = coreProperty.toString
      val propertyTypesArray = connection.createArrayOf("VARCHAR", coreProperty.propertyTypes.map(_.toString).toArray)
      insertProperties.setString(1, uuid)
      insertProperties.setString(2, propertyName)
      insertProperties.setArray(3, propertyTypesArray)
      insertProperties.addBatch()

    insertProperties.executeBatch()
    // Insert PropertyValueSet and Collection rows
    val insertPropertyValueSets = connection.prepareStatement(
      "INSERT INTO property_value_sets(pk) VALUES (?)"
    )
    val insertCollections = connection.prepareStatement(
      "INSERT INTO collections(pk) VALUES (?)"
    )
    for
      coreCollection <- CoreCollections.values
    do
      val uuid = coreCollection.uuid.toString
      insertPropertyValueSets.setString(1, uuid)
      insertPropertyValueSets.addBatch()
      insertCollections.setString(1, uuid)
      insertCollections.addBatch()

    for
      coreCollection <- CoreCollections.values
      (_, uuid) <- coreCollection.propertyPropertyValueSets
    do
      insertPropertyValueSets.setString(1, uuid)
      insertPropertyValueSets.addBatch()

    insertPropertyValueSets.executeBatch()
    insertCollections.executeBatch()
    // Associate Properties with Collections
    val insertPropertiesCollections = connection.prepareStatement(
      "INSERT INTO properties__collections(property_pk, collection_pk, property_value_set_pk, relationship) VALUES (?, ?, ?, ?)"
    )
    for
      coreCollection <- CoreCollections.values
      property <- coreCollection.properties
    do
      val collectionUUID = coreCollection.uuid.toString
      val propertyUUID = property.uuid.toString
      insertPropertiesCollections.setString(1, propertyUUID)
      insertPropertiesCollections.setString(2, collectionUUID)
      insertPropertiesCollections.setString(3, collectionUUID)
      insertPropertiesCollections.setString(4, "property_of_collection")
      insertPropertiesCollections.addBatch()
    // Associate CommonPropertiesOfProperties with Properties of CommonProperties
    val commonPropertiesOfPropertiesUUID = CoreCollections.CommonPropertiesOfProperties.uuid.toString
    // name
    val namePropertyValueSetPk = CoreCollections.CommonProperties.propertyPropertyValueSets(CoreProperties.name)
    insertPropertiesCollections.setString(1, CoreProperties.name.uuid.toString)
    insertPropertiesCollections.setString(2, commonPropertiesOfPropertiesUUID)
    insertPropertiesCollections.setString(3, namePropertyValueSetPk)
    insertPropertiesCollections.setString(4, "collection_of_properties_of_property")
    insertPropertiesCollections.addBatch()
    // description
    val descriptionPropertyValueSetPk = CoreCollections.CommonProperties.propertyPropertyValueSets(CoreProperties.description)
    insertPropertiesCollections.setString(1, CoreProperties.description.uuid.toString)
    insertPropertiesCollections.setString(2, commonPropertiesOfPropertiesUUID)
    insertPropertiesCollections.setString(3, descriptionPropertyValueSetPk)
    insertPropertiesCollections.setString(4, "collection_of_properties_of_property")
    insertPropertiesCollections.addBatch()
    insertPropertiesCollections.executeBatch()
    // TODO: Insert PropertyValues for the properties of name and description to set in place sensible defaults
    val insertPropertyValueStrings = connection.prepareStatement(
      "INSERT INTO property_value_varchars(property_value_set_pk, property_pk, property_value) VALUES (?, ?, ?)"
    )
    val insertPropertyValueInts = connection.prepareStatement(
      "INSERT INTO property_value_ints(property_value_set_pk, property_pk, property_value) VALUES (?, ?, ?)"
    )
    // CommonProperties.name
    insertPropertyValueStrings.setString(1, CoreCollections.CommonProperties.uuid.toString)
    insertPropertyValueStrings.setString(2, CoreProperties.name.uuid.toString)
    insertPropertyValueStrings.setString(3, "Common Properties")
    insertPropertyValueStrings.addBatch()
    // CommonProperties.name.min_values
    insertPropertyValueInts.setString(1, namePropertyValueSetPk)
    insertPropertyValueInts.setString(2, CoreProperties.min_values.uuid.toString)
    insertPropertyValueInts.setInt(3, 1)
    insertPropertyValueInts.addBatch()
    // CommonProperties.name.max_values
    insertPropertyValueInts.setString(1, namePropertyValueSetPk)
    insertPropertyValueInts.setString(2, CoreProperties.max_values.uuid.toString)
    insertPropertyValueInts.setInt(3, Int.MaxValue)
    insertPropertyValueInts.addBatch()
    // CommonProperties.description
    insertPropertyValueStrings.setString(1, CoreCollections.CommonProperties.uuid.toString)
    insertPropertyValueStrings.setString(2, CoreProperties.description.uuid.toString)
    insertPropertyValueStrings.setString(3, "A Collection of Properties automatically available to all other Collections")
    insertPropertyValueStrings.addBatch()
    // CommonProperties.description.min_values
    insertPropertyValueInts.setString(1, descriptionPropertyValueSetPk)
    insertPropertyValueInts.setString(2, CoreProperties.min_values.uuid.toString)
    insertPropertyValueInts.setInt(3, 0)
    insertPropertyValueInts.addBatch()
    // CommonProperties.description.max_values
    insertPropertyValueInts.setString(1, descriptionPropertyValueSetPk)
    insertPropertyValueInts.setString(2, CoreProperties.max_values.uuid.toString)
    insertPropertyValueInts.setInt(3, Int.MaxValue)
    insertPropertyValueInts.addBatch()
    insertPropertyValueInts.executeBatch()
