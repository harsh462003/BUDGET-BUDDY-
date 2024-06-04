package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;

public class CurrencyConverter extends AppCompatActivity {
    private EditText inputAmount;
    private EditText resultEditText;
    private Spinner currencySpinner;
    private Button convert;
    private double[] exchangeRates = {0.012, 0.011, 0.0095, 0.018, 0.016, 0.22, 0.044, 299.71, 0.087, 1.11};
    private String[] currencies = {"USD", "Euro", "Pound", "AUSD", "SGD", "African Rand", "UAE Dhiram", "Vietnam Dong", "Chinese Yuan", "Russian Ruble"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        inputAmount = findViewById(R.id.amountEditText);
        resultEditText = findViewById(R.id.convertedAmountEditText);
        currencySpinner = findViewById(R.id.currencySpinner);
        convert = findViewById(R.id.convertButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currencies);
        currencySpinner.setAdapter(adapter);

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = currencySpinner.getSelectedItemPosition();
                calculateCurrency(position);
            }


        });


        ImageButton back=findViewById(R.id.imageButton2);
        back.setOnClickListener(v->{

            Intent i=new Intent(CurrencyConverter.this, HomeActivity.class);
            startActivity(i);

        });
    }

    private void calculateCurrency(int position) {
        String inputStr = inputAmount.getText().toString().trim();
        if (!inputStr.isEmpty()) {
            double input = Double.parseDouble(inputStr);
            double convertedAmount = input * exchangeRates[position];
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            resultEditText.setText(decimalFormat.format(convertedAmount));
        } else {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        }
    }
}
