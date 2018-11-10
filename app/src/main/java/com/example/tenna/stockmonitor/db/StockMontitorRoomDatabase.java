//Code modified from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6

package com.example.tenna.stockmonitor.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Stock.class, Book.class}, version = 2)
public abstract class StockMontitorRoomDatabase extends RoomDatabase {
    public abstract StockDao stockDao();
    public abstract BookDao bookDao();

    private static volatile StockMontitorRoomDatabase INSTANCE;

    static StockMontitorRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StockMontitorRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StockMontitorRoomDatabase.class, "stock_monitor_database")
                            .addCallback(sRoomDatabaseCallback)
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

    //modified from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#11
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final StockDao mDao;

        PopulateDbAsync(StockMontitorRoomDatabase db) {
            mDao = db.stockDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Stock stock = new Stock("Tesla",44.44, 7, -4.5, "tsla");
            mDao.insert(stock);
            stock = new Stock("Facebook",66.45, 4, 6.7, "fb");
            mDao.insert(stock);
            return null;
        }
    }
}
