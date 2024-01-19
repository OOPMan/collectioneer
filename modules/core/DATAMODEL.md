# Collectioneer Data Model

This document details the data model of the Collectioneer application

## Notes
* Collections contain Items
* Items have Properties
* Properties have a Value or Value(s)
* Values are typed (String, Boolean, Integer, Decimal, JSON, BLOB, CLOB, etc)
* Multiple Collections can share the same Item
* Multiple Items can share the same Property
* Multiple Properties can share the same value (type permitting)
* Multiple Instances of the same Item can exist in a Collection
* An Instance of an Item within a Collection may represent Multiple Items
* Each Instance of an Item will have the same Properties as each other Instance of the Item but MAY have different
  Values associated with said Properties (Example: You may have multiple copies of a specific card within your collection
  but same may be of higher quality than others)


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
* collections_source_collections_assn
* items
* items_collections_assn
* items_item_types_assn

### collections
The collection table is used to model the concept of a collection. A collection may be virtual or non-virtual.
A virtual collection can be considered to be one that exists in terms of information but does not actually physically
exist. For example, a virtual collection of MTG cards may be constructed from a larger physical or virtual collection 
of MTG cards. Actual examples include:
* A decklist that can be constructed from cards in your physical collection
* A Set of cards within the larger virtual collection of all MTG cards
#### Columns
* id
* name
* description
* virtual
* deleted
* created
* modified

### collection_properties

### collections_source_collections_assn

### items

### item_properties

### items_collections_assn

### items_item_types_assn

