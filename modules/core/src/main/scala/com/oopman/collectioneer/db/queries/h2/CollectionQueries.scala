package com.oopman.collectioneer.db.queries.h2

import com.oopman.collectioneer.db.entity.{Collections, c}
import scalikejdbc.*

object CollectionQueries:
  def all =
    sql"""
         SELECT ${c.result.*}
         FROM ${Collections.as(c)}
    """

