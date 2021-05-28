package com.oopman.collectioneer.db

import doobie._
import doobie.implicits._
import doobie.implicits.javatimedrivernative._
import doobie.h2.implicits._
import org.h2.api.TimestampWithTimeZone

import java.time.ZonedDateTime


case class Collection
(
  id: Long,
  name: String,
  description: Option[String],
  virtual: Boolean = false,
  deleted: Boolean = false,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now(),
)


def retrieveCollectionById(id: Long) =
  sql"""SELECT id, name, description, virtual, deleted, created, modified
       |FROM collections
       |WHERE id = $id""".stripMargin.query[Collection]

def retrieveCollectionsByName(name: String) =
  sql"""SELECT id, name, description, virtual, deleted, created, modified
       |FROM collections
       |WHERE name = $name""".stripMargin.query[Collection]

def retrieveCollectionsByVirtual(virtual: Boolean) =
  sql"""SELECT id, name, description, virtual, deleted, created, modified
       |FROM collections
       |WHERE virtual = $virtual""".stripMargin.query[Collection]

def retrieveCollectionsByDeleted(deleted: Boolean) =
  sql"""SELECT id, name, description, virtual, deleted, created, modified
       |FROM collections
       |WHERE deleted = $deleted""".stripMargin.query[Collection]

def createCollection(name: String, description: Option[String] = None, virtual: Boolean = false) =
  sql"""INSERT INTO collections(name, description, virtual)
       |VALUES ($name, $description, $virtual)""".stripMargin.update
