package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.db.traits.entity.projected.{Property, PropertyValue}

given hasPropertyToProperty: Conversion[properties.HasProperty, Property] with
  def apply(a: properties.HasProperty) = a.property

given hasPropertyPropertyValueTupleToPropertyPropetyValueTuple
: Conversion[(properties.HasProperty, PropertyValue), (Property, PropertyValue)] =
  (t: (properties.HasProperty, PropertyValue)) => t._1.property -> t._2