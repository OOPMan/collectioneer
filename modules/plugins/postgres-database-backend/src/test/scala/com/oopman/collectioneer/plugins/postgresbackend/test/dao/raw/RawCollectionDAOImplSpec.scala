package com.oopman.collectioneer.plugins.postgresbackend.test.dao.raw

import com.oopman.collectioneer.CoreCollections
import com.oopman.collectioneer.db.SortDirection
import com.oopman.collectioneer.db.entity.raw.{Collection, Relationship}
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.{ParentCollection, SourceOfPropertiesAndPropertyValues}
import com.oopman.collectioneer.plugins.postgresbackend.dao.raw.CollectionDAOImpl
import com.oopman.collectioneer.plugins.postgresbackend.test.{BaseFunSuite, Fixtures}

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

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

  it should "return a List of raw Collection objects matching the PropertyValueComparisons supplied" in { implicit session =>
    import com.oopman.collectioneer.db.PropertyValueQueryDSL.*
    import com.oopman.collectioneer.{CoreProperties, given}
    val collections = CollectionDAOImpl.getAllMatchingPropertyValues(Seq(
      CoreProperties.name equalTo "Common Properties"
    ))
    assert(collections.length == 1)
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.CollectionDAOImpl.getAllRelatedMatchingPropertyValues"

  it should "return a List of raw Collection objects matching the PropertyValueComparisons supplied, either directly or via Relationship" in { implicit session =>
    import com.oopman.collectioneer.db.PropertyValueQueryDSL.*
    import com.oopman.collectioneer.plugins.postgresbackend.dao.raw.RelationshipDAOImpl
    import com.oopman.collectioneer.{CoreCollections, CoreProperties, given}
    val collection = Collection()
    val relationship = Relationship(
      collectionPK = collection.pk,
      relatedCollectionPK = CoreCollections.commonProperties.pk,
      relationshipType = SourceOfPropertiesAndPropertyValues
    )
    val createCollectionResult = CollectionDAOImpl.createCollections(Seq(collection))
    assert(createCollectionResult.length > 0)
    val createRelationshipResult = RelationshipDAOImpl.createRelationships(Seq(relationship))
    assert(createRelationshipResult.length > 0)
    val collections = CollectionDAOImpl.getAllRelatedMatchingPropertyValues(Seq(
      CoreProperties.name equalTo "Common Properties"
    ))
    assert(collections.length == 2)
  }

  behavior of "com.oopman.collectioneer.plugins.postgresbackend.dao.raw.CollectionDAOImpl.getAllMatchingConstraints"

  it should "return a List of raw Collections objects matching the supplied Constraints (all parameters filled)" in { implicit session =>
    import com.oopman.collectioneer.db.PropertyValueQueryDSL.*
    val fixtures = new Fixtures()
    import fixtures._
    val expectedCollectionPKs = List(
      childCofRootB, childBofRootB, childAofRootB, childCofRootA, childBofRootA, childAofRootA
    ).map(_.pk)
    val collections = CollectionDAOImpl.getAllMatchingConstraints(
      comparisons = Seq(
        intProperty gte 1,
        intProperty lte 6
      ),
      collectionPKs = Some(expectedCollectionPKs),
      parentCollectionPKs = Some(List(rootA, rootB).map(_.pk)),
      sortProperties = Seq((intProperty, SortDirection.Desc)),
      offset = Some(0),
      limit = Some(10)
    )
    assert(collections.length == 6)
    val collectionPKs = collections.map(_.pk)
    assertResult(expectedCollectionPKs)(collectionPKs)
  }

  it should "return a List of raw Collections objects matching the supplied Constraints (PropertyValueComparisons)" in { implicit session =>
    import com.oopman.collectioneer.db.PropertyValueQueryDSL.*
    val fixtures = new Fixtures()
    import fixtures._
    val collectionsA = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      (textProperty like "%1%") or (textProperty equalTo "6")
    ))
    assert(collectionsA.length == 2)
    val collectionsB = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      bytesProperty equalTo "1".getBytes
    ))
    assert(collectionsB.length == 1)
    val collectionsC = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      smallintProperty notEqualTo 2.toShort
    ))
    assert(collectionsC.length == 5)
    val collectionsD = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      intProperty gte 0,
      intProperty lt 4
    ))
    assert(collectionsD.length == 3)
    val collectionsE = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      bigintProperty gte BigInt(1),
      bigintProperty lte BigInt(5)
    ))
    assert(collectionsE.length == 5)
    val collectionsF = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      ((numericProperty gte BigDecimal(1.1)) and (numericProperty lte BigDecimal(2.2))) or
      ((numericProperty gte BigDecimal(5.5)) and (numericProperty lte BigDecimal(6.6)))
    ))
    assert(collectionsF.length == 4)
    val collectionsG = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      floatProperty notEqualTo 1f,
      floatProperty notEqualTo 2f
    ))
    assert(collectionsG.length == 4)
    val collectionsH = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      doubleProperty equalTo 2f
    ))
    assert(collectionsH.length == 1)
    val collectionsI = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      booleanProperty equalTo true
    ))
    assert(collectionsI.length == 4)
    val collectionsJ = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      dateProperty gt LocalDate.parse("2025-03-01")
    ))
    assert(collectionsJ.length == 3)
    val collectionsK = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      timeProperty lt LocalTime.parse("04:00:00")
    ))
    assert(collectionsK.length == 3)
    val collectionsL = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      timestampProperty lte ZonedDateTime.parse("2025-04-01T00:00:00+04:00")
    ))
    assert(collectionsL.length == 4)
    val collectionsM = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      uuidProperty equalToAny List(
        UUID.fromString("0735c441-6574-4e47-8f33-13528a9eba11"),
        UUID.fromString("d84f3689-e322-45f4-96f7-80602f70d507")
      )
    ))
    assert(collectionsM.length == 2)
    //TODO: Currently broken. See https://github.com/OOPMan/collectioneer/issues/20
