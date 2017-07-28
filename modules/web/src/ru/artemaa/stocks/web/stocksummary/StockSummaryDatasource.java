package ru.artemaa.stocks.web.stocksummary;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import ru.artemaa.stocks.entity.StockSummary;
import ru.artemaa.stocks.service.StockSummaryService;

import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Artem Areshko
 *         27.07.2017
 */
public class StockSummaryDatasource extends CollectionDatasourceImpl<StockSummary, UUID> {

    //@Inject
    private StockSummaryService summaryService = AppBeans.get(StockSummaryService.class);

    @Override
    protected void loadData(Map<String, Object> params) {
        data.clear();

        dataLoadError = null;
        try {
            Objects.requireNonNull(summaryService, "Summary service can't be null");
            summaryService.getStockSummaries().forEach(stockSummary -> data.put(stockSummary.getId(), stockSummary));
        } catch (Throwable e) {
            dataLoadError = e;
        }
    }
}
