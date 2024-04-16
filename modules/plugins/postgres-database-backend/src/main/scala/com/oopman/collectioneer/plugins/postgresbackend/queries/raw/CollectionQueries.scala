package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import com.oopman.collectioneer.plugins.postgresbackend.entity.raw.Collection
import scalikejdbc.*

import java.util.UUID

object CollectionQueries:
  def insert =
    sql"""
          INSERT INTO collection(pk, virtual, deleted)
          VALUES ( ?, ?, ? );
       """

  def upsert =
    sql"""
          INSERT INTO collection(pk, virtual, deleted)
          VALUES (?, ?, ?)
          ON CONFLICT(pk) DO UPDATE
          set virtual = excluded.virtual, deleted = excluded.deleted, modified = now()
       """

  def all =
    sql"""
         SELECT ${Collection.c1.result.*}
         FROM ${Collection.as(Collection.c1)}
    """

  def allMatchingPKs(collectionPKs: Seq[UUID]) =
    sql"""
         SELECT ${Collection.c1.result.*}
         FROM ${Collection.as(Collection.c1)}
         WHERE ${Collection.c1.pk} IN (${collectionPKs})
       """

  def allInnerJoining(fromSQL: String, fromColumn: String) =
    SQL(s"""
          SELECT c1.*
          FROM $fromSQL AS f1
          INNER JOIN collection AS c1 ON c1.pk = f1.$fromColumn
        """)
