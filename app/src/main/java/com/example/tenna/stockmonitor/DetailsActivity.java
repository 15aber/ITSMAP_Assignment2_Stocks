package com.example.tenna.stockmonitor;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tenna.stockmonitor.db.Book;

import java.util.Date;
import java.util.List;

import static com.example.tenna.stockmonitor.Constants.BROADCAST__SERVICE_DATA_UPDATED;
import static com.example.tenna.stockmonitor.Constants.CURRENT_BOOK;
import static com.example.tenna.stockmonitor.Constants.EDIT_REQUEST;
import static com.example.tenna.stockmonitor.Constants.STOCK_NAME;
import static com.example.tenna.stockmonitor.Constants.STOCK_NUM;
import static com.example.tenna.stockmonitor.Constants.STOCK_PRICE;
import static com.example.tenna.stockmonitor.Constants.STOCK_SECTOR;
import static java.lang.String.format;

public class DetailsActivity extends AppCompatActivity {

    StockMonitorService mService;
    boolean mBound = false;
    List<Book> allBooks;
    int currentBookPosition;
    Book currentBook;

    private String stockName;
    private double stockLatestPrice;
    private int numOfStock;
    private String stockSector;
    private String primaryExchange;
    private Date lastestTimestamp;
    private double purchasePrice;


    private int[] stockSectors = {
            R.string.sector_tech,
            R.string.sector_health,
            R.string.sector_bas_mat
    };

    private TextView tvStockName;
    private TextView tvStockPrice;
    private TextView tvStockNum;
    private TextView tvStockSector;
    private TextView tvPrimaryExchange;
    private TextView tvLatestTimestamp;
    private TextView tvPurchasePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Bind to LocalService
        Intent intent = new Intent(this, StockMonitorService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


        if (savedInstanceState != null) {
            stockName = savedInstanceState.getString(STOCK_NAME);
            stockLatestPrice = savedInstanceState.getDouble(STOCK_PRICE);
            numOfStock = savedInstanceState.getInt(STOCK_NUM);
            stockSector = savedInstanceState.getString(STOCK_SECTOR);
        } else {
            final Intent data = getIntent();
            currentBookPosition = data.getIntExtra(CURRENT_BOOK, 0);
            setValues();
        }

        updateUI();

        //Subscribe to dataupdated broadcasts
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST__SERVICE_DATA_UPDATED);
        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult,filter);
    }

    private void setValues() {
        if(mBound) {
            allBooks = mService.getAllBooks();
            currentBook = allBooks.get(currentBookPosition);
            stockName = currentBook.getCompanyName();
            stockLatestPrice = currentBook.getLatestValue();
            numOfStock = currentBook.getNumOfStocks();
            stockSector = currentBook.getStockSector();
            primaryExchange = currentBook.getPrimaryExchange();
            lastestTimestamp = currentBook.getLastestTimestamp();
            purchasePrice = currentBook.getPurchasePrice();
        }
    }

    private void updateUI() {
        if(currentBook != null) {
            // Capture the layout's TextView and set the string as its text
            tvStockName = findViewById(R.id.textViewDetailsName);
            tvStockName.setText(stockName);
            tvStockPrice = findViewById(R.id.textViewPrice);
            tvStockPrice.setText(format("%.2f", stockLatestPrice));
            tvStockNum = findViewById(R.id.textViewDetailsStockNum);
            tvStockNum.setText(String.valueOf(numOfStock));
            tvStockSector = findViewById(R.id.textViewDetailsStockSector);
            tvStockSector.setText(stockSector);
            tvPrimaryExchange = findViewById(R.id.tv_primary_exchange);
            tvPrimaryExchange.setText(primaryExchange);
            tvLatestTimestamp = findViewById(R.id.tv_latest_timestamp);
            tvLatestTimestamp.setText(DateFormat.format("yyyy.MM.dd", lastestTimestamp).toString());
            tvPurchasePrice = findViewById(R.id.tv_purchase_price);
            tvPurchasePrice.setText(format("%.2f", purchasePrice));


        } else {
            Log.i("DetailsActivity:", "Update UI: Data not ready.");
        }
    }

    /** Called when the user taps the Back button */
    public void goToOverview(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    /** Called when the user taps the Edit button */
    public void goToEdit(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(CURRENT_BOOK, currentBookPosition);
        startActivityForResult(intent, EDIT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_REQUEST && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, getText(R.string.cancelled_string), Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, getText(R.string.save_string), Toast.LENGTH_SHORT).show();
            //to do get position

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Modified from: https://developer.android.com/guide/components/activities/activity-lifecycle.html
        outState.putString(STOCK_NAME, stockName);
        outState.putDouble(STOCK_PRICE, stockLatestPrice);
        outState.putInt(STOCK_NUM, numOfStock);
        outState.putString(STOCK_SECTOR, stockSector);
        super.onSaveInstanceState(outState);
    }

    //define our broadcast receiver for (local) broadcasts.
    // Registered and unregistered in onStart() and onStop() methods
    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("DetailsActivity", "Broadcast received from service");
            //handleBackgroundResult(result);
            if(mBound) {
                setValues();
                updateUI();
            }
        }
    };

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            StockMonitorService.LocalBinder binder = (StockMonitorService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            setValues();
            updateUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        mBound = false;
    }
}
