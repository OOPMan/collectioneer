package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.db.entity.raw.Relationship
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.ChildOf
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.plugins.gatcg.properties.CommonProperties
import com.oopman.collectioneer.{CoreCollections, CoreProperties, given}

import java.util.UUID

val name = "Grand Archive TCG"
val description = "An anime TCG with western game design"

val GATCGRootCollection = Collection(
  pk = "b3192f7b-d4d6-4510-ba5c-aa1b60ab3982",
  virtual = true,
  propertyValues = Map(
    CoreProperties.name -> PropertyValue(textValues = name :: Nil),
    CoreProperties.description -> PropertyValue(textValues = description :: Nil),
    CommonProperties.isGATCGCollection -> PropertyValue (booleanValues = true :: Nil),
    // TODO: Add properties for links
  )
)

val GATCGPropertiesCollection = Collection(
  pk = "7308d9e3-d814-42b2-ac05-41a0e8b99299",
  virtual = true,
  properties = properties.AllProperties,
  propertyValues = Map(
    CoreProperties.name -> PropertyValue(textValues = "Grand Archive TCG Properties" :: Nil),
    CoreProperties.description -> PropertyValue(textValues = "A collection of properties belong to the Grand Archive TCG Plugin" :: Nil)
  )
)

val GATCGRootCollectionRelationship = Relationship(        
  pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${GATCGRootCollection.pk}-${CoreCollections.root.pk}-${ChildOf}".getBytes),
  relatedCollectionPK = GATCGRootCollection,
  relationshipType = ChildOf,
  collectionPK = CoreCollections.root,
)

val GATCGPropertiesCollectionRelationship = Relationship(
  pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${GATCGPropertiesCollection.pk}-${CoreCollections.properties.pk}-${ChildOf}".getBytes),
  relatedCollectionPK = GATCGPropertiesCollection,
  relationshipType = ChildOf,
  collectionPK = CoreCollections.properties
)