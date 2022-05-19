create table collections
(
    id identity not null,
    name varchar not null,
    description varchar null,
    virtual boolean not null default false,
    deleted boolean not null default false,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint collections_pk
        primary key (id)
);
create index collections_idx1 ON collections(name);
create index collections_idx2 ON collections(virtual);
create index collections_idx3 ON collections(deleted);

create table collections__source_collections
(
    collection_id bigint not null,
    source_collection_id bigint not null,
    index int not null default 0,
    source_type enum('ITEMS', 'ITEM_TYPES') not null default 'ITEMS',
    constraint collections__source_collections__pk
        primary key (collection_id, source_collection_id),
    constraint collections__source_collections__collection_id_fk
        foreign key (collection_id) references collections(id),
    constraint collections__source_collections__source_collection_id_fk
        foreign key (source_collection_id) references collections(id)
);

create table items
(
    id identity not null,
    name varchar not null,
    description varchar null,
    virtual boolean not null default false,
    deleted boolean not null default false,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint items_pk
        primary key (id)
);
create index items_idx1 ON items(name);
create index items_idx2 ON items(virtual);
create index items_idx3 ON items(deleted);

create table items__collections
(
    id identity not null,
    item_id bigint not null,
    collection_id bigint not null,
    index int not null default 0,
    assn_type enum('COLLECTION_OF', 'INSTANCE_OF') not null default 'COLLECTION_OF',
    constraint items__collections__item_id_fk
        foreign key (item_id) references items(id),
    constraint items__collections__collection_id_fk
        foreign key (collection_id) references collections(id)
);
create index items__collections__idx1
    on items__collections(item_id, collection_id);

create table items__item_types
(
    item_id bigint not null,
    item_type_id bigint not null,
    index int not null default 0,
    constraint items__item_types__pk
        primary key (item_id, item_type_id),
    constraint items__item_types__item_id_fk
        foreign key (item_id) references items(id),
    constraint items__item_types__item_type_id_fk
        foreign key (item_type_id) references items(id)
);

create table properties
(
    id identity not null,
    name varchar not null,
    property_type enum('string', 'bigint', 'double', 'bool', 'timestamp', 'clob', 'blob', 'uuid', 'json') not null,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP()
);

create table properties__items__collections
(
    property_id bigint not null,
    items__collections__id bigint not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint properties__items__collections__property_id_fk
        foreign key (property_id) references properties(id),
    constraint properties__items__collections__items__collections__id_fk
        foreign key (items__collections__id) references items__collections(id)
);
create unique index properties__items__collections__uq1
    on properties__items__collections(property_id, items__collections__id);

create table properties__items
(
    property_id bigint not null,
    item_id bigint not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint properties__items__property_id_fk
        foreign key (property_id) references properties(id),
    constraint properties__items__item_id_fk
        foreign key (item_id) references items(id)
);
create unique index properties__items__uq1
    on properties__items(property_id);

create table properties__collections
(
    property_id bigint not null,
    collection_id bigint not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint properties__collections__property_id_fk
        foreign key (property_id) references properties(id),
    constraint properties__collections__collection_id_fk
        foreign key (collection_id) references collections(id)
);
create unique index properties__collections__uq1
    on properties__collections(property_id);

create table property_value_strings
(
    property_id bigint not null,
    property_value varchar not null,
    index int not null default 0,
    version bigint not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_string_property_id_fk
        foreign key (property_id) references properties(id)
);
create index property_value_strings_idx1
    on property_value_strings(property_id, version);
create index property_value_strings_idx2
    on property_value_strings(property_id, index);
create index property_value_strings_idx3
    on property_value_strings(property_id, created);

create table property_value_bigints
(
    property_id bigint not null,
    property_value bigint not null,
    index int not null default 0,
    version bigint not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_bigint_property_id_fk
        foreign key (property_id) references properties(id)
);
create index property_value_bigints_idx1
    on property_value_bigints(property_id, version);
create index property_value_bigints_idx2
    on property_value_bigints(property_id, index);
create index property_value_bigints_idx3
    on property_value_bigints(property_id, created);

create table property_value_doubles
(
    property_id bigint not null,
    property_value double precision not null,
    index int not null default 0,
    version bigint not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_double_property_id_fk
        foreign key (property_id) references properties(id)
);
create index property_value_doubles_idx1
    on property_value_doubles(property_id, version);
create index property_value_doubles_idx2
    on property_value_doubles(property_id, index);
create index property_value_doubles_idx3
    on property_value_doubles(property_id, created);

create table property_value_bools
(
    property_id bigint not null,
    property_value boolean not null,
    index int not null default 0,
    version bigint not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_bool_property_id_fk
        foreign key (property_id) references properties(id)
);
create index property_value_bools_idx1
    on property_value_bools(property_id, version);
create index property_value_bools_idx2
    on property_value_bools(property_id, index);
create index property_value_bools_idx3
    on property_value_bools(property_id, created);

create table property_value_timestamps
(
    property_id bigint not null,
    property_value timestamp with time zone not null,
    index int not null default 0,
    version bigint not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_timestamp_property_id_fk
        foreign key (property_id) references properties(id)
);
create index property_value_timestamps_idx1
    on property_value_timestamps(property_id, version);
create index property_value_timestamps_idx2
    on property_value_timestamps(property_id, index);
create index property_value_timestamps_idx3
    on property_value_timestamps(property_id, created);

create table property_value_clobs
(
    property_id bigint not null,
    property_value clob not null,
    index int not null default 0,
    version bigint not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_clob_property_id_fk
        foreign key (property_id) references properties(id)
);
create index property_value_clobs_idx1
    on property_value_clobs(property_id, version);
create index property_value_clobs_idx2
    on property_value_clobs(property_id, index);
create index property_value_clobs_idx3
    on property_value_clobs(property_id, created);

create table property_value_blobs
(
    property_id bigint not null,
    property_value blob not null,
    index int not null default 0,
    version bigint not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_blob_property_id_fk
        foreign key (property_id) references properties(id)
);
create index property_value_blobs_idx1
    on property_value_blobs(property_id, version);
create index property_value_blobs_idx2
    on property_value_blobs(property_id, index);
create index property_value_blobs_idx3
    on property_value_blobs(property_id, created);

create table property_value_uuids
(
    property_id bigint not null,
    property_value uuid not null,
    index int not null default 0,
    version bigint not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_uuid_property_id_fk
        foreign key (property_id) references properties(id)
);
create index property_value_uuids_idx1
    on property_value_uuids(property_id, version);
create index property_value_uuids_idx2
    on property_value_uuids(property_id, index);
create index property_value_uuids_idx3
    on property_value_uuids(property_id, created);

create table property_value_json
(
    property_id bigint not null,
    property_value json not null,
    index int not null default 0,
    version bigint not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_json_property_id_fk
        foreign key (property_id) references properties(id)
);
create index property_value_json_idx1
    on property_value_json(property_id, version);
create index property_value_json_idx2
    on property_value_json(property_id, index);
create index property_value_json_idx3
    on property_value_json(property_id, created);