# Collectioneer Data Model

This document details the data model of the Collectioneer application

## Concept

The concept of the Collectioneer data model is to provide a database structure that is flexible
enough to represent relatively generic collections of items. 

The original intended goal was for this software to be used to manage a collection of boardgames 
while also being capable of being used as a deck builder application for card games.

The Collectioneer model consists of:

* Items, which may be virtual or non-virtual, may have relationships to one or more other Items 
  and/or Collections and may have Properties associated with them
* Collections, which may be virtual or non-virtual, may have relationships to one or more other
  Collections and/or Items and may have Properties associated with them

Simply put:
* Collections consist of Items, for example:
  * A Constructed Deck (Collection) of MTG cards (Items)
  * A Boardgame (Collection) containing various Game Components (Items)
  * A Music Album (Collection) consisting of various tracks (Items)
* Collections may also be subsets of other Collections:
  * The MTG Constructed Deck (Collection) is constructed from Cards (Items) that are part of a 
    larger overall Collection owned by the player
  * Said larger overall Collection of MTG Cards (Items) contains instances of Cards (Items) that
    are themselves parts of various Sets (Collections) 
  * Additionally, the Cards (Items) themselves can be treated as a Collection of Attributes (Items)


## Tables

* collections
* collection_properties
* collections_source_collections_assn
* items
* item_properties
* items_collections_assn
* items_item_types_assn

### collections

### collection_properties

### collections_source_collections_assn

### items

### item_properties

### items_collections_assn

### items_item_types_assn

