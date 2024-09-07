package com.oopman.collectioneer

import java.util.UUID


given stringToUUID: Conversion[String, UUID] = (s: String) => UUID.fromString(s)

given coreCollectionsToCollection: Conversion[CoreCollections, db.entity.projected.Collection] =
  (c: CoreCollections) => c.collection

given corePropertiesToProperty: Conversion[CoreProperties, db.entity.projected.Property] =
  (p: CoreProperties) =>  p.property
