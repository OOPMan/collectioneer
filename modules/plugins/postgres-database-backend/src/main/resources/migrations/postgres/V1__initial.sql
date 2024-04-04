/**
  The core concept within Collectioneer is that of the Collection.

  A Collection is essentially a group of Properties with Values recorded for those Properties and it may in turn
  be related to other Collections in some fashion or other. The most common relationship of a Collection to other
  Collections is that it functions as a parent Collection. For example, a Collection representing a MTG deck would
  function as a parent to the Collections representing each individual Card in the MTG deck. Further details on the
  available relationships between Collections are documented below with the `collection__related_collection` table.

  Values recorded against the Properties associated with a Collection are grouped together using a Property Value Set,
  which functions as a tool to simplify the structure of the differently typed Property Value tables.

  A Collection be may be flagged as Virtual using the relevant column. This column is used to denote whether the
  Collection can be said to exist in the real world (E.g. Your MTG deck, a boardgame on your shelf) or be virtual in
  the sense that the items which comprise the collection may be conceptually real but don't actually exist in the real
  world (E.g. A MTG deck you are planning to build but haven't actually put together yet, a boardgame in the store

  Additionally, a Collection features columns recording when it was Created and Last Modified as well as one to
  denote whether it has been marked as Deleted.
 */
create table collection
(
    pk uuid not null default gen_random_uuid(),
    virtual boolean not null default false,
    deleted boolean not null default false,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint collection__pk
        primary key (pk)
);

/**
  The collection__related_collection table is used to define relationships between different Collections
  within the dataset.

  The relationship column defines the relationship between the Collection and the Related Collection. The possible
  values for this column function as follows:

  * `ParentCollection` is the simplest value and simply indicates that the Related Collection functions as a parent
    of the Collection. For example, Collection A might represent a users Collection of MTG cards. Each child
    Collection of this would represent a specific MTG card. Another Collection X might represent a specific set of
    MTG cards (E.g. Tempest) and yet another Collection Y  might represent a users boardgame Collection with each child
    Collection being a specific boardgame within that Collection Y.
    // TODO: Rewrite
  * `SourceOfPropertiesAndPropertyValues` indicates that the Related Collection functions as a source of Properties that the Collection
    may make use of in addition to any explicitly associated with it via the `property__collection` table. For
    example, we may have Collection A that is associated with the property common to all boardgames via the
    `property__collection` table (E.g. Minimum number of players, Maximum number of players, Genres, etc). If we have
    a Collection B, representing an actual specific boardgame it in turn can associate itself with Collection A and
    indicate Collection A to be a `source_of_property` which in turn allows the application to determine that for
    Collection B the property associated with Collection A may be used.
    // TODO: Maybe rewrite?
  * `SourceOfChildCollections` indicates that the Related Collection functions as a source of Collections that
    that may in turn be associated with the Collection as child Collections in turn. This sounds complicated
    but that's really just due to the wording. If we have Collection A, representing a users Collection of MTG cards,
    and create Collection B, representing a specific deck that the user is building, then we can define the relationship
    to indicate that the parent Collection, Collection A, is a `source_of_child_collection` for the child Collection,
    Collection B. This in turn allows Collection B to associate itself with child Collections of Collection A
 */
create type relationship_type as enum('ParentCollection', 'SourceOfPropertiesAndPropertyValues', 'SourceOfChildCollections');
create table relationship
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    related_collection_pk uuid not null,
    relationship_type relationship_type not null default 'ParentCollection',
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint collection__related_collection__pk
        primary key (pk),
    constraint collection__related_collection__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint collection__related_collection__related_collection_pk_fk
        foreign key (related_collection_pk) references collection(pk)
);

/**
  The relationship__collection table is used to define relationships between Relationships themselves and individual
  Collections for the purpose of allowing Properties and Property Values to be associated with the Relationship object.

  This allows for metadata about the Relationship between two Collections to be stored and interacted with
 */
