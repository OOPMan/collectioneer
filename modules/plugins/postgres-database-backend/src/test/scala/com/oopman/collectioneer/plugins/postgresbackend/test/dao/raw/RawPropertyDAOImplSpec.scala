package com.oopman.collectioneer.plugins.postgresbackend.test.dao.raw

import com.oopman.collectioneer.{CoreProperties, given}
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.db.entity.raw.Property
import com.oopman.collectioneer.db.traits.entity.raw.{PropertyCollectionRelationshipType, PropertyType}
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
    val newResult = PropertyDAOImpl.createOrUpdateProperties(Seq(updatedProperty))
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
    import com.oopman.collectioneer.db.entity.projected.{Property as ProjectedProperty, PropertyValue as ProjectedPropertyValue}
    import com.oopman.collectioneer.plugins.postgresbackend.dao.projected.PropertyDAOImpl as ProjectedPropertyDAOImpl
    val property = ProjectedProperty(
      propertyValues = Map(
        CoreProperties.visible -> ProjectedPropertyValue(booleanValues = List(true)),
        CoreProperties.minValues -> ProjectedPropertyValue(intValues = List(1))
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

  it should "return a List of Properties associated with the supplied Collection PKs and PropertyCollectionRelationshipTypes" in { implicit session =>
    import com.oopman.collectioneer.db.entity.projected.{Collection as ProjectedCollection, Property as ProjectedProperty, PropertyValue as ProjecetedPropertyValue}
    import com.oopman.collectioneer.plugins.postgresbackend.dao.projected.PropertyDAOImpl as ProjectedPropertyDAOImpl
    import com.oopman.collectioneer.plugins.postgresbackend.dao.projected.CollectionDAOImpl as ProjectedCollectionDAOImpl
    val propertyA = ProjectedProperty(propertyName = "propertyA", propertyTypes = List(PropertyType.text))
    val propertyB = ProjectedProperty(propertyName = "propertyB", propertyTypes = List(PropertyType.int))
    val collection = ProjectedCollection( properties = List(propertyA, propertyB))
    ProjectedPropertyDAOImpl.createProperties(Seq(propertyA, propertyB))
    ProjectedCollectionDAOImpl.createCollections(Seq(collection))
    val propertiesByCollection = PropertyDAOImpl.getAllByPropertyCollection(Seq(collection.pk), Seq(PropertyCollectionRelationshipType.PropertyOfCollection))
    assert(propertiesByCollection.size == 1)
    assert(propertiesByCollection(collection.pk).length == 2)
    val inputPKs = Set(propertyA.pk, propertyB.pk)
    val outputPKs = propertiesByCollection(collection.pk).map(_.pk).toSet
    assert(outputPKs.equals(inputPKs))
  }
