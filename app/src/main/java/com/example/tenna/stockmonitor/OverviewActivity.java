package com.example.tenna.stockmonitor;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.tenna.stockmonitor.Constants.DETAILS_REQUEST;
import static com.example.tenna.stockmonitor.Constants.STOCK_NAME;
import static com.example.tenna.stockmonitor.Constants.STOCK_NUM;
import static com.example.tenna.stockmonitor.Constants.STOCK_PRICE;
import static com.example.tenna.stockmonitor.Constants.STOCK_SECTOR;

public class OverviewActivity extends AppCompatActivity {

    private String stockName;
    private double stockPrice;
    private int numOfStock;
    private int stockSector; //0 = Technology, 1 = Healthcare, 2 = Basic Materials

    private int[] stockSectorsImg = {
            R.drawable.tech_img,
            R.drawable.icon_healthcare,
            R.drawable.basic_materials
    };

    private TextView tvStockName;
    private ImageView ivStockSector;
    private TextView tvStockPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Modified from: https://developer.android.com/guide/components/activities/activity-lifecycle.html
        if (savedInstanceState != null) {
            stockName = savedInstanceState.getString(STOCK_NAME);
            stockPrice = savedInstanceState.getDouble(STOCK_PRICE);
            numOfStock = savedInstanceState.getInt(STOCK_NUM);
            stockSector = savedInstanceState.getInt(STOCK_SECTOR);
        } else {
            stockName = getString(R.string.default_stock);
            stockPrice = Double.parseDouble(getString(R.string.default_price));
            numOfStock = Integer.parseInt(getString(R.string.default_stock_num));
            stockSector = Integer.parseInt(getString(R.string.default_stock_sector_number));
        }
        drawUI();
    }

    public void drawUI() {
        tvStockName = findViewById(R.id.stock);
        tvStockName.setText(stockName);
        tvStockPrice = findViewById(R.id.textViewPrice);
        tvStockPrice.setText(getString(R.string.purchased_string) + " " + String.format("%.2f", stockPrice));
        ivStockSector = findViewById(R.id.imageView2);
        ivStockSector.setImageDrawable(ContextCompat.getDrawable(this, stockSectorsImg[stockSector]));
    }

    /** Called when the user taps the Details button */

    public void goToDetails(View view) {
        // Modified from: https://developer.android.com/guide/components/activities/intro-activities.html
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(STOCK_NAME, stockName);
        intent.putExtra(STOCK_PRICE, stockPrice);
        intent.putExtra(STOCK_NUM, numOfStock);
        intent.putExtra(STOCK_SECTOR, stockSector);
        startActivityForResult(intent, DETAILS_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DETAILS_REQUEST) {
            if (resultCode == RESULT_OK) {
                stockName = data.getStringExtra(STOCK_NAME);
                stockPrice = data.getDoubleExtra(STOCK_PRICE, 0);
                numOfStock = data.getIntExtra(STOCK_NUM, 0);
                stockSector = data.getIntExtra(STOCK_SECTOR, 0);
                drawUI();
            }
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
