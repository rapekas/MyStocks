package ru.artemaa.stocks.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;

@MetaClass(name = "stocks$StockSummary")
public class StockSummary extends AbstractNotPersistentEntity {
    private static final long serialVersionUID = 5580946325692918983L;

    @MetaProperty(mandatory = true)
    protected Stock stock;

    @MetaProperty
    protected Integer amount = 0;

    @MetaProperty
    protected Double totalPrice = 0.0;

    @MetaProperty
    protected Double avgPrice = 0.0;

    @MetaProperty
    protected Double totalDividends = 0.0;

    @MetaProperty
    protected Double avgDividends = 0.0;

    @MetaProperty
    protected Double priceDividendsRatio = 0.0;

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Stock getStock() {
        return stock;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setAvgPrice(Double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Double getAvgPrice() {
        return avgPrice;
    }

    public void setTotalDividends(Double totalDividends) {
        this.totalDividends = totalDividends;
    }

    public Double getTotalDividends() {
        return totalDividends;
    }

    public void setAvgDividends(Double avgDividends) {
        this.avgDividends = avgDividends;
    }

    public Double getAvgDividends() {
        return avgDividends;
    }

    public void setPriceDividendsRatio(Double priceDividendsRatio) {
        this.priceDividendsRatio = priceDividendsRatio;
    }

    public Double getPriceDividendsRatio() {
        return priceDividendsRatio;
    }


}