package com.oopman.collectioneer

import com.oopman.collectioneer.db.traits.entity.{raw, projected}

import java.util.UUID


given stringToUUID: Conversion[String, UUID] = (s: String) => UUID.fromString(s)

given coreCollectionsToCollection: Conversion[CoreCollections, projected.Collection] =
  (c: CoreCollections) => c.collection

given coreCollectionsToUUID: Conversion[CoreCollections, UUID] =
  (c: CoreCollections) => c.collection.pk

given CollectionToUUID: Conversion[raw.Collection, UUID] =
  (c: db.traits.entity.raw.Collection) => c.pk

given PropertyToUUID: Conversion[raw.Property, UUID] =
  (p: db.traits.entity.raw.Property) => p.pk
