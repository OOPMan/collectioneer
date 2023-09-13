package com.oopman.collectioneer.db.migrations.h2

import com.oopman.collectioneer.{CoreProperties, PropertyTypes}
import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}

val properties = List(
  (CoreProperties.name, List(PropertyTypes.varchar)),
  (CoreProperties.description, List(PropertyTypes.varchar, PropertyTypes.clob)),
  (CoreProperties.default_value, PropertyTypes.values.toList),
  (CoreProperties.min_value, PropertyTypes.values.toList),
  (CoreProperties.max_value, PropertyTypes.values.toList),
  (CoreProperties.min_values, List(PropertyTypes.int)),
  (CoreProperties.max_values, List(PropertyTypes.int))
)
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
    for ((property, propertyTypes) <- properties) {
      val propertyTypesArray = connection.createArrayOf("VARCHAR", propertyTypes.map(_.toString).toArray)
      insertProperties.setString(1, property.uuid.toString)
      insertProperties.setString(2, property.toString)
      insertProperties.setArray(3, propertyTypesArray)
      insertProperties.addBatch()
    }
    insertProperties.executeBatch()
  }
}
