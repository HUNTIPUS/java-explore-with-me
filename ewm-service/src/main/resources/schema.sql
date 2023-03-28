drop table if exists users cascade;
drop table if exists categories cascade;
drop table if exists compilations cascade;
drop table if exists location cascade;
drop table if exists events cascade;
drop table if exists compilations_events cascade;
drop table if exists requests cascade;
drop table if exists comments cascade;

create table if not exists users
(
    id bigint generated always as identity primary key,
    email varchar(100),
    name varchar(100),
    constraint uq_email unique (email)
);

create table if not exists categories
(
    id bigint generated always as identity primary key,
    name varchar(100),
    constraint uq_name unique (name)
);

create table if not exists compilations
(
    id bigint generated always as identity primary key,
    title varchar(120),
    pinned boolean,
    constraint uq_title unique (title)
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
    state varchar(20),
    published_on timestamp,
    constraint fk_events_to_users foreign key (id_user) references users (id),
    constraint fk_events_to_category foreign key (id_category) references categories (id),
    constraint fk_events_to_location foreign key (lat, lon) references location (lat, lon)
);

create table if not exists compilations_events
(
    id bigint generated always as identity primary key,
    id_event bigint,
    id_compilation bigint,
    constraint fk_to_compilation foreign key (id_compilation) references compilations (id),
    constraint fk_to_events foreign key (id_event) references events (id)
);

create table if not exists requests
(
    id bigint generated always as identity primary key,
    id_user bigint,
    id_event bigint,
    status varchar(20),
    created timestamp,
    constraint fk_requests_to_users foreign key (id_user) references users (id),
    constraint fk_requests_to_events foreign key (id_event) references events (id)
);

create table if not exists comments
(
    id bigint generated always as identity primary key,
    id_user bigint,
    id_event bigint,
    description varchar(7000),
    created timestamp,
    constraint fk_comments_to_users foreign key (id_user) references users (id),
    constraint fk_comments_to_events foreign key (id_event) references events (id)
);