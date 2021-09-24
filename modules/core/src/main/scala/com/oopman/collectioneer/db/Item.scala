package com.oopman.collectioneer.db

import doobie.*
import doobie.implicits.*
import doobie.util.fragments
import doobie.util.ExecutionContexts
import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import doobie.implicits.javatimedrivernative.*
import doobie.h2.implicits.*
import org.h2.api.TimestampWithTimeZone

import java.time.ZonedDateTime

case class Item
(
  id: Long,
  name: String,
  description: Option[String],
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
)

def retrieveItemById(id: Long): Query0[Item] =
  sql"""SELECT id, name, description, virtual, deleted, created, modified
       |FROM items
       |WHERE id = $id""".stripMargin.query[Item]