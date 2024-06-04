package com.example.mad_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3; // Updated version
    private static final String DATABASE_NAME = "UserDatabase";
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_TRANSACTIONS = "Transactions";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_BALANCE = "balance"; // New column for balance

    private static final String KEY_TRANSACTION_ID = "transaction_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TYPE = "type"; // "Credit" or "Debit"
    private static final String KEY_DATE = "date";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT,"
                + KEY_BALANCE + " REAL DEFAULT 0.0" + ")";
        db.execSQL(CREATE_USERS_TABLE);
        Log.d("DBHandler", "Users table created");

        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + KEY_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_ID + " INTEGER,"
                + KEY_AMOUNT + " REAL," + KEY_DESCRIPTION + " TEXT,"
                + KEY_TYPE + " TEXT," + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
        Log.d("DBHandler", "Transactions table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS);
        db.execSQL("DELETE FROM " + TABLE_TRANSACTIONS);
        db.close();
    }

    public void addNewUser(String name, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_BALANCE, 0.0); // Initialize balance to 0.0

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public boolean doesUsernameExist(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID}, KEY_USERNAME + "=?",
                new String[]{username}, null, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public int getLatestUserId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + KEY_ID + ") FROM " + TABLE_USERS, null);
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            cursor.close();
            return userId;
        }
        return -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID}, KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{username, password}, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID}, KEY_USERNAME + "=?",
                new String[]{username}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
            cursor.close();
            return userId;
        }
        return -1;
    }

    public void addTransaction(int userId, double amount, String description, String type, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_AMOUNT, amount);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_TYPE, type);
        values.put(KEY_DATE, date);

        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + KEY_DATE + " DESC", null);
    }

    public Cursor getTransactionsByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TRANSACTIONS, null, KEY_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, KEY_DATE + " DESC");
    }

    public double getBalance(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_BALANCE}, KEY_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_BALANCE));
            cursor.close();
            return balance;
        }
        return 0.0;
    }

    public void updateBalance(int userId, double newBalance) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BALANCE, newBalance);

        db.update(TABLE_USERS, values, KEY_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
    }
}
