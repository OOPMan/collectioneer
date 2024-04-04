package com.oopman.collectioneer.db.scalikejdbc.traits.dao

trait ProjectedDAOs:
  val CollectionDAO: projected.ScalikeCollectionDAO
  val PropertyDAO: projected.ScalikePropertyDAO
  val PropertyValueDAO: projected.ScalikePropertyValueDAO

trait RawDAOs:
  val CollectionDAO: raw.ScalikeCollectionDAO
  val PropertyCollectionDAO: raw.ScalikePropertyCollectionDAO
  val PropertyDAO: raw.ScalikePropertyDAO
  val RelationshipCollectionDAO: raw.ScalikeRelationshipCollectionDAO
  val RelationshipDAO: raw.ScalikeRelationshipDAO

trait DAOs:
  val projected: ProjectedDAOs
  val raw: RawDAOs

trait DatabaseBackend:
  val dao: DAOs
