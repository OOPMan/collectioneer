package com.oopman.collectioneer.db

import scalikejdbc.*
import distage.*
import izumi.fundamentals.platform.functional.Identity

import java.sql.Connection
import javax.sql.DataSource

type DBConnectionProvider = () => DBConnection

object Injection:
  def getInjectorAndModule[F[_A], A](dbProvider: DBConnectionProvider): (Injector[Identity], ModuleDef) =
    val injector = Injector()
    val module = new ModuleDef:
      include(dao.DAOModule)
      include(h2.H2DatabaseBackendModule)
      make[DBConnectionProvider].from(dbProvider)
    (injector, module)

  def getInjectorAndModule[F[_], A](datasourceUri: String): (Injector[Identity], ModuleDef) =
    getInjectorAndModule(() => NamedDB(datasourceUri))

  def getInjectorAndModule[F[_], A](connection: Connection, autoclose: Boolean): (Injector[Identity], ModuleDef) =
    getInjectorAndModule(() => DB(connection).autoClose(autoclose))

  def getInjectorAndModule[F[_], A](dataSource: DataSource, autoclose: Boolean): (Injector[Identity], ModuleDef) =
    getInjectorAndModule(() => DB(dataSource.getConnection).autoClose(autoclose))

  // The following methods don't seem to work...
  def produceRun[F[_], A](dbProvider: DBConnectionProvider)(function: Functoid[F[A]]): F[A] =
    val (injector, module) = getInjectorAndModule(dbProvider)
    injector.produceRun(module)(function)

  def produceRun[F[_], A](datasourceUri: String)(function: Functoid[F[A]]): F[A] =
    produceRun(() => NamedDB(datasourceUri))(function)

  def produceRun[F[_], A](connection: Connection, autoclose: Boolean)(function: Functoid[F[A]]): F[A] =
    produceRun(() => DB(connection).autoClose(autoclose))(function)
    
  def produceRun[F[_], A](dataSource: DataSource, autoclose: Boolean)(function: Functoid[F[A]]): F[A] =
    produceRun(() => DB(dataSource.getConnection).autoClose(autoclose))(function)

  def main(args: Array[String]): Unit =
    produceRun("jdbc:h2:./test") { (p: dao.projected.PropertyValueDAO) => p.getPropertyValuesByPropertyValueSet(Seq()) }