-- begin STOCKS_CURRENCY
create unique index IDX_STOCKS_CURRENCY_UK_CODE on STOCKS_CURRENCY (CODE) where DELETE_TS is null ^
-- end STOCKS_CURRENCY
-- begin STOCKS_OPERATION
alter table STOCKS_OPERATION add constraint FK_STOCKS_OPERATION_STOCK foreign key (STOCK_ID) references STOCKS_STOCK(ID)^
-- end STOCKS_OPERATION
-- begin STOCKS_STOCK
alter table STOCKS_STOCK add constraint FK_STOCKS_STOCK_CURRENCY foreign key (CURRENCY_ID) references STOCKS_CURRENCY(ID)^
create unique index IDX_STOCKS_STOCK_UK_NAME on STOCKS_STOCK (NAME) where DELETE_TS is null ^
create unique index IDX_STOCKS_STOCK_UK_CODE on STOCKS_STOCK (CODE) where DELETE_TS is null ^
-- end STOCKS_STOCK
