package com.oopman.collectioneer.plugins.gui.controls

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.traits.entity.projected.{Property, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.plugins.{GUIPlugin, PropertyValueEditorGUIPlugin}
import distage.Id
import scalafx.scene.control.CheckBox
import scalafx.stage.Stage

class BooleanPropertyValueEditorGUIPlugin(stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), PropertyValueEditorGUIPlugin:
  def canEditPropertyValuesForProperty(property: Property): Boolean =
    property.propertyTypes.contains(PropertyType.Float)

  def generatePropertyValueEditor(property: Property, propertyValue: Option[PropertyValue]): PropertyValueEditorGUIPlugin.PropertyValueEditor =
    new BooleanPropertyValueEditorGUIPlugin.BooleanPropertyValueEditor(property, propertyValue)

  def getName: String = "Boolean PropertyValue Editor GUI Plugin"

  def getVersion: String = "0.0.1"

  override def getRank: Int = -1

private object BooleanPropertyValueEditorGUIPlugin:
  private class BooleanPropertyValueEditor(property: Property, propertyValue: Option[PropertyValue])
  extends PropertyValueEditorGUIPlugin.PropertyValueEditor(property, propertyValue), PropertyValueEditorGUIPlugin.MinValuesMaxValuesAwarePropertyValueEditor[CheckBox]:

    def getPropertyValue: Option[PropertyValue] =
      val values =
        for
          inputNode <- inputNodes
          value = inputNode.selected.value
        yield value
      Some(entity.projected.PropertyValue(
        booleanValues = values.toSeq
      ))

    override def generateInputNode: CheckBox =
      val checkBox = new CheckBox()
      checkBox