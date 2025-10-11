package com.oopman.collectioneer

import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.db.traits.entity.raw.Relationship
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.ChildOf
import com.oopman.collectioneer.given

private object CoreRelationshipUUIDs:
  val commonPropertiesChildOfProperties = "f3ff67b6-f1b9-43a3-99d9-961dc40b921b"
  val commonPropertiesOfPropertiesChildOfProperties = "ec863b4c-3873-47d4-8af9-300b6fcf7110"

enum CoreRelationships(val relationship: Relationship):
  case commonPropertiesChildOfProperties extends CoreRelationships(raw.Relationship(
    pk = CoreRelationshipUUIDs.commonPropertiesChildOfProperties,
    relatedCollectionPK = CoreCollections.commonProperties,
    relationshipType = ChildOf,
    collectionPK = CoreCollections.properties,
  ))

  case commonPropertiesOfPropertiesChildOfProperties extends CoreRelationships(raw.Relationship(
    pk = CoreRelationshipUUIDs.commonPropertiesOfPropertiesChildOfProperties,
    relatedCollectionPK = CoreCollections.commonPropertiesOfProperties,
    relationshipType = ChildOf,
    collectionPK = CoreCollections.properties,
  ))