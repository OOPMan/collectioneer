package com.oopman.collectioneer.plugins.postgresbackend.test

import com.oopman.collectioneer.db.PropertyValueQueryDSL.*
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.plugins.postgresbackend.PropertyValueQueryDSLSupport
import com.oopman.collectioneer.{CoreProperties, given}

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

class PropertyValueQueryDSLSupportSpec extends BaseFunSuite:
  behavior of "com.oopman.collectioneer.plugins.postgresbackend.PropertyValueQueryDSLSupport.propertyTypeToScalarCast"
  
  it should "converts PropertyType to String" in { implicit session => 
    assert(PropertyValueQueryDSLSupport.propertyTypeToScalarCast(PropertyType.bytes) == "bytea")
    assert(PropertyValueQueryDSLSupport.propertyTypeToScalarCast(PropertyType.float) == "real")
    assert(PropertyValueQueryDSLSupport.propertyTypeToScalarCast(PropertyType.double) == "double precision")
    assert(PropertyValueQueryDSLSupport.propertyTypeToScalarCast(PropertyType.json) == "jsonb")
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.PropertyValueQueryDSLSupport.comparisonToSQL"
  
  it should "generate a 2-Tuple containing a String and a Seq[Any]" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalTo "String"
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
  }

  it should "handle BigInt PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq(1, 2).map(BigInt.apply)
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "int8")
  }

  it should "handle Boolean PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq(true, false)
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "bool")
  }

  it should "handle Array[Byte] PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq("some".getBytes, "bytes".getBytes)
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "bytea")
    // TODO: Confirm parameters are actually strings formatted suitably
  }

  it should "handle LocalDate PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq(LocalDate.now(), LocalDate.now().plusDays(1))
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "date")
  }

  it should "handle Double PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq(1, 2).map(_.toDouble)
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "float8")
  }

  it should "handle Float PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq(1, 2).map(_.toFloat)
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "float4")
  }

  it should "handle Int PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq(1, 2)
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "int4")
  }

  it should "handle io.circe.Json PropertyValueVectorComparisons" in { implicit session =>
    val json = io.circe.Json.fromString("{}")
    val comparison: Comparison = CoreProperties.name equalToAny Seq(json, json)
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "varchar")
  }

  it should "handle BigDecimal PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq(1, 2).map(BigDecimal.apply)
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "numeric")
  }

  it should "handle String PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq("1", "2")
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "varchar")
  }

  it should "handle LocalTime PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq(LocalTime.now(), LocalTime.now().minusHours(1))
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "time")
  }

  it should "handle ZonedDateTime PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq(ZonedDateTime.now(), ZonedDateTime.now().plusHours(5))
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "timestamp")
  }

  it should "handle UUID PropertyValueVectorComparisons" in { implicit session =>
    val comparison: Comparison = CoreProperties.name equalToAny Seq(UUID.randomUUID(), UUID.randomUUID())
    val (comparsonSQL, parameters) = PropertyValueQueryDSLSupport.comparisonToSQL(comparison)
    assert(comparsonSQL.nonEmpty)
    assert(parameters.length == 2)
    assert(parameters.head.isInstanceOf[UUID])
    assert(parameters(1).isInstanceOf[java.sql.Array])
    assert(parameters(1).asInstanceOf[java.sql.Array].getBaseTypeName == "varchar")
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.PropertyValueQueryDSLSupport.comparisonsToSQL"

  it should "generate a 2-Tuple containing a String and a Seq[Any]" in { implicit session =>
    val comparisons: Seq[Comparison] = Seq(
      CoreProperties.name equalTo "String",
      CoreProperties.visible equalTo true
    )
    val resultOption = PropertyValueQueryDSLSupport.comparisonsToSQL(comparisons)
    assert(resultOption.nonEmpty)
    resultOption
      .map { (comparisonsSQL, parameters) =>
        assert(comparisonsSQL.nonEmpty)
        assert(parameters.length == 4)
      }
  }
