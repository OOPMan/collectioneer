package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.CoreProperties
import com.oopman.collectioneer.db.SortDirection
import com.oopman.collectioneer.db.traits.dao.raw.CollectionDAO
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.raw.Collection as RawCollection
import com.oopman.collectioneer.plugins.gatcg.GATCGRootCollection
import com.oopman.collectioneer.plugins.gatcg.properties.{CommonProperties, EditionProperties, SetProperties}
import com.oopman.collectioneer.plugins.{GUIPlugin, MainViewGUIPlugin}
import distage.Id
import scalafx.scene.control.TreeCell
import scalafx.stage.Stage

import java.util.UUID

class GATCGMainViewGUIPlugin(stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), MainViewGUIPlugin:
  private val whitelistedPropertyPKs = Seq(SetProperties.setPrefix.property.pk, EditionProperties.collectorNumber.property.pk)

  private def setCollectionCellFactory(cell: TreeCell[Collection], collection: Collection): Unit =
    val setName = collection.propertyValues
      .find(pv => pv.property.pk == CoreProperties.name.property.pk)
      .flatMap(pv => pv.textValues.headOption)
      .getOrElse(collection.pk.toString)
    val setPrefix = collection.propertyValues
      .find(pv => pv.property.pk == SetProperties.setPrefix.property.pk)
      .flatMap(pv => pv.textValues.headOption)
      .getOrElse("?")
    cell.text = s"$setName ($setPrefix)"

  private def cardCollectionCellFactory(cell: TreeCell[Collection], collection: Collection): Unit =
    val cardName = collection.propertyValues
      .find(pv => pv.property.pk == CoreProperties.name.property.pk)
      .flatMap(pv => pv.textValues.headOption)
      .getOrElse(collection.pk.toString)
    val collectorNumber = collection.propertyValues
      .find(pv => pv.property.pk == EditionProperties.collectorNumber.property.pk)
      .flatMap(pv => pv.textValues.headOption)
      .getOrElse("?")
    cell.text = s"$cardName ($collectorNumber)"

  def canGetCollectionsListTreeViewCellFactory(collection: Collection): Boolean =
    collection.propertyValues.exists(pv => whitelistedPropertyPKs.contains(pv.property.pk))

  def canGetRawChildCollections(collection: Collection): Boolean =
    collection.propertyValues.exists(pv => pv.property.pk == SetProperties.setPrefix.property.pk)

  def canGetPropertyPKsForInflation(collection: Collection): Boolean =
    if collection.pk == GATCGRootCollection.pk then true
    else collection.propertyValues.exists(pv => pv.property.pk == SetProperties.setPrefix.property.pk)

  def getCollectionsListTreeViewCellFactory(collection: Collection): (TreeCell[Collection], Collection) => Unit =
    if collection.propertyValues.exists(pv => pv.property.pk == SetProperties.setPrefix.property.pk)
    then setCollectionCellFactory
    else cardCollectionCellFactory

  def getRawChildCollections(collection: Collection, collectionDAO: CollectionDAO): List[RawCollection] =
    collectionDAO.getAllMatchingConstraints(
      parentCollectionPKs = Some(Seq(collection.pk)),
      sortProperties = Seq(EditionProperties.collectorNumber.property -> SortDirection.Asc)
    )

  def getPropertyPKsForInflation(collection: Collection): Seq[UUID] =
    if collection.pk == GATCGRootCollection.pk
    then Seq(CoreProperties.name.property.pk, SetProperties.setPrefix.property.pk)
    else Seq(CoreProperties.name.property.pk, EditionProperties.collectorNumber.property.pk)

  def getName: String = "GATCG UI Plugin"

  def getShortName: String = "GATCGGUI"

  def getVersion: String = "master"

