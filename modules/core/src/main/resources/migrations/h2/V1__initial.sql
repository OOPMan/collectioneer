
/**
  Property Value Sets are used to group multiple Property Value rows together, allowing them to be associated with rows
  in the `properties__collections__related_collections` and `properties__collections` tables without the need to
  create numerous M2M tables for each combination of the above tables and the numerous type-specific Property Value
  tables below.
 */
create table property_value_sets
(
    pk uuid not null default RANDOM_UUID(),
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_sets__pk
        primary key (pk)
);

/**
  The core concept within Collectioneer is that of the Collection.

  A Collection is essentially a group of Properties with Values recorded for those Properties and it may in turn
  be related to other Collections in some fashion or other. The most common relationship of a Collection to other
  Collections is that it functions as a parent Collection. For example, a Collection representing a MTG deck would
  function as a parent to the Collections representing each individual Card in the MTG deck. Further details on the
  available relationships between Collections are documented below with the `collections__related_collections` table.

  Values recorded against the Properties associated with a Collection are grouped together using a Property Value Set,
  which functions as a tool to simplify the structure of the differently typed Property Value tables.

  A Collection be may be flagged as Virtual using the relevant column. This column is used to denote whether the
  Collection can be said to exist in the real world (E.g. Your MTG deck, a boardgame on your shelf) or be virtual in
  the sense that the items which comprise the collection may be conceptually real but don't actually exist in the real
  world (E.g. A MTG deck you are planning to build but haven't actually put together yet, a boardgame in the store

  Additionally, a Collection features columns recording when it was Created and Last Modified as well as one to
  denote whether it has been marked as Deleted.
 */
create table collections
(
    pk uuid not null default RANDOM_UUID(),
    virtual boolean not null default false,
    deleted boolean not null default false,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint collections__pk
        primary key (pk)
);

/**
  A Property is a named, typed information storage element that may have Values stored against it. Values stored
  against a Property are not associated solely with that Property as doing so would essentially mean that for a given
  Property all elements using said Property would share the same set stored of Values. Rather, a combination of
  Property and Property Value Set is used to distinguish between different Value "instances" of a given Property.

  The Property Type determines which specific Property Value table will be used to store data against the Property.

  Additionally, a Property features columns recording when it was Created and Last Modified as well as one to
  denote whether it has been marked as Deleted.
 */
create table properties
(
    pk uuid not null default RANDOM_UUID(),
    property_name varchar not null,
    property_type enum(
        'varchar', 'varbinary', 'tinyint', 'smallint', 'int', 'bigint', 'numeric', 'float', 'double', 'boolean',
        'date', 'time', 'timestamp', 'clob', 'blob', 'uuid', 'json') array not null,
    deleted boolean not null default false,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint properties__pk
        primary key (pk)
);

/**
  The collections__related_collections table is used to define relationships between different Collections
  within the dataset.

  The relationship column defines the relationship between the Collection and the Related Collection. The possible
  values for this column function as follows:

  * `parent_collection` is the simplest value and simply indicates that the Related Collection functions as a parent
    of the Collection. For example, Collection A might represent a users Collection of MTG cards. Each child
    Collection of this would represent a specific MTG card. Another Collection X might represent a specific set of
    MTG cards (E.g. Tempest) and yet another Collection Y  might represent a users boardgame Collection with each child
    Collection being a specific boardgame within that Collection Y.

  * `source_of_properties` indicates that the Related Collection functions as a source of Properties that the Collection
    may make use of in addition to any explicitly associated with it via the `properties__collections` table. For
    example, we may have Collection A that is associated with the properties common to all boardgames via the
    `properties__collections` table (E.g. Minimum number of players, Maximum number of players, Genres, etc). If we have
    a Collection B, representing an actual specific boardgame it in turn can associate itself with Collection A and
    indicate Collection A to be a `source_of_properties` which in turn allows the application to determine that for
    Collection B the properties associated with Collection A may be used.

  * `source_of_child_collections` indicates that the Related Collection functions as a source of Collections that
    that may in turn be associated with the Collection as child Collections in turn. This sounds complicated
    but that's really just due to the wording. If we have Collection A, representing a users Collection of MTG cards,
    and create Collection B, representing a specific deck that the user is building, then we can define the relationship
    to indicate that the parent Collection, Collection A, is a `source_of_child_collections` for the child Collection,
    Collection B. This in turn allows Collection B to associate itself with child Collections of Collection A
 */
create table collections__related_collections
(
    pk uuid not null default RANDOM_UUID(),
    collection_pk uuid not null,
    related_collection_pk uuid not null,
    relationship enum('parent_collection', 'source_of_properties', 'source_of_child_collections') not null default 'parent_collection',
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint collections__related_collections__pk
        primary key (pk),
    constraint collections__related_collections__collection_pk_fk
        foreign key (collection_pk) references collections(pk),
    constraint collections__related_collections__related_collection_pk_fk
        foreign key (related_collection_pk) references collections(pk)
);

