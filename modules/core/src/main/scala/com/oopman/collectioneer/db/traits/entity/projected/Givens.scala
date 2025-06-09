package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits.entity.projected

import java.util.UUID

given projectedHasPropertyToProjectedProperty: Conversion[projected.HasProperty, projected.Property] =
  (hasProperty: projected.HasProperty) => hasProperty.property

given projectedHasPropertyPropertyValueTupleToProjectedPropertyPropetyValueTuple
: Conversion[(projected.HasProperty, projected.PropertyValue), (projected.Property, projected.PropertyValue)] =
  (tuple: (projected.HasProperty, projected.PropertyValue)) => tuple._1.property -> tuple._2

given projectedHasCollectionToRawCollection: Conversion[projected.HasCollection, projected.Collection] =
  (hasCollection: projected.HasCollection) => hasCollection.collection

given projectedHasCollectionToUUID: Conversion[projected.HasCollection, UUID] =
  (hasCollection: projected.HasCollection) => hasCollection.collection.pk