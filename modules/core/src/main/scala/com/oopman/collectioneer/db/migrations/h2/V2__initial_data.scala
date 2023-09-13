package com.oopman.collectioneer.db.migrations.h2

import com.oopman.collectioneer.{CoreCollections, CoreProperties, PropertyTypes}
import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}

/**
 *
 */
class V2__initial_data extends BaseJavaMigration {
  override def migrate(context: Context): Unit = {
    val connection = context.getConnection
    // Insert Property rows
    val insertProperties = connection.prepareStatement(
      "INSERT INTO properties(pk, property_name, property_type) VALUES (?, ?, ?)"
    )
    for (coreProperty <- CoreProperties.values) {
      val uuid = coreProperty.uuid.toString
      val propertyName = coreProperty.toString
      val propertyTypesArray = connection.createArrayOf("VARCHAR", coreProperty.propertyTypes.map(_.toString).toArray)
      insertProperties.setString(1, uuid)
      insertProperties.setString(2, propertyName)
      insertProperties.setArray(3, propertyTypesArray)
      insertProperties.addBatch()
    }
    insertProperties.executeBatch()
    // Insert PropertyValueSet and Collection rows
    val insertPropertyValueSets = connection.prepareStatement(
      "INSERT INTO property_value_sets(pk) VALUES (?)"
    )
    val insertCollections = connection.prepareStatement(
      "INSERT INTO collections(pk) VALUES (?)"
    )
    for (coreCollection <- CoreCollections.values) {
      val uuid = coreCollection.uuid.toString
      insertPropertyValueSets.setString(1, uuid)
      insertPropertyValueSets.addBatch()
      insertCollections.setString(1, uuid)
      insertCollections.addBatch()
    }
    insertPropertyValueSets.executeBatch()
    insertCollections.executeBatch()
    // Associate Properties with Collections
    val insertPropertiesCollections = connection.prepareStatement(
      "INSERT INTO properties__collections(property_pk, collection_pk, property_value_set_pk) VALUES (?, ?, ?)"
    )
    for {
      coreCollection <- CoreCollections.values
      property <- coreCollection.properties
    } {
      val collectionUUID = coreCollection.uuid.toString
      val propertyUUID = property.uuid.toString
      insertPropertiesCollections.setString(1, propertyUUID)
      insertPropertiesCollections.setString(2, collectionUUID)
      insertPropertiesCollections.setString(3, collectionUUID)
      insertPropertiesCollections.addBatch()
    }
    insertPropertiesCollections.executeBatch()
  }
}
