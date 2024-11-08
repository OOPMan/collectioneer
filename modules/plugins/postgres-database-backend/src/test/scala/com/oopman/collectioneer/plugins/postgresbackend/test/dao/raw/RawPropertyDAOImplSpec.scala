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

  it should "create a Property" in { implicit session =>
    val property = Property()
    val result = PropertyDAOImpl.createOrUpdateProperties(Seq(property))
    assert(result.length > 0)
  }

  it should "update a Property" in { implicit session =>
    val property = Property()
    val result = PropertyDAOImpl.createOrUpdateProperties(Seq(property))
    assert(result.length > 0)
    val updatedProperty = property.copy(propertyName = "something different")
    val newResult = PropertyDAOImpl.createOrUpdateProperties(Seq(property))
    assert(newResult.length > 0)
  }

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

  it should "return a list of Properties matching the PropertyValueComparisons supplied" in { implicit session =>
    import com.oopman.collectioneer.db.PropertyValueQueryDSL.*
    import com.oopman.collectioneer.db.entity.projected.{Property as ProjectedProperty, PropertyValue as ProjecetedPropertyValue}
    import com.oopman.collectioneer.plugins.postgresbackend.dao.projected.PropertyDAOImpl as ProjectedPropertyDAOImpl
    val property = ProjectedProperty(
      propertyValues = List(
        ProjecetedPropertyValue(property = CoreProperties.visible, booleanValues = List(true)),
        ProjecetedPropertyValue(property = CoreProperties.minValues, intValues = List(1))
      )
    )
    ProjectedPropertyDAOImpl.createOrUpdateProperties(Seq(property))
    val properties = PropertyDAOImpl.getAllMatchingPropertyValues(Seq(
      CoreProperties.visible equalTo true
    ))
    assert(properties.length == 1)
    assert(properties.head.pk.equals(property.pk))
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.PropertyDAOImpl.getAllByPropertyCollection"
