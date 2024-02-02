package com.oopman.collectioneer.plugins.gatcg.migrations.h2

import com.oopman.collectioneer.db.{Injection, dao}
import com.oopman.collectioneer.plugins.gatcg.GATCGProperties
import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}

class V1__initial_data extends BaseJavaMigration:

  def executeMigration(DAOs: dao.DAOs): Unit =
    val propertyDAO = DAOs.raw.propertyDAO
    propertyDAO.createProperties(GATCGProperties.values.toList.map(_.property))

  override def canExecuteInTransaction: Boolean = false
  override def migrate(context: Context): Unit =
    val connection = context.getConnection
    Injection.produceRun(connection, false)(executeMigration)
