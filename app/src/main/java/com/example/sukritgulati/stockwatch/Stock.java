package com.example.sukritgulati.stockwatch;

/**
 * Created by sukritgulati on 3/10/17.
 */

public class Stock implements  Comparable<Stock>{

    String StockSymbol;
    String CompanyName;
    Double LastTradePrice;
    Double PriceChangeAmount;
    Double PriceChangePercentage;

    public Stock(String stockSymbol, String companyName, Double lastTradePrice, Double priceChangeAmount, Double priceChangePercentage) {
        StockSymbol = stockSymbol;
        CompanyName = companyName;
        LastTradePrice = lastTradePrice;
        PriceChangeAmount = priceChangeAmount;
        PriceChangePercentage = priceChangePercentage;
    }

    public String getStockSymbol() {
        return StockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        StockSymbol = stockSymbol;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public Double getLastTradePrice() {
        return LastTradePrice;
    }

    public void setLastTradePrice(Double lastTradePrice) {
        LastTradePrice = lastTradePrice;
    }

    public Double getPriceChangeAmount() {
        return PriceChangeAmount;
    }

    public void setPriceChangeAmount(Double priceChangeAmount) {
        PriceChangeAmount = priceChangeAmount;
    }

    public Double getPriceChangePercentage() {
        return PriceChangePercentage;
    }

    public void setPriceChangePercentage(Double priceChangePercentage) {
        PriceChangePercentage = priceChangePercentage;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Stock){
            Stock toCompare = (Stock) obj;
            return this.StockSymbol.equals(toCompare.StockSymbol);
        }
        return false;
    }

    @Override
    public int compareTo(Stock o) {
        return getStockSymbol().compareTo(o.getStockSymbol());
    }
}
