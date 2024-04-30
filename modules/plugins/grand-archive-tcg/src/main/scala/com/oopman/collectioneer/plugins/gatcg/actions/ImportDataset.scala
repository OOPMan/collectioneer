package com.oopman.collectioneer.plugins.gatcg.actions.ImportDataset

import com.oopman.collectioneer.db.entity.projected.{Collection, Property, PropertyValue}
import com.oopman.collectioneer.db.entity.raw.Relationship
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.{ParentCollection, SourceOfPropertiesAndPropertyValues}
import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.plugins.gatcg.properties.{CardProperties, CommonProperties, EditionProperties, SetProperties}
import com.oopman.collectioneer.plugins.gatcg.{Card, GATCGRootCollection, given}
import com.oopman.collectioneer.{CoreProperties, given}

import java.util.UUID

def importDataset(cards: List[Card],
                  collectionDAO: traits.dao.projected.CollectionDAO,
                  relationshipDAO: traits.dao.raw.RelationshipDAO) =
    // TODO: Query for existing data to allow Update to succeed
    val existingSetsMap: Map[(String, String), UUID] = Map()
    val existingSetDataMap: Map[(String, String), UUID] = Map()
    val existingCardsMap: Map[(String, String), UUID] = Map()
    val existingCardDataMap: Map[String, UUID] = Map()
    val existingEditionDataMap: Map[String, UUID] = Map()
    val existingRelationships: Map[(UUID, UUID, RelationshipType), UUID] = Map()
    // Generate Sets
    val sets = cards.flatMap(_.editions.map(_.set)).distinctBy(set => (set.prefix, set.language))
    val setMap = Map.from(sets.map(set => ((set.prefix, set.language), Collection(
      pk = existingSetsMap.getOrElse((set.prefix, set.language), UUID.randomUUID),
      virtual = true,
      propertyValues = List(
        PropertyValue(property = CoreProperties.name, textValues = List(set.name)),
        PropertyValue(property = CommonProperties.isGATCGSet, booleanValues = List(true))
      )
    ))))
    val setDataMap = Map.from(sets.map(set => ((set.prefix, set.language), Collection(
      pk = existingSetDataMap.getOrElse((set.prefix, set.language), UUID.randomUUID),
      virtual = true,
      propertyValues = List(
        PropertyValue(property = SetProperties.setName, textValues = List(set.name)),
        PropertyValue(property = SetProperties.setPrefix, textValues = List(set.prefix)),
        PropertyValue(property = SetProperties.setLanguage, textValues = List(set.language)),
        PropertyValue(property = CommonProperties.isGATCGSetData, booleanValues = List(true))
      )
    ))))
    // Generate Collections containing CardProperties
    val cardDataMap = Map.from(cards.map(card => (card.uuid, Collection(
      pk = existingCardDataMap.getOrElse(card.uuid, UUID.randomUUID),
      virtual = true,
      propertyValues = List(
        PropertyValue(property = CoreProperties.name, textValues = List(card.name)),
        PropertyValue(property = CardProperties.cardUID, textValues = List(card.uuid)),
        PropertyValue(property = CardProperties.element, textValues = List(card.element)),
        PropertyValue(property = CardProperties.types, textValues = card.types),
        PropertyValue(property = CardProperties.classes, textValues = card.classes),
        PropertyValue(property = CardProperties.subTypes, textValues = card.subtypes),
        PropertyValue(property = CardProperties.effect, textValues = card.effect_raw.map(List(_)).getOrElse(Nil)),
        PropertyValue(property = CardProperties.memoryCost, smallintValues = card.cost_memory.map(i => List(i.toShort)).getOrElse(Nil)),
        PropertyValue(property = CardProperties.reserveCost, smallintValues = card.cost_reserve.map(i => List(i.toShort)).getOrElse(Nil)),
        PropertyValue(property = CardProperties.level, smallintValues = card.level.map(i => List(i.toShort)).getOrElse(Nil)),
        PropertyValue(property = CardProperties.speed, textValues = card.speed.map(b => List(if b then "Fast" else "Slow")).getOrElse(Nil)),
        PropertyValue(property = CardProperties.legality, jsonValues = card.legality.map(j => List(j)).getOrElse(Nil)),
        PropertyValue(property = CardProperties.power, smallintValues = card.power.map(p => List(p.toShort)).getOrElse(Nil)),
        PropertyValue(property = CardProperties.life, smallintValues = card.life.map(l => List(l.toShort)).getOrElse(Nil)),
        PropertyValue(property = CardProperties.durability, smallintValues = card.durability.map(d => List(d.toShort)).getOrElse(Nil)),
        PropertyValue(property = CommonProperties.isGATCGCardData, booleanValues = List(true))
      )
    ))))
    // Generate Collections containing EditionProperties
    val editions = cards.flatMap(_.editions)
    val editionDataMap = Map.from(editions.map(edition => (edition.uuid, Collection(
      pk = existingEditionDataMap.getOrElse(edition.uuid, UUID.randomUUID),
      virtual = true,
      propertyValues = List(
        PropertyValue(property = EditionProperties.editionUID, textValues = List(edition.uuid)),
        PropertyValue(property = EditionProperties.cardUID, textValues = List(edition.card_id)),
        PropertyValue(property = EditionProperties.collectorNumber, textValues = List(edition.collector_number)),
        PropertyValue(property = EditionProperties.illustrator, textValues = List(edition.illustrator)),
        PropertyValue(property = EditionProperties.slug, textValues = List(edition.slug)),
        PropertyValue(property = EditionProperties.rarity, smallintValues = List(edition.rarity.toShort)), // TODO: Covert to String
        PropertyValue(property = EditionProperties.effect, textValues = edition.effect.map(List(_)).getOrElse(Nil)),
        PropertyValue(property = EditionProperties.flavourText, textValues = edition.flavor.map(List(_)).getOrElse(Nil)),
        PropertyValue(property = CommonProperties.isGATCGEditionData, booleanValues = List(true))
      )
    ))))
    // Generate Card Collections that will be configured as children of Set Collections and source their Properties and PropertyValues from CardProperties and EditionProperties collections
    val cardsMap = Map.from(
      editions.map(edition => (
        (
          setMap.get((edition.set.prefix, edition.set.language)),
          setDataMap.get((edition.set.prefix, edition.set.language)),
          editionDataMap.get(edition.uuid),
          cardDataMap.get(edition.card_id)
        ),
        Collection(
          pk = existingCardsMap.getOrElse((edition.card_id, edition.uuid), UUID.randomUUID),
          virtual = true,
          propertyValues = List(PropertyValue(property = CommonProperties.isGATCGCard, booleanValues = List(true)))
        )
      )
      ))
    // Generate Relationships
    val relationships = cardsMap.flatMap {
      case ((Some(set), Some(setData), Some(editionData), Some(cardData)), card) => List(
        Relationship(collectionPK = card.pk, relatedCollectionPK = set.pk, relationshipType = ParentCollection),
        Relationship(collectionPK = card.pk, relatedCollectionPK = setData.pk, relationshipType = SourceOfPropertiesAndPropertyValues),
        Relationship(collectionPK = card.pk, relatedCollectionPK = editionData.pk, relationshipType = SourceOfPropertiesAndPropertyValues),
        Relationship(collectionPK = card.pk, relatedCollectionPK = cardData.pk, relationshipType = SourceOfPropertiesAndPropertyValues)
      )
      case _ =>
        // logger.warn("This should never happen")
        Nil
    }
    val distinctRelationships = relationships
      .toList
      .appendedAll(setMap.map((key, set) => Relationship(collectionPK = set.pk, relatedCollectionPK = GATCGRootCollection.pk, relationshipType = ParentCollection)))
      .distinctBy(relationship => (relationship.collectionPK, relationship.relatedCollectionPK, relationship.relationshipType))
      .filterNot(relationship => existingRelationships.contains((relationship.collectionPK, relationship.relatedCollectionPK, relationship.relationshipType)))
    // Write data
    collectionDAO.createOrUpdateCollections(
      List(GATCGRootCollection) ++ setDataMap.values.toList ++ editionDataMap.values.toList ++
        cardDataMap.values.toList ++ cardsMap.values.toList ++ setMap.values.toList
    )
    relationshipDAO.createOrUpdateRelationships(distinctRelationships)
    // TODO: Replace with a real response
    "Something"


