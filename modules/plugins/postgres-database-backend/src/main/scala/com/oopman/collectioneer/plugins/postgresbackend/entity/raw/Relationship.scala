package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.traits.entity.raw.{Relationship, HasTopLevelCollectionPKAndLevel, RelationshipType}
import scalikejdbc.*

import java.util.UUID

object Relationship extends SQLSyntaxSupport[raw.Relationship]:
  override val schemaName = Some("public")
  override val tableName = "relationship"
  val r1 = Relationship.syntax("r1")
  val r2 = Relationship.syntax("r2")
  
  def wrappedResultSetToRelationship(rs: WrappedResultSet): Relationship =
    raw.Relationship(
      pk = UUID.fromString(rs.string("pk")),
      collectionPK = UUID.fromString(rs.string("collection_pk")), 
      relatedCollectionPK = UUID.fromString(rs.string("related_collection_pk")), 
      relationshipType = RelationshipType.valueOf(rs.string("relationship_type")),
      index = rs.int("index"), 
      created = rs.dateTime("created"), 
      modified = rs.dateTime("modified")
    )
    
  def wrappedResultSetToRelationshipAndHasTopLevelCollectionPKAndLevel(rs: WrappedResultSet): Relationship & HasTopLevelCollectionPKAndLevel =
    new raw.Relationship(
      pk = UUID.fromString(rs.string("pk")),
      collectionPK = UUID.fromString(rs.string("collection_pk")),
      relatedCollectionPK = UUID.fromString(rs.string("related_collection_pk")),
      relationshipType = RelationshipType.valueOf(rs.string("relationship_type")),
      index = rs.int("index"),
      created = rs.dateTime("created"),
      modified = rs.dateTime("modified")
    ) with HasTopLevelCollectionPKAndLevel(
      topLevelCollectionPK = UUID.fromString(rs.string("top_level_collection_pk")),
      level = rs.int("level")
    )
  
  def relationshipSeqToBatchUpsertSeq(relationships: Seq[Relationship]): Seq[Seq[Any]] =
    relationships.map(relationship => Seq(
      relationship.pk,
      relationship.collectionPK,
      relationship.relatedCollectionPK,
      relationship.relationshipType.toString,
      relationship.index
    ))
