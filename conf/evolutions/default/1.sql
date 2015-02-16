# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "ACCOUNT" ("STATION_ID" VARCHAR NOT NULL PRIMARY KEY,"MEMBER_ID" VARCHAR NOT NULL PRIMARY KEY,"PASSWORD" VARCHAR NOT NULL);

# --- !Downs

drop table "ACCOUNT";