//    import io.circe.*
//    import io.circe.parser.*
//    val collectionsN = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
//      jsonProperty equalTo parse("""{"1":true}""").getOrElse(Json.Null)
//    ))
//    assert(collectionsN.length == 1)
    val collectionsO = CollectionDAOImpl.getAllMatchingConstraints(comparisons = Seq(
      (compositeProperty equalTo "1") or (compositeProperty equalTo 2)
    ))
    assert(collectionsO.length == 2)
  }

  it should "return a List of raw Collections objects matching the supplied constraints (ParentCollections)" in { implicit session =>
    val fixtures = new Fixtures()
    import fixtures._
    val collectionsA = CollectionDAOImpl.getAllMatchingConstraints(parentCollectionPKs = Some(Seq(rootA.pk)))
    assert(collectionsA.length == 3)
    assertResult(Set(childAofRootA.pk, childBofRootA.pk, childCofRootA.pk))(collectionsA.map(_.pk).toSet)
  }

  it should "return a List of raw Collections objects matching the supplied constraints (Collections)" in { implicit session =>
    val fixtures = new Fixtures()
    import fixtures._
    val collectionsA = CollectionDAOImpl.getAllMatchingConstraints(collectionPKs = Some(Seq(
      rootA.pk, childCofRootA.pk, rootB.pk, childAofRootB.pk
    )))
    assert(collectionsA.length == 4)
    assertResult(Set(rootA.pk, childCofRootA.pk, rootB.pk, childAofRootB.pk))(collectionsA.map(_.pk).toSet)
  }

  it should "return a List of raw Collections objects matching the supplied constraints (SortProperties)" in { implicit session =>
    val fixtures = new Fixtures()
    import fixtures._
    val expectedCollectionPKs = List(
      childCofRootB, childBofRootB, childAofRootB, childCofRootA, childBofRootA, childAofRootA
    ).map(_.pk)
    val collectionsA = CollectionDAOImpl.getAllMatchingConstraints(
      parentCollectionPKs = Some(Seq(rootA.pk, rootB.pk)),
      sortProperties = Seq((compositeProperty, SortDirection.Desc))
    )
    assert(collectionsA.length == 6)
    assertResult(expectedCollectionPKs)(collectionsA.map(_.pk))
  }

  it should "return a List of raw Collections objects matching the supplied constraints (Offset)" in { implicit session =>
    val fixtures = new Fixtures()
    import fixtures._
    val collectionsA = CollectionDAOImpl.getAllMatchingConstraints(
      parentCollectionPKs = Some(Seq(rootA.pk, rootB.pk)),
      offset = Some(3)
    )
    assert(collectionsA.length == 3)
  }

  it should "return a List of raw Collections objects matching the supplied constraints (Limit)" in { implicit session =>
    val fixtures = new Fixtures()
    val collectionsA = CollectionDAOImpl.getAllMatchingConstraints(
      limit = Some(4)
    )
    assert(collectionsA.length == 4)
  }

  it should "return a List of raw Collections objects matching the supplied constraints (No Parameters)" in { implicit session =>
    val fixtures = new Fixtures()
    val collectionsA = CollectionDAOImpl.getAllMatchingConstraints()
    assert(collectionsA.nonEmpty)
  }