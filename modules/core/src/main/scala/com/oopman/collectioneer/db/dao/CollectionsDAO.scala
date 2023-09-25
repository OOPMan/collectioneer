package com.oopman.collectioneer.db.dao

import scalikejdbc._
import com.oopman.collectioneer.db.entity.{Collections, c1}
import com.oopman.collectioneer.db.queries.h2.CollectionQueries


object CollectionsDAO:
  def getAll()(implicit session: DBSession = AutoSession): List[Collections] =
    CollectionQueries.all.map(Collections(c1.resultName)).list.apply()


class CollectionsDAO(val connectionPoolName: String):

  def getAll(): List[Collections] = NamedDB(connectionPoolName) readOnly { implicit session => CollectionsDAO.getAll() }
