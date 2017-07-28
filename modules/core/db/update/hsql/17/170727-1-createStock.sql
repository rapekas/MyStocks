create table STOCKS_STOCK (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(80) not null,
    CODE varchar(20) not null,
    CURRENCY_ID varchar(36) not null,
    --
    primary key (ID)
);
