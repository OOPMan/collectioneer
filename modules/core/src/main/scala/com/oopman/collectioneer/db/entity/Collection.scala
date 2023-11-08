package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

trait Collection:
  val pk: UUID
  val virtual: Boolean
  val deleted: Boolean
  val created: ZonedDateTime
  val modified: ZonedDateTime
  
object Collection:
  def collectionsListToBatchInsertSeqList(collections: List[Collection]): List[Seq[Any]] =
    collections.map(c => Seq(
      c.pk.toString,
      c.virtual,
      c.deleted
    ))
    
  def collectionsListToBatchUpsertSeqList(collections: List[Collection]): List[Seq[Any]] =
    collections.map(c => Seq(
      c.pk.toString,
      c.virtual,
      c.deleted,
      c.modified
    ))