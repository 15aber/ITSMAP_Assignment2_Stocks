//Code modified from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#12

package com.example.tenna.stockmonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddStockActivity extends AppCompatActivity {

    public static final String EXTRA_STOCK_SYMBOL = "com.example.Assignment2.STOCK_SYMBOL";
    public static final String EXTRA_STOCK_NUM = "com.example.Assignment2.STOCK_NUM";

    private EditText tvAddStockSymbol;
    private EditText tvAddStockNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        tvAddStockSymbol = findViewById(R.id.tv_add_stock_symbol);
        tvAddStockNum = findViewById(R.id.tv_add_stock_num);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(tvAddStockSymbol.getText()) || TextUtils.isEmpty(tvAddStockNum.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    replyIntent.putExtra(EXTRA_STOCK_SYMBOL, tvAddStockSymbol.getText().toString());
                    replyIntent.putExtra(EXTRA_STOCK_NUM, Integer.parseInt(tvAddStockNum.getText().toString()));
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
