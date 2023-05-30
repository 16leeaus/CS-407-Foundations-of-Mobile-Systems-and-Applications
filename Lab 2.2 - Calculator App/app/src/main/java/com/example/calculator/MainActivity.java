package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int value1, value2;

    public void getValues() {

        // Get the first value:
        EditText editText1 = findViewById(R.id.editTextValue1);
        value1 = Integer.parseInt(editText1.getText().toString());

        // Get the second value:
        EditText editText2 = findViewById(R.id.editTextValue2);
        value2 = Integer.parseInt(editText2.getText().toString());

    }

    public void calcAdd (View view) {
        getValues();
        int sum = value1 + value2;
        displayResult(Integer.toString(sum));
    }

    public void calcSub(View view) {
        getValues();
        int difference = value1 - value2;
        displayResult(Integer.toString(difference));
    }

    public void calcMult(View view) {
        getValues();
        int product = value1 * value2;
        displayResult(Integer.toString(product));
    }

    public void calcDiv(View view) {
        getValues();
        double quotient = value1 / (value2 * 1.0);
        displayResult(Double.toString(quotient));
    }

    public void displayResult(String s) {
        Intent intent = new Intent(this, DisplayResults.class);
        intent.putExtra("result", s);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}