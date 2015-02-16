# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "ACCOUNT" ("STATION_ID" VARCHAR NOT NULL,"MEMBER_ID" VARCHAR NOT NULL,"PASSWORD" VARCHAR NOT NULL);
alter table "ACCOUNT" add constraint "pk_a" primary key("STATION_ID","MEMBER_ID");

# --- !Downs

alter table "ACCOUNT" drop constraint "pk_a";
drop table "ACCOUNT";

