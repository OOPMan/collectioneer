package com.oopman.collectioneer.db.h2.queries.raw

import com.oopman.collectioneer.db.entity.raw.Collection
import com.oopman.collectioneer.db.traits.queries.raw.CollectionQueries
import scalikejdbc.*

import java.util.UUID

object CollectionQueries extends CollectionQueries:
  val insert =
    sql"""
          INSERT INTO COLLECTION(pk, virtual, deleted)
          VALUES ( ?, ?, ? );
       """

  val upsert =
    sql"""
          MERGE INTO COLLECTION(pk, virtual, deleted, modified)
          KEY (pk)
          VALUES ( ?, ?, ?, ? );
       """

  val all =
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
