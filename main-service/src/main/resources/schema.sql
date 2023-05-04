create sequence if not exists "participationRequests_id_seq"
    as integer;

alter sequence "participationRequests_id_seq" owner to postgres;

create table if not exists categories
(
    id   serial
        constraint categories_pk
            primary key,
    name varchar(50) not null
);

alter table categories
    owner to postgres;

create unique index if not exists categories_id_uindex
    on categories (id);

create unique index if not exists categories_name_uindex
    on categories (name);

create table if not exists users
(
    id    serial
        constraint users_pk
            primary key,
    name  varchar(50) not null,
    email varchar(50)
);

alter table users
    owner to postgres;

create unique index if not exists users_id_uindex
    on users (id);

create table if not exists locations
(
    id  serial
        constraint locations_pk
            primary key,
    lat double precision not null,
    lon double precision not null
);

alter table locations
    owner to postgres;

create unique index if not exists locations_id_uindex
    on locations (id);

create table if not exists events
(
    id                  serial
        constraint events_pk
            primary key,
    annotation          varchar(2000) not null,
    "confirmed_requests" integer,
    "created_on"         timestamp,
    description         varchar(2000),
    "event_date"         timestamp   not null,
    paid                boolean     not null,
    "participant_limit"  integer,
    "published_on"       timestamp,
    "request_moderation" boolean,
    state               varchar(20) not null,
    title               varchar(100),
    hits               integer,
    category_id         integer     not null
        constraint events_categories_id_fk
            references categories
            on update set null on delete set null,
    initiator_id        integer     not null
        constraint events_users_id_fk
            references users
            on update cascade on delete cascade,
    location_id         integer
        constraint events_locations_id_fk
            references locations
            on update set null on delete set null
);

alter table events
    owner to postgres;

create unique index if not exists events_id_uindex
    on events (id);

create table if not exists participation_requests
(
    id           integer default nextval('"participationRequests_id_seq"'::regclass) not null
        constraint participationrequests_pk
            primary key,
    event_id     integer                                                             not null
        constraint participation_requests_events_id_fk
            references events
            on update cascade on delete cascade,
    requester_id integer                                                             not null
        constraint participation_requests_users_id_fk
            references users
            on update cascade on delete cascade,
    status       varchar(15)                                                         not null,
    created      timestamp
);

alter table participation_requests
    owner to postgres;

alter sequence "participationRequests_id_seq" owned by participation_requests.id;

create unique index if not exists participationrequests_id_uindex
    on participation_requests (id);

create table if not exists compilations
(
    id     serial
        constraint compilations_pk
            primary key,
    pinned boolean,
    title  varchar(300)
);

alter table compilations
    owner to postgres;

create unique index if not exists compilations_id_uindex
    on compilations (id);

create table if not exists compilations_events
(
    event_id       integer not null
        constraint compilations_events_events_id_fk
            references events
            on update cascade on delete cascade,
    compilation_id integer not null
        constraint compilations_events_compilations_id_fk
            references compilations
            on update cascade on delete cascade
);

alter table compilations_events
    owner to postgres;

create table if not exists comments
(
    id       serial,
    user_id  int           not null
        constraint comments_users_id_fk
            references users
            on update cascade on delete cascade,
    event_id int           not null
        constraint comments_events_id_fk
            references events
            on update cascade on delete cascade,
    text     varchar(2000) not null
);

create unique index if not exists comments_id_uindex
    on comments (id);

alter table comments
    owner to postgres;


