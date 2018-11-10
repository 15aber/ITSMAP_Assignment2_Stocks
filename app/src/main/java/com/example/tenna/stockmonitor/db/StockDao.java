//modified from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#4
//and from https://codelabs.developers.google.com/codelabs/android-persistence/#3

package com.example.tenna.stockmonitor.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StockDao {
    //Add new stock to stock_table
    @Insert
    void insert(Stock stock);

    //Delete all stocks in stock_table
    @Query("DELETE FROM stock_table")
    void deleteAll();

    //get all stocks
    @Query("SELECT * from stock_table ORDER BY stock_name ASC")
    LiveData<List<Stock>> getAllStocks();

    //get stock by id
    @Query("select * from stock_table where id = :id")
    Stock loadStockById(int id);
}
