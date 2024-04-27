drop table if exists DOCUMENT;

create table DOCUMENT
(
    ID int not null,
    TITLE varchar(200),
    CONTENT varchar(255),
    IN_Z timestamp not null,
    OUT_Z timestamp not null
);

alter table DOCUMENT add constraint DOCUMENT_PK primary key (ID, OUT_Z);

drop table if exists OBJECT_SEQUENCE;

create table OBJECT_SEQUENCE
(
    SEQUENCE_NAME varchar(64) not null,
    NEXT_VALUE bigint
);

alter table OBJECT_SEQUENCE add constraint OBJECT_SEQUENCE_PK primary key (SEQUENCE_NAME);

