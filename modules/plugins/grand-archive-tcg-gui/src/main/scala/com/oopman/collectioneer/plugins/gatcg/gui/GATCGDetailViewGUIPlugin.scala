package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.Injection
import com.oopman.collectioneer.db.traits.dao.projected.CollectionDAO
import com.oopman.collectioneer.db.traits.dao.raw.RelationshipDAO
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.raw.{HasTopLevelCollectionPKAndLevel, Relationship, given}
import com.oopman.collectioneer.plugins.gatcg.properties.{CommonProperties, EditionProperties}
import com.oopman.collectioneer.plugins.{DetailViewGUIPlugin, GUIPlugin}
import distage.{Id, ModuleDef}
import scalafx.scene.control.Tab
import scalafx.stage.Stage


class GATCGDetailViewGUIPlugin(gatcgSubConfig: GATCGSubConfig, stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), DetailViewGUIPlugin:
  def canRenderCollection(collection: Collection): Boolean =
    collection
      .propertyValues
      .get(CommonProperties.isGATCGCardCollection)
      .exists(_.booleanValues.contains(true))

  def generateCollectionRenderer(collection: Collection): Collection => Option[Tab] =
    val moduleDef = new ModuleDef:
      make[Collection].from(collection)
      make[GATCGCollectionRenderer].from[GATCGCollectionRenderer]
    val renderer = Injection(moduleDef).produce[GATCGCollectionRenderer]
    renderer.render

  def getName: String = "GATCG DetailView GUI Plugin"

  def getShortName: String = "GATCGDetailViewGUI"

  def getVersion: String = "master"
