package com.example.tenna.stockmonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.tenna.stockmonitor.Constants.EDIT_REQUEST;
import static com.example.tenna.stockmonitor.Constants.STOCK_NAME;
import static com.example.tenna.stockmonitor.Constants.STOCK_NUM;
import static com.example.tenna.stockmonitor.Constants.STOCK_PRICE;
import static com.example.tenna.stockmonitor.Constants.STOCK_SECTOR;

public class DetailsActivity extends AppCompatActivity {

    private String stockName;
    private double stockPrice;
    private int numOfStock;
    private int stockSector;
    private int[] stockSectors = {
            R.string.sector_tech,
            R.string.sector_health,
            R.string.sector_bas_mat
    };

    private TextView tvStockName;
    private TextView tvStockPrice;
    private TextView tvStockNum;
    private TextView tvStockSector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState != null) {
            stockName = savedInstanceState.getString(STOCK_NAME);
            stockPrice = savedInstanceState.getDouble(STOCK_PRICE);
            numOfStock = savedInstanceState.getInt(STOCK_NUM);
            stockSector = savedInstanceState.getInt(STOCK_SECTOR);
        } else {
            final Intent data = getIntent();
            stockName = data.getStringExtra(STOCK_NAME);
            stockPrice = data.getDoubleExtra(STOCK_PRICE, Double.parseDouble(getString(R.string.default_price)));
            numOfStock = data.getIntExtra(STOCK_NUM, Integer.parseInt(getString(R.string.default_stock_num)));
            stockSector = data.getIntExtra(STOCK_SECTOR, Integer.parseInt(getString(R.string.default_stock_sector_number)));
        }

        // Capture the layout's TextView and set the string as its text
        tvStockName = findViewById(R.id.textViewDetailsName);
        tvStockName.setText(stockName);
        tvStockPrice = findViewById(R.id.textViewPrice);
        tvStockPrice.setText(String.format("%.2f", stockPrice));
        tvStockNum = findViewById(R.id.textViewDetailsStockNum);
        tvStockNum.setText(String.valueOf(numOfStock));
        tvStockSector = findViewById(R.id.textViewDetailsStockSector);
        tvStockSector.setText(stockSectors[stockSector]);

    }

    /** Called when the user taps the Back button */
    public void goToOverview(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    /** Called when the user taps the Edit button */
    public void goToEdit(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(STOCK_NAME, stockName);
        intent.putExtra(STOCK_PRICE, stockPrice);
        intent.putExtra(STOCK_NUM, numOfStock);
        intent.putExtra(STOCK_SECTOR, stockSector);
        startActivityForResult(intent, EDIT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_REQUEST && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, getText(R.string.cancelled_string), Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, getText(R.string.save_string), Toast.LENGTH_SHORT).show();
            stockName = data.getStringExtra(STOCK_NAME);
            stockPrice = data.getDoubleExtra(STOCK_PRICE, 0);
            numOfStock = data.getIntExtra(STOCK_NUM, 0);
            stockSector = data.getIntExtra(STOCK_SECTOR, 0);

            Intent intent = new Intent();
            intent.putExtra(STOCK_NAME, stockName);
            intent.putExtra(STOCK_PRICE, stockPrice);
            intent.putExtra(STOCK_NUM, numOfStock);
            intent.putExtra(STOCK_SECTOR, stockSector);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Modified from: https://developer.android.com/guide/components/activities/activity-lifecycle.html
        outState.putString(STOCK_NAME, stockName);
        outState.putDouble(STOCK_PRICE, stockPrice);
        outState.putInt(STOCK_NUM, numOfStock);
        outState.putInt(STOCK_SECTOR, stockSector);
        super.onSaveInstanceState(outState);
    }
}
