alter table STOCKS_STOCK add constraint FK_STOCKS_STOCK_CURRENCY foreign key (CURRENCY_ID) references STOCKS_CURRENCY(ID);
create unique index IDX_STOCKS_STOCK_UNIQ_CODE on STOCKS_STOCK (CODE) ;
create unique index IDX_STOCKS_STOCK_UNIQ_NAME on STOCKS_STOCK (NAME) ;
