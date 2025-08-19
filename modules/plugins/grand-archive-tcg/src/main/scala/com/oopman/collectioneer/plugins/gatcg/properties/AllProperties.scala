package com.oopman.collectioneer.plugins.gatcg.properties

import com.oopman.collectioneer.db.traits.entity.projected

val AllProperties: Seq[projected.Property] =
  CardProperties.properties ++
  CirculationProperties.properties ++
  CommonProperties.properties ++
  EditionProperties.properties ++
  ReferenceProperties.properties ++
  RuleProperties.properties ++
  SetProperties.properties ++
  SetCardProperties.properties
