drop table if exists users cascade;
drop table if exists categories cascade;
drop table if exists compilations cascade;
drop table if exists location cascade;
drop table if exists events cascade;
drop table if exists requests cascade;

create table if not exists users
(
    id bigint generated always as identity primary key,
    email varchar(100) unique,
    name varchar(100)
);

create table if not exists categories
(
    id bigint generated always as identity primary key,
    name varchar(100) unique
);

create table if not exists compilations
(
    id bigint generated always as identity primary key,
    title varchar(120),
    pinned boolean
);

create table if not exists location
(
    lat float,
    lon float,
    primary key (lat, lon)
);

create table if not exists events
(
    id bigint generated always as identity primary key,
    id_user bigint,
    id_category bigint,
    id_compilation bigint,
    lat float,
    lon float,
    annotation varchar(2000),
    title varchar(120),
    description varchar(7000),
    created_on timestamp,
    event_date timestamp,
    participant_limit int,
    paid boolean,
    request_moderation boolean,
    constraint fk_events_to_users foreign key (id_user) references users (id),
    constraint fk_events_to_category foreign key (id_category) references categories (id),
    constraint fk_events_to_compilation foreign key (id_compilation) references compilations (id),
    constraint fk_events_to_location foreign key (lat, lon) references location (lat, lon)
);

create table if not exists requests
(
    id_user bigint,
    id_event bigint,
    status varchar(20),
    constraint fk_requests_to_users foreign key (id_user) references users (id),
    constraint fk_requests_to_events foreign key (id_event) references events (id)
);