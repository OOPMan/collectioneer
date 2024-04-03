package com.oopman.collectioneer.db.scalikejdbc.dao

import com.oopman.collectioneer.db.traits.dao
import distage.*

case class ProjectedDAOs
(
  CollectionDAO: projected.CollectionDAO,
  PropertyDAO: projected.PropertyDAO,
  PropertyValueDAO: projected.PropertyValueDAO
) extends dao.ProjectedDAOs

case class RawDAOs
(
  CollectionDAO: raw.CollectionDAO,
  PropertyCollectionDAO: raw.PropertyCollectionDAO,
  PropertyDAO: raw.PropertyDAO,
  RelationshipCollectionDAO: raw.RelationshipCollectionDAO,
  RelationshipDAO: raw.RelationshipDAO
) extends dao.RawDAOs

case class DAOs
(
  projected: ProjectedDAOs,
  raw: RawDAOs 
) extends dao.DAOs

object DAOModule extends ModuleDef:
  make[projected.CollectionDAO]
  make[projected.PropertyDAO]
  make[projected.PropertyValueDAO]
  make[raw.CollectionDAO]
  make[raw.PropertyCollectionDAO]
  make[raw.PropertyDAO]
  make[raw.RelationshipCollectionDAO]
  make[raw.RelationshipDAO]
  make[ProjectedDAOs]
  make[RawDAOs]
  make[DAOs]
