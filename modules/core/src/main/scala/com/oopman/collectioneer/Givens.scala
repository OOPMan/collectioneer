package com.oopman.collectioneer

import java.util.UUID


given stringToUUID: Conversion[String, UUID] with
  def apply(s: String): UUID = UUID.fromString(s)


given coreCollectionsToCollection: Conversion[CoreCollections, db.entity.projected.Collection] with
  def apply(c: CoreCollections): db.entity.projected.Collection = c.collection


given corePropertiesToProperty: Conversion[CoreProperties, db.entity.projected.Property] with
  def apply(p: CoreProperties): db.entity.projected.Property = p.property
