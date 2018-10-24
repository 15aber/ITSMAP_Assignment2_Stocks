package com.example.tenna.stockmonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.Arrays;

import static com.example.tenna.stockmonitor.Constants.STOCK_NAME;
import static com.example.tenna.stockmonitor.Constants.STOCK_NUM;
import static com.example.tenna.stockmonitor.Constants.STOCK_PRICE;
import static com.example.tenna.stockmonitor.Constants.STOCK_SECTOR;

public class EditActivity extends AppCompatActivity {

    private String stockName;
    private double stockPrice;
    private int numOfStock;
    private int stockSector;
    private int[] stockSectors = {
            R.id.radioButtonEditTech,
            R.id.radioButtonEditHealth,
            R.id.radioButtonEditBM
    };

    private EditText etStockName;
    private EditText etStockPrice;
    private EditText etStockNum;
    private RadioGroup rgStockSector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

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
        etStockName = findViewById(R.id.editTextEditStockName);
        etStockName.setText(stockName);
        etStockPrice = findViewById(R.id.editTextEditStockPrice);
        etStockPrice.setText(String.valueOf(stockPrice));
        etStockNum = findViewById(R.id.editTextEditStockNum);
        etStockNum.setText(String.valueOf(numOfStock));
        rgStockSector = findViewById(R.id.radioGroupEditStockSector);
        rgStockSector.check(stockSectors[stockSector]);
    }

    public boolean validateInput() {
        boolean valid = true;
        if (etStockName.getText().toString().isEmpty()) {
            etStockName.setError(getString(R.string.company_name_string) + " " + getString(R.string.required_string));
            valid = false;
        }
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

        Intent intent = new Intent();

        stockName = etStockName.getText().toString();
        intent.putExtra(STOCK_NAME, stockName);
        stockPrice = Double.parseDouble(etStockPrice.getText().toString());
        intent.putExtra(STOCK_PRICE, stockPrice);
        numOfStock = Integer.parseInt(etStockNum.getText().toString());
        intent.putExtra(STOCK_NUM, numOfStock);
        stockSector = rgStockSector.indexOfChild(findViewById(rgStockSector.getCheckedRadioButtonId()));
        intent.putExtra(STOCK_SECTOR, stockSector);

        setResult(RESULT_OK, intent);
        finish();
    }
}
