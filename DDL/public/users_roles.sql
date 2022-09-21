create table users_roles
(
    user_id bigint not null
        constraint "USERS_ROLES_constraint"
            references users,
    role    varchar(50)
);

alter table users_roles
    owner to root;

