package com.oopman.collectioneer.db.scalikejdbc.dao

import com.oopman.collectioneer.db.{scalikejdbc, traits}
import distage.*

case class ScalikeProjectedDAOImpls
(
  CollectionDAO: projected.CollectionDAOImpl,
  PropertyDAO: projected.PropertyDAOImpl,
  PropertyValueDAO: projected.PropertyValueDAOImpl
) extends traits.dao.ProjectedDAOs

case class ScalikedRawDAOImpls
(
  CollectionDAO: raw.CollectionDAOImpl,
  PropertyCollectionDAO: raw.PropertyCollectionDAOImpl,
  PropertyDAO: raw.PropertyDAOImpl,
  RelationshipCollectionDAO: raw.RelationshipCollectionDAOImpl,
  RelationshipDAO: raw.RelationshipDAOImpl
) extends traits.dao.RawDAOs

case class ScalikeDAOImpls
(
  projected: ScalikeProjectedDAOImpls,
  raw: ScalikedRawDAOImpls
) extends traits.dao.DAOs

case class ScalikeDatabaseBackendImpl
(
  dao: ScalikeDAOImpls
) extends traits.dao.DatabaseBackend

object DAOImplModule extends ModuleDef:
  make[traits.dao.projected.CollectionDAO].from[scalikejdbc.dao.projected.CollectionDAOImpl]
  make[traits.dao.projected.PropertyDAO].from[scalikejdbc.dao.projected.PropertyDAOImpl]
  make[traits.dao.projected.PropertyValueDAO].from[scalikejdbc.dao.projected.PropertyValueDAOImpl]
  make[traits.dao.raw.CollectionDAO].from[scalikejdbc.dao.raw.CollectionDAOImpl]
  make[traits.dao.raw.PropertyDAO].from[scalikejdbc.dao.raw.PropertyDAOImpl]
  make[traits.dao.raw.PropertyCollectionDAO].from[scalikejdbc.dao.raw.PropertyCollectionDAOImpl]
  make[traits.dao.raw.RelationshipDAO].from[scalikejdbc.dao.raw.RelationshipDAOImpl]
  make[traits.dao.raw.RelationshipCollectionDAO].from[scalikejdbc.dao.raw.RelationshipCollectionDAOImpl]
  make[traits.dao.ProjectedDAOs].from[ScalikeProjectedDAOImpls]
  make[traits.dao.RawDAOs].from[ScalikedRawDAOImpls]
  make[traits.dao.DAOs].from[ScalikeDAOImpls]
  make[traits.dao.DatabaseBackend].from[ScalikeDatabaseBackendImpl]
