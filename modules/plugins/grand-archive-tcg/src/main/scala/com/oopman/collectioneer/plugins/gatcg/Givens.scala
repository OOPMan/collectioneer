package com.oopman.collectioneer.plugins.gatcg

import com.oopman.collectioneer.db.entity.projected.Property

given hasPropertyToProperty: Conversion[properties.HasProperty, Property] with
  def apply(a: properties.HasProperty) = a.property
