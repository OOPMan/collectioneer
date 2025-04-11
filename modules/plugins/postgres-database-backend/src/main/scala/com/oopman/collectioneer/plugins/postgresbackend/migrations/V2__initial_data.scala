package com.oopman.collectioneer.plugins.postgresbackend.migrations

import com.oopman.collectioneer.{CoreCollections, CoreProperties, given}
import com.oopman.collectioneer.db.entity.raw.Relationship
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.ParentCollection
import com.oopman.collectioneer.plugins.postgresbackend.dao.projected.{CollectionDAOImpl, PropertyDAOImpl}
import com.oopman.collectioneer.plugins.postgresbackend.dao.raw.RelationshipDAOImpl
import org.flywaydb.core.api.migration.{BaseJavaMigration, Context}
import scalikejdbc.DB

/**
 *
 */
class V2__initial_data extends BaseJavaMigration:
  override def canExecuteInTransaction: Boolean = false
  override def migrate(context: Context): Unit =
    DB(context.getConnection).localTx { implicit session =>
      PropertyDAOImpl.createProperties(CoreProperties.values.map(_.property))
      CollectionDAOImpl.createCollections(CoreCollections.values.map(_.collection))
      RelationshipDAOImpl.createRelationships(Seq(
        Relationship(
          collectionPK = CoreCollections.commonProperties,
          relatedCollectionPK = CoreCollections.root,
          relationshipType = ParentCollection
        ),
        Relationship(
          collectionPK = CoreCollections.commonPropertiesOfProperties,
          relatedCollectionPK = CoreCollections.root,
          relationshipType = ParentCollection
        )
      ))
    }
