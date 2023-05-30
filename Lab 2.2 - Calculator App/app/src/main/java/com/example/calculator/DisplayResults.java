package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayResults extends AppCompatActivity {

    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        textView2 = (TextView) findViewById(R.id.textView2);
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        textView2.setText(result);
    }
}