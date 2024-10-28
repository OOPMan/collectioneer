package com.oopman.collectioneer.plugins.postgresbackend.test.dao.raw

import com.oopman.collectioneer.{CoreProperties, given}
import com.oopman.collectioneer.db.entity.raw.Property
import com.oopman.collectioneer.plugins.postgresbackend.test.BaseFunSuite
import com.oopman.collectioneer.plugins.postgresbackend.dao.raw.PropertyDAOImpl

class RawPropertyDAOImplSpec extends BaseFunSuite:
  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.PropertyDAOImpl.createProperties"

  it should "create a Property" in { implicit session =>
    val property = Property()
    val result = PropertyDAOImpl.createProperties(Seq(property))
    assert(result.length > 0)
  }

  it should "fail to create a Property that already exists" in { implicit session =>
    val property = Property()
    val result = PropertyDAOImpl.createProperties(Seq(property))
    assert(result.length > 0)
    assertThrows[java.sql.BatchUpdateException](PropertyDAOImpl.createProperties(Seq(property)))
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.PropertyDAOImpl.createOrUpdateProperties"

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.PropertyDAOImpl.getAll"
  
  it should "return a list of Properties" in { implicit session =>
    val properties = PropertyDAOImpl.getAll
    assert(properties.nonEmpty)
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.PropertyDAOImpl.getAllMatchingPKs"

  it should "return a list of Properties" in { implicit session =>
    val properties = PropertyDAOImpl.getAllMatchingPKs(Seq(CoreProperties.name.pk))
    assert(properties.length == 1)
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.PropertyDAOImpl.getAllMatchingPropertyValues"

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.PropertyDAOImpl.getAllByPropertyCollection"
