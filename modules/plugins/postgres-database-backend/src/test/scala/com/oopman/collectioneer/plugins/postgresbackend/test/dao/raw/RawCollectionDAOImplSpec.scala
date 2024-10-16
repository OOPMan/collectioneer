package com.oopman.collectioneer.plugins.postgresbackend.test.dao.raw

import com.oopman.collectioneer.CoreCollections
import com.oopman.collectioneer.db.entity.raw.Collection
import com.oopman.collectioneer.plugins.postgresbackend.dao.raw.CollectionDAOImpl
import com.oopman.collectioneer.plugins.postgresbackend.test.BaseFunSuite

class RawCollectionDAOImplSpec extends BaseFunSuite:
  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.CollectionDAOImpl.createCollections"

  it should "create a Collection" in { implicit session =>
    val collection = Collection()
    val result = CollectionDAOImpl.createCollections(Seq(collection))
    assert(result.length > 0)
  }
  
  it should "fail to create a Collection that already exists" in { implicit session => 
    val collection = Collection()
    val result = CollectionDAOImpl.createCollections(Seq(collection))
    assert(result.length > 0)
    assertThrows[java.sql.BatchUpdateException](CollectionDAOImpl.createCollections(Seq(collection)))
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.CollectionDAOImpl.createOrUpdateCollections"

  it should "create a Collection" in { implicit session =>
    val collection = Collection()
    val result = CollectionDAOImpl.createOrUpdateCollections(Seq(collection))
    assert(result.length > 0)
  }

  it should "update a Collection" in { implicit session =>
    val collection = Collection()
    val result = CollectionDAOImpl.createOrUpdateCollections(Seq(collection))
    assert(result.length > 0)
    val updatedCollection = collection.copy(virtual = true)
    val newResult = CollectionDAOImpl.createOrUpdateCollections(Seq(updatedCollection))
    assert(result.length > 0)
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.CollectionDAOImpl.getAll"

  it should "return a List of raw Collection objects" in { implicit session =>
    val collections = CollectionDAOImpl.getAll
    assert(collections.nonEmpty)
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.CollectionDAOImpl.getAllMatchingPKs"

  it should "return a List of raw Collection objects" in { implicit session =>
    val collections = CollectionDAOImpl.getAllMatchingPKs(Seq(
      CoreCollections.commonProperties.collection.pk,
      CoreCollections.commonPropertiesOfProperties.collection.pk
    ))
    assert(collections.length == 2)
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.CollectionDAOImpl.getAllMatchingPropertyValues"
  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.CollectionDAOImpl.getAllRelatedMatchingPropertyValues"
