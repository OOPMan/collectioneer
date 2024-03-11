package com.oopman.collectioneer.db.dao

import distage._

case class ProjectedDAOs
(
  collectionDAO: projected.CollectionDAO,
  propertyValueDAO: projected.PropertyValueDAO
)

case class RawDAOs
(
  collectionDAO: raw.CollectionDAO,
  propertyDAO: raw.PropertyDAO,
  propertyValueSetDAO: raw.PropertyValueSetDAO,
  propertyPropertyValueSetDAO: raw.PropertyPropertyValueSetDAO
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
  make[raw.PropertyValueSetDAO]
  make[raw.PropertyPropertyValueSetDAO]
  make[ProjectedDAOs]
  make[RawDAOs]
  make[DAOs]
