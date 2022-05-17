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
create index collections_name_idx ON collections(name);
create index collections_virtual_idx ON collections(virtual);
create index collections_deleted_idx ON collections(deleted);

create table collections_source_collections_assn
(
    collection_id bigint not null,
    source_collection_id bigint not null,
    index int not null default 0,
    source_type enum('ITEMS', 'ITEM_TYPES') not null default 'ITEMS',
    constraint collections_source_collections_assn_pk
        primary key (collection_id, source_collection_id),
    constraint collections_source_collections_assn_collection_id_fk
        foreign key (collection_id) references collections(id),
    constraint collections_source_collections_assn_source_collection_id_fk
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
create index items_name_idx ON items(name);
create index items_virtual_idx ON items(virtual);
create index items_deleted_idx ON items(deleted);

create table items_collections_assn
(
    item_id bigint not null,
    collection_id bigint not null,
    index int not null default 0,
    assn_type enum('COLLECTION_OF', 'INSTANCE_OF') not null default 'COLLECTION_OF',
    constraint items_collections_assn_item_id_fk
        foreign key (item_id) references items(id),
    constraint items_collections_assn_collection_id_fk
        foreign key (collection_id) references collections(id)
);
create index items_collections_assn_idx1
    on items_collections_assn(item_id, collection_id);

create table items_item_types_assn
(
    item_id bigint not null,
    item_type_id bigint not null,
    index int not null default 0,
    constraint item_item_types_assn_pk
        primary key (item_id, item_type_id),
    constraint item_item_types_assn_item_id_fk
        foreign key (item_id) references items(id),
    constraint item_item_types_assn_item_type_id_fk
        foreign key (item_type_id) references items(id)
);

create table properties
(
    property_id identity not null,
    name varchar not null,
    property_type enum('string', 'bigint', 'double', 'bool', 'timestamp', 'clob', 'blob', 'uuid', 'json') not null,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP()
);

create table properties_items_assn
(
    property_id bigint not null,
    item_id bigint not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint properties_items_assn_property_id_fk
        foreign key (property_id) references properties(property_id),
    constraint properties_items_assn_item_id_fk
        foreign key (item_id) references items(id)
);
create unique index properties_items_assn_property_id_unique_index
    on properties_items_assn(property_id);

create table properties_collections_assn
(
    property_id bigint not null,
    collection_id bigint not null,
    index int not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint properties_collections_assn_property_id_fk
        foreign key (property_id) references properties(property_id),
    constraint properties_collections_assn_collection_id_fk
        foreign key (collection_id) references collections(id)
);
create unique index properties_collections_assn_property_id_unique_index
    on properties_collections_assn(property_id);

create table property_value_strings
(
    property_id bigint not null,
    property_value varchar not null,
    index int not null default 0,
    version bigint not null default 0,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    constraint property_value_string_property_id_fk
        foreign key (property_id) references properties(property_id)
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
        foreign key (property_id) references properties(property_id)
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
        foreign key (property_id) references properties(property_id)
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
        foreign key (property_id) references properties(property_id)
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
        foreign key (property_id) references properties(property_id)
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
        foreign key (property_id) references properties(property_id)
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
        foreign key (property_id) references properties(property_id)
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
        foreign key (property_id) references properties(property_id)
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
        foreign key (property_id) references properties(property_id)
);
create index property_value_json_idx1
    on property_value_json(property_id, version);
create index property_value_json_idx2
    on property_value_json(property_id, index);
create index property_value_json_idx3
    on property_value_json(property_id, created);