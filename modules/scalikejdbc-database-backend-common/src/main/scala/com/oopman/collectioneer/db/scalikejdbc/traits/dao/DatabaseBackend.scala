package com.oopman.collectioneer.db.scalikejdbc.traits.dao

trait ProjectedDAOs:
  val CollectionDAO: projected.CollectionDAO
  val PropertyDAO: projected.PropertyDAO
  val PropertyValueDAO: projected.PropertyValueDAO

trait RawDAOs:
  val CollectionDAO: raw.CollectionDAO
  val PropertyCollectionDAO: raw.PropertyCollectionDAO
  val PropertyDAO: raw.PropertyDAO
  val RelationshipCollectionDAO: raw.RelationshipCollectionDAO
  val RelationshipDAO: raw.RelationshipDAO

trait DAOs:
  val projected: ProjectedDAOs
  val raw: RawDAOs

trait DatabaseBackend:
  val dao: DAOs
