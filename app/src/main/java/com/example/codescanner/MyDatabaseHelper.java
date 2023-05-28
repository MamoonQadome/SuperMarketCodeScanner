package com.example.codescanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    Context context;
    private static final String DBName = "YamisDATABASE.db";
    private static final String TABLENAME = "SUPERMARKET";

    private static final String COLUMN_ITEM_ID = "_ITEM_ID";
    private static final String COLUMN_NAME = "_ITEM_NAME";
    private static final String COLUMN_PRICE = "_ITEM_PRICE";
    private static final int VERSION = 1;


    String[] items = new String[2];
    public MyDatabaseHelper(@Nullable Context context)
    {
        super(context, DBName, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
         /*   String query = "CREATE TABLE "+TABLENAME+" ( "+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +COLUMN_ITEM_ID+" TEXT NOT NULL, "+
                   COLUMN_NAME+" TEXT NOT NULL, "+COLUMN_PRICE+" INTEGER NOT NULL);";*/
       String query = "CREATE TABLE "+TABLENAME+" ("+COLUMN_ITEM_ID +" TEXT PRIMARY KEY, "+COLUMN_NAME+" TEXT, "+COLUMN_PRICE+" FLOAT);";

            db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
                db.execSQL("DROP TABLE IF EXISTS "+ TABLENAME);
                onCreate(db);
    }

    public void AddItems(String ItemID, String ItemName, Float ItemPrice)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ITEM_ID,ItemID);
        cv.put(COLUMN_NAME,ItemName);
        cv.put(COLUMN_PRICE,ItemPrice);

        long result = db.insert(TABLENAME,null,cv);
        if(result == -1)
            Toast.makeText(context, "Added Failed", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
    }



    public String ShowingItems(String ItemID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLENAME+" WHERE "+COLUMN_ITEM_ID+" = ?",new String[]{ItemID});
        DecimalFormat df = new DecimalFormat("#.##");

        if(cursor.moveToFirst())
        return "Name: "+ cursor.getString(1)+"\nValue: "+ String.format("%.02f",Float.parseFloat(cursor.getString(2)))+" JD";

        return "not found";
    }
    public String[] ItemNV(String ItemID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLENAME+" WHERE "+COLUMN_ITEM_ID+" = ?",new String[]{ItemID});
        DecimalFormat df = new DecimalFormat("#.##");

        if(cursor.moveToFirst()) {
            items[0] = cursor.getString(1);
            items[1] = cursor.getString(2);
        }

        return items;

    }

    public boolean modifyItem(String ItemCode,String ItemName, float ItemValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME,ItemName);
        cv.put(COLUMN_PRICE,ItemValue);

        db.update(TABLENAME,cv,COLUMN_ITEM_ID+" =?", new String[]{ItemCode});
        Toast.makeText(context, "modified successfully", Toast.LENGTH_SHORT).show();
        return true;
    }



}
