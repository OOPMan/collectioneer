package com.oopman.collectioneer.db.scalikejdbc.dao

import distage.*

case class ProjectedDAOs
(
  collectionDAO: projected.CollectionDAO,
  propertyDAO: projected.PropertyDAO,
  propertyValueDAO: projected.PropertyValueDAO
)

case class RawDAOs
(
  collectionDAO: raw.CollectionDAO,
  propertyCollectionDAO: raw.PropertyCollectionDAO,
  propertyDAO: raw.PropertyDAO,
  relationshipCollectionDAO: raw.RelationshipCollectionDAO,
  relationshipDAO: raw.RelationshipDAO
)

case class DAOs
(
  projected: ProjectedDAOs,
  raw: RawDAOs 
)

val DAOModule = new ModuleDef:
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
