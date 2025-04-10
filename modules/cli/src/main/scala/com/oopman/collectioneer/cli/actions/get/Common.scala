package com.oopman.collectioneer.cli.actions.get

import com.oopman.collectioneer.db.traits

import java.util.HexFormat

object Common:
  private val hexFormat = HexFormat.of()

  def propertyValueToSeqOfStrings(propertyValue: traits.entity.projected.PropertyValue): Seq[String] =
    propertyValue.textValues ++
    propertyValue.byteValues.map(hexFormat.formatHex) ++
    propertyValue.smallintValues.map(_.toString) ++
    propertyValue.intValues.map(_.toString) ++
    propertyValue.bigintValues.map(_.toString()) ++
    propertyValue.numericValues.map(_.toString()) ++
    propertyValue.floatValues.map(_.toString) ++
    propertyValue.doubleValues.map(_.toString) ++
    propertyValue.booleanValues.map(_.toString) ++
    propertyValue.dateValues.map(_.toString) ++
    propertyValue.timeValues.map(_.toString) ++
    propertyValue.timestampValues.map(_.toString) ++
    propertyValue.uuidValues.map(_.toString) ++
    propertyValue.jsonValues.map(_.spaces2)
