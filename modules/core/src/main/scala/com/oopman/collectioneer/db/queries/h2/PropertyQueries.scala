package com.oopman.collectioneer.db.queries.h2

import com.oopman.collectioneer.db.entity.{Property, p1}
import scalikejdbc._

object PropertyQueries:

  def all =
    sql"""
          SELECT ${p1.result.*}
          FROM ${Property.as(p1)}
     """
