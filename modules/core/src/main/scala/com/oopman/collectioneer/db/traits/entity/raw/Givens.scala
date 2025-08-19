package com.oopman.collectioneer.db.traits.entity.raw

import com.oopman.collectioneer.db.traits.entity.{projected, raw}

import java.util.UUID

given rawHasPropertyToRawProperty: Conversion[raw.HasProperty, raw.Property] =
  (hasProperty: raw.HasProperty) => hasProperty.property

given rawHasPropertyPropertyValueTupleToRawPropertyPropetyValueTuple
: Conversion[(raw.HasProperty, projected.PropertyValue), (raw.Property, projected.PropertyValue)] =
  (tuple: (raw.HasProperty, projected.PropertyValue)) => tuple._1.property -> tuple._2
  
given rawHasCollectionToRawCollection: Conversion[raw.HasCollection, raw.Collection] =
  (hasCollection: raw.HasCollection) => hasCollection.collection
  
given rawHasCollectionToUUID: Conversion[raw.HasCollection, UUID] =
  (hasCollection: raw.HasCollection) => hasCollection.collection.pk