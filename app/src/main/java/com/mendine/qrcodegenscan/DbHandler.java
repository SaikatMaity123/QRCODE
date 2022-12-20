package com.mendine.qrcodegenscan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHandler extends SQLiteOpenHelper {
    private static final String dbname = "QR_SCAN.db";
    SQLiteDatabase sqLiteDatabase;


    public void openDB() {
        sqLiteDatabase = this.getWritableDatabase();
    }

    // Close database connection
    private void closeDB() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }

    public DbHandler(@Nullable Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String rx_table = "create table qr_scan_data(scan_name text)";
        db.execSQL(rx_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS qr_scan_data");
        onCreate(db);
    }

    public boolean insert_data(String scan_name) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put("scan_name", scan_name);
        long rx_insert = sqLiteDatabase.insert("qr_scan_data", null, cv);
        if (rx_insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getinfo() {
        openDB();
        Cursor c = sqLiteDatabase.rawQuery("select * from qr_scan_data", null);
        //Cursor c =sqLiteDatabase.rawQuery("select * from users where username=?",new String[]{username});
        return c;
    }

    public void deleteProfile() {
        try {
            openDB();
            sqLiteDatabase.execSQL("DELETE FROM qr_scan_data");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
    }
}
