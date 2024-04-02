package com.oopman.collectioneer.plugins.postgresbackend.queries.raw

import com.oopman.collectioneer.db.traits.queries.raw.CollectionQueries
import com.oopman.collectioneer.plugins.postgresbackend.entity.raw.Collection
import scalikejdbc.*

import java.util.UUID

object CollectionQueries extends CollectionQueries:
  def insert =
    sql"""
          INSERT INTO collection(pk, virtual, deleted)
          VALUES ( ?, ?, ? );
       """

  def upsert =
    sql"""
          MERGE INTO COLLECTION(pk, virtual, deleted, modified)
          KEY (pk)
          VALUES (?, ?, ?, now());
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
