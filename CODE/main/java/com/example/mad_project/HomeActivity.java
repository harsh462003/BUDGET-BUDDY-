package com.example.mad_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private EditText amountEditText, descriptionEditText;
    private TextView balanceTextView;
    private Button cashIn, cashOut, currency_convertor, investmentButton, historyButton;
    private double balance = 0.0;
    private DBHandler dbHandler;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: Initializing views");

        amountEditText = findViewById(R.id.amountEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        balanceTextView = findViewById(R.id.balanceTextView);
        cashIn = findViewById(R.id.cashInButton);
        cashOut = findViewById(R.id.cashOutButton);
        currency_convertor = findViewById(R.id.convertButton);
        investmentButton = findViewById(R.id.investButton);
        historyButton = findViewById(R.id.button2);
        ImageButton back = findViewById(R.id.imageButton);
        back.setOnClickListener(v->{

            Intent i=new Intent(HomeActivity.this, MainActivity.class);
            startActivity(i);

        });

        dbHandler = new DBHandler(this);

        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        if (userId != -1) {
            balance = dbHandler.getBalance(userId);
            updateBalance();
        } else {
            Log.e(TAG, "User ID not found!");
        }

        cashIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTransaction(true);
            }
        });

        cashOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTransaction(false);
            }
        });

        investmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInvestmentCalciActivity();
            }
        });

        currency_convertor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrencyConverter();
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHistoryActivity();
            }
        });


    }

    private void handleTransaction(boolean isCashIn) {
        String amountStr = amountEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        if (!amountStr.isEmpty()) {
            double amount = Double.parseDouble(amountStr);
            balance = isCashIn ? balance + amount : balance - amount;
            updateBalance();
            dbHandler.updateBalance(userId, balance);

            // Save transaction in the database
            String type = isCashIn ? "Credit" : "Debit";
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            dbHandler.addTransaction(userId, amount, description, type, date);

            Toast.makeText(HomeActivity.this, "Transaction saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HomeActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBalance() {
        balanceTextView.setText(String.format(Locale.getDefault(), "My Balance: ₹%.2f", balance));
        amountEditText.setText("");
        descriptionEditText.setText("");
    }

    private void openInvestmentCalciActivity() {
        Intent intent = new Intent(this, Investment_calci.class);
        startActivity(intent);
    }

    private void openCurrencyConverter() {
        Intent intent = new Intent(this, CurrencyConverter.class);
        startActivity(intent);
    }

    private void openHistoryActivity() {
        Intent intent = new Intent(this, history.class);
        startActivity(intent);
    }
}
