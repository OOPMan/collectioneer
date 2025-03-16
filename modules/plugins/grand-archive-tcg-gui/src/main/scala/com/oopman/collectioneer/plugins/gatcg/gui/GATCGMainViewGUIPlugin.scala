package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.db.traits.dao.raw.CollectionDAO
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.plugins.gatcg.GATCGRootCollection
import com.oopman.collectioneer.plugins.gatcg.properties.CommonProperties
import com.oopman.collectioneer.plugins.{GUIPlugin, MainViewGUIPlugin}
import distage.Id
import scalafx.scene.control.TreeCell
import scalafx.stage.Stage

import java.util.UUID

class GATCGMainViewGUIPlugin(stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), MainViewGUIPlugin:
  private def canProcessCollection(collection: Collection): Boolean =
    if collection.pk == GATCGRootCollection.pk then true
    else collection.propertyValues.exists(pv => {
      CommonProperties.values.map(_.property.pk).contains(pv.property.pk)
    })

  def getCollectionsListTreeViewCellFactory(collection: Collection): Option[(TreeCell[Collection], Collection) => Unit] = ???
  /** TODO:
   *  1. Check if can process, if not return None
   *  2. Format so that Sets include the shortcode in brackets, cards contain the Collector number in brackets
   */

  def getRawChildCollections(collection: Collection, collectionDAO: CollectionDAO): Option[List[Collection]] = ???
  /** TODO:
   *  1. Check if can process, if not return None
   *  2. Query for child collections of input collection, sort by Collectors number, then name (Maybe differentiate between Sets and Cards?
   *  3. Return result
   */

  def getPropertyPKsForInflation(collection: Collection): Option[Seq[UUID]] = ???
  /** TODO:
   *  1. Check if can process, if not return None
   *  2. Properties for inflation: Name, Collector Number, Set Prefix
   */


  def getName: String = "GATCG UI Plugin"

  def getShortName: String = "GATCGGUI"

  def getVersion: String = "master"

