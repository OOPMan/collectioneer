package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.db.traits.entity.projected

given hasPropertyToProperty: Conversion[properties.HasProperty, projected.Property] =
  (hasProperty: properties.HasProperty) => hasProperty.property

given hasPropertyPropertyValueTupleToPropertyPropetyValueTuple
: Conversion[(properties.HasProperty, projected.PropertyValue), (projected.Property, projected.PropertyValue)] =
  (tuple: (properties.HasProperty, projected.PropertyValue)) => tuple._1.property -> tuple._2
