package com.oopman.collectioneer.db.h2.queries.raw

import com.oopman.collectioneer.db.entity.raw.Collection
import com.oopman.collectioneer.db.traits.queries.raw.CollectionQueries
import scalikejdbc.*

import java.util.UUID

object CollectionQueries extends CollectionQueries:
  def insert =
    sql"""
          INSERT INTO COLLECTION(pk, virtual, deleted)
          VALUES ( ?, ?, ? );
       """

  def upsert =
    sql"""
          MERGE INTO COLLECTION(pk, virtual, deleted, modified)
          KEY (pk)
          VALUES (?, ?, ?, CURRENT_TIMESTAMP());
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
