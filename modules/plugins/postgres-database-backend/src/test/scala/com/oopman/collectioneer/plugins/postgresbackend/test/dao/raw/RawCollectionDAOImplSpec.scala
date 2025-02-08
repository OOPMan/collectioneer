package com.oopman.collectioneer.plugins.postgresbackend.test.dao.raw

import com.oopman.collectioneer.CoreCollections
import com.oopman.collectioneer.db.entity.raw.{Collection, Relationship}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType.{bigint, boolean, bytes, date, double, float, int, json, numeric, smallint, text, time, timestamp, uuid}
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.SourceOfPropertiesAndPropertyValues
import com.oopman.collectioneer.plugins.postgresbackend.dao.raw.CollectionDAOImpl
import com.oopman.collectioneer.plugins.postgresbackend.test.BaseFunSuite
import io.circe.parser.parse
import scalikejdbc.DBSession

import java.time.{LocalDate, OffsetTime, ZonedDateTime}
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

  def getAllMatchingConstraintsFixture(implicit session: DBSession) = new {
    import io.circe._, io.circe.parser._
    import com.oopman.collectioneer.plugins.postgresbackend.dao.raw.RelationshipDAOImpl
    import com.oopman.collectioneer.db.entity.projected.{
      Collection as ProjectedCollection,
      Property as ProjectedProperty,
      PropertyValue as ProjectedPropertyValue
    }
    import com.oopman.collectioneer.plugins.postgresbackend.dao.projected.{
      CollectionDAOImpl as ProjectedCollectionDAOImpl,
      PropertyDAOImpl as ProjectedPropertyDAOImpl
    }
    // Property fixtures
    val textProperty = ProjectedProperty(propertyName = "text", propertyTypes = List(text))
    val bytesProperty = ProjectedProperty(propertyName = "bytes", propertyTypes = List(bytes))
    val smallintProperty = ProjectedProperty(propertyName = "smallint", propertyTypes = List(smallint))
    val intProperty = ProjectedProperty(propertyName = "int", propertyTypes = List(int))
    val bigintProperty = ProjectedProperty(propertyName = "bigint", propertyTypes = List(bigint))
    val numericProperty = ProjectedProperty(propertyName = "numeric", propertyTypes = List(numeric))
    val floatProperty = ProjectedProperty(propertyName = "float", propertyTypes = List(float))
    val doubleProperty = ProjectedProperty(propertyName = "double", propertyTypes = List(double))
    val booleanProperty = ProjectedProperty(propertyName = "boolean", propertyTypes = List(boolean))
    val dateProperty = ProjectedProperty(propertyName = "date", propertyTypes = List(date))
    val timeProperty = ProjectedProperty(propertyName = "time", propertyTypes = List(time))
    val timestampProperty = ProjectedProperty(propertyName = "timestamp", propertyTypes = List(timestamp))
    val uuidProperty = ProjectedProperty(propertyName = "uuid", propertyTypes = List(uuid))
    val jsonProperty = ProjectedProperty(propertyName = "json", propertyTypes = List(json))
    val compositeProperty = ProjectedProperty(propertyName = "text and int", propertyTypes = List(text, int))
    val allProperties = Seq(
      textProperty, bytesProperty, smallintProperty, intProperty, bigintProperty, numericProperty, floatProperty,
      doubleProperty, booleanProperty, dateProperty, timeProperty, timestampProperty, uuidProperty, jsonProperty,
      compositeProperty
    )
    ProjectedPropertyDAOImpl.createProperties(allProperties)
    // TODO: Collection fiuxtures
    val rootCollectionA = ProjectedCollection()
    val childAofRootA = ProjectedCollection(propertyValues = List(
      ProjectedPropertyValue(property = textProperty, textValues = List("1")),
      ProjectedPropertyValue(property = bytesProperty, byteValues = List("1".getBytes)),
      ProjectedPropertyValue(property = smallintProperty, smallintValues = List(1.toShort)),
      ProjectedPropertyValue(property = intProperty, intValues = List(1)),
      ProjectedPropertyValue(property = bigintProperty, bigintValues = List(BigInt(1))),
      ProjectedPropertyValue(property = numericProperty, numericValues = List(BigDecimal(1.1))),
      ProjectedPropertyValue(property = floatProperty, floatValues = List(1f)),
      ProjectedPropertyValue(property = doubleProperty, doubleValues = List(1f)),
      ProjectedPropertyValue(property = booleanProperty, booleanValues = List(true)),
      ProjectedPropertyValue(property = dateProperty, dateValues = List(LocalDate.parse("2025-01-01"))),
      ProjectedPropertyValue(property = timeProperty, timeValues = List(OffsetTime.parse("00:00:00+01:00"))),
      ProjectedPropertyValue(property = timestampProperty, timestampValues = List(ZonedDateTime.parse("2025-01-01T00:00:00+01:00"))),
      ProjectedPropertyValue(property = uuidProperty, uuidValues = List(UUID.fromString("0735c441-6574-4e47-8f33-13528a9eba11"))),
      ProjectedPropertyValue(property = jsonProperty, jsonValues = List(parse("{1:true}").getOrElse(Json.Null))),
      ProjectedPropertyValue(property = compositeProperty, textValues = List("1"), intValues = List(1))
    ))
    val childBofRootA = ProjectedCollection(propertyValues = List())
    val childCofRootA = ProjectedCollection(propertyValues = List())
    val rootCollectionB = ProjectedCollection()
    val childAofRootB = ProjectedCollection(propertyValues = List())
    val childBofRootB = ProjectedCollection(propertyValues = List())
    val childCofRootB = ProjectedCollection(propertyValues = List())
    // TODO: Relationship fixtures
  }

  it should "return a List of raw Collections objects matching the supplied Constraints" in { implicit session =>
    import com.oopman.collectioneer.db.PropertyValueQueryDSL.*
    val fixtures = getAllMatchingConstraintsFixture
    // TODO: All parameters test

  }

  // TODO: Test of PropertyValueComparisons
  // TODO: Test of ParentCollections constraint
  // TODO: Test of Collections constraint
  // TODO: Test of Offset
  // TODO: Test of Limit