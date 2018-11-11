package com.example.tenna.stockmonitor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tenna.stockmonitor.db.Book;

import java.util.List;

import static com.example.tenna.stockmonitor.Constants.BROADCAST__SERVICE_DATA_UPDATED;
import static com.example.tenna.stockmonitor.Constants.CURRENT_BOOK;
import static com.example.tenna.stockmonitor.Constants.NOT_SET_BEFORE_VALUE;
import static com.example.tenna.stockmonitor.Constants.STOCK_NUM;
import static com.example.tenna.stockmonitor.Constants.STOCK_PRICE;

public class EditActivity extends AppCompatActivity {

    StockMonitorService mService;
    boolean mBound = false;
    boolean notSetBefore = true;

    private int currentBookPosition;
    private Book currentBook;
    private List<Book> allBooks;
    private String stockName;
    private double purchasePrice;
    private int numOfStock;
    private String stockSector;

//    private int[] stockSectors = {
//            R.id.radioButtonEditTech,
//            R.id.radioButtonEditHealth,
//            R.id.radioButtonEditBM
//    };

    private TextView etStockName;
    private EditText etStockPrice;
    private EditText etStockNum;
    private TextView tvStockSector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Bind to LocalService
        Intent intent = new Intent(this, StockMonitorService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        if (savedInstanceState != null) {
            purchasePrice = savedInstanceState.getDouble(STOCK_PRICE);
            numOfStock = savedInstanceState.getInt(STOCK_NUM);
            notSetBefore = savedInstanceState.getBoolean(NOT_SET_BEFORE_VALUE);
        }
        final Intent data = getIntent();
        currentBookPosition = data.getIntExtra(CURRENT_BOOK, 0);
        setValues();
        updateUI();


        //Subscribe to dataupdated broadcasts
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST__SERVICE_DATA_UPDATED);
    }

    private void setValues() {
        if(mBound) {
            allBooks = mService.getAllBooks();
            currentBook = allBooks.get(currentBookPosition);
            stockName = currentBook.getCompanyName();
            stockSector = currentBook.getStockSector();
            if (notSetBefore) {
                purchasePrice = currentBook.getPurchasePrice();
                numOfStock = currentBook.getNumOfStocks();
                notSetBefore = false;
            }
        }
    }

    private void updateUI() {
        if (currentBook!=null) {
            // Capture the layout's TextView and set the string as its text
            etStockName = findViewById(R.id.editTextViewStockName);
            etStockName.setText(stockName);
            etStockPrice = findViewById(R.id.editTextEditStockPrice);
            etStockPrice.setText(String.valueOf(purchasePrice));
            etStockNum = findViewById(R.id.editTextEditStockNum);
            etStockNum.setText(String.valueOf(numOfStock));
            tvStockSector = findViewById(R.id.textViewEditStockSector);
            tvStockSector.setText(stockSector);
        }
    }


    public boolean validateInput() {
        boolean valid = true;
        if (etStockPrice.getText().toString().isEmpty() || Double.parseDouble(etStockPrice.getText().toString()) == 0) {
            etStockPrice.setError(getString(R.string.price_string) + " " + getString(R.string.required_string));
            valid = false;
        }
        if (etStockNum.getText().toString().isEmpty() || Integer.parseInt(etStockNum.getText().toString()) == 0) {
            etStockNum.setError(getString(R.string.stock_num_string) + " " + getString(R.string.required_string));
            valid = false;
        }
        return valid;
    }

    /** Called when the user taps the Cancel button */
    public void goToDetails(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    /** Called when the user taps the Save button */
    public void goToOverview(View view) {
        if (!validateInput())
            return;

        saveBookChanges();
        Intent intent = new Intent();
        intent.putExtra(CURRENT_BOOK, currentBookPosition);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void saveBookChanges() {
        purchasePrice = Double.parseDouble(etStockPrice.getText().toString());
        numOfStock = Integer.parseInt(etStockNum.getText().toString());

        mService.saveBookChanges(currentBookPosition, purchasePrice, numOfStock);
    }

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

    // Modified from: https://developer.android.com/guide/components/activities/activity-lifecycle.html
    //Save inputs for configuration changes
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putDouble(STOCK_PRICE, Double.parseDouble(etStockPrice.getText().toString()));
        outState.putInt(STOCK_NUM, Integer.parseInt(etStockNum.getText().toString()));
        outState.putBoolean(NOT_SET_BEFORE_VALUE, notSetBefore);
        super.onSaveInstanceState(outState);
    }
}
