drop table if exists stats cascade;

create table if not exists stats
(
    id bigint generated always as identity primary key,
    app varchar(100),
    uri varchar(100),
    ip varchar(100),
    time_stamp TIMESTAMP
);