package com.oopman.collectioneer.gui

import com.oopman.collectioneer.db.traits.entity.projected.{Collection, Property as ProjectedProperty}
import com.oopman.collectioneer.db.traits.entity.raw.{Property, RelationshipType, given}
import com.oopman.collectioneer.db.{SortDirection, entity, traits}
import com.oopman.collectioneer.gui.controls.PropertyEditor
import com.oopman.collectioneer.plugins.PropertyValueEditorGUIPlugin
import com.oopman.collectioneer.{CoreCollections, CoreProperties, Injection}
import scalafx.Includes.jfxActionEvent2sfx
import scalafx.collections.{ObservableBuffer, ObservableMap}
import scalafx.concurrent.Task
import scalafx.event.ActionEvent
import scalafx.scene.control.*
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.util.StringConverter

import java.util.UUID
import scala.language.{implicitConversions, postfixOps}

abstract class CreateCollectionVBox(parentCollectionPK: UUID) extends VBox:
  def onDone(collection: Option[Collection] = None): Unit

  private lazy val plugins: Seq[PropertyValueEditorGUIPlugin] = Injection.produce[Set[PropertyValueEditorGUIPlugin]]().toSeq.sortBy(_.getRank)
  private val propertyValueEditorHasErrors = ObservableMap.empty[PropertyValueEditorGUIPlugin.PropertyValueEditor, Boolean]
  private val propertyValueEditors = scala.collection.mutable.Set.empty[PropertyValueEditorGUIPlugin.PropertyValueEditor]
  private val propertyEditorsGridPane = new GridPane
  private val propertyEditorsScrollPane = new ScrollPane:
    content = propertyEditorsGridPane

  private def removeRowFromPropertyEditorsGridPane(rowIndex: Int): Unit =
    propertyEditorsGridPane.children.removeIf { node =>
      val nodeRowIndex = Option(javafx.scene.layout.GridPane.getRowIndex(node)).map(_.toInt).getOrElse(0)
      nodeRowIndex == rowIndex
    }
    for
      node <- propertyEditorsGridPane.children
      currentRowIndex = Option(javafx.scene.layout.GridPane.getRowIndex(node)).map(_.toInt).getOrElse(0)
      if currentRowIndex > rowIndex
    do
      javafx.scene.layout.GridPane.setRowIndex(node, currentRowIndex - 1)
      try propertyEditorsGridPane.rowConstraints.remove(rowIndex - 1)
      catch
        case _ => // TODO: Warn?

  private def addProperty(property: ProjectedProperty, removable: Boolean = true): Unit =
    val usablePlugins = plugins.filter(_.canEditPropertyValuesForProperty(property))
    val rowNumber = propertyEditorsGridPane.getRowCount
    val label = new Label(property.propertyName + ":")
//    val propertyEditor = new PropertyEditor(property)
    val propertyValueEditor = usablePlugins.head.generatePropertyValueEditor(property, None) // TODO: Do this properly
    val subscription = propertyValueEditor.hasErrors.subscribe(hasErrors => propertyValueEditorHasErrors(propertyValueEditor) = hasErrors)
    propertyValueEditors.add(propertyValueEditor)

    propertyEditorsGridPane.add(label, 1, rowNumber)
    propertyEditorsGridPane.add(propertyValueEditor, 2, rowNumber)

    if removable then
      val removeButton: Button = new Button("-"):
        onAction = { event =>
          val rowIndex = Option(GridPane.getRowIndex(this)).map(_.toInt).getOrElse(0)
          removeRowFromPropertyEditorsGridPane(rowIndex)
          subscription.unsubscribe()
          propertyValueEditors.remove(propertyValueEditor)
        }
      propertyEditorsGridPane.add(removeButton, 0, rowNumber)

  // Property Adder controls
  private val propertyLoadProgressIndicator = new ProgressIndicator:
    visible = true
  // TODO: Make an icon button
  private val addPropertyButton: Button = new Button("+"):
    disable = true
    onAction = { e =>
      val selectedProperty = propertyChoiceBox.selectionModel().getSelectedItem
      addProperty(selectedProperty)
      // TODO: Select Property should not be available in the picker
    }

  private val propertyChoiceBox = new ChoiceBox[ProjectedProperty]:
    disable = true
    converter = StringConverter(
      fromStringFunction = propertyName => items().stream().filter(p => p.propertyName == propertyName).findFirst().get(),
      toStringFunction = property => Option(property).map(property => property.propertyName).getOrElse("")
    )
    onAction = event => addPropertyButton.disable = false

  private val propertyGroupChoiceBox = new ChoiceBox[(String, Seq[ProjectedProperty])]:
    converter = StringConverter(
      fromStringFunction = propertyGroupName => items().stream().filter((pn, _) => propertyGroupName == pn).findFirst().get(),
      toStringFunction = t => Option(t).map(_._1).getOrElse("")
    )
    onAction = { event =>
      val (_, properties) = selectionModel().getSelectedItem
      propertyChoiceBox.items = ObservableBuffer.from(properties)
      propertyChoiceBox.disable = false
    }

  private val propertyAdderHBox = new HBox(propertyLoadProgressIndicator)
  // Save/Cancel controls
  private val cancelCreateCollectionButton: Button = new Button("Cancel"):
    onAction = event =>
      saveButton.disable = true
      cancelCreateCollectionButton.disable = true
      onDone()

  private val saveButton: Button = new Button("Save"):

    disable = true
    onAction = event =>
      saveButton.disable = true
      saveProgressIndicator.visible = true
      val propertyValues =
        for
          propertyValueEditor <- propertyValueEditors
          propertyValue <- propertyValueEditor.getPropertyValue
        yield propertyValueEditor.property -> propertyValue
      val collection = entity.projected.Collection(
        virtual = virtualCheckbox.selected.value,
        propertyValues = propertyValues.toMap
      )
      val relationship = entity.raw.Relationship(
        collectionPK = parentCollectionPK,
        relatedCollectionPK = collection.pk,
        relationshipType = relationshipChoiceBox.selectionModel().getSelectedItem
      )
      val worker = Task {
        Injection.produceRun() {
          (collectionDAO: traits.dao.projected.CollectionDAO, relationshipDAO: traits.dao.raw.RelationshipDAO) =>
            val resultsA = collectionDAO.createOrUpdateCollections(collection :: Nil)
            val resultsB = relationshipDAO.createOrUpdateRelationships(relationship :: Nil)
            resultsA ++ resultsB
        }
      }
      worker.onSucceeded = { e =>
        // TODO: Might need to check worker.value to confirm writes
        onDone(Some(collection))
      }
      worker.onFailed = { e =>
        // TODO: Warn of failure
        onDone(None)
      }
      val thread = new Thread(worker)
      thread.setDaemon(true)
      thread.start()

  private val saveProgressIndicator = new ProgressIndicator:
    visible = false

  private val virtualCheckbox = new CheckBox
  private val virtualHBox = new HBox(new Label("Virtual"), virtualCheckbox)
  private val relationshipChoiceBox = new ChoiceBox[RelationshipType]:
    items = ObservableBuffer.from(RelationshipType.values)
    converter = StringConverter(
      fromStringFunction = relationShipTypeString => RelationshipType.valueOf(relationShipTypeString.replaceAll("\\s", "").trim),
      toStringFunction = relationShipType => Option(relationShipType).map(_.toString.replaceAll("([A-Z])", " $1").trim).getOrElse("")
    )
  private val relationshipHBox = new HBox(new Label("Relationship to parent Collection"), relationshipChoiceBox)

  private val saveCancelHBox = new HBox(cancelCreateCollectionButton, saveButton, saveProgressIndicator)

  // CreateCollectionVBox structure
  children = Seq(propertyEditorsScrollPane, propertyAdderHBox, virtualHBox, relationshipHBox, saveCancelHBox)
  // Init
  private val worker = Task {
    val collections: Seq[Collection] = Injection.produceRun() {
      (collectionDAO: traits.dao.raw.CollectionDAO, projectedCollectionDAO: traits.dao.projected.CollectionDAO) =>
        // TODO: Might be a cleaner way to load all these?
        val collections = collectionDAO.getAllMatchingConstraints(
          parentCollectionPKs = Some(Seq(CoreCollections.properties.collection.pk)),
          sortProperties = Seq(CoreProperties.name.property -> SortDirection.Asc)
        )
        projectedCollectionDAO.inflateRawCollections(collections)
    }
    collections
  }
  worker.onSucceeded = { e =>
    val collections = worker.getValue
    // TODO: Properties on Projected Collections are Projected Properties so this
    val propertiesByPropertyGroup =
      for
        collection <- collections
        properties = collection.properties.filterNot(property => property == CoreProperties.name || property == CoreProperties.description)
      yield
        val projectedProperties = properties.flatMap {
          case p: ProjectedProperty => Some(p)
          case _ => None
        }
        collection.propertyValues(CoreProperties.name).stringValues.head -> projectedProperties

//    val propertiesByPropertyGroup = collections
//      .map(collection => collection.propertyValues(CoreProperties.name).textValues.head -> collection.properties.filterNot(property => property == CoreProperties.name || property == CoreProperties.description))
    propertyGroupChoiceBox.items = ObservableBuffer.from(propertiesByPropertyGroup)
    propertyAdderHBox.children = Seq(propertyGroupChoiceBox, propertyChoiceBox, addPropertyButton)
  }
  worker.onFailed = { e =>
    // TODO: Show error message
    onDone(None)
  }
  private val thread = new Thread(worker)
  thread.setDaemon(true)
  thread.start()

  propertyValueEditorHasErrors.onChange {
    if (propertyValueEditorHasErrors.values().contains(true)) saveButton.disable = true
    else saveButton.disable = false
  }
  addProperty(CoreProperties.name.property, false)
  addProperty(CoreProperties.description.property, false)

