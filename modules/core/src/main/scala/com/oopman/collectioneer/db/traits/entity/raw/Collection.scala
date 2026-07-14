package com.oopman.collectioneer.db.traits.entity.raw

import com.oopman.collectioneer.db.traits

import java.time.OffsetDateTime
import java.util.UUID

trait Collection:
  def pk: UUID
  def virtual: Boolean
  def deleted: Boolean
  def created: OffsetDateTime
  def modified: OffsetDateTime
  
  def rawCopyWith(pk: UUID = pk,
                  virtual: Boolean = virtual,
                  deleted: Boolean = deleted,
                  created: OffsetDateTime = created,
                  modified: OffsetDateTime = modified): Collection

  override def equals(obj: Any): Boolean = obj match {
    case collection: Collection => pk.equals(collection.pk)
    case _ => false
  }
  
  def exactlyEquals(obj: Any): Boolean = super.equals(obj)

  override def hashCode(): Int = pk.hashCode()

  override def toString: String = s"Raw Collection ($pk)"


trait HasCollection:
  def collection: Collection