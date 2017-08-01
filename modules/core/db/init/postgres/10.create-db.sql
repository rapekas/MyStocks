-- begin STOCKS_CURRENCY
create table STOCKS_CURRENCY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(80) not null,
    CODE varchar(3) not null,
    --
    primary key (ID)
)^
-- end STOCKS_CURRENCY
-- begin STOCKS_OPERATION
create table STOCKS_OPERATION (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    STOCK_ID uuid not null,
    TYPE_ integer not null,
    DATE_ date not null,
    PRICE double precision not null,
    AMOUNT integer not null,
    --
    primary key (ID)
)^
-- end STOCKS_OPERATION
-- begin STOCKS_STOCK
create table STOCKS_STOCK (
    ID uuid,
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
    CURRENCY_ID uuid not null,
    --
    primary key (ID)
)^
-- end STOCKS_STOCK
