package com.example.tenna.stockmonitor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tenna.stockmonitor.db.Book;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.tenna.stockmonitor.Constants.BROADCAST__SERVICE_DATA_UPDATED;
import static com.example.tenna.stockmonitor.Constants.NOTIFY_ID;

// code modified from https://developer.android.com/guide/components/bound-services#java
public class StockMonitorService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private AsyncUpdateTask asyncUpdateTask;
    private boolean started = false;
    private ArrayList<Book> books;

    public StockMonitorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        books = new ArrayList<>();
        addBook("TSLA", 4);
        addBook("FB", 4);
        addBook("ROST", 2);
        addBook("PEP", 2);
        addBook("AAPL", 2);
        addBook("BIDU", 2);
        addBook("GOOGL", 4);
        addBook("EBAY", 3);
        addBook("NFLX", 3);
        addBook("AMZN", 4);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Only start the background loop once
        if(!started && intent!=null) {

            startServiceWithNotification();

            asyncUpdateTask = new AsyncUpdateTask();
            asyncUpdateTask.execute();

        } else {
            Log.d("LOG", "Background service onStartCommand - already started!");
        }
        return START_STICKY;
    }

    public void saveBookChanges(int position, double purchasePrice, int numOfStock) {
        Book book = books.get(position);
        book.setPurchasePrice(purchasePrice);
        book.setNumOfStocks(numOfStock);
        broadcastTaskResult("Book changed");
        Log.i("Service:", "Purchase price and number of stocks of book:" + book + ", has been changed.");
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        StockMonitorService getService() {
            // Return this instance of LocalService so clients can call public methods
            return StockMonitorService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public void addBook(String stockSymbol, int numOfStock) {
        final Book book = new Book(stockSymbol, numOfStock);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, buildURL(book.getSymbol()), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("JSON", "JSON: " + jsonObject);

                        try {
                            String companyName = jsonObject.getString("companyName");
                            book.setCompanyName(companyName);
                            String primaryExchange = jsonObject.getString("primaryExchange");
                            book.setPrimaryExchange(primaryExchange);
                            double latestValue = Double.parseDouble(jsonObject.getString("latestPrice"));
                            book.setLatestValue(latestValue);
                            book.setPurchasePrice(latestValue); //set purchase price
                            Date latestTimestamp = new Date(Long.parseLong(jsonObject.getString("latestUpdate")));
                            book.setLastestTimestamp(latestTimestamp);
                            String stockSector = jsonObject.getString("sector");
                            book.setStockSector(stockSector);

                            books.add(book);
                            broadcastTaskResult("New book added");
                            Log.i("Service", "Updated book: " + book);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error", "Book couldn't be added: " + error);
                    }
                }
        );
        MySingleton.getInstance(StockMonitorService.this).addToRequestQueue(jsonObjectRequest);
    }



    private void startServiceWithNotification() {
        //wait = intent.getLongExtra(EXTRA_TASK_TIME_MS, DEFAULT_WAIT);
        Log.d("LOG", "Stock Monitor service onStartCommand");
        started = true;

        //Intent notificationIntent = new Intent(this, MainActivity.class);
        // PendingIntent pendingIntent =
        // PendingIntent.getActivity(this, 0, notificationIntent, 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { // Do not do this at home :)
            NotificationChannel mChannel = new NotificationChannel("myChannel", "Visible myChannel", NotificationManager.IMPORTANCE_LOW);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Notification notification =
                new NotificationCompat.Builder(this, "myChannel")
                        .setContentTitle("StockMonitor")
                        .setContentText("Fetching Data")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        //        .setContentIntent(pendingIntent)
                        .setTicker("TickerTock")
                        .setChannelId("myChannel")
                        .build();

        //calling Android to
        startForeground(NOTIFY_ID, notification);
    }

    //AsyncTask to execute in background every 2 minutes
    public class AsyncUpdateTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            while(started){
                Log.d("Service:","AsyncUpdateTask entered");
                try {
                    //Use Volley to update data from online API
                    updateAllBooks();
                    Thread.sleep(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public void updateAllBooks() {
        Log.i("Service: ", "Updating all books.");
        for (final Book book: books) {
            //update each book
            updateSingleBook(book);
        }
    }

    public void updateSingleBook(final Book book) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, buildURL(book.getSymbol()), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("JSON", "JSON: " + jsonObject);

                        try {
                            String companyName = jsonObject.getString("companyName");
                            book.setCompanyName(companyName);
                            String primaryExchange = jsonObject.getString("primaryExchange");
                            book.setPrimaryExchange(primaryExchange);
                            double latestValue = Double.parseDouble(jsonObject.getString("latestPrice"));
                            book.setLatestValue(latestValue);
                            Date latestTimestamp = new Date(Long.parseLong(jsonObject.getString("latestUpdate")));
                            book.setLastestTimestamp(latestTimestamp);
                            String stockSector = jsonObject.getString("sector");
                            book.setStockSector(stockSector);

                            Log.i("Service", "Updated book: " + book);
                            broadcastTaskResult("Book updated");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Service", "Error occurred: " + error);
                    }
                }
        );
        MySingleton.getInstance(StockMonitorService.this).addToRequestQueue(jsonObjectRequest);
    }



    String buildURL(String symbol) {
        return "https://api.iextrading.com/1.0/stock/" + symbol + "/quote";
    }

    void parseValuesFromJson(Book book, JSONObject jsonObject) {

    }

    //send local broadcast
    private void broadcastTaskResult(String result){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BROADCAST__SERVICE_DATA_UPDATED);
        Log.d("LOG", "Broadcasting:" + result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

}
