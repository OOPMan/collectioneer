package com.oopman.collectioneer.plugins.gatcg.actions

import com.oopman.collectioneer.CoreCollections.root
import com.oopman.collectioneer.db.entity.projected.{Collection, Property, PropertyValue}
import com.oopman.collectioneer.db.entity.raw.Relationship
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType
import com.oopman.collectioneer.db.traits.entity.raw.RelationshipType.{ParentCollection, SourceOfPropertiesAndPropertyValues}
import com.oopman.collectioneer.db.{entity, traits}
import com.oopman.collectioneer.plugins.gatcg.properties.{AllProperties, CardProperties, CommonProperties, EditionProperties, SetProperties}
import com.oopman.collectioneer.plugins.gatcg.{Card, GATCGRootCollection}
import com.oopman.collectioneer.{CoreProperties, given}
import com.oopman.collectioneer.db.traits.entity.raw.given

import java.util.UUID


def importDataset(cards: List[Card],
                  collectionDAO: traits.dao.projected.CollectionDAO,
                  rawCollectionDAO: traits.dao.raw.CollectionDAO,
                  propertyDAO: traits.dao.projected.PropertyDAO,
                  propertyValueDAO: traits.dao.projected.PropertyValueDAO,
                  relationshipDAO: traits.dao.raw.RelationshipDAO) =
    // Create/Update properties
    propertyDAO.createOrUpdateProperties(AllProperties)
    // Generate Sets
    val sets = cards.flatMap(_.editions.map(_.set)).distinctBy(set => (set.prefix, set.language))
    val setUUIDS: Map[(String, String), UUID] = Map.from(sets.map(set => ((set.prefix, set.language), UUID.nameUUIDFromBytes(s"GATCG-set-${set.prefix}-${set.language}".getBytes))))
    val setMap = Map.from(sets.map(set => ((set.prefix, set.language), Collection(
      pk = setUUIDS.getOrElse((set.prefix, set.language), UUID.randomUUID),
      virtual = true,
      propertyValues = Map(
        CoreProperties.name -> PropertyValue(textValues = List(set.name)),
        SetProperties.setPrefix -> PropertyValue(textValues = List(set.prefix)),
        SetProperties.setLanguage -> PropertyValue(textValues = List(set.language)),
        CommonProperties.isGATCGSet -> PropertyValue(booleanValues = List(true))
      )
    ))))
    val setDataUUIDS: Map[(String, String), UUID] = Map.from(sets.map(set => ((set.prefix, set.language), UUID.nameUUIDFromBytes(s"GATCG-setdata-${set.prefix}-${set.language}".getBytes))))
    val setDataMap = Map.from(sets.map(set => ((set.prefix, set.language), Collection(
      pk = setDataUUIDS.getOrElse((set.prefix, set.language), UUID.randomUUID),
      virtual = true,
      propertyValues = Map(
        SetProperties.setName -> PropertyValue(textValues = List(set.name)),
        SetProperties.setPrefix -> PropertyValue(textValues = List(set.prefix)),
        SetProperties.setLanguage -> PropertyValue(textValues = List(set.language)),
        CommonProperties.isGATCGSetData -> PropertyValue(booleanValues = List(true))
      )
    ))))
    // Generate Collections containing CardProperties
    val cardUUIDs = Map.from(cards.map(card => (card.uuid, UUID.nameUUIDFromBytes(s"GATCG-card-${card.uuid}".getBytes))))
    val cardDataMap = Map.from(cards.map(card => (card.uuid, Collection(
      pk = cardUUIDs.getOrElse(card.uuid, UUID.randomUUID),
      virtual = true,
      propertyValues = Map(
        CoreProperties.name -> PropertyValue(textValues = List(card.name)),
        CardProperties.cardUID -> PropertyValue(textValues = List(card.uuid)),
        CardProperties.element -> PropertyValue(textValues = List(card.element)),
        CardProperties.types -> PropertyValue(textValues = card.types),
        CardProperties.classes -> PropertyValue(textValues = card.classes),
        CardProperties.subTypes -> PropertyValue(textValues = card.subtypes),
        CardProperties.effect -> PropertyValue(textValues = card.effect_raw.map(List(_)).getOrElse(Nil)),
        CardProperties.memoryCost -> PropertyValue(smallintValues = card.cost_memory.map(i => List(i.toShort)).getOrElse(Nil)),
        CardProperties.reserveCost -> PropertyValue(smallintValues = card.cost_reserve.map(i => List(i.toShort)).getOrElse(Nil)),
        CardProperties.level -> PropertyValue(smallintValues = card.level.map(i => List(i.toShort)).getOrElse(Nil)),
        CardProperties.speed -> PropertyValue(textValues = card.speed.map(b => List(if b then "Fast" else "Slow")).getOrElse(Nil)),
        CardProperties.legality -> PropertyValue(jsonValues = card.legality.map(j => List(j)).getOrElse(Nil)),
        CardProperties.power -> PropertyValue(smallintValues = card.power.map(p => List(p.toShort)).getOrElse(Nil)),
        CardProperties.life -> PropertyValue(smallintValues = card.life.map(l => List(l.toShort)).getOrElse(Nil)),
        CardProperties.durability -> PropertyValue(smallintValues = card.durability.map(d => List(d.toShort)).getOrElse(Nil)),
        CommonProperties.isGATCGCardData -> PropertyValue(booleanValues = List(true))
      )
    ))))
    // Generate Collections containing EditionProperties
    val editions = cards.flatMap(_.editions)
    val editionUUIDs = Map.from(editions.map(edition => (edition.uuid, UUID.nameUUIDFromBytes(s"GATCG-edition-${edition.uuid}".getBytes))))
    val editionDataMap = Map.from(editions.map(edition => (edition.uuid, Collection(
      pk = editionUUIDs.getOrElse(edition.uuid, UUID.randomUUID),
      virtual = true,
      propertyValues = Map[traits.entity.raw.Property, traits.entity.projected.PropertyValue](
        EditionProperties.editionUID -> PropertyValue(textValues = List(edition.uuid)),
        EditionProperties.cardUID -> PropertyValue(textValues = List(edition.card_id)),
        EditionProperties.collectorNumber -> PropertyValue(textValues = List(edition.collector_number)),
        EditionProperties.illustrator -> PropertyValue(textValues = List(edition.illustrator)),
        EditionProperties.slug -> PropertyValue(textValues = List(edition.slug)),
        EditionProperties.rarity -> PropertyValue(smallintValues = List(edition.rarity.toShort)), // TODO: Covert to String
        EditionProperties.effect -> PropertyValue(textValues = edition.effect.map(List(_)).getOrElse(Nil)),
        EditionProperties.flavourText -> PropertyValue(textValues = edition.flavor.map(List(_)).getOrElse(Nil)),
        CommonProperties.isGATCGEditionData -> PropertyValue(booleanValues = List(true))
      )
    ))))
    // Generate Card Collections that will be configured as children of Set Collections and source their Properties and PropertyValues from CardProperties and EditionProperties collections
    val cardEditionUUIDs = Map.from(editions.map(edition => ((edition.card_id, edition.uuid), UUID.nameUUIDFromBytes(s"GATCG-card-edition-${edition.card_id}-${edition.uuid}".getBytes))))
    val cardsMap = Map.from(
      editions.map(edition => (
        (
          setMap.get((edition.set.prefix, edition.set.language)),
          setDataMap.get((edition.set.prefix, edition.set.language)),
          editionDataMap.get(edition.uuid),
          cardDataMap.get(edition.card_id)
        ),
        Collection(
          pk = cardEditionUUIDs.getOrElse((edition.card_id, edition.uuid), UUID.randomUUID),
          virtual = true,
          propertyValues = Map(
            CommonProperties.isGATCGCard -> PropertyValue(booleanValues = List(true))
          )
        )
      )
      ))
    // Generate Relationships
    val relationships = cardsMap.flatMap {
      case ((Some(set), Some(setData), Some(editionData), Some(cardData)), card) => List(
        Relationship(
          pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${card.pk}-${set.pk}-${ParentCollection}".getBytes),
          collectionPK = card,
          relatedCollectionPK = set,
          relationshipType = ParentCollection),
        Relationship(
          pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${card.pk}-${setData.pk}-${SourceOfPropertiesAndPropertyValues}".getBytes),
          collectionPK = card,
          relatedCollectionPK = setData,
          relationshipType = SourceOfPropertiesAndPropertyValues),
        Relationship(
          pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${card.pk}-${editionData.pk}-${SourceOfPropertiesAndPropertyValues}".getBytes),
          collectionPK = card,
          relatedCollectionPK = editionData,
          relationshipType = SourceOfPropertiesAndPropertyValues),
        Relationship(
          pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${card.pk}-${cardData.pk}-${SourceOfPropertiesAndPropertyValues}".getBytes),
          collectionPK = card,
          relatedCollectionPK = cardData,
          relationshipType = SourceOfPropertiesAndPropertyValues)
      )
      case _ =>
        // logger.warn("This should never happen")
        Nil
    }
    val distinctRelationships = relationships
      .toList
      .appended(Relationship(
        pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${GATCGRootCollection.pk}-${root.pk}-${ParentCollection}".getBytes),
        collectionPK = GATCGRootCollection,
        relatedCollectionPK = root,
        relationshipType = ParentCollection
      ))
      .appendedAll(setMap.map((key, set) => Relationship(
        pk = UUID.nameUUIDFromBytes(s"GATCG-relationship-${set.pk}-${GATCGRootCollection.pk}-${ParentCollection}".getBytes),
        collectionPK = set,
        relatedCollectionPK = GATCGRootCollection,
        relationshipType = ParentCollection
      )))
      .distinctBy(relationship => (relationship.collectionPK, relationship.relatedCollectionPK, relationship.relationshipType))
    // Write data
    collectionDAO.createOrUpdateCollections(List(GATCGRootCollection) ++ setDataMap.values.toList ++ editionDataMap.values.toList ++
      cardDataMap.values.toList ++ cardsMap.values.toList ++ setMap.values.toList)
    relationshipDAO.createOrUpdateRelationships(distinctRelationships)
    // TODO: Replace with a real response
    "Something"


