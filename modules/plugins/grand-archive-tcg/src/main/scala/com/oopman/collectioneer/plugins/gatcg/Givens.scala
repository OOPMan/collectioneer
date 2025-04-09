package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.db.traits.entity.projected

given hasPropertyToProperty: Conversion[properties.HasProperty, projected.Property] with
  def apply(a: properties.HasProperty) = a.property

given hasPropertyPropertyValueTupleToPropertyPropetyValueTuple
: Conversion[(properties.HasProperty, projected.PropertyValue), (projected.Property, projected.PropertyValue)] =
  (t: (properties.HasProperty, projected.PropertyValue)) => t._1.property -> t._2