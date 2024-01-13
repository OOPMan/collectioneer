package com.oopman.collectioneer.db.dao

import distage._

val DAOModule = new ModuleDef:
  make[projected.PropertyValueDAO]
  make[raw.CollectionDAO]
  make[raw.PropertyCollectionDAO]
  make[raw.PropertyDAO]
  make[raw.PropertyValueSetDAO]
