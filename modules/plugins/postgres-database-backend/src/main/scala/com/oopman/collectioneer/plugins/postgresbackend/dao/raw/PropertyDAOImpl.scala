package com.oopman.collectioneer.plugins.postgresbackend.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.scalikejdbc.entity.Utils
import com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw.ScalikePropertyDAO
import com.oopman.collectioneer.db.traits.entity.raw.{Property, PropertyCollectionRelationshipType}
import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.plugins.postgresbackend
import scalikejdbc.{AutoSession, DBSession}

import java.util.UUID

object PropertyDAOImpl extends ScalikePropertyDAO:

  def createProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Seq[Int] =
    postgresbackend.queries.raw.PropertyQueries
      .insert
      .batch(postgresbackend.entity.raw.Property.propertiesSeqToBatchInsertSeq(properties)*)
      .apply()

  def createOrUpdateProperties(properties: Seq[Property])(implicit session: DBSession = AutoSession): Seq[Int] =
    postgresbackend.queries.raw.PropertyQueries
      .upsert
      .batch(postgresbackend.entity.raw.Property.propertiesSeqToBatchInsertSeq(properties)*)
      .apply()

  def getAll(implicit session: DBSession = AutoSession): Seq[entity.raw.Property] =
    postgresbackend.queries.raw.PropertyQueries
      .all
      .map(postgresbackend.entity.raw.Property(postgresbackend.entity.raw.Property.p1.resultName))
      .list
      .apply()

  def getAllMatchingPKs(propertyPKs: Seq[UUID])(implicit session: DBSession): Seq[Property] =
    postgresbackend.queries.raw.PropertyQueries
      .allMatchingPKs
      .bind(session.connection.createArrayOf("varchar", propertyPKs.toArray))
      .map(postgresbackend.entity.raw.Property(postgresbackend.entity.raw.Property.p1.resultName))
      .list
      .apply()

  def getAllMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession = AutoSession): Seq[Property] =
    postgresbackend.PropertyValueQueryDSLSupport
      .comparisonsToSQL(comparisons)
      .map((comparisonSQL, parameters) =>
        postgresbackend.queries.raw.PropertyQueries
          .innerJoiningPropertyCollection(s"($comparisonSQL)", "collection_pk", PropertyCollectionRelationshipType.CollectionOfPropertiesOfProperty)
          .bind(parameters*)
          .map(resultSet => postgresbackend.entity.raw.Property.apply(resultSet))
          .list
          .apply()
      )
      .getOrElse(getAll)
    
  def getAllByPropertyCollection
  (collectionPKs: Seq[UUID], propertyCollectionRelationshipTypes: Seq[PropertyCollectionRelationshipType])
  (implicit session: DBSession = AutoSession): Map[UUID, List[Property]] =
    postgresbackend.queries.raw.PropertyQueries
      .allByPropertyCollection()
      .bind(
        session.connection.createArrayOf("varchar", collectionPKs.toArray),
        session.connection.createArrayOf("varchar", propertyCollectionRelationshipTypes.map(_.toString).toArray)
      )
      .map(rs => {
        val property = postgresbackend.entity.raw.Property.apply(rs)
        Utils.resultSetArrayToListOf[UUID](rs, "collection_pks").map((_, property))
      })
      .list
      .apply()
      .flatten
      .groupBy(_._1)
      .map((collectionPK, data) => (collectionPK, data.map(_._2)))
