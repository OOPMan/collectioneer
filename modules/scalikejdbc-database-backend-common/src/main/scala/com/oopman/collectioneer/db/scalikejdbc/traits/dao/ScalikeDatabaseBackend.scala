package com.oopman.collectioneer.db.scalikejdbc.traits.dao

trait ScalikeProjectedDAOs:
  val CollectionDAO: projected.ScalikeCollectionDAO
  val PropertyDAO: projected.ScalikePropertyDAO
  val PropertyValueDAO: projected.ScalikePropertyValueDAO

trait ScalikeRawDAOs:
  val CollectionDAO: raw.ScalikeCollectionDAO
  val PropertyCollectionDAO: raw.ScalikePropertyCollectionDAO
  val PropertyDAO: raw.ScalikePropertyDAO
  val RelationshipCollectionDAO: raw.ScalikeRelationshipCollectionDAO
  val RelationshipDAO: raw.ScalikeRelationshipDAO

trait ScalikeDAOs:
  val projected: ScalikeProjectedDAOs
  val raw: ScalikeRawDAOs

trait ScalikeDatabaseBackend:
  val dao: ScalikeDAOs
