package com.example.bazy_danych_dobrze;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MarketDatabaseHelper extends SQLiteOpenHelper {
    //public static int v=0;
    private static final String DBNAME="MarketStand";
    public static int DBVER=2;
    private static final String id="id";
    public final static String STAND="STAND";
    MarketDatabaseHelper(Context context) {
        super(context,DBNAME,null,DBVER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlString="CREATE TABLE STAND (_id INTEGER PRIMARY KEY AUTOINCREMENT,"+"NAME TEXT, QUANTITY INTEGER)";
        db.execSQL(sqlString);
        ContentValues itemValues=new ContentValues();
        for(int i=0; i<Activity01.comRange.length;i++)
        {
            itemValues.clear();
            itemValues.put("NAME",Activity01.comRange[i]);
            itemValues.put("QUANTITY",0);
            db.insert("STAND",null,itemValues);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    if(oldVersion<newVersion)
    {
       //v=1;
        String sqlString="ALTER TABLE STAND ADD COLUMN DESCRIPTION STRING";
        db.execSQL(sqlString);
        ContentValues itemValues=new ContentValues();
        for(int i=0; i<Activity01.comRange.length;i++)
        {

            db.execSQL("UPDATE " + "STAND" + " SET DESCRIPTION = " + "'" + Activity01.comRange1[i] + "' " + "WHERE _id = " + "'" + (i+1) + "'");//!!!!!!!!!
        }



    }

        //onCreate(db);
    }
}
