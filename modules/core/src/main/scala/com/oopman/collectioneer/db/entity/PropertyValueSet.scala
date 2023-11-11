package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

trait PropertyValueSet:
  val pk: UUID
  val created: ZonedDateTime

object PropertyValueSet:
  def propertyValueSetListToBatchInsertUpsertSeqList(pvs: List[PropertyValueSet]): List[Seq[Any]] =
    pvs.map(p => Seq(p.pk.toString))