/**
  The properties__collections table is used to define define relationships between Collections and Properties.

  The relationship column defines the relationship between the Property and the Collection. The possible values for this
  column function as follows:

  * `property_of_collection` indicates that the Property is a property of the Collection. Examples of such a Property
    include the name of the Collection as well as a description of the Collection.
  * `collection_of_properties_of_property` indicate that the Property has Properties of its own and that these Properties
    are grouped as Properties of the associated Collection. Examples include metadata related to a specific Property,
    such as a default value, value constraints and similar elements.
 */
create table properties__collections
(
    property_pk uuid not null,
    collection_pk uuid not null,
    property_value_set_pk uuid not null,
    index int not null default 0,
    relationship enum('property_of_collection', 'collection_of_properties_of_property') not null default 'property_of_collection',
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint properties__collections__pk
        primary key (property_pk, collection_pk),
    constraint properties__collections__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint properties__collections__property_pk_fk
        foreign key (property_pk) references properties(pk),
    constraint properties__collections__collection_pk_fk
        foreign key (collection_pk) references collections(pk)
);

/**
  This table associates Properties with relationships between Collections as stored by the `collections__related_collections`
  table. It may also associated Properties with a specific Collection in the relationship that are only valid in the
  context of the relationship.

  This allows for meta-data about the relationship to be stored without adding additional columns to the
  `collections__related_collections` table that may not make sense for all values of the `relationship` column of that
  table.

  The `property_value_set_pk` column allows the relationship between the two Collections to have Property Values
  associated with it. This allows for metadata pertaining to the relationship to be stored. For example, if the
  `parent_collection` relationship is used then a Property like `quantity` might be applied to the relationship
  between the Collections to indicate that a given quantity of the child collection is part of the parent Collection.
  For example, a MTG deck may contain 3 of some specific card. This would be modelled using a row in the
  `collections__related_collections` table with a `relationship` of `parent_collection`, `collection_pk` referencing
  the Collection associated with the specific card, `relation_collection_pk` referencing the Collection associated with
  the entire Collection of MTG cards and a `property_value_set_pk` referencing a Property Value Set storing data
  that includes the `quantity` property.
 */
create table properties__collections__related_collections
(
    property_pk uuid not null,
    collections__related_collections_pk uuid not null,
    property_value_set_pk uuid not null,
    relationship enum('property_of_relationship', 'property_of_collection', 'property_of_related_collection') not null default 'property_of_relationship',
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint properties__collections__related_collections__pk
        primary key (property_pk, collections__related_collections_pk),
    constraint collections__related_collections__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint properties__collections__related_collections_property_pk_fk
        foreign key (property_pk) references properties(pk),
    constraint properties__collections__related_collections_collections__related_collections_pk_fk
        foreign key (collections__related_collections_pk) references collections__related_collections(pk)
);

create table property_value_varchars
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value varchar not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_varchars__pk
        primary key (pk),
    constraint property_value_varchars__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_varchars__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_varbinarys
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value varbinary not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_varbinarys__pk
        primary key (pk),
    constraint property_value_varbinarys__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_varbinarys__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_tinyints
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value tinyint not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_tinyints__pk
        primary key (pk),
    constraint property_value_tinyints__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_tinyints__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_smallints
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value smallint not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_smallints__pk
        primary key (pk),
    constraint property_value_smallints__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_smallints__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_ints
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value int not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_ints__pk
        primary key (pk),
    constraint property_value_ints__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_ints__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_bigints
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value bigint not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_bigints__pk
        primary key (pk),
    constraint property_value_bigints__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_bigints__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_numerics
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value numeric not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_numerics__pk
        primary key (pk),
    constraint property_value_numerics__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_numerics__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_floats
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value float not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_floats__pk
        primary key (pk),
    constraint property_value_floats__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_floats__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_doubles
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value double not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_doubles__pk
        primary key (pk),
    constraint property_value_doubles__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_doubles__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_booleans
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value boolean not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_booleans__pk
        primary key (pk),
    constraint property_value_booleans__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_booleans__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_dates
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value date not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_dates__pk
        primary key (pk),
    constraint property_value_dates__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_dates__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_times
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value time not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_times__pk
        primary key (pk),
    constraint property_value_times__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_times__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_timestamps
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value timestamp not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_timestamps__pk
        primary key (pk),
    constraint property_value_timestamps__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_timestamps__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_clobs
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value clob not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_clobs__pk
        primary key (pk),
    constraint property_value_clobs__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_clobs__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_blobs
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value blob not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_blobs__pk
        primary key (pk),
    constraint property_value_blobs__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_blobs__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_uuids
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value uuid not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_uuids__pk
        primary key (pk),
    constraint property_value_uuids__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_uuids__property_pk_fk
        foreign key (property_pk) references properties(pk)
);

create table property_value_jsons
(
    pk uuid not null default RANDOM_UUID(),
    property_value_set_pk uuid not null,
    property_pk uuid not null,
    property_value json not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_jsons__pk
        primary key (pk),
    constraint property_value_jsons__property_value_set_pk_fk
        foreign key (property_value_set_pk) references property_value_sets(pk),
    constraint property_value_jsons__property_pk_fk
        foreign key (property_pk) references properties(pk)
);