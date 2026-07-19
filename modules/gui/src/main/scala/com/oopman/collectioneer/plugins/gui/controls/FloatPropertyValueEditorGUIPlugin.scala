package com.oopman.collectioneer.plugins.gui.controls

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.traits.entity.projected.{Property, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.plugins.{GUIPlugin, PropertyValueEditorGUIPlugin}
import distage.Id
import scalafx.scene.control.TextField
import scalafx.stage.Stage

class FloatPropertyValueEditorGUIPlugin(stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), PropertyValueEditorGUIPlugin:
  def canEditPropertyValuesForProperty(property: Property): Boolean =
    property.propertyTypes.contains(PropertyType.Float)

  def generatePropertyValueEditor(property: Property, propertyValue: Option[PropertyValue]): PropertyValueEditorGUIPlugin.PropertyValueEditor =
    new FloatPropertyValueEditorGUIPlugin.FloatPropertyValueEditor(property, propertyValue)

  def getName: String = "Float PropertyValue Editor GUI Plugin"

  def getVersion: String = "0.0.1"

  override def getRank: Int = -1

private object FloatPropertyValueEditorGUIPlugin:
  private class FloatPropertyValueEditor(property: Property, propertyValue: Option[PropertyValue])
  extends PropertyValueEditorGUIPlugin.PropertyValueEditor(property, propertyValue), PropertyValueEditorGUIPlugin.MinValuesMaxValuesAwarePropertyValueEditor[TextField]:

    def getPropertyValue: Option[PropertyValue] =
      val values =
        for
          inputNode <- inputNodes
          value = inputNode.text.value
          floatValue <- value.toFloatOption
        yield floatValue
      Some(entity.projected.PropertyValue(
        floatValues = values.toSeq
      ))

    override def generateInputNode: TextField =
      val textField = new TextField
      val key = s"${property.propertyName}:${textField.hashCode}"
      validator.createCheck()
        .dependsOn(key, textField.text)
        .withMethod { context =>
          val value = context.get[String](key)
          try value.toFloat
          catch case _: NumberFormatException => context.error("Invalid 32-bit floating point value")
        }
        .decorates(textField)
        .immediate()
      textField