package com.oopman.collectioneer.plugins.postgresbackend.test

import com.oopman.collectioneer.plugins.postgresbackend.PropertyValueQueryDSLSupport

import java.time.{LocalDate, OffsetTime, ZonedDateTime}
import java.util.UUID

class PropertyValueQueryDSLSupportSpec extends BaseFunSuite:
  behavior of "com.oopman.collectioneer.plugins.postgresbackend.PropertyValueQueryDSLSupport.makeJDBCArray"
  
  it should "create a java.sql.Array from an Array[BigInt]" in { implicit session => 
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(BigInt(1), BigInt(2)))
    assert(result.getBaseTypeName == "int8")
  }

  it should "create a java.sql.Array from an Array[Boolean]" in { implicit session =>
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(true, false))
    assert(result.getBaseTypeName == "bool")
  }

  it should "create a java.sql.Array from an Array[Byte]" in { implicit session =>
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(1.toByte, 2.toByte))
    assert(result.getBaseTypeName == "bytea")
  }

  it should "create a java.sql.Array from an Array[LocalDate]" in { implicit session =>
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(LocalDate.now()))
    assert(result.getBaseTypeName == "date")
  }

  it should "create a java.sql.Array from an Array[Double]" in { implicit session =>
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(1.toDouble, 2.toDouble))
    assert(result.getBaseTypeName == "float8")
  }

  it should "create a java.sql.Array from an Array[Float]" in { implicit session =>
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(1.toFloat, 2.toFloat))
    assert(result.getBaseTypeName == "float8")
  }

  it should "create a java.sql.Array from an Array[Int]" in { implicit session =>
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(1, 2))
    assert(result.getBaseTypeName == "int4")
  }

  it should "create a java.sql.Array from an Array[io.circe.Json]" in { implicit session =>
    val json = io.circe.Json.fromString("{}")
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(json, json))
    assert(result.getBaseTypeName == "varchar")
  }

  it should "create a java.sql.Array from an Array[BigDecimal]" in { implicit session =>
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(BigDecimal.valueOf(1.5)))
    assert(result.getBaseTypeName == "numeric")
  }

  it should "create a java.sql.Array from an Array[String]" in { implicit session =>
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array("asas", "bada"))
    assert(result.getBaseTypeName == "varchar")
  }

  it should "create a java.sql.Array from an Array[OffsetTime]" in { implicit session =>
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(OffsetTime.now()))
    assert(result.getBaseTypeName == "time")
  }

  it should "create a java.sql.Array from an Array[ZonedDateTime]" in { implicit session =>
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(ZonedDateTime.now()))
    assert(result.getBaseTypeName == "timestamp")
  }

  it should "create a java.sql.Array from an Array[UUID]" in { implicit session =>
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(Array(UUID.randomUUID(), UUID.randomUUID()))
    assert(result.getBaseTypeName == "varchar")
  }
