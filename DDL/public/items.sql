create table items
(
    id                integer default nextval('items_id_seq'::regclass) not null
        constraint "ITEMS_pkey"
            primary key,
    name              varchar(255)                                      not null,
    category_id       integer                                           not null
        constraint "ITEMS_constraint"
            references categories,
    short_description varchar(1000)                                     not null,
    full_description  varchar(2000)                                     not null,
    image_src         varchar(500)                                      not null
);

alter table items
    owner to bgwjobjbceklba;

