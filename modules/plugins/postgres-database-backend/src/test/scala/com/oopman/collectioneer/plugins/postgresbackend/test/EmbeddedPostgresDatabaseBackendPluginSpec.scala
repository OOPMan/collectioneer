package com.oopman.collectioneer.plugins.postgresbackend.test

import com.oopman.collectioneer.Config
import com.oopman.collectioneer.plugins.postgresbackend.EmbeddedPostgresDatabaseBackendPlugin
import org.scalatest.flatspec.AnyFlatSpec
import java.util.UUID

class EmbeddedPostgresDatabaseBackendPluginSpec extends AnyFlatSpec:
  case class TestConfig(datasourceUri: Option[String]) extends Config

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.EmbeddedPostgresDatabaseBackendPlugin.compatibleWithDatasourceUri"

  it should "return true for JDBC URLs that start with jdbc:embeddedpostgresql: and match the required JDBC URL structure" in {
    assertResult(true) {
      EmbeddedPostgresDatabaseBackendPlugin(TestConfig(Some("jdbc:embeddedpostgresql:"))).compatibleWithDatasourceUri
    }
    assertResult(true) {
      EmbeddedPostgresDatabaseBackendPlugin(TestConfig(Some("jdbc:embeddedpostgresql:/"))).compatibleWithDatasourceUri
    }
    assertResult(true) {
      EmbeddedPostgresDatabaseBackendPlugin(TestConfig(Some("jdbc:embeddedpostgresql://"))).compatibleWithDatasourceUri
    }
    assertResult(true) {
      EmbeddedPostgresDatabaseBackendPlugin(TestConfig(Some("jdbc:embeddedpostgresql:///"))).compatibleWithDatasourceUri
    }
    assertResult(true) {
      EmbeddedPostgresDatabaseBackendPlugin(TestConfig(Some("jdbc:embeddedpostgresql://PATH/"))).compatibleWithDatasourceUri
    }
    assertResult(true) {
      EmbeddedPostgresDatabaseBackendPlugin(TestConfig(Some("jdbc:embeddedpostgresql://PATH/DATABASE"))).compatibleWithDatasourceUri
    }
    assertResult(true) {
      EmbeddedPostgresDatabaseBackendPlugin(TestConfig(Some("jdbc:embeddedpostgresql://PATH/DATABASE?parameter1=true"))).compatibleWithDatasourceUri
    }
    assertResult(true) {
      EmbeddedPostgresDatabaseBackendPlugin(TestConfig(Some("jdbc:embeddedpostgresql://PATH/DATABASE?parameter1=true&parameter2=false"))).compatibleWithDatasourceUri
    }
  }

  it should "return false for JDBC URLs that do not start with jdbc:embeddedpostgresql:" in {
    assertResult(false) {
      EmbeddedPostgresDatabaseBackendPlugin(TestConfig(Some("jdbc:postgresql:///"))).compatibleWithDatasourceUri
    }
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.EmbeddedPostgresDatabaseBackendPlugin.getDatasource"

  it should "setup an EmbeddedPostgresDatabaseBackend with the default postgres database at the provided location and return a DataSource" in {
    val path = os.pwd / UUID.randomUUID().toString
    val percentEncodedPath = EmbeddedPostgresDatabaseBackendPlugin.encodePercentString(path.toString)
    val config = TestConfig(Some(s"jdbc:embeddedpostgresql://$percentEncodedPath"))
    val dataSource = EmbeddedPostgresDatabaseBackendPlugin(config, true).getDatasource
    val connection = dataSource.getConnection
    connection.createStatement().execute("CREATE TABLE test(pk INTEGER PRIMARY KEY);")
    connection.createStatement().execute("INSERT INTO test(pk) VALUES (1);");
    val resultSet = connection.createStatement().executeQuery("SELECT COUNT(*) FROM test WHERE pk = 1;")
    assert(resultSet.next())
    assert(resultSet.getInt(1) == 1)
  }

  it should "setup an EmbeddedPostgresDatabaseBackend with a custom database at the provided location and return a DataSource" in {
    val path = os.pwd / UUID.randomUUID().toString
    val percentEncodedPath = EmbeddedPostgresDatabaseBackendPlugin.encodePercentString(path.toString)
    val config = TestConfig(Some(s"jdbc:embeddedpostgresql://$percentEncodedPath/custom_database"))
    val dataSource = EmbeddedPostgresDatabaseBackendPlugin(config, true).getDatasource
    val connection = dataSource.getConnection
    connection.createStatement().execute("CREATE TABLE test(pk INTEGER PRIMARY KEY);")
    connection.createStatement().execute("INSERT INTO test(pk) VALUES (1);");
    val resultSet1 = connection.createStatement().executeQuery("SELECT COUNT(*) FROM test WHERE pk = 1;")
    assert(resultSet1.next())
    assert(resultSet1.getInt(1) == 1)
    val resultSet2 = connection.createStatement().executeQuery("SELECT current_database();")
    assert(resultSet2.next())
    assert(resultSet2.getString(1) == "custom_database")
  }

  it should "be able to resume operation with an already an existing EmbeddedPostgresDatabaseBackend at the provided location and return a DataSource" in {
    val path = os.pwd / UUID.randomUUID().toString
    val percentEncodedPath = EmbeddedPostgresDatabaseBackendPlugin.encodePercentString(path.toString)
    val config = TestConfig(Some(s"jdbc:embeddedpostgresql://$percentEncodedPath"))
    // removeDataOnShutdown needs to be false, otherwise EmbeddedPostgres tries to run initdb against the existing files...
    val embeddedPostgresDatabaseBackendPlugin1 = EmbeddedPostgresDatabaseBackendPlugin(config, removeDataOnShutdown = false)
    val dataSource1 = embeddedPostgresDatabaseBackendPlugin1.getDatasource
    val connection1 = dataSource1.getConnection
    connection1.createStatement().execute("CREATE TABLE test(pk INTEGER PRIMARY KEY);")
    connection1.createStatement().execute("INSERT INTO test(pk) VALUES (1);");
    embeddedPostgresDatabaseBackendPlugin1.shutDown()
    // removeDataOnShutdown needs to be false, otherwise EmbeddedPostgres tries to run initdb against the existing files...
    val embeddedPostgresDatabaseBackendPlugin2 = EmbeddedPostgresDatabaseBackendPlugin(config, removeDataOnShutdown = false)
    val dataSource2 = embeddedPostgresDatabaseBackendPlugin2.getDatasource
    val connection2 = dataSource2.getConnection
    val resultSet1 = connection2.createStatement().executeQuery("SELECT COUNT(*) FROM test WHERE pk = 1;")
    assert(resultSet1.next())
    assert(resultSet1.getInt(1) == 1)
    embeddedPostgresDatabaseBackendPlugin2.shutDown()
    // Because removeDataOnShutdown is false, we need to manually clean-up for this test...
    os.remove.all(path)
  }

  it should "be able to resume operation with an already an existing EmbeddedPostgresDatabaseBackend with a custom database at the provided location and return a DataSource" in {
    val path = os.pwd / UUID.randomUUID().toString
    val percentEncodedPath = EmbeddedPostgresDatabaseBackendPlugin.encodePercentString(path.toString)
    val config = TestConfig(Some(s"jdbc:embeddedpostgresql://$percentEncodedPath/custom_database"))
    // removeDataOnShutdown needs to be false, otherwise EmbeddedPostgres tries to run initdb against the existing files...
    val embeddedPostgresDatabaseBackendPlugin1 = EmbeddedPostgresDatabaseBackendPlugin(config, removeDataOnShutdown = false)
    val dataSource1 = embeddedPostgresDatabaseBackendPlugin1.getDatasource
    val connection1 = dataSource1.getConnection
    connection1.createStatement().execute("CREATE TABLE test(pk INTEGER PRIMARY KEY);")
    connection1.createStatement().execute("INSERT INTO test(pk) VALUES (1);");
    val resultSet1 = connection1.createStatement().executeQuery("SELECT current_database();")
    assert(resultSet1.next())
    assert(resultSet1.getString(1) == "custom_database")
    embeddedPostgresDatabaseBackendPlugin1.shutDown()
    // removeDataOnShutdown needs to be false, otherwise EmbeddedPostgres tries to run initdb against the existing files...
    val embeddedPostgresDatabaseBackendPlugin2 = EmbeddedPostgresDatabaseBackendPlugin(config, removeDataOnShutdown = false)
    val dataSource2 = embeddedPostgresDatabaseBackendPlugin2.getDatasource
    val connection2 = dataSource2.getConnection
    val resultSet2 = connection2.createStatement().executeQuery("SELECT COUNT(*) FROM test WHERE pk = 1;")
    assert(resultSet2.next())
    assert(resultSet2.getInt(1) == 1)
    val resultSet3 = connection2.createStatement().executeQuery("SELECT current_database();")
    assert(resultSet3.next())
    assert(resultSet3.getString(1) == "custom_database")
    embeddedPostgresDatabaseBackendPlugin2.shutDown()
    // Because removeDataOnShutdown is false, we need to manually clean-up for this test...
    os.remove.all(path)
  }