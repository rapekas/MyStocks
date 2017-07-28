alter table STOCKS_OPERATION add constraint FK_STOCKS_OPERATION_STOCK foreign key (STOCK_ID) references STOCKS_STOCK(ID);
