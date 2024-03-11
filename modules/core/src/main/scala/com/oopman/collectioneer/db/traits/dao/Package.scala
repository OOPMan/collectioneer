package com.oopman.collectioneer.db.traits.dao

trait ProjectedDAOObjects:
  val CollectionDAO: projected.CollectionDAO
  val PropertyDAO: projected.PropertyDAO
  val PropertyValueDAO: projected.PropertyValueDAO

trait RawDAOObjects:
  val CollectionDAO: raw.CollectionDAO
  val PropertyDAO: raw.PropertyDAO
  val PropertyValueSetDAO: raw.PropertyValueSetDAO
  val PropertyPropertyValueSetDAO: raw.PropertyPropertyValueSetDAO

trait DAOObjects:
  val projected: ProjectedDAOObjects
  val raw: RawDAOObjects
