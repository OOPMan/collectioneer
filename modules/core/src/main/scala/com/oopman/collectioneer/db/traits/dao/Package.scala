package com.oopman.collectioneer.db.traits.dao

trait ProjectedDAOObjects:
  val PropertyValueDAO: projected.PropertyValueDAO

trait RawDAOObjects:
  val CollectionDAO: raw.CollectionDAO

trait DAOObjects:
  val projected: ProjectedDAOObjects
  val raw: RawDAOObjects
