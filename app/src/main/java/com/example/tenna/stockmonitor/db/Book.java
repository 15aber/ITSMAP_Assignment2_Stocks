package com.example.tenna.stockmonitor.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Date;

@Entity(tableName = "book_table")
@TypeConverters(DateConverter.class)
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String symbol;

    @ColumnInfo(name = "company_name")
    private String companyName;

    @ColumnInfo(name = "primary_exchange")
    private String primaryExchange;

    @ColumnInfo(name = "latest_value")
    private double latestValue;

    @ColumnInfo(name = "latest_timestamp")
    private Date lastestTimestamp;

    @ColumnInfo(name = "stock_sector")
    private String stockSector;

    @ColumnInfo(name = "num_of_stocks")
    private int numOfStocks;

    @ColumnInfo(name = "purchase_price")
    private double purchasePrice;

    public Book(@NonNull String symbol, int numOfStocks) {
        this.symbol = symbol;
        this.numOfStocks = numOfStocks;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public String getPrimaryExchange() {
        return primaryExchange;
    }

    public void setPrimaryExchange(String primaryExchange) {
        this.primaryExchange = primaryExchange;
    }

    public double getLatestValue() {
        if(this.latestValue == 0)
            return 0.0;
        return latestValue;
    }

    public void setLatestValue(double latestValue) {
        this.latestValue = latestValue;
    }

    public Date getLastestTimestamp() {
        return lastestTimestamp;
    }

    public void setLastestTimestamp(Date lastestTimestamp) {
        this.lastestTimestamp = lastestTimestamp;
    }


    public String getStockSector() {
        return stockSector;
    }

    public void setStockSector(String stockSector) {
        this.stockSector = stockSector;
    }

    public int getNumOfStocks() {
        return numOfStocks;
    }

    public void setNumOfStocks(int numOfStocks) {
        this.numOfStocks = numOfStocks;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    //Code modified from https://stackoverflow.com/questions/739192/java-method-finding-object-in-array-list-given-a-known-attribute-value
    //compare Books on id
    public boolean equals(Book obj) {
        EqualsBuilder builder = new EqualsBuilder().append(this.getId(), obj.getId());
        return builder.isEquals();
    }
}
