package com.oopman.collectioneer.db.queries.h2

import com.oopman.collectioneer.db.entity.Collection
import doobie._
import doobie.implicits._
import doobie.util.fragments
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import cats.implicits._
import doobie.implicits.javatimedrivernative.*
import doobie.h2.implicits.*
import org.h2.api.TimestampWithTimeZone
import java.time.ZonedDateTime


def retrieveCollectionById(id: Long): Query0[Collection] =
  sql"""SELECT id, name, description, virtual, deleted, created, modified
       |FROM collections
       |WHERE id = $id""".stripMargin.query[Collection]

def retrieveCollectionsByName(name: String): Query0[Collection] =
  sql"""SELECT id, name, description, virtual, deleted, created, modified
       |FROM collections
       |WHERE name = $name""".stripMargin.query[Collection]

def retrieveCollectionsByVirtual(virtual: Boolean): Query0[Collection] =
  sql"""SELECT id, name, description, virtual, deleted, created, modified
       |FROM collections
       |WHERE virtual = $virtual""".stripMargin.query[Collection]

def retrieveCollectionsByDeleted(deleted: Boolean): Query0[Collection] =
  sql"""SELECT id, name, description, virtual, deleted, created, modified
       |FROM collections
       |WHERE deleted = $deleted""".stripMargin.query[Collection]

def createCollection(name: String, description: Option[String] = None, virtual: Boolean = false): Update0 =
  sql"""INSERT INTO collections(name, description, virtual)
       |VALUES ($name, $description, $virtual)""".stripMargin.update

def updateCollection(id: Long, name: Option[String], description: Option[String], virtual: Option[Boolean]): Update0 =
  val updates = List(
    name.map(value => fr"name = $value"),
    description.map(value => fr"description = $value"),
    virtual.map(value => fr"virtual = $value"),
    Some(fr"modified = CURRENT_TIMESTAMP()")
  )
  val setFragment = fragments.setOpt(updates: _*)
  (fr"UPDATE collections" ++ setFragment ++ fr"WHERE id = $id").update

def deleteCollection(id: Long): Update0 =
  sql"""UPDATE collections
       |SET deleted = true, modified = CURRENT_TIMESTAMP()
       |WHERE id = $id""".stripMargin.update