create table relationship__collection
(
    relationship_pk uuid not null,
    collection_pk uuid not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint relationship__collection__pk
        primary key (relationship_pk, collection_pk),
    constraint relationship__collection__relationship_pk_fk
        foreign key (relationship_pk) references relationship(pk),
    constraint relationship__collection__collection_pk_fk
        foreign key (collection_pk) references collection(pk)
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
create type property_type as enum(
    'text', 'bytes', 'smallint', 'int', 'bigint', 'numeric', 'float', 'double', 'boolean', 'date', 'time', 'timestamp',
    'uuid', 'json'
);
create table property
(
    pk uuid not null default gen_random_uuid(),
    property_name varchar not null,
    property_types property_type[] not null,
    deleted boolean not null default false,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property__pk
        primary key (pk)
);

/**
  The property__collection table is used to define define relationships between Collections and Properties.

  The relationship column defines the relationship between the Property and the Collection. The possible values for this
  column function as follows:

  * `PropertyOfCollection` indicates that the Property is a property of the Collection. Examples of such a Property
    include the name of the Collection as well as a description of the Collection.
  * `CollectionOfPropertiesOfProperty` indicate that the Property has Properties of its own and that these Properties
    are grouped as Properties of the associated Collection. Examples include metadata related to a specific Property,
    such as a default value, value constraints and similar elements.
 */
create type property__collection__relationship_type as enum('PropertyOfCollection', 'CollectionOfPropertiesOfProperty');
create table property__collection
(
    property_pk uuid not null,
    collection_pk uuid not null,
    index int not null default 0,
    property__collection_relationship_type property__collection__relationship_type not null default 'PropertyOfCollection',
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property__collection__pk
        primary key (property_pk, collection_pk),
    constraint property__collection__property_pk_fk
        foreign key (property_pk) references property(pk),
    constraint property__collection__collection_pk_fk
        foreign key (collection_pk) references collection(pk)
);

create table property_value_text
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value text not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_varchar__pk
        primary key (pk),
    constraint property_value_varchar__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_varchar__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_bytes
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value bytea not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_varbinary__pk
        primary key (pk),
    constraint property_value_varbinary__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_varbinary__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_smallint
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value smallint not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_smallint__pk
        primary key (pk),
    constraint property_value_smallint__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_smallint__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_int
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value int not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_int__pk
        primary key (pk),
    constraint property_value_int__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_int__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_bigint
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value bigint not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_bigint__pk
        primary key (pk),
    constraint property_value_bigint__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_bigint__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_numeric
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value numeric not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_numeric__pk
        primary key (pk),
    constraint property_value_numeric__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_numeric__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_float
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value real not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_float__pk
        primary key (pk),
    constraint property_value_float__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_float__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_double
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value double precision not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_double__pk
        primary key (pk),
    constraint property_value_double__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_double__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_boolean
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value boolean not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_boolean__pk
        primary key (pk),
    constraint property_value_boolean__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_boolean__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_date
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value date not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_date__pk
        primary key (pk),
    constraint property_value_date__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_date__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_time
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value time with time zone not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_time__pk
        primary key (pk),
    constraint property_value_time__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_time__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_timestamp
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value timestamp with time zone not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_timestamp__pk
        primary key (pk),
    constraint property_value_timestamp__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_timestamp__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_uuid
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value uuid not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_uuid__pk
        primary key (pk),
    constraint property_value_uuid__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_uuid__property_pk_fk
        foreign key (property_pk) references property(pk)
);

create table property_value_json
(
    pk uuid not null default gen_random_uuid(),
    collection_pk uuid not null,
    property_pk uuid not null,
    property_value jsonb not null,
    index int not null default 0,
    created timestamp with time zone not null default now(),
    modified timestamp with time zone not null default now(),
    constraint property_value_json__pk
        primary key (pk),
    constraint property_value_json__collection_pk_fk
        foreign key (collection_pk) references collection(pk),
    constraint property_value_json__property_pk_fk
        foreign key (property_pk) references property(pk)
);