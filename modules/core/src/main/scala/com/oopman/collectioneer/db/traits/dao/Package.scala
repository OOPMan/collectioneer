package com.oopman.collectioneer.db.traits.dao

trait ProjectedDAOObjects:
  val CollectionDAO: projected.CollectionDAO
  val PropertyDAO: projected.PropertyDAO
  val PropertyValueDAO: projected.PropertyValueDAO

trait RawDAOObjects:
  val CollectionDAO: raw.CollectionDAO
  val PropertyCollectionDAO: raw.PropertyCollectionDAO
  val PropertyDAO: raw.PropertyDAO
  val RelationshipCollectionDAO: raw.RelationshipCollectionDAO
  val RelationshipDAO: raw.RelationshipDAO

trait DAOObjects:
  val projected: ProjectedDAOObjects
  val raw: RawDAOObjects
