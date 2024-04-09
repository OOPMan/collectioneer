package com.oopman.collectioneer.plugins.postgresbackend.entity.raw

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.traits.entity.raw.{Relationship, RelationshipType}
import scalikejdbc.*

import java.util.UUID

object Relationship extends SQLSyntaxSupport[raw.Relationship]:
  override val schemaName = Some("public")
  override val tableName = "relationship"
  val r1 = Relationship.syntax("r1")
  val r2 = Relationship.syntax("r2")
  
  def apply(rs: WrappedResultSet) =
    raw.Relationship(
      pk = UUID.fromString(rs.string("pk")),
      collectionPK = UUID.fromString(rs.string("collection_pk")), 
      relatedCollectionPK = UUID.fromString(rs.string("related_collection_pk")), 
      relationshipType = RelationshipType.valueOf(rs.string("relationship_type")),
      index = rs.int("index"), 
      created = rs.dateTime("created"), 
      modified = rs.dateTime("modified")
    )
  
  def relationshipSeqToBatchUpsertSeq(relationships: Seq[Relationship]): Seq[Seq[Any]] =
    relationships.map(relationship => Seq(
      relationship.pk,
      relationship.collectionPK,
      relationship.relatedCollectionPK,
      relationship.relationshipType.toString,
      relationship.index
    ))
