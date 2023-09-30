package com.oopman.collectioneer.db.queries.h2

import com.oopman.collectioneer.db.entity.{Collections, c1}
import scalikejdbc.*

import java.util.UUID

object CollectionQueries:
  def all =
    sql"""
         SELECT ${c1.result.*}
         FROM ${Collections.as(c1)}
    """

  def allMatchingPKs(collectionPKs: Seq[UUID]) =
    sql"""
         SELECT ${c1.result.*}
         FROM ${Collections.as(c1)}
         WHERE {$c1.pk} IN ${collectionPKs}
       """
