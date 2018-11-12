package com.example.tenna.stockmonitor.db;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BookRepository {
    private BookDao mBookDao;
    private List<Book> books;

    public BookRepository(Application application) {
        StockMonitorDatabase db = StockMonitorDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        books = mBookDao.getAll();
    }

    //public List<Book> getAllBooks(){return books;}

//    //Retrieve all books from db
    public List<Book> getAll() throws InterruptedException, ExecutionException {
        return new getAllAsyncTask(mBookDao).execute().get();
    }

    private class getAllAsyncTask extends AsyncTask<Void, Void, List<Book>> {

        private BookDao mAsyncTaskDao;

        getAllAsyncTask(BookDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<Book> doInBackground(Void... url) {
            return mAsyncTaskDao.getAll();
        }
    }


    //Insert book into db
    public void insert (Book book) {
        new insertAsyncTask(mBookDao).execute(book);
    }

    private static class insertAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao mAsyncTaskDao;

        insertAsyncTask(BookDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Book... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


}
