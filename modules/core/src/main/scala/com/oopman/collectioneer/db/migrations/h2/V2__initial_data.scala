package com.oopman.collectioneer.db.migrations.h2

import com.oopman.collectioneer.{CoreCollections, CoreProperties}
import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.dao
import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}
import scalikejdbc.DB

/**
 *
 */
class V2__initial_data extends BaseJavaMigration:
  override def canExecuteInTransaction: Boolean = false
  override def migrate(context: Context): Unit =
    val connection = context.getConnection
    val db = DB(connection).autoClose(false)
    // Insert Property rows
    val propertiesDAO = new dao.raw.PropertyDAO(() => db)
    propertiesDAO.createProperties(CoreProperties.values.toList.map(property => entity.raw.Property(
      pk = property.uuid,
      propertyName = property.toString,
      propertyTypes = property.propertyTypes
    )))
    // Insert PropertyValueSet and Collection rows
    val insertPropertyValueSets = connection.prepareStatement(
      "INSERT INTO property_value_set(pk) VALUES (?)"
    )
    val insertCollections = connection.prepareStatement(
      "INSERT INTO collection(pk) VALUES (?)"
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
      "INSERT INTO property__collection(property_pk, collection_pk, property_value_set_pk, relationship) VALUES (?, ?, ?, ?)"
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
    val insertPropertyValueVarchars = connection.prepareStatement(
      "INSERT INTO property_value_varchar(property_value_set_pk, property_pk, property_value) VALUES (?, ?, ?)"
    )
    val insertPropertyValueInts = connection.prepareStatement(
      "INSERT INTO property_value_int(property_value_set_pk, property_pk, property_value) VALUES (?, ?, ?)"
    )
    // CommonProperties.name
    insertPropertyValueVarchars.setString(1, CoreCollections.CommonProperties.uuid.toString)
    insertPropertyValueVarchars.setString(2, CoreProperties.name.uuid.toString)
    insertPropertyValueVarchars.setString(3, "Common Properties")
    insertPropertyValueVarchars.addBatch()
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
    insertPropertyValueVarchars.setString(1, CoreCollections.CommonProperties.uuid.toString)
    insertPropertyValueVarchars.setString(2, CoreProperties.description.uuid.toString)
    insertPropertyValueVarchars.setString(3, "A Collection of Properties automatically available to all other Collections")
    insertPropertyValueVarchars.addBatch()
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
    // Execute batch
    insertPropertyValueVarchars.executeBatch()
    insertPropertyValueInts.executeBatch()
