package com.oopman.collectioneer.db.h2.dao.raw

import com.oopman.collectioneer.db.traits.dao.raw.RelationshipDAO
import com.oopman.collectioneer.db.traits.entity.raw.Relationship
import scalikejdbc.DBSession

import java.util.UUID

object RelationshipDAO extends RelationshipDAO:
  def createRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int] = ???

  def createOrUpdateRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int] = ???

  def deleteRelationships(relationships: Seq[Relationship])(implicit session: DBSession): Array[Int] = ???

  def getAllMatchingCollectionPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): List[Relationship] = ???

  def getAllMatchingRelatedCollectionPKs(relatedCollectionPKs: Seq[UUID])(implicit session: DBSession): List[Relationship] = ???
  
