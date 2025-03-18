package com.oopman.collectioneer

import java.util.UUID


given stringToUUID: Conversion[String, UUID] = (s: String) => UUID.fromString(s)

given coreCollectionsToCollection: Conversion[CoreCollections, db.traits.entity.projected.Collection] =
  (c: CoreCollections) => c.collection

// TODO: Shouldn't this be db.traits.entity.raw.Property?
given corePropertiesToProperty: Conversion[CoreProperties, db.entity.projected.Property] =
  (p: CoreProperties) =>  p.property

given coreCollectionsToUUID: Conversion[CoreCollections, UUID] =
  (c: CoreCollections) => c.collection.pk

given CollectionToUUID: Conversion[db.traits.entity.raw.Collection, UUID] =
  (c: db.traits.entity.raw.Collection) => c.pk

given corePropertiesToUUID: Conversion[CoreProperties, UUID] =
  (p: CoreProperties) => p.property.pk

given PropertyToUUID: Conversion[db.traits.entity.raw.Property, UUID] =
  (p: db.traits.entity.raw.Property) => p.pk