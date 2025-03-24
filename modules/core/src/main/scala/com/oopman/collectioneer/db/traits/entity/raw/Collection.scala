package com.oopman.collectioneer.db.traits.entity.raw

import com.oopman.collectioneer.db.traits

import java.time.ZonedDateTime
import java.util.UUID

trait Collection:
  val pk: UUID
  val virtual: Boolean
  val deleted: Boolean
  val created: ZonedDateTime
  val modified: ZonedDateTime
  
  def rawCopyWith(pk: UUID = pk,
                  virtual: Boolean = virtual,
                  deleted: Boolean = deleted,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): Collection

  override def equals(obj: Any): Boolean = obj match {
    case collection: Collection => pk.equals(collection.pk)
    case _ => false
  }
  
  def exactlyEquals(obj: Any): Boolean = super.equals(obj)

  override def hashCode(): Int = pk.hashCode()

  override def toString: String = s"Raw Collection ($pk)"
