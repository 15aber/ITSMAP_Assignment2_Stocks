package com.example.tenna.stockmonitor;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.tenna.stockmonitor.db.Stock;
import com.example.tenna.stockmonitor.db.StockMonitorRepository;

import java.util.List;

public class StockMonitorViewModel extends AndroidViewModel {
    private StockMonitorRepository repository;
    private LiveData<List<Stock>> allStocks;

    public StockMonitorViewModel (Application application) {
        super(application);
        repository = new StockMonitorRepository(application);
        allStocks = repository.getAllStocks();
    }

    public LiveData<List<Stock>> getAllStocks() {
        return allStocks;
    }

    void insert(Stock stock) {
        repository.insert(stock);
    }
}
