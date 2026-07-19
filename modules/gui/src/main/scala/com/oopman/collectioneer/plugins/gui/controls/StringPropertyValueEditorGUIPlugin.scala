package com.oopman.collectioneer.plugins.gui.controls

import com.oopman.collectioneer.db.entity
import com.oopman.collectioneer.db.traits.entity.projected.{Property, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.PropertyType
import com.oopman.collectioneer.plugins.{GUIPlugin, PropertyValueEditorGUIPlugin}
import distage.Id
import scalafx.scene.Node
import scalafx.scene.control.{Button, TextArea, TextField}
import scalafx.scene.layout.HBox
import scalafx.stage.Stage

class StringPropertyValueEditorGUIPlugin(stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), PropertyValueEditorGUIPlugin:
  def canEditPropertyValuesForProperty(property: Property): Boolean =
    property.propertyTypes.contains(PropertyType.String)

  def generatePropertyValueEditor(property: Property, propertyValue: Option[PropertyValue]): PropertyValueEditorGUIPlugin.PropertyValueEditor =
    new StringPropertyValueEditorGUIPlugin.StringPropertyValueEditor(property, propertyValue)

  def getName: String = "String PropertyValue Editor GUI Plugin"

  def getVersion: String = "0.0.1"

  override def getRank: Int = -1

private object StringPropertyValueEditorGUIPlugin:
  private class TextEditorHBox extends HBox:
    private val textArea = new TextArea
    private val textField = new TextField
    private var activeTextEditor: Node = textField
    private val switcher: Button = new Button("Switch"):
      onAction = event =>
        activeTextEditor = activeTextEditor match
          case _: TextField => textArea
          case _: TextArea => textField
        children = activeTextEditor :: switcher :: Nil
      

    textArea.text <==> textField.text

    def getText: String = textArea.text.value

    children = activeTextEditor :: switcher :: Nil

  private class StringPropertyValueEditor(property: Property, propertyValue: Option[PropertyValue])
  extends PropertyValueEditorGUIPlugin.PropertyValueEditor(property, propertyValue), PropertyValueEditorGUIPlugin.MinValuesMaxValuesAwarePropertyValueEditor[TextEditorHBox]:

    def getPropertyValue: Option[PropertyValue] =
      val textValues =
        for inputNode <- inputNodes.toSeq
        text = inputNode.getText
        if !text.isBlank
        yield text
      Some(entity.projected.PropertyValue(
        stringValues = textValues
      ))

    def generateInputNode: TextEditorHBox =
      new TextEditorHBox
