package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.db.entity.projected.{Collection, PropertyValue}
import com.oopman.collectioneer.db.entity.raw.Relationship
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.ParentCollection
import com.oopman.collectioneer.{CoreCollections, CoreProperties, given}
import com.oopman.collectioneer.db.traits.entity.raw.given

import java.util.UUID

val name = "Grand Archive TCG"
val description = "An anime TCG with western game design"

val GATCGRootCollection = Collection(
  pk = "b3192f7b-d4d6-4510-ba5c-aa1b60ab3982",
  virtual = true,
  propertyValues = Map(
    CoreProperties.name -> PropertyValue(textValues = name :: Nil),
    CoreProperties.description -> PropertyValue(textValues = description :: Nil),
    // TODO: Add properties for links
  )
)

val GATCGRootCollectionRelationship = Relationship(        
  pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${GATCGRootCollection.pk}-${CoreCollections.root.pk}-${ParentCollection}".getBytes),
  collectionPK = GATCGRootCollection,
  relatedCollectionPK = CoreCollections.root,
  relationshipType = ParentCollection
)