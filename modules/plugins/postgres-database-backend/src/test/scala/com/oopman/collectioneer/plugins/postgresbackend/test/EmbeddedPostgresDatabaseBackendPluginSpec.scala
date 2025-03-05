package com.oopman.collectioneer.plugins.postgresbackend.test

import com.oopman.collectioneer.Config
import com.oopman.collectioneer.plugins.postgresbackend.EmbeddedPostgresDatabaseBackendPlugin
import org.scalatest.flatspec.AnyFlatSpec

class EmbeddedPostgresDatabaseBackendPluginSpec extends AnyFlatSpec:
  case class TestConfig(datasourceUri: Option[String]) extends Config

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.EmbeddedPostgresDatabaseBackendPlugin.compatibleWithDatasourceUri"

  it should "return true for JDBC URLs that start with jdbc:embeddedpostgresql:" in {
    assertResult(true) {
      EmbeddedPostgresDatabaseBackendPlugin(TestConfig(Some("jdbc:embeddedpostgresql:///"))).compatibleWithDatasourceUri
    }
  }