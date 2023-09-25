package com.oopman.collectioneer.db.queries.h2

import com.oopman.collectioneer.db.entity.{Properties, p1}
import scalikejdbc._

object PropertyQueries:

  def all =
    sql"""
          SELECT ${p1.result.*}
          FROM ${Properties.as(p1)}
     """
