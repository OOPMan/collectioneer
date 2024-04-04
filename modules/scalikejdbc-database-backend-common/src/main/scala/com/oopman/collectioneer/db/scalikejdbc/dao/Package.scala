package com.oopman.collectioneer.db.scalikejdbc.dao

import com.oopman.collectioneer.db.traits.dao.{DAOs, ProjectedDAOs, RawDAOs}
import distage.*

case class ScalikeProjectedDAOImpls
(
  CollectionDAO: projected.CollectionDAOImpl,
  PropertyDAO: projected.PropertyDAOImpl,
  PropertyValueDAO: projected.PropertyValueDAOImpl
) extends ProjectedDAOs

case class ScalikedRawDAOImpls
(
  CollectionDAO: raw.CollectionDAOImpl,
  PropertyCollectionDAO: raw.PropertyCollectionDAOImpl,
  PropertyDAO: raw.PropertyDAOImpl,
  RelationshipCollectionDAO: raw.RelationshipCollectionDAOImpl,
  RelationshipDAO: raw.RelationshipDAOImpl
) extends RawDAOs

case class ScalikeDAOImpls
(
  projected: ScalikeProjectedDAOImpls,
  raw: ScalikedRawDAOImpls
) extends DAOs

object DAOImplModule extends ModuleDef:
  make[projected.CollectionDAOImpl]
  make[projected.PropertyDAOImpl]
  make[projected.PropertyValueDAOImpl]
  make[raw.CollectionDAOImpl]
  make[raw.PropertyCollectionDAOImpl]
  make[raw.PropertyDAOImpl]
  make[raw.RelationshipCollectionDAOImpl]
  make[raw.RelationshipDAOImpl]
  make[ScalikeProjectedDAOImpls]
  make[ScalikedRawDAOImpls]
  make[ScalikeDAOImpls]
