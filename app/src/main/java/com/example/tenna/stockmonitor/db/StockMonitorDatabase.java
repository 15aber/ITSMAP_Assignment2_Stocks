package com.example.tenna.stockmonitor.db;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

//Modified from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/
@Database(entities = {Book.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class StockMonitorDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
    private static volatile StockMonitorDatabase INSTANCE;

    static StockMonitorDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StockMonitorDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StockMonitorDatabase.class, "stock_monitor_database")
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }

            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final BookDao mDao;

        PopulateDbAsync(StockMonitorDatabase db) {
            mDao = db.bookDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Book book = new Book("AAPL", 4);
            mDao.insert(book);
            book = new Book("FB", 3);
            mDao.insert(book);
            return null;
        }
    }
}
