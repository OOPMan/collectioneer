package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.Injection
import com.oopman.collectioneer.db.traits.dao.projected.CollectionDAO
import com.oopman.collectioneer.db.traits.dao.raw.RelationshipDAO
import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.plugins.gatcg.properties.{CommonProperties, EditionProperties}
import com.oopman.collectioneer.plugins.{DetailViewGUIPlugin, GUIPlugin}
import distage.Id
import scalafx.scene.control.Tab
import scalafx.scene.image.{Image, ImageView}
import scalafx.stage.Stage

import java.util.UUID

class GATCGDetailViewGUIPlugin(gatcgSubConfig: GATCGSubConfig, stage: Stage @Id("com.oopman.collectioneer.plugins.GUIPlugin.stage"))
extends GUIPlugin(stage), DetailViewGUIPlugin:
  def canRenderCollection(collection: Collection): Boolean =
    collection
      .propertyValues
      .get(CommonProperties.isGATCGCardCollection)
      .exists(_.booleanValues.contains(true))

  def generateCollectionRenderer(collection: Collection): Collection => Tab =
    // Note: Input collection will be a SetCardCollection (maybe?)
    val (relationshipHierarchy, collectionMap) = Injection.produceRun() {
      (relationshipDAO: RelationshipDAO, collectionDAO: CollectionDAO) =>
        val relationshipHierarchy = relationshipDAO.getRelationshipHierarchyByCollectionPKs(collection.pk :: Nil)
        val collectionPKs = relationshipHierarchy.map(_.relatedCollectionPK).distinct
        val collectionMap = collectionDAO.getAllMatchingPKs(collectionPKs).map(collection => collection.pk -> collection).toMap
        relationshipHierarchy -> collectionMap
    }
    val inputStreamOption = collectionMap
      .values
      .find(_.propertyValues.contains(CommonProperties.isGATCGEdition))
      .flatMap(_.propertyValues.get(EditionProperties.image))
      .flatMap(_.textValues.headOption)
      .map(image => UUID.nameUUIDFromBytes(image.getBytes))
      .map(imageHash => s"${gatcgSubConfig.imagePath}/$imageHash.jpg")
      .map(imagePath => os.Path(imagePath).getInputStream)
    // TODO: We might need to load the parent Set collection
    // TODO: Scan down relationshipHierarchy until we find the the CardCollection
    // TODO: Location the correct Edition(s) based on the Set
    // TODO: Display image(s) from the Editions
    (collection: Collection) => {
      new Tab:
        text = "GATCG Card"
        for (inputStream <- inputStreamOption) do
          content = new ImageView:
            image = new Image(inputStream)

    }


  def getName: String = "GATCG DetailView GUI Plugin"

  def getShortName: String = "GATCGDetailViewGUI"

  def getVersion: String = "master"
