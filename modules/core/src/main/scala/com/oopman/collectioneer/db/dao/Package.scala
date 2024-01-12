package com.oopman.collectioneer.db.dao

import distage._

val DAOModule = new ModuleDef:
  make[projected.PropertyValueDAO]
