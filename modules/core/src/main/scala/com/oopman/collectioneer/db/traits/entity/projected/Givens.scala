package com.oopman.collectioneer.db.traits.entity.projected

import com.oopman.collectioneer.db.traits.entity.projected

given projectedHasPropertyToProjectedProperty: Conversion[projected.HasProperty, projected.Property] =
  (hasProperty: projected.HasProperty) => hasProperty.property

given projectedHasPropertyPropertyValueTupleToProjectedPropertyPropetyValueTuple
: Conversion[(projected.HasProperty, projected.PropertyValue), (projected.Property, projected.PropertyValue)] =
  (tuple: (projected.HasProperty, projected.PropertyValue)) => tuple._1.property -> tuple._2