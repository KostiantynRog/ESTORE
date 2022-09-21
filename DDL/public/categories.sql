create table categories
(
    id          integer default nextval('categories_id_seq'::regclass) not null
        constraint "CATEGORIES_pkey"
            primary key,
    name        varchar(255)                                           not null,
    description varchar(1000)                                          not null
);

alter table categories
    owner to bgwjobjbceklba;

