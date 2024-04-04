package com.oopman.collectioneer.plugins.postgresbackend

import com.oopman.collectioneer.db.scalikejdbc
import com.oopman.collectioneer.plugins.postgresbackend
import distage.*

case class PostgresProjectedDAOs
(
  CollectionDAO: scalikejdbc.traits.dao.projected.ScalikeCollectionDAO,
  PropertyDAO: scalikejdbc.traits.dao.projected.ScalikePropertyDAO,
  PropertyValueDAO: scalikejdbc.traits.dao.projected.ScalikePropertyValueDAO
) extends scalikejdbc.traits.dao.ScalikeProjectedDAOs

case class PostgresRawDAOs
(
  CollectionDAO: scalikejdbc.traits.dao.raw.ScalikeCollectionDAO,
  PropertyCollectionDAO: scalikejdbc.traits.dao.raw.ScalikePropertyCollectionDAO,
  PropertyDAO: scalikejdbc.traits.dao.raw.ScalikePropertyDAO,
  RelationshipCollectionDAO: scalikejdbc.traits.dao.raw.ScalikeRelationshipCollectionDAO,
  RelationshipDAO: scalikejdbc.traits.dao.raw.ScalikeRelationshipDAO
) extends scalikejdbc.traits.dao.ScalikeRawDAOs

case class PostgresDAOs
(
  projected: scalikejdbc.traits.dao.ScalikeProjectedDAOs,
  raw: scalikejdbc.traits.dao.ScalikeRawDAOs

) extends scalikejdbc.traits.dao.ScalikeDAOs

case class PostgresDatabaseBackend
(
  dao: scalikejdbc.traits.dao.ScalikeDAOs

) extends scalikejdbc.traits.dao.ScalikeDatabaseBackend

val PostgresDatabaseBackendModule = new ModuleDef:
  make[scalikejdbc.traits.dao.projected.ScalikeCollectionDAO].from(postgresbackend.dao.projected.CollectionDAOImpl)
  make[scalikejdbc.traits.dao.projected.ScalikePropertyDAO].from(postgresbackend.dao.projected.PropertyDAOImpl)
  make[scalikejdbc.traits.dao.projected.ScalikePropertyValueDAO].from(postgresbackend.dao.projected.PropertyValueDAOImpl)
  make[scalikejdbc.traits.dao.raw.ScalikeCollectionDAO].from(postgresbackend.dao.raw.CollectionDAOImpl)
  make[scalikejdbc.traits.dao.raw.ScalikePropertyDAO].from(postgresbackend.dao.raw.PropertyDAOImpl)
  make[scalikejdbc.traits.dao.raw.ScalikePropertyCollectionDAO].from(postgresbackend.dao.raw.PropertyCollectionDAOImpl)
  make[scalikejdbc.traits.dao.raw.ScalikeRelationshipDAO].from(postgresbackend.dao.raw.RelationshipDAOImpl)
  make[scalikejdbc.traits.dao.raw.ScalikeRelationshipCollectionDAO].from(postgresbackend.dao.raw.RelationshipCollectionDAOImpl)
  make[scalikejdbc.traits.dao.ScalikeProjectedDAOs].from[PostgresProjectedDAOs]
  make[scalikejdbc.traits.dao.ScalikeRawDAOs].from[PostgresRawDAOs]
  make[scalikejdbc.traits.dao.ScalikeDAOs].from[PostgresDAOs]
  make[scalikejdbc.traits.dao.ScalikeDatabaseBackend].from[PostgresDatabaseBackend]
