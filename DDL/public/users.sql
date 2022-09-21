create table users
(
    id                      bigint                   default nextval('user_id_seq'::regclass) not null
        constraint "USERS_pkey"
            primary key,
    username                varchar(50)
        unique,
    password                varchar(256),
    first_name              varchar(50),
    last_name               varchar(50),
    register_date           timestamp with time zone default now(),
    account_non_expired     boolean                  default true,
    account_non_locked      boolean                  default true,
    credentials_non_expired boolean                  default true,
    enabled                 boolean                  default true
);

alter table users
    owner to root;

