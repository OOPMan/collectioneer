package com.oopman.collectioneer.plugins.postgresbackend.test

import com.oopman.collectioneer.db.entity.raw.Relationship
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType.*
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.{ChildOf, SourceOfPropertiesAndPropertyValues}
import scalikejdbc.DBSession

import java.time.{LocalDate, LocalTime, ZonedDateTime}
import java.util.UUID

class Fixtures()(implicit session: DBSession):
  import com.oopman.collectioneer.db.entity.projected.{Collection as ProjectedCollection, Property as ProjectedProperty, PropertyValue as ProjectedPropertyValue}
  import com.oopman.collectioneer.plugins.postgresbackend.dao.projected.{CollectionDAOImpl as ProjectedCollectionDAOImpl, PropertyDAOImpl as ProjectedPropertyDAOImpl}
  import com.oopman.collectioneer.plugins.postgresbackend.dao.raw.RelationshipDAOImpl
  import io.circe.*
  import io.circe.parser.*
  // Property fixtures
  val textProperty = ProjectedProperty(propertyName = "text", propertyTypes = List(text))
  val bytesProperty = ProjectedProperty(propertyName = "bytes", propertyTypes = List(bytes))
  val smallintProperty = ProjectedProperty(propertyName = "smallint", propertyTypes = List(smallint))
  val intProperty: ProjectedProperty = ProjectedProperty(propertyName = "int", propertyTypes = List(int))
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
  ProjectedPropertyDAOImpl.createOrUpdateProperties(allProperties)
  // Collection fixtures
  val rootA = ProjectedCollection()
  val childAofRootA = ProjectedCollection(propertyValues = Map(
    textProperty -> ProjectedPropertyValue(textValues = List("1")),
    bytesProperty -> ProjectedPropertyValue(byteValues = List("1".getBytes)),
    smallintProperty -> ProjectedPropertyValue(smallintValues = List(1.toShort)),
    intProperty -> ProjectedPropertyValue(intValues = List(1)),
    bigintProperty -> ProjectedPropertyValue(bigintValues = List(BigInt(1))),
    numericProperty -> ProjectedPropertyValue(numericValues = List(BigDecimal(1.1))),
    floatProperty -> ProjectedPropertyValue(floatValues = List(1f)),
    doubleProperty -> ProjectedPropertyValue(doubleValues = List(1f)),
    booleanProperty -> ProjectedPropertyValue(booleanValues = List(true)),
    dateProperty -> ProjectedPropertyValue(dateValues = List(LocalDate.parse("2025-01-01"))),
    timeProperty -> ProjectedPropertyValue(timeValues = List(LocalTime.parse("01:00:00"))),
    timestampProperty -> ProjectedPropertyValue(timestampValues = List(ZonedDateTime.parse("2025-01-01T00:00:00+01:00"))),
    uuidProperty -> ProjectedPropertyValue(uuidValues = List(UUID.fromString("0735c441-6574-4e47-8f33-13528a9eba11"))),
    jsonProperty -> ProjectedPropertyValue(jsonValues = List(parse("""{"1":true}""").getOrElse(Json.Null))),
    compositeProperty -> ProjectedPropertyValue(textValues = List("1"), intValues = List(1))
  ))
  val childBofRootA = ProjectedCollection(propertyValues = Map(
    textProperty -> ProjectedPropertyValue(textValues = List("2")),
    bytesProperty -> ProjectedPropertyValue(byteValues = List("2".getBytes)),
    smallintProperty -> ProjectedPropertyValue(smallintValues = List(2.toShort)),
    intProperty -> ProjectedPropertyValue(intValues = List(2)),
    bigintProperty -> ProjectedPropertyValue(bigintValues = List(BigInt(2))),
    numericProperty -> ProjectedPropertyValue(numericValues = List(BigDecimal(2.2))),
    floatProperty -> ProjectedPropertyValue(floatValues = List(2f)),
    doubleProperty -> ProjectedPropertyValue(doubleValues = List(2f)),
    booleanProperty -> ProjectedPropertyValue(booleanValues = List(false)),
    dateProperty -> ProjectedPropertyValue(dateValues = List(LocalDate.parse("2025-02-01"))),
    timeProperty -> ProjectedPropertyValue(timeValues = List(LocalTime.parse("02:00:00"))),
    timestampProperty -> ProjectedPropertyValue(timestampValues = List(ZonedDateTime.parse("2025-02-01T00:00:00+02:00"))),
    uuidProperty -> ProjectedPropertyValue(uuidValues = List(UUID.fromString("d84f3689-e322-45f4-96f7-80602f70d507"))),
    jsonProperty -> ProjectedPropertyValue(jsonValues = List(parse("""{"2":false}""").getOrElse(Json.Null))),
    compositeProperty -> ProjectedPropertyValue(textValues = List("2"), intValues = List(2))
  ))
  val childCofRootA = ProjectedCollection(propertyValues = Map(
    textProperty -> ProjectedPropertyValue(textValues = List("3")),
    bytesProperty -> ProjectedPropertyValue(byteValues = List("3".getBytes)),
    smallintProperty -> ProjectedPropertyValue(smallintValues = List(3.toShort)),
    intProperty -> ProjectedPropertyValue(intValues = List(3)),
    bigintProperty -> ProjectedPropertyValue(bigintValues = List(BigInt(3))),
    numericProperty -> ProjectedPropertyValue(numericValues = List(BigDecimal(3.1))),
    floatProperty -> ProjectedPropertyValue(floatValues = List(3f)),
    doubleProperty -> ProjectedPropertyValue(doubleValues = List(3f)),
    booleanProperty -> ProjectedPropertyValue(booleanValues = List(true)),
    dateProperty -> ProjectedPropertyValue(dateValues = List(LocalDate.parse("2025-03-01"))),
    timeProperty -> ProjectedPropertyValue(timeValues = List(LocalTime.parse("03:00:00"))),
    timestampProperty -> ProjectedPropertyValue(timestampValues = List(ZonedDateTime.parse("2025-03-01T00:00:00+01:00"))),
    uuidProperty -> ProjectedPropertyValue(uuidValues = List(UUID.fromString("a83eb86f-dbcb-4271-96e2-d7b97943953b"))),
    jsonProperty -> ProjectedPropertyValue(jsonValues = List(parse("""{"3":true}""").getOrElse(Json.Null))),
    compositeProperty -> ProjectedPropertyValue(textValues = List("3"), intValues = List(3))
  ))
  val rootB = ProjectedCollection()
  val childAofRootB = ProjectedCollection(propertyValues = Map(
    textProperty -> ProjectedPropertyValue(textValues = List("4")),
    bytesProperty -> ProjectedPropertyValue(byteValues = List("4".getBytes)),
    smallintProperty -> ProjectedPropertyValue(smallintValues = List(4.toShort)),
    intProperty -> ProjectedPropertyValue(intValues = List(4)),
    bigintProperty -> ProjectedPropertyValue(bigintValues = List(BigInt(4))),
    numericProperty -> ProjectedPropertyValue(numericValues = List(BigDecimal(4.4))),
    floatProperty -> ProjectedPropertyValue(floatValues = List(4f)),
    doubleProperty -> ProjectedPropertyValue(doubleValues = List(4f)),
    booleanProperty -> ProjectedPropertyValue(booleanValues = List(true)),
    dateProperty -> ProjectedPropertyValue(dateValues = List(LocalDate.parse("2025-04-01"))),
    timeProperty -> ProjectedPropertyValue(timeValues = List(LocalTime.parse("04:00:00"))),
    timestampProperty -> ProjectedPropertyValue(timestampValues = List(ZonedDateTime.parse("2025-04-01T00:00:00+04:00"))),
    uuidProperty -> ProjectedPropertyValue(uuidValues = List(UUID.fromString("2f2040ae-5c99-4cd4-8d02-4ae45c42a942"))),
    jsonProperty -> ProjectedPropertyValue(jsonValues = List(parse("""{"4":true}""").getOrElse(Json.Null))),
    compositeProperty -> ProjectedPropertyValue(textValues = List("4"), intValues = List(4))
  ))
  val childBofRootB = ProjectedCollection(propertyValues = Map(
    textProperty -> ProjectedPropertyValue(textValues = List("5")),
    bytesProperty -> ProjectedPropertyValue(byteValues = List("5".getBytes)),
    smallintProperty -> ProjectedPropertyValue(smallintValues = List(5.toShort)),
    intProperty -> ProjectedPropertyValue(intValues = List(5)),
    bigintProperty -> ProjectedPropertyValue(bigintValues = List(BigInt(5))),
    numericProperty -> ProjectedPropertyValue(numericValues = List(BigDecimal(5.5))),
    floatProperty -> ProjectedPropertyValue(floatValues = List(5f)),
    doubleProperty -> ProjectedPropertyValue(doubleValues = List(5f)),
    booleanProperty -> ProjectedPropertyValue(booleanValues = List(false)),
    dateProperty -> ProjectedPropertyValue(dateValues = List(LocalDate.parse("5055-05-01"))),
    timeProperty -> ProjectedPropertyValue(timeValues = List(LocalTime.parse("05:00:00"))),
    timestampProperty -> ProjectedPropertyValue(timestampValues = List(ZonedDateTime.parse("5055-05-01T00:00:00+05:00"))),
    uuidProperty -> ProjectedPropertyValue(uuidValues = List(UUID.fromString("fb6f4ab1-3145-4338-b359-557695cb538c"))),
    jsonProperty -> ProjectedPropertyValue(jsonValues = List(parse("""{"5":false}""").getOrElse(Json.Null))),
    compositeProperty -> ProjectedPropertyValue(textValues = List("5"), intValues = List(5))
  ))
  val childCofRootB = ProjectedCollection(propertyValues = Map(
    textProperty -> ProjectedPropertyValue(textValues = List("6")),
    bytesProperty -> ProjectedPropertyValue(byteValues = List("6".getBytes)),
    smallintProperty -> ProjectedPropertyValue(smallintValues = List(6.toShort)),
    intProperty -> ProjectedPropertyValue(intValues = List(6)),
    bigintProperty -> ProjectedPropertyValue(bigintValues = List(BigInt(6))),
    numericProperty -> ProjectedPropertyValue(numericValues = List(BigDecimal(6.6))),
    floatProperty -> ProjectedPropertyValue(floatValues = List(6f)),
    doubleProperty -> ProjectedPropertyValue(doubleValues = List(6f)),
    booleanProperty -> ProjectedPropertyValue(booleanValues = List(true)),
    dateProperty -> ProjectedPropertyValue(dateValues = List(LocalDate.parse("2025-06-01"))),
    timeProperty -> ProjectedPropertyValue(timeValues = List(LocalTime.parse("06:00:00"))),
    timestampProperty -> ProjectedPropertyValue(timestampValues = List(ZonedDateTime.parse("2025-06-06T00:00:00+06:00"))),
    uuidProperty -> ProjectedPropertyValue(uuidValues = List(UUID.fromString("2ab4b796-7c6d-4f25-8686-f9b5e6106121"))),
    jsonProperty -> ProjectedPropertyValue(jsonValues = List(parse("""{"6":true}""").getOrElse(Json.Null))),
    compositeProperty -> ProjectedPropertyValue(textValues = List("6"), intValues = List(6))
  ))
  val rootC = ProjectedCollection(propertyValues = Map(textProperty -> ProjectedPropertyValue(textValues = "rootC":: Nil)))
  val childAofRootC = ProjectedCollection(propertyValues = Map(textProperty -> ProjectedPropertyValue(textValues = "childAofRootC":: Nil)))
  val child1OfChildA = ProjectedCollection(propertyValues = Map(textProperty -> ProjectedPropertyValue(textValues = "child1OfChildA":: Nil)))
  val child2OfChildA = ProjectedCollection(propertyValues = Map(textProperty -> ProjectedPropertyValue(textValues = "child2OfChildA":: Nil)))
  val childXofChild1 = ProjectedCollection(propertyValues = Map(textProperty -> ProjectedPropertyValue(textValues = "childXofChild1":: Nil)))
  val childYofChild1 = ProjectedCollection(propertyValues = Map(textProperty -> ProjectedPropertyValue(textValues = "childYofChild1":: Nil)))
  val childXofChild2 = ProjectedCollection(propertyValues = Map(textProperty -> ProjectedPropertyValue(textValues = "childXofChild2":: Nil)))
  val childYofChild2 = ProjectedCollection(propertyValues = Map(textProperty -> ProjectedPropertyValue(textValues = "childYofChild2":: Nil)))
  val allCollections = Seq(
    rootA, childAofRootA, childBofRootA, childCofRootA, rootB, childAofRootB, childBofRootB, childCofRootB, rootC,
    childAofRootC, child1OfChildA, child2OfChildA, childXofChild1, childYofChild1, childXofChild2, childYofChild2
  )
  ProjectedCollectionDAOImpl.createOrUpdateCollections(allCollections)
  // Relationship fixtures
  val relationships = Seq(
    Relationship(relatedCollectionPK = childAofRootA.pk, relationshipType = ChildOf, collectionPK = rootA.pk),
    Relationship(relatedCollectionPK = childBofRootA.pk, relationshipType = ChildOf, collectionPK = rootA.pk),
    Relationship(relatedCollectionPK = childCofRootA.pk, relationshipType = ChildOf, collectionPK = rootA.pk),
    Relationship(relatedCollectionPK = childAofRootB.pk, relationshipType = ChildOf, collectionPK = rootB.pk),
    Relationship(relatedCollectionPK = childBofRootB.pk, relationshipType = ChildOf, collectionPK = rootB.pk),
    Relationship(relatedCollectionPK = childCofRootB.pk, relationshipType = ChildOf, collectionPK = rootB.pk),
    Relationship(relatedCollectionPK = childAofRootC.pk, relationshipType = SourceOfPropertiesAndPropertyValues, collectionPK = rootC.pk),
    Relationship(relatedCollectionPK = child1OfChildA.pk, relationshipType = ChildOf, collectionPK = childAofRootC.pk),
    Relationship(relatedCollectionPK = child2OfChildA.pk, relationshipType = ChildOf, collectionPK = childAofRootC.pk),
    Relationship(relatedCollectionPK = childXofChild1.pk, relationshipType = ChildOf, collectionPK = child1OfChildA.pk),
    Relationship(relatedCollectionPK = childYofChild1.pk, relationshipType = SourceOfPropertiesAndPropertyValues, collectionPK = child1OfChildA.pk),
    Relationship(relatedCollectionPK = childXofChild2.pk, relationshipType = ChildOf, collectionPK = child2OfChildA.pk),
    Relationship(relatedCollectionPK = childYofChild2.pk, relationshipType = SourceOfPropertiesAndPropertyValues, collectionPK = child2OfChildA.pk)
  )
  RelationshipDAOImpl.createOrUpdateRelationships(relationships)
