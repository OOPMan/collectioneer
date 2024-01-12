package com.oopman.collectioneer.db.traits.entity

import java.time.ZonedDateTime
import java.util.UUID

trait PropertyValueSet:
  val pk: UUID
  val created: ZonedDateTime

object PropertyValueSet:
  def propertyValueSetSeqToBatchInsertUpsertSeqSeq(pvs: Seq[PropertyValueSet]): Seq[Seq[Any]] =
    pvs.map(p => Seq(p.pk.toString))
