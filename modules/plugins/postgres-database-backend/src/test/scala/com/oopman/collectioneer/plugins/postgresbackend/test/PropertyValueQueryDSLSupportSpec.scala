package com.oopman.collectioneer.plugins.postgresbackend.test

import com.oopman.collectioneer.db.PropertyValueQueryDSL
import com.oopman.collectioneer.plugins.postgresbackend.PropertyValueQueryDSLSupport

class PropertyValueQueryDSLSupportSpec extends BaseFunSuite:
  behavior of "com.oopman.collectioneer.plugins.postgresbackend.PropertyValueQueryDSLSupport.makeJDBCArray"
  
  it should "create a java.sql.Array from an Array[BigInt]" in { implicit session => 
    val anArray: Array[PropertyValueQueryDSL.Value] = Array(BigInt(1), BigInt(2))
    val result = PropertyValueQueryDSLSupport.makeJDBCArray(anArray)
    assert(result.getBaseTypeName == "BIGINT")
  }

