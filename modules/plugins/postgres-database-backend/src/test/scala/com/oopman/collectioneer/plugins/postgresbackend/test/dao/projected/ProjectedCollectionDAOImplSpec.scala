package com.oopman.collectioneer.plugins.postgresbackend.test.dao.projected

import com.oopman.collectioneer.plugins.postgresbackend.test.BaseFunSuite
import com.oopman.collectioneer.{CoreCollections, CoreProperties, given}
import com.oopman.collectioneer.plugins.postgresbackend.dao.projected.CollectionDAOImpl

class ProjectedCollectionDAOImplSpec extends BaseFunSuite:
  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.projected.CollectionDAOImpl.getAllMatchingPKs"

  it should "return a List of projected Collections constrained by the given Collection PKs" in { implicit session =>
    val collections = CollectionDAOImpl.getAllMatchingPKs(Seq(CoreCollections.commonProperties.collection.pk))
    assert(collections.length == 1)
    val collection = collections.head
    assert(collection.properties.length == 2)
    assert(collection.properties.exists(_.pk.equals(CoreProperties.name.property.pk)))
    assert(collection.properties.exists(_.pk.equals(CoreProperties.description.property.pk)))
    assert(collection.propertyValues.length == 2)
  }

  it should "return a List of projected Collections constrained by the given Collection. Properties and PropertyValues retrieved should be constrained by given PropertyPKs" in { implicit session =>
    val collections = CollectionDAOImpl.getAllMatchingPKs(
      collectionPKs = Seq(CoreCollections.commonPropertiesOfProperties.collection.pk),
      propertyPKs = Seq(CoreProperties.defaultValue.property.pk, CoreProperties.minValues.property.pk)
    )
    assert(collections.length == 1)
    val collection = collections.head
    assert(collection.properties.length == 2)
    assert(collection.properties.exists(_.pk.equals(CoreProperties.defaultValue.property.pk)))
    assert(collection.properties.exists(_.pk.equals(CoreProperties.minValues.property.pk)))
  }
