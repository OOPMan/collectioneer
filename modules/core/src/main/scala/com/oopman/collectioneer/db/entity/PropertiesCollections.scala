package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

enum PropertiesCollectionsRelationship:
  case PropertyOfCollection extends PropertiesCollectionsRelationship
  case CollectionOfPropertiesOfProperty extends PropertiesCollectionsRelationship

case class PropertiesCollections
(
  propertyPK: UUID,
  collectionPK: UUID,
  propertyValueSetPK: UUID,
  index: Int = 0,
  relationship: PropertiesCollectionsRelationship = PropertiesCollectionsRelationship.PropertyOfCollection,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
)
