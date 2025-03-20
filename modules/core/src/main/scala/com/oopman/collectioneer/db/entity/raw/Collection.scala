package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.ZonedDateTime
import java.util.UUID

case class Collection
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
) extends raw.Collection:

  def rawCopyWith(pk: UUID = pk,
                  virtual: Boolean = virtual,
                  deleted: Boolean = deleted,
                  created: ZonedDateTime = created,
                  modified: ZonedDateTime = modified): raw.Collection =
    copy(pk = pk, virtual = virtual, deleted = deleted, created = created, modified = modified)
