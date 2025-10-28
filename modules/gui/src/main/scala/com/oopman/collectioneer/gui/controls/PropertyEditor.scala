package com.oopman.collectioneer.gui.controls

import com.oopman.collectioneer.db.traits.entity.projected.PropertyValue
import com.oopman.collectioneer.db.traits.entity.raw.Property
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.layout.{HBox, Pane, VBox}

/**
 * UI component providing the following
 *
 * Property name label
 * Input field for inputting data for the property
 * Ability to add/remove input fields
 * Ability to remove PropertyEditor? (Maybe this should be handled by CreateCollectionVBox?)
 * @param property
 *
 * TODO: This class and the stuff inside it should inherit from javafx rather than scalafx as the delegate system will work against things
 */
class PropertyEditor(val property: Property) extends javafx.scene.layout.Pane:
  val inputBoxes = new VBox

  def getPropetyValue: PropertyValue = ???

  private def addInputBox(insertionIndex: Int = -1, includeRemoveButton: Boolean = true): Unit =
    val container = new HBox

    val addButton = new Button("+"):
      onAction = event => addInputBox(inputBoxes.children.indexOf(container) + 1)

    val removeButtonPane = new Pane:
      minWidth = 35
      if includeRemoveButton then
        val removeButton = new Button("-"):
          onAction = event => inputBoxes.children.remove(container)
        children.add(removeButton)

    // TODO: Some propertyTypes should not use text
    // TODO: Allow switching between TextField and TextArea using a button?
    // TODO: Add field prompt based on propertyTypes
    // TODO: Text content needs to be validated based on property.propertyTypes
    val textArea = new TextField

    container.children = textArea :: removeButtonPane :: addButton :: Nil
    if insertionIndex == 0 then inputBoxes.children.prepend(container)
    else if insertionIndex > 0 then inputBoxes.children.insert(insertionIndex, container)
    else inputBoxes.children.append(container)

  addInputBox(includeRemoveButton = false)
  getChildren.add(inputBoxes)


