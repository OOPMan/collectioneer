package com.oopman.collectioneer.db.traits.dao

trait ProjectedDAOObjects:
  val PropertyValueDAO: projected.PropertyValueDAO

trait RawDAOObjects:
  val CollectionDAO: raw.CollectionDAO
  val PropertyCollectionDAO: raw.PropertyCollectionDAO
  val PropertyDAO: raw.PropertyDAO
  val PropertyValueSetDAO: raw.PropertyValueSetDAO

trait DAOObjects:
  val projected: ProjectedDAOObjects
  val raw: RawDAOObjects
