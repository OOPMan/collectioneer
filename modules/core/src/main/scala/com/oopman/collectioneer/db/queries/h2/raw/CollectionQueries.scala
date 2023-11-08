package com.oopman.collectioneer.db.queries.h2.raw

import com.oopman.collectioneer.db.entity.raw.c1
import com.oopman.collectioneer.db.entity.raw.Collection
import scalikejdbc.*

import java.util.UUID

object CollectionQueries:
  def insert =
    sql"""
          INSERT INTO COLLECTION(pk, virtual, deleted)
          VALUES ( ?, ?, ? );
       """

  def upsert =
    sql"""
          MERGE INTO COLLECTION(pk, virtual, deleted, modified)
          KEY (pk)
          VALUES ( ?, ?, ?, ? );
       """

  def all =
    sql"""
         SELECT ${c1.result.*}
         FROM ${Collection.as(c1)}
    """

  def allMatchingPKs(collectionPKs: Seq[UUID]) =
    sql"""
         SELECT ${c1.result.*}
         FROM ${Collection.as(c1)}
         WHERE ${c1.pk} IN (${collectionPKs})
       """
