package com.oopman.collectioneer.db.queries.h2

import com.oopman.collectioneer.db.entity.{Collections, c1}
import scalikejdbc.*

object CollectionQueries:
  def all =
    sql"""
         SELECT ${c1.result.*}
         FROM ${Collections.as(c1)}
    """
