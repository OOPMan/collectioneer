package com.oopman.collectioneer.db.dao.raw

import com.oopman.collectioneer.db.entity.{Collection, c1}
import com.oopman.collectioneer.db.queries.h2.CollectionQueries
import scalikejdbc.*

import java.util.UUID


object CollectionDAO:
  def getAll(implicit session: DBSession = AutoSession): List[Collection] =
    CollectionQueries.all.map(Collection(c1.resultName)).list.apply()

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[Collection] =
    CollectionQueries.allMatchingPKs(collectionPKs).map(Collection(c1.resultName)).list.apply()


class CollectionDAO(val connectionPoolName: String):

  def getAll: List[Collection] = NamedDB(connectionPoolName) readOnly { implicit session => CollectionDAO.getAll }

  def getAllMatchingPKs(collectionPKs: Seq[UUID]): List[Collection] =
    NamedDB(connectionPoolName) readOnly { implicit session => CollectionDAO.getAllMatchingPKs(collectionPKs) }
