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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tenna.stockmonitor.adapter.BookListAdapter;
import com.example.tenna.stockmonitor.db.Book;

import java.util.ArrayList;
import java.util.List;

import static com.example.tenna.stockmonitor.Constants.BROADCAST__SERVICE_DATA_UPDATED;

public class OverviewActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    StockMonitorService mService;
    boolean mBound = false;
    List<Book> allBooks;

    private BookListAdapter adapter;
    ImageButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        startStockMonitorService();

        RecyclerView stockRecyclerView = findViewById(R.id.stock_recyclerview);
        adapter = new BookListAdapter(this);
        stockRecyclerView.setAdapter(adapter);
        stockRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        allBooks = new ArrayList<Book>();
        Book book = new Book("aapl", 220);
        allBooks.add(book);

        adapter.setBooks(allBooks);

        addButton = (ImageButton) findViewById(R.id.AddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addNewBook();
                Intent intent = new Intent(getApplicationContext(), AddStockActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this.getApplicationContext(), StockMonitorService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST__SERVICE_DATA_UPDATED);
        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        mBound = false;
    }

    //starts background service, taskTime indicates desired sleep period in ms for broadcasts
    private void startStockMonitorService() {
        Intent stockMonitorServiceIntent = new Intent(this.getApplicationContext(), StockMonitorService.class);
        startService(stockMonitorServiceIntent);
    }

    //Modified from https://developer.android.com/guide/components/bound-services#java
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            StockMonitorService.LocalBinder binder = (StockMonitorService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    //define our broadcast receiver for (local) broadcasts.
    // Registered and unregistered in onStart() and onStop() methods
    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity", "Broadcast reveiced from service");
            //handleBackgroundResult(result);
            allBooks = mService.getAllBooks();
            adapter.setBooks(allBooks);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            mService.addBook(data.getStringExtra(AddStockActivity.EXTRA_STOCK_SYMBOL),data.getIntExtra(AddStockActivity.EXTRA_STOCK_NUM, 0));
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}
