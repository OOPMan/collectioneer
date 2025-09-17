package com.oopman.collectioneer.plugins.postgresbackend.migrations

import com.oopman.collectioneer.plugins.postgresbackend.dao.projected.{CollectionDAOImpl, PropertyDAOImpl}
import com.oopman.collectioneer.plugins.postgresbackend.dao.raw.RelationshipDAOImpl
import com.oopman.collectioneer.{CoreCollections, CoreProperties, CoreRelationships}
import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}
import scalikejdbc.DB

class R__core_data extends BaseJavaMigration:
  override def canExecuteInTransaction: Boolean = false
  override def migrate(context: Context): Unit =
    DB(context.getConnection).localTx { implicit session =>
      PropertyDAOImpl.createOrUpdateProperties(CoreProperties.values.map(_.property))
      CollectionDAOImpl.createOrUpdateCollections(CoreCollections.values.map(_.collection))
      RelationshipDAOImpl.createOrUpdateRelationships(CoreRelationships.values.map(_.relationship))
    }
