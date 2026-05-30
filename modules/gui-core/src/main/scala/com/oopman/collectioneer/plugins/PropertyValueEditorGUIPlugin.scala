package com.oopman.collectioneer.plugins

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.traits.entity.projected.{Property, PropertyValue}
import com.oopman.collectioneer.db.traits.entity.raw.rawHasPropertyToRawProperty
import javafx.beans.property.{ReadOnlyBooleanProperty, ReadOnlyObjectProperty}
import net.synedra.validatorfx.{ValidationResult, Validator}
import scalafx.beans.property.IntegerProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.Node
import scalafx.scene.control.Button
import scalafx.scene.layout.{HBox, Pane, VBox}

trait PropertyValueEditorGUIPlugin extends GUIPlugin:
  def canEditPropertyValuesForProperty(property: Property): Boolean
  def generatePropertyValueEditor(property: Property, propertyValue: Option[PropertyValue]): PropertyValueEditorGUIPlugin.PropertyValueEditor

object PropertyValueEditorGUIPlugin:
  trait PropertyValueEditor(val property: Property, protected val propertyValue: Option[PropertyValue]) extends Pane:
    protected val validator = new Validator

    def hasErrors: ReadOnlyBooleanProperty = validator.containsErrorsProperty()
    def hasWarnings: ReadOnlyBooleanProperty = validator.containsWarningsProperty()
    def validationResults: ReadOnlyObjectProperty[ValidationResult] = validator.validationResultProperty()

    def getPropertyValue: Option[PropertyValue]
    
  trait MinValuesMaxValuesAwarePropertyValueEditor[T <: Node] extends PropertyValueEditor:
    protected val inputContainersVBox = new VBox
    protected val minValues: Int = property.propertyValues.get(CoreProperties.minValues).flatMap(_.intValues.headOption).getOrElse(1)
    protected val maxValues: Int = property.propertyValues.get(CoreProperties.maxValue).flatMap(_.intValues.headOption).getOrElse(Integer.MAX_VALUE)
    protected val inputNodes: ObservableBuffer[T] = ObservableBuffer.empty[T]
    protected val inputNodesLength: IntegerProperty = IntegerProperty(inputNodes.length)
    inputNodes.onChange { (_, changes) =>
      for change <- changes do change match {
        case ObservableBuffer.Add(_, _) => inputNodesLength.value = inputNodesLength.value + 1 // TODO: Maybe we do the create logic here too?
        case ObservableBuffer.Remove(_, _) => inputNodesLength.value = inputNodesLength.value - 1 // TODO: And the remove logic here?
        case _ =>
      }
    }

    protected def generateInputNode: T

    protected def addInputBox(insertionIndex: Int = -1): Unit =
      val inputContainerHBox = new HBox
      val inputNode = generateInputNode

      val addButtonPane = new Pane:
        minWidth = 35
        children.append {
          new Button("+"):
            disable <== inputNodesLength >= maxValues
            onAction = event =>
              if inputContainersVBox.children.size < maxValues
              then addInputBox(inputContainersVBox.children.indexOf(inputContainerHBox) + 1)
              // else DISPLAY WARNING ABOUT BEING UNABLE TO ADD VALUES?
        }

      val removeButtonPane = new Pane:
        minWidth = 35
        children.append {
          new Button("-"):
            disable <== inputNodesLength <= minValues
            onAction = event =>
              if inputContainersVBox.children.length > minValues then
                inputContainersVBox.children.remove(inputContainerHBox)
                inputNodes.remove(inputNodes.indexOf(inputNode))
        }

      inputContainerHBox.children = inputNode :: removeButtonPane :: addButtonPane :: Nil
      if insertionIndex == 0 then
        inputContainersVBox.children.prepend(inputContainerHBox)
        inputNodes.prepend(inputNode)
      else if insertionIndex > 0 then
        inputContainersVBox.children.insert(insertionIndex, inputContainerHBox)
        inputNodes.insert(insertionIndex, inputNode)
      else
        inputContainersVBox.children.append(inputContainerHBox)
        inputNodes.append(inputNode)

    if minValues >= 1 then for _ <- 1 to minValues do addInputBox()
    children.add(inputContainersVBox)