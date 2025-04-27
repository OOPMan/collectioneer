package com.oopman.collectioneer.plugins.gatcg.actions

import com.oopman.collectioneer.db.entity.projected.{Collection, Property, PropertyValue}
import com.oopman.collectioneer.db.entity.raw.Relationship
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.{ParentCollection, SourceOfPropertiesAndPropertyValues}
import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.{CoreCollections, CoreProperties, given}
import com.oopman.collectioneer.db.traits.entity.raw.given
import com.oopman.collectioneer.plugins.gatcg.properties.{AllProperties, CardProperties, CommonProperties, EditionProperties, SetProperties}
import com.oopman.collectioneer.plugins.gatcg.{GATCGRootCollection, GATCGRootCollectionRelationship, Models, given}

import java.util.UUID


def importDataset(cards: List[Models.Card],
                  collectionDAO: traits.dao.projected.CollectionDAO,
                  rawCollectionDAO: traits.dao.raw.CollectionDAO,
                  propertyDAO: traits.dao.projected.PropertyDAO,
                  propertyValueDAO: traits.dao.projected.PropertyValueDAO,
                  relationshipDAO: traits.dao.raw.RelationshipDAO) =
  // Create/Update properties
  propertyDAO.createOrUpdateProperties(AllProperties)
  // Generate Cards, Editions, Circulations
  val setMap: collection.mutable.Map[Models.Set, Collection] = collection.mutable.Map()
  val setDataMap: collection.mutable.Map[Models.Set, Collection] = collection.mutable.Map()
  val circulationMap: collection.mutable.Map[Models.Circulation, Collection] = collection.mutable.Map()
  // Function to process Cards
  def processCard(card: Models.Card): (Seq[Collection], Seq[Relationship]) =
    import com.oopman.collectioneer.plugins.gatcg.extensions.Card.*
    val cardDataCollection: Collection = card.asCollection
    val rules = card.rule.getOrElse(Nil)
    val processRuleResults = rules.map(processRule(cardDataCollection))
    val references = card.references ++ card.referenced_by
    val processReferenceResults = references.map(processReference(cardDataCollection))
    val editionsBySet = card.editions.groupBy(_.set)
    val processSetEditionResults = editionsBySet.map(processSetEditions(cardDataCollection)).values
    val (listOfCollectionSeqs, listOfRelationshipSeqs) =
      (processRuleResults ++ processReferenceResults ++ processSetEditionResults).unzip
    val collections = listOfCollectionSeqs.flatten
    val relationships = listOfRelationshipSeqs.flatten
    (collections :+ cardDataCollection, relationships)
  // Function to process Editions-by-Set
  def processSetEditions(cardDataCollection: Collection)(set: Models.Set, editions: Seq[Models.Edition]): (Models.Set, (Seq[Collection], Seq[Relationship])) =
    import com.oopman.collectioneer.plugins.gatcg.extensions.Set.*
    val setDataCollection = setDataMap.getOrElseUpdate(set, set.asCollection)
    val setCollection = setMap.getOrElseUpdate(set, Collection(
      pk = UUID.nameUUIDFromBytes(s"GATCG-set-collection-${set.id}-${set.prefix}-${set.name}-${set.language}".getBytes),
      virtual = true,
      propertyValues = Map(
        CommonProperties.isGATCGSetCollection -> PropertyValue(booleanValues = true :: Nil),
      )
    ))
    val setCardCollection = Collection(
      pk = UUID.nameUUIDFromBytes(s"GATCG-set-card-collection-${cardDataCollection.pk}-${set.id}-${set.prefix}-${set.name}-${set.language}".getBytes),
      virtual = true,
      propertyValues = Map(
        CommonProperties.isGATCGCardCollection -> PropertyValue(booleanValues = true :: Nil),
        EditionProperties.collectorNumber -> PropertyValue(textValues = editions.map(_.collector_number))
      )
    )
    val (collections, relationships) = editions
      .map(processEdition(cardDataCollection, setDataCollection))
      .reduce((left, right) => (left._1 ++ right._1, left._2 ++ right._2))
    val additionalRelationships = List(
      Relationship(
        pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${setCollection.pk}-${setDataCollection.pk}-$SourceOfPropertiesAndPropertyValues".getBytes),
        collectionPK = setCollection.pk,
        relatedCollectionPK = setDataCollection.pk,
        relationshipType = SourceOfPropertiesAndPropertyValues
      ),
      Relationship(
        pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${setCardCollection.pk}-${setCollection.pk}-$ParentCollection".getBytes),
        collectionPK = setCardCollection.pk,
        relatedCollectionPK = setCollection.pk,
        relationshipType = ParentCollection
      ),
      Relationship(
        pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${setCardCollection.pk}-${cardDataCollection.pk}-$SourceOfPropertiesAndPropertyValues".getBytes),
        collectionPK = setCardCollection.pk,
        relatedCollectionPK = cardDataCollection.pk,
        relationshipType = SourceOfPropertiesAndPropertyValues
      ),
    )
    set -> (collections :+ setCardCollection, relationships ++ additionalRelationships)
  // Function to process Editions
  def processEdition(cardData: Collection, setDataCollection: Collection)(edition: Models.Edition): (Seq[Collection], Seq[Relationship]) =
    import com.oopman.collectioneer.plugins.gatcg.extensions.Edition.*
    val editionCollection: Collection = edition.asCollection
    val circulations = (edition.circulations ++ edition.circulationTemplates).distinct
    val processCirculationResults = circulations.map(processCirculation(editionCollection))
    val otherOrientations = edition.other_orientations.getOrElse(Nil)
    val processInnerCardResults = otherOrientations.map(processInnerCard(editionCollection, setDataCollection))
    val (listOfCollectionSeqs, listOfRelationshipSeqs) = (processCirculationResults ++ processInnerCardResults).unzip
    val collections = listOfCollectionSeqs.flatten
    val relationships = listOfRelationshipSeqs.flatten
    val additionalRelationships = List(
      Relationship(
        pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${editionCollection.pk}-${cardData.pk}-$ParentCollection".getBytes),
        collectionPK = editionCollection.pk,
        relatedCollectionPK = cardData.pk,
        relationshipType = ParentCollection
      ),
      Relationship(
        pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${setDataCollection.pk}-${editionCollection.pk}-$ParentCollection".getBytes),
        collectionPK = setDataCollection,
        relatedCollectionPK = editionCollection,
        relationshipType = ParentCollection
      )
    )
    (collections :+ editionCollection, relationships ++ additionalRelationships)
  // Function to process InnerEditions nested within InnerCards
  def processInnerEdition(cardCollection: Collection, setDataCollection: Collection)(innerEdition: Models.InnerEdition): (Seq[Collection], Seq[Relationship]) =
    import com.oopman.collectioneer.plugins.gatcg.extensions.InnerEdition.*
    val innerEditionCollection: Collection = innerEdition.asCollection
    val relationships = List(
      Relationship(
        pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${innerEditionCollection.pk}-${cardCollection.pk}-$ParentCollection".getBytes),
        collectionPK = innerEditionCollection.pk,
        relatedCollectionPK = cardCollection.pk,
        relationshipType = ParentCollection
      ),
      // TODO: Determine if having multiple parent relationships for a single Collection is a problem...
      Relationship(
        pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${setDataCollection.pk}-${innerEditionCollection.pk}-$ParentCollection".getBytes),
        collectionPK = setDataCollection,
        relatedCollectionPK = innerEditionCollection,
        relationshipType = ParentCollection
      )
    )
    (innerEditionCollection :: Nil, relationships)
  // Function to process InnerCards nested within Editions
  def processInnerCard(editionCollection: Collection, setDataCollection: Collection)(innerCard: Models.InnerCard): (Seq[Collection], Seq[Relationship]) =
    import com.oopman.collectioneer.plugins.gatcg.extensions.InnerCard.*
    val innerCardCollection: Collection = innerCard.asCollection
    val relationship = Relationship(
      pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${innerCardCollection.pk}-${editionCollection.pk}-$ParentCollection".getBytes),
      collectionPK = innerCardCollection.pk,
      relatedCollectionPK = editionCollection.pk,
      relationshipType = ParentCollection
    )
    val rules = innerCard.rule.getOrElse(Nil)
    val processRuleResults = rules.map(processRule(innerCardCollection))
    val references = (innerCard.references ++ innerCard.referenced_by).flatten
    val processReferenceResults = references.map(processReference(innerCardCollection))
    val processInnerEditionResults = processInnerEdition(innerCardCollection, setDataCollection)(innerCard.edition) :: Nil
    val (listOfCollectionSeqs, listOfRelationshipSeqs) =
      (processRuleResults ++ processReferenceResults ++ processInnerEditionResults).unzip
    val collections = listOfCollectionSeqs.flatten
    val relationships = listOfRelationshipSeqs.flatten
    (collections :+ innerCardCollection, relationships :+ relationship)
  // Function to process Circulations
  def processCirculation(editionCollection: Collection)(circulation: Models.Circulation): (Seq[Collection], Seq[Relationship]) =
    import com.oopman.collectioneer.plugins.gatcg.extensions.Circulation.*
    val circulationCollection = circulationMap.getOrElseUpdate(circulation, circulation.asCollection)
    val relationship = Relationship(
      pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${circulationCollection.pk}-${editionCollection.pk}-$ParentCollection".getBytes),
      collectionPK = circulationCollection.pk,
      relatedCollectionPK = editionCollection.pk,
      relationshipType = ParentCollection
    )
    (circulationCollection :: Nil, relationship :: Nil)
  // Function to process Rules
  def processRule(cardCollection: Collection)(rule: Models.Rule): (Seq[Collection], Seq[Relationship]) =
    import com.oopman.collectioneer.plugins.gatcg.extensions.Rule.*
    val ruleCollection: Collection = rule.asCollection
    val relationship = Relationship(
      pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${ruleCollection.pk}-${cardCollection.pk}-$ParentCollection".getBytes),
      collectionPK = ruleCollection.pk,
      relatedCollectionPK = cardCollection.pk,
      relationshipType = ParentCollection
    )
    (ruleCollection :: Nil, relationship :: Nil)
  // Function to process References
  def processReference(cardCollection: Collection)(reference: Models.Reference): (Seq[Collection], Seq[Relationship]) =
    import com.oopman.collectioneer.plugins.gatcg.extensions.Reference.*
    val referenceCollection: Collection = reference.asCollection
    val relationship = Relationship(
      pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${referenceCollection.pk}-${cardCollection.pk}-$ParentCollection".getBytes)  ,
      collectionPK = referenceCollection.pk,
      relatedCollectionPK = cardCollection.pk,
      relationshipType = ParentCollection
    )
    (referenceCollection :: Nil, relationship :: Nil)
  // Process and import data
  val (listOfCollectionSeqs, listOfRelationshipSeqs) = cards.map(processCard).unzip
  val collections = listOfCollectionSeqs.flatten
  val relationships = listOfRelationshipSeqs.flatten
  val setRelationships = setMap.values.map(setCollection => Relationship(
    pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${setCollection.pk}-${GATCGRootCollection.pk}-$ParentCollection".getBytes),
    collectionPK = setCollection,
    relatedCollectionPK = GATCGRootCollection,
    relationshipType = ParentCollection
  ))
  val allCollections = GATCGRootCollection :: Nil ++ setMap.values ++ setDataMap.values ++ circulationMap.values ++ collections
  def allRelationships = GATCGRootCollectionRelationship :: Nil ++ setRelationships ++ relationships
  val distinctRelationships = allRelationships.distinctBy(relationship =>
    (relationship.collectionPK, relationship.relatedCollectionPK, relationship.relationshipType)
  )
  collectionDAO.createOrUpdateCollections(allCollections)
  relationshipDAO.createOrUpdateRelationships(distinctRelationships)
  "Replace with something real"
