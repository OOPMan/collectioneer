package com.oopman.collectioneer.plugins.gatcg.gui

import com.oopman.collectioneer.db.traits.entity.projected.Collection
import com.oopman.collectioneer.db.traits.dao.projected.CollectionDAO
import com.oopman.collectioneer.db.traits.dao.raw.RelationshipDAO
import com.oopman.collectioneer.plugins.gatcg.properties.{CommonProperties, EditionProperties, SetCardProperties}
import com.oopman.collectioneer.db.traits.entity.raw.{HasTopLevelCollectionPKAndLevel, Relationship, given}
import scalafx.scene.control.{Label, Tab, TabPane}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}

import java.util.UUID

class GATCGCollectionRenderer(gatcgSubConfig: GATCGSubConfig, collection: Collection, relationshipDAO: RelationshipDAO, collectionDAO: CollectionDAO):
  private val relationshipHierarchy = relationshipDAO.getRelationshipHierarchyByCollectionPKs(collection.pk :: Nil)
  private val collectionPKs = relationshipHierarchy.map(_.relatedCollectionPK).distinct
  // TODO: Projected load is failing to work correctly in this scenario, see cards like Lu Bu, Circulations are
  // TODO: coming through with duplicate data. This was partially a data issue in the GATCG plugin which has been
  // TODO: corrected but a less mild version of the problem persists, which indicates that the nested PropertyValue
  // TODO loading system has a big. Not critical for now but needs to be fixed when said code is reworked due to its performance issues
  private val collections = collectionDAO.getAllMatchingPKs(collectionPKs)
  private val cardDataCollectionOption = collections.find(_.properties.contains(CommonProperties.isGATCGCard))

  private def generateInnerEditionCollection(innerEditionCollection: Collection): Option[InnerEdition] =
    val childRelationships = relationshipHierarchy.filter(_.collectionPK == innerEditionCollection.pk)
    val childPKs = childRelationships.map(_.relatedCollectionPK).toSet
    val childCollections = collections.filter(collection => childPKs.contains(collection.pk))
    val setOption = childCollections.find(_.properties.contains(CommonProperties.isGATCGSet)).map(SetData.apply)
    for set <- setOption yield InnerEdition(
      collection = innerEditionCollection,
      set = set
    )

  private def generateInnerCardCollections(innerCardCollections: Seq[Collection]): Seq[InnerCard] =
    for
      innerCardCollection <- innerCardCollections
      childRelationships = relationshipHierarchy.filter(_.collectionPK == innerCardCollection.pk)
      childPKs = childRelationships.map(_.relatedCollectionPK).toSet
      childCollections = collections.filter(collection => childPKs.contains(collection.pk))
      rules = childCollections.filter(_.properties.contains(CommonProperties.isGATCGRule)).map(Rule.apply)
      references = childCollections.filter(_.properties.contains(CommonProperties.isGATCGReference)).map(Reference.apply)
      innerEditionCollection <- childCollections.find(_.properties.contains(CommonProperties.isGATCGInnerEdition))
      innerEdition <- generateInnerEditionCollection(innerEditionCollection)
    yield
      InnerCard(
        collection = innerCardCollection,
        innerEdition = innerEdition,
        references = references,
        rules = rules
      )

  private def generateEditionCollections(editionCollections: Seq[Collection]): Seq[Edition] =
    for
      editionCollection <- editionCollections
      childRelationships = relationshipHierarchy.filter(_.collectionPK == editionCollection.pk)
      childPKs = childRelationships.map(_.relatedCollectionPK).toSet
      childCollections = collections.filter(collection => childPKs.contains(collection.pk))
      // TODO: Singular, use find instead
      sets = childCollections.filter(_.properties.contains(CommonProperties.isGATCGSet)).map(SetData.apply)
      circulations = childCollections.filter(_.properties.contains(CommonProperties.isGATCGCirculation)).map(Circulation.apply)
      innerCardCollections = childCollections.filter(_.properties.contains(CommonProperties.isGATCGInnerCard))
    yield
      Edition(
        collection = editionCollection,
        set = sets.head,
        circulations = circulations,
        innerCards = generateInnerCardCollections(innerCardCollections)
      )

  private def generateCardDataCollection(): Option[CardData] =
    for
      cardDataCollection <- cardDataCollectionOption
      childRelationships = relationshipHierarchy.filter(_.collectionPK == cardDataCollection.pk)
      childPKs = childRelationships.map(_.relatedCollectionPK).toSet
      childCollections = collections.filter(collection => childPKs.contains(collection.pk))
      rules = childCollections.filter(_.properties.contains(CommonProperties.isGATCGRule)).map(Rule.apply)
      references = childCollections.filter(_.properties.contains(CommonProperties.isGATCGReference)).map(Reference.apply)
      editionCollections = childCollections.filter(_.properties.contains(CommonProperties.isGATCGEdition))
    yield
      CardData(
        collection = cardDataCollection,
        references = references,
        rules = rules,
        editions = generateEditionCollections(editionCollections)
      )

  private val cardDataOption = generateCardDataCollection()
  private val initialImageInputStreamOption =
    for
      cardData <- cardDataOption
      propertyValue <- collection.propertyValues.get(SetCardProperties.primaryEditionUID)
      primaryEditionUID <- propertyValue.textValues.headOption
      primaryEdition <- cardData.editions.find(_.editionUID == primaryEditionUID)
    yield
      val imageUUID = UUID.nameUUIDFromBytes(primaryEdition.image.getBytes)
      val imagePath = s"${gatcgSubConfig.imagePath}/$imageUUID.jpg"
      os.Path(imagePath).getInputStream

  def render(collection: Collection): Option[Tab] =
    for cardData <- cardDataOption yield new Tab:
      text = "GATCG Card"
      content = new VBox:
        children = Seq(
          new HBox:
            children = Seq(
              // Image
              new ImageView:
                initialImageInputStreamOption.foreach(inputStream => image = new Image(inputStream))
              ,
              // Main Card Content Area
              new TabPane:
                tabs = Seq(
                  new Tab:
                    text = "Main"
                    content = new Label:
                      text = "Main Card Text Area"
                  ,
                  new Tab:
                    text = "Collector"
                    content = new Label:
                      text = "Collector Data"
                )
              ,
              // Edition Data
              new VBox:
                children = Seq(
                  new Label:
                    text = "Current Edition Details"
                  ,
                  new Label:
                    text = "Available Edition Links"
                )
            )
          ,
          new HBox:
            children = Seq(
              new Label:
                text = "Legality section"
              ,
              new Label:
                text = "Rules section"
            )
        )
