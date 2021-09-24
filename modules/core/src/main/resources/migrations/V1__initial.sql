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

create table item_properties
(
    item_id bigint not null,
    name varchar not null,
    deleted boolean not null default false,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    type enum('string', 'bigint', 'double', 'bool', 'timestamp', 'clob', 'blob', 'uuid', 'array', 'json') not null,
    string_value varchar null,
    bigint_value bigint null,
    double_value double null,
    bool_value boolean null,
    clob_value clob null,
    blob_value blob null,
    uuid_value uuid null,
    array_value array null,
    array_type enum('string', 'bigint', 'double', 'bool', 'timestamp') null,
    json_value json null,
    timestamp_value timestamp with time zone null,
    constraint properties_pk
        primary key (item_id, name),
    constraint properties_item_id_fk
        foreign key (item_id) references items(id)
);

create table collection_properties
(
    collection_id bigint not null,
    name varchar not null,
    deleted boolean not null default false,
    created timestamp with time zone not null default CURRENT_TIMESTAMP(),
    modified timestamp with time zone not null default CURRENT_TIMESTAMP(),
    type enum('string', 'bigint', 'double', 'bool', 'timestamp', 'clob', 'blob', 'uuid', 'array', 'json') not null,
    string_value varchar null,
    bigint_value bigint null,
    double_value double null,
    bool_value boolean null,
    clob_value clob null,
    blob_value blob null,
    uuid_value uuid null,
    array_value array null,
    array_type enum('string', 'bigint', 'double', 'bool', 'timestamp') null,
    json_value json null,
    timestamp_value timestamp with time zone null,
    constraint properties_pk
        primary key (collection_id, name),
    constraint properties_item_id_fk
        foreign key (collection_id) references collections(id)
);