package com.oopman.collectioneer.db.dao

import scalikejdbc.*
import com.oopman.collectioneer.db.entity.{Collection, c1}
import com.oopman.collectioneer.db.queries.h2.CollectionQueries

import java.util.UUID


object CollectionsDAO:
  def getAll(implicit session: DBSession = AutoSession): List[Collection] =
    CollectionQueries.all.map(Collection(c1.resultName)).list.apply()

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[Collection] =
    CollectionQueries.allMatchingPKs(collectionPKs).map(Collection(c1.resultName)).list.apply()


class CollectionsDAO(val connectionPoolName: String):

  def getAll: List[Collection] = NamedDB(connectionPoolName) readOnly { implicit session => CollectionsDAO.getAll }

  def getAllMatchingPKs(collectionPKs: Seq[UUID]): List[Collection] =
    NamedDB(connectionPoolName) readOnly { implicit session => CollectionsDAO.getAllMatchingPKs(collectionPKs) }
