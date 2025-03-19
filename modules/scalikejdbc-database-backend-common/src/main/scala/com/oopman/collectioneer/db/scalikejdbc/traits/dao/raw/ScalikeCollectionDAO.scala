package com.oopman.collectioneer.db.scalikejdbc.traits.dao.raw

import com.oopman.collectioneer.db.PropertyValueQueryDSL.Comparison
import com.oopman.collectioneer.db.SortDirection
import com.oopman.collectioneer.db.traits.entity.raw.{Collection, Property}
import scalikejdbc.DBSession

import java.util.UUID

trait ScalikeCollectionDAO:
  def createCollections(collections: Seq[Collection])(implicit session: DBSession): Seq[Int]
  def createOrUpdateCollections(collections: Seq[Collection])(implicit session: DBSession): Seq[Int]
  def getAll(implicit session: DBSession): Seq[Collection]
  def getAllMatchingConstraints(comparisons: Seq[Comparison] = Nil,
                                collectionPKs: Option[Seq[UUID]] = None,
                                parentCollectionPKs: Option[Seq[UUID]] = None,
                                sortProperties: Seq[(Property, SortDirection)] = Nil,
                                offset: Option[Int] = None,
                                limit: Option[Int] = None)(implicit session: DBSession): Seq[Collection]
  def getAllMatchingPKs(collectionPKs: Seq[UUID])(implicit session: DBSession): Seq[Collection]
  def getAllMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession): Seq[Collection]
  def getAllRelatedMatchingPropertyValues(comparisons: Seq[Comparison])(implicit session: DBSession): Seq[Collection]