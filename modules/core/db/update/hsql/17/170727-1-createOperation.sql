create table STOCKS_OPERATION (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DATE_ date not null,
    PRICE double precision not null,
    TYPE_ integer not null,
    AMOUNT integer not null,
    STOCK_ID varchar(36) not null,
    --
    primary key (ID)
);
