//Modified from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7

package com.example.tenna.stockmonitor.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class StockMonitorRepository {
    private StockDao stockDao;
    private BookDao bookDao;

    private LiveData<List<Stock>> allStocks;
    private LiveData<List<Book>> allBooks;

    public StockMonitorRepository(Application application) {
        StockMontitorRoomDatabase db = StockMontitorRoomDatabase.getDatabase(application);
        stockDao = db.stockDao();
        bookDao = db.bookDao();
        allStocks = stockDao.getAllStocks();
        allBooks = bookDao.getAllBooks();
    }

    public LiveData<List<Stock>> getAllStocks() {
        return allStocks;
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }

    public void insert (Stock stock) {
        new insertStockAsyncTask(stockDao).execute(stock);
    }

    private static class insertStockAsyncTask extends AsyncTask<Stock, Void, Void> {

        private StockDao mAsyncTaskDao;

        insertStockAsyncTask(StockDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Stock... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void insert (Book book) {
        new insertBookAsyncTask(bookDao).execute(book);
    }

    private static class insertBookAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao mAsyncTaskDao;

        insertBookAsyncTask(BookDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Book... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
