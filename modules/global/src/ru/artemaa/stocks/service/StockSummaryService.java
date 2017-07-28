package ru.artemaa.stocks.service;


import ru.artemaa.stocks.entity.StockSummary;

import java.util.List;
import java.util.UUID;

public interface StockSummaryService {
    String NAME = "stocks_StockSummaryService";

    StockSummary getStockSummary(UUID stockId);

    List<StockSummary> getStockSummaries();

}