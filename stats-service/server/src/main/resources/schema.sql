
create sequence if not exists hit_id_seq as integer;

create table if not exists hits
(
    id   integer default nextval('hit_id_seq') not null
        constraint hits_pk
            primary key,
    app       varchar(30)  not null,
    uri       varchar(100) not null,
    ip        varchar(12)  not null,
    timestamp timestamp    not null
);

create unique index if not exists hits_id_uindex
    on hits (id);

