package com.mendine.qrcodegenscan;

import android.content.Context;

public class MyConstantData {
    public static void cleanDB(Context context, DbHandler db) {
        try {
            // delete database table
            db.deleteProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
