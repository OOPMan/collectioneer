package com.oopman.collectioneer.db.entity

import java.time.ZonedDateTime
import java.util.UUID

enum PropertiesCollectionsRelatedCollectionsRelationship:
  case PropertyOfRelationship extends PropertiesCollectionsRelatedCollectionsRelationship
  case PropertyOfCollection extends PropertiesCollectionsRelatedCollectionsRelationship
  case PropertyOfRelatedCollection extends PropertiesCollectionsRelatedCollectionsRelationship

case class PropertiesCollectionsRelatedCollections
(
  propertyPK: UUID,
  collectionsRelatedCollectionsPK: UUID,
  propertyValueSetPK: UUID,
  relationship: PropertiesCollectionsRelatedCollectionsRelationship = PropertiesCollectionsRelatedCollectionsRelationship.PropertyOfRelationship,
  index: Int = 0,
  created: ZonedDateTime = ZonedDateTime.now(),
  modified: ZonedDateTime = ZonedDateTime.now()
)
