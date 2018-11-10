package com.example.tenna.stockmonitor.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "stock_table")
public class Stock {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "stock_name")
    private String stockName;

    @NonNull
    @ColumnInfo(name = "stock_price")
    private double stockPrice;

    @ColumnInfo(name = "stock_price_diff")
    private double stockPriceDifference;

    @ColumnInfo(name = "stock_num")
    private int numOfStock;

    @NonNull
    @ColumnInfo(name = "book_symbol")
    private String bookSymbol;

    public Stock(String stockName, double stockPrice, int numOfStock, double stockPriceDifference, String bookSymbol) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.numOfStock = numOfStock;
        this.stockPriceDifference = stockPriceDifference;
        this.bookSymbol = bookSymbol;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockName() {
        return this.stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getStockPrice() {
        return this.stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public int getNumOfStock() {
        return this.numOfStock;
    }

    public void setNumOfStock(int numOfStock) {
        this.numOfStock = numOfStock;
    }

    public double getStockPriceDifference() {
        return this.stockPriceDifference;
    }

    public void setStockPriceDifference(double stockLatestPrice) {
        this.stockPriceDifference = stockLatestPrice-stockPrice;
    }

    @NonNull
    public String getBookSymbol() {
        return bookSymbol;
    }

    public void setBookSymbol(@NonNull String bookSymbol) {
        this.bookSymbol = bookSymbol;
    }
}
