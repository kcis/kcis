# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "ACCOUNT" ("ID" INTEGER NOT NULL PRIMARY KEY,"EMAIL" VARCHAR NOT NULL,"PASSWORD" VARCHAR NOT NULL);

# --- !Downs

drop table "ACCOUNT";

