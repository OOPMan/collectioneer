package com.oopman.collectioneer.db.dao

import distage.*

case class ProjectedDAOs
(
  collectionDAO: projected.CollectionDAO,
  propertyValueDAO: projected.PropertyValueDAO
)

case class RawDAOs
(
  collectionDAO: raw.CollectionDAO,
  propertyDAO: raw.PropertyDAO,
)

case class DAOs
(
  projected: ProjectedDAOs,
  raw: RawDAOs 
)

val DAOModule = new ModuleDef:
  make[projected.CollectionDAO]
  make[projected.PropertyValueDAO]
  make[raw.CollectionDAO]
  make[raw.PropertyDAO]
  make[ProjectedDAOs]
  make[RawDAOs]
  make[DAOs]
