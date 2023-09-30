package com.oopman.collectioneer.db.dao

import scalikejdbc.*
import com.oopman.collectioneer.db.entity.{Collections, c1}
import com.oopman.collectioneer.db.queries.h2.CollectionQueries

import java.util.UUID


object CollectionsDAO:
  def getAll(implicit session: DBSession = AutoSession): List[Collections] =
    CollectionQueries.all.map(Collections(c1.resultName)).list.apply()

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[Collections] =
    CollectionQueries.allMatchingPKs(collectionPKs).map(Collections(c1.resultName)).list.apply()


class CollectionsDAO(val connectionPoolName: String):

  def getAll: List[Collections] = NamedDB(connectionPoolName) readOnly { implicit session => CollectionsDAO.getAll }

  def getAllMatchingPKs(collectionPKs: Seq[UUID]): List[Collections] =
    NamedDB(connectionPoolName) readOnly { implicit session => CollectionsDAO.getAllMatchingPKs(collectionPKs) }
