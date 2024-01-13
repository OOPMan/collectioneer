package com.oopman.collectioneer.db

import scalikejdbc.*
import distage.*

import java.sql.Connection

type DBConnectionProvider = () => DBConnection

object Injection:
  def produceRun[F[_], A](dbProvider: DBConnectionProvider)(function: Functoid[F[A]]): F[A] =
    val injector = Injector()
    val module = new ModuleDef:
      include(dao.DAOModule)
      include(h2.H2DatabaseBackendModule)
      make[DBConnectionProvider].from(dbProvider)
    injector.produceRun(module)(function)

  def produceRun[F[_], A](datasourceUri: String)(function: Functoid[F[A]]): F[A] =
    produceRun(() => NamedDB(datasourceUri))(function)

  def produceRun[F[_], A](connection: Connection, autoclose: Boolean = false)(function: Functoid[F[A]]): F[A] =
    produceRun(() => DB(connection).autoClose(autoclose))(function)

  def main(args: Array[String]): Unit =
    produceRun("jdbc:h2:./test") { (p: dao.projected.PropertyValueDAO) => p.getPropertyValuesByPropertyValueSet(Seq()) }