package com.oopman.collectioneer

import com.oopman.collectioneer.db.traits.entity.raw.Relationship
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.ChildOf
import com.oopman.collectioneer.db.entity.raw
import com.oopman.collectioneer.given
import com.oopman.collectioneer.db.traits.entity.raw.given

private object CoreRelationshipUUIDs:
  val commonPropertiesChildOfRoot = "f3ff67b6-f1b9-43a3-99d9-961dc40b921b"
  val commonPropertiesOfPropertiesChildOfRoot = "ec863b4c-3873-47d4-8af9-300b6fcf7110"

enum CoreRelationships(val relationship: Relationship):
  case commonPropertiesChildOfRoot extends CoreRelationships(raw.Relationship(
    pk = CoreRelationshipUUIDs.commonPropertiesChildOfRoot,
    relatedCollectionPK = CoreCollections.commonProperties,
    relationshipType = ChildOf,
    collectionPK = CoreCollections.root,
  ))

  case commonPropertiesOfPropertiesChildOfRoot extends CoreRelationships(raw.Relationship(
    pk = CoreRelationshipUUIDs.commonPropertiesOfPropertiesChildOfRoot,
    relatedCollectionPK = CoreCollections.commonPropertiesOfProperties,
    relationshipType = ChildOf,
    collectionPK = CoreCollections.root,
  ))