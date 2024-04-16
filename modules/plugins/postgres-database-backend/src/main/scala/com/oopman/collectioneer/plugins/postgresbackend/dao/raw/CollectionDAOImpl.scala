package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw.ScalikeCollectionDAO
import com.oopman.collectioneer.db.traits.entity.raw.Collection
import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.*

import java.util.UUID


object CollectionDAOImpl extends ScalikeCollectionDAO:
  def createCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    postgresbackend.queries.raw.CollectionQueries
      .insert
      .batch(postgresbackend.entity.raw.Collection.collectionsSeqToBatchInsertSeq(collections): _*)
      .apply()

  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession = AutoSession): Array[Int] =
    postgresbackend.queries.raw.CollectionQueries
      .upsert
      .batch(postgresbackend.entity.raw.Collection.collectionsSeqToBatchInsertSeq(collections): _*)
      .apply()

  def getAll(implicit session: DBSession = AutoSession): List[entity.raw.Collection] =
    postgresbackend.queries.raw.CollectionQueries
      .all
      .map(postgresbackend.entity.raw.Collection(postgresbackend.entity.raw.Collection.c1.resultName))
      .list
      .apply()

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[entity.raw.Collection] =
    postgresbackend.queries.raw.CollectionQueries
      .allMatchingPKs(collectionPKs)
      .map(postgresbackend.entity.raw.Collection(postgresbackend.entity.raw.Collection.c1.resultName))
      .list
      .apply()

  def getAllMatchingPropertyValues(comparisons: Comparison*)(implicit session: DBSession = AutoSession): List[Collection] =
    val comparison = comparisons.reduce((c1, c2) => c1 and c2)
    val (comparisonSQL, parameters) = postgresbackend.PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    postgresbackend.queries.raw.CollectionQueries
      .allInnerJoining(s"($comparisonSQL)", "collection_pk")
      .bind(parameters: _*)
      .map(resultSet => postgresbackend.entity.raw.Collection.apply(resultSet))
      .list
      .apply()
