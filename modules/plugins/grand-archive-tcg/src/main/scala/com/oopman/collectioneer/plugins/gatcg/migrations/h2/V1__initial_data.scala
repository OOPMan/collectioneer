package com.oopman.collectioneer.plugins.gatcg.migrations.h2

import com.oopman.collectioneer.db.{Injection, dao}
import com.oopman.collectioneer.plugins.gatcg.properties
import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}

class V1__initial_data extends BaseJavaMigration:

  def executeMigration(DAOs: dao.DAOs): Unit =
    val propertyDAO = DAOs.projected.propertyDAO
    propertyDAO.createOrUpdateProperties(properties.CardProperties.values.map(_.property))
    propertyDAO.createOrUpdateProperties(properties.CirculationProperties.values.map(_.property))
    propertyDAO.createOrUpdateProperties(properties.EditionProperties.values.map(_.property))
    propertyDAO.createOrUpdateProperties(properties.SetProperties.values.map(_.property))

  override def canExecuteInTransaction: Boolean = false
  override def migrate(context: Context): Unit =
    val connection = context.getConnection
    Injection.produceRun(connection, false)(executeMigration)
