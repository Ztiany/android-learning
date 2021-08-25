package me.ztiany.android.database.origin;

import android.database.sqlite.SQLiteDatabase;

import me.ztiany.android.database.App;

public class DatabaseManager {

    private int mOpenCounter = 0;
    private volatile static DatabaseManager instance;
    private static DBHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    private DatabaseManager() {
        mDatabaseHelper = new DBHelper(App.getApp());
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter == 0) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        mOpenCounter++;//引用加1
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if (mOpenCounter == 0) {
            // Closing database
            mDatabase.close();
        }
    }

}