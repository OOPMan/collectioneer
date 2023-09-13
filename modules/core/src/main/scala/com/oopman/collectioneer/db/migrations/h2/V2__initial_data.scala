package com.oopman.collectioneer.db.migrations.h2

import com.oopman.collectioneer.{CoreProperties, PropertyTypes}
import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}

/**
 *
 */
class V2__initial_data extends BaseJavaMigration {
  override def migrate(context: Context): Unit = {
    val connection = context.getConnection
    val insertProperties = connection.prepareStatement(
      "INSERT INTO properties(pk, property_name, property_type) VALUES (?, ?, ?)"
    )
    // Insert Property rows
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
  }
}
