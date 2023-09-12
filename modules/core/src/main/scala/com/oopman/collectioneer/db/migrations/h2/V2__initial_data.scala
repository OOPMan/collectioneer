package com.oopman.collectioneer.db.migrations.h2

import com.oopman.collectioneer.CoreProperties
import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}

val allTypes = List(
  "varchar", "varbinary", "tinyint", "smallint", "int", "bigint", "numeric", "float", "double", "boolean", "date",
  "time", "timestamp", "clob", "blob", "uuid", "json"
)
val properties = List(
  (CoreProperties.name, List("varchar")),
  (CoreProperties.description, List("varchar", "clob")),
  (CoreProperties.default_value, allTypes),
  (CoreProperties.min_values, List("int")),
  (CoreProperties.max_values, List("int"))
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
    for ((property, property_type) <- properties) {
      insertProperties.setString(1, property.uuid.toString)
      insertProperties.setString(2, property.toString)
      insertProperties.setArray(3, connection.createArrayOf("VARCHAR", property_type.toArray))
      insertProperties.addBatch()
    }
    insertProperties.executeBatch()


  }
}
