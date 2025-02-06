package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw.ScalikeCollectionDAO
import com.oopman.collectioneer.db.traits.entity.raw.{Collection, Property}
import com.oopman.collectioneer.db.{SortDirection, entity, traits}
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

  def getAllMatchingConstraints(comparisons: Seq[Comparison] = Nil,
                                collectionPKs: Option[Seq[UUID]] = None,
                                parentCollectionPKs: Option[Seq[UUID]] = None,
                                sortProperties: Seq[(Property, SortDirection)] = Nil,
                                offset: Option[Int] = None,
                                limit: Option[Int] = None)(implicit session: DBSession): List[Collection] =
    val (comparisonSQL, parameters) = postgresbackend.PropertyValueQueryDSLSupport.comparisonsToSQL(comparisons).unzip
    postgresbackend.queries.raw.CollectionQueries
      .allMatchingConstraints(comparisonSQL, collectionPKs, parentCollectionPKs, sortProperties, offset, limit)
      .bind(parameters.getOrElse(Nil): _*)
      .map(resultSet => postgresbackend.entity.raw.Collection.apply(resultSet))
      .list
      .apply()

  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession = AutoSession): List[entity.raw.Collection] =
    postgresbackend.queries.raw.CollectionQueries
      .allMatchingPKs
      .bind(session.connection.createArrayOf("varchar", collectionPKs.toArray))
      .map(postgresbackend.entity.raw.Collection(postgresbackend.entity.raw.Collection.c1.resultName))
      .list
      .apply()

  def getAllMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession = AutoSession): List[Collection] =
    postgresbackend.PropertyValueQueryDSLSupport
      .comparisonsToSQL(comparisons)
      .map((comparisonSQL, parameters) => 
        postgresbackend.queries.raw.CollectionQueries
          .allInnerJoining(s"($comparisonSQL)", "collection_pk")
          .bind(parameters: _*)
          .map(resultSet => postgresbackend.entity.raw.Collection.apply(resultSet))
          .list
          .apply()
      )
      .getOrElse(getAll)

  def getAllRelatedMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession = AutoSession): List[Collection] =
    postgresbackend.PropertyValueQueryDSLSupport
      .comparisonsToSQL(comparisons)
      .map((comparisonSQL, parameters) => 
        postgresbackend.queries.raw.CollectionQueries
        .allRelatedMatchingPropertyValues(comparisonSQL)
        .bind(parameters: _*)
        .map(resultSet => postgresbackend.entity.raw.Collection.apply(resultSet))
        .list
        .apply()
      )
      .getOrElse(getAll)

