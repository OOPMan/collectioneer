package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.traits
import scalikejdbc.*

import java.util.UUID

object PropertyCollection extends SQLSyntaxSupport[raw.PropertyCollection]:
  override val schemaName = Some("public")
  override val tableName = "property_collection"
  val pc1 = PropertyCollection.syntax("pc1")
  val pc2 = PropertyCollection.syntax("pc2")

  def apply(pc: ResultName[raw.PropertyCollection])(rs: WrappedResultSet): raw.PropertyCollection =
    apply(rs, pc.propertyPK, pc.collectionPK, pc.index, pc.propertyCollectionRelationshipType, pc.created, pc.modified)

  def apply(rs: WrappedResultSet,
            propertyPKColumn: String = "property_pk",
            collectionPKColumn: String = "collection_pk",
            indexColumn: String = "index",
            propertyCollectionRelationshipTypeColumn: String = "property_collection_relationship_type",
            createdColumn: String = "created",
            modifiedColumn: String = "modified"): raw.PropertyCollection =
    raw.PropertyCollection(
      propertyPK = UUID.fromString(rs.string(propertyPKColumn)),
      collectionPK = UUID.fromString(rs.string(collectionPKColumn)),
      index = rs.int(indexColumn),
      propertyCollectionRelationshipType = traits.entity.raw.PropertyCollectionRelationshipType.valueOf(rs.string(propertyCollectionRelationshipTypeColumn)),
      created = rs.dateTime(createdColumn),
      modified = rs.dateTime(modifiedColumn)
    )

  def propertyCollectionSeqToBatchUpsertSeq(propertyCollections: Seq[traits.entity.raw.PropertyCollection]): Seq[Seq[Any]] =
    propertyCollections.map(pc => Seq(
      pc.propertyPK,
      pc.collectionPK,
      pc.index,
      pc.propertyCollectionRelationshipType.toString
    ))
