package com.example.mad_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private DBHandler dbHandler;
    private List<Transaction> transactionList;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ImageButton back = findViewById(R.id.imageButton4);
        back.setOnClickListener(v -> {
            Intent i = new Intent(history.this, HomeActivity.class);
            startActivity(i);
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHandler = new DBHandler(this);

        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        if (userId != -1) {
            loadTransactions(userId);
        } else {
            Log.e(TAG, "User ID not found!");
        }
    }

    private void loadTransactions(int userId) {
        transactionList = new ArrayList<>();
        Cursor cursor = dbHandler.getTransactionsByUserId(userId);

        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                transactionList.add(new Transaction(type, String.valueOf(amount), description, date));
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(adapter);
    }
}
