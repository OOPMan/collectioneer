package com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

trait Collection:
  val pk: UUID
  val virtual: Boolean
  val deleted: Boolean
  val created: ZonedDateTime
  val modified: ZonedDateTime
  
object Collection:
  def collectionsListToBatchInsertSeqList(collections: Seq[Collection]): Seq[Seq[Any]] =
    collections.map(c => Seq(
      c.pk.toString,
      c.virtual,
      c.deleted
    ))
    
  def collectionsListToBatchUpsertSeqList(collections: Seq[Collection]): Seq[Seq[Any]] =
    collections.map(c => Seq(
      c.pk.toString,
      c.virtual,
      c.deleted
    ))