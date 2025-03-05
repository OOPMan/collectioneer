package com.oopman.collectioneer.plugins.postgresbackend.test

import com.oopman.collectioneer.Config
import com.oopman.collectioneer.plugins.postgresbackend.EmbeddedPostgresDatabaseBackendPlugin
import org.scalatest.flatspec.AnyFlatSpec

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