package com.oopman.collectioneer.plugins.gui.controls

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.traits.entity.projected.{Property, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.plugins.{GUIPlugin, PropertyValueEditorGUIPlugin}
import distage.Id
import net.synedra.validatorfx.Validator
import scalafx.scene.control.TextField
import scalafx.stage.Stage

class SmallintPropertyValueEditorGUIPlugin(stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), PropertyValueEditorGUIPlugin:
  def canEditPropertyValuesForProperty(property: Property): Boolean =
    property.propertyTypes.contains(PropertyType.smallint)

  def generatePropertyValueEditor(property: Property, propertyValue: Option[PropertyValue]): PropertyValueEditorGUIPlugin.PropertyValueEditor =
    new SmallintPropertyValueEditorGUIPlugin.SmallintPropertyValueEditor(property, propertyValue)

  def getName: String = "Smallint PropertyValue Editor GUI Plugin"

  def getVersion: String = "0.0.1"

  override def getRank: Int = -1

private object SmallintPropertyValueEditorGUIPlugin:
  private class SmallintPropertyValueEditor(property: Property, propertyValue: Option[PropertyValue])
  extends PropertyValueEditorGUIPlugin.PropertyValueEditor(property, propertyValue), PropertyValueEditorGUIPlugin.MinValuesMaxValuesAwarePropertyValueEditor[TextField]:

    def getPropertyValue: Option[PropertyValue] =
      val values = inputNodes.map(_.text.value).map(java.lang.Short.parseShort).toSeq
      Some(entity.projected.PropertyValue(
        smallintValues = values
      ))

    override def generateInputNode: TextField =
      val textField = new TextField
      val key = s"${property.propertyName}:${textField.hashCode}"
      validator.createCheck()
        .dependsOn(key, textField.text)
        .withMethod { context =>
          val value = context.get[String](key)
          try java.lang.Short.parseShort(value)
          catch case _: NumberFormatException => context.error("Invalid smallint")
        }
        .decorates(textField)
        .immediate()
      textField