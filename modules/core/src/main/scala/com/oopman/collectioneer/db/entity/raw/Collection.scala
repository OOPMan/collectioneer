package com.oopman.collectioneer.db.entity.raw

import com.oopman.collectioneer.db.traits.entity.raw

import java.time.OffsetDateTime
import java.util.UUID

case class Collection
(
  pk: UUID = UUID.randomUUID(),
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: OffsetDateTime = OffsetDateTime.now(),
  modified: OffsetDateTime = OffsetDateTime.now(),
) extends raw.Collection:

  def rawCopyWith(pk: UUID = pk,
                  virtual: Boolean = virtual,
                  deleted: Boolean = deleted,
                  created: OffsetDateTime = created,
                  modified: OffsetDateTime = modified): raw.Collection =
    copy(pk = pk, virtual = virtual, deleted = deleted, created = created, modified = modified)
