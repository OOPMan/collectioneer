package com.oopman.collectioneer.db.dao

import distage._

case class ProjectedDAOs
(
  propertyValue: projected.PropertyValueDAO
)

case class RawDAOs
(
  collectionDAO: raw.CollectionDAO,
  propertyDAO: raw.PropertyDAO,
  propertyCollectionDAO: raw.PropertyCollectionDAO,
  propertyValueSetDAO: raw.PropertyValueSetDAO
)

case class DAOs
(
  projected: ProjectedDAOs,
  raw: RawDAOs 
)

val DAOModule = new ModuleDef:
  make[projected.PropertyValueDAO]
  make[raw.CollectionDAO]
  make[raw.PropertyCollectionDAO]
  make[raw.PropertyDAO]
  make[raw.PropertyValueSetDAO]
  make[ProjectedDAOs]
  make[RawDAOs]
  make[DAOs]
