package com.webapp.oasis.SqlliteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.webapp.oasis.Model.AdminItemListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "order.db";
    public static final String TABLE_NAME = "item_table";
    public static final String COL_2 = "ItemID";
    public static final String COL_3 = "BrandName";
    public static final String COL_4 = "ItemName";
    public static final String COL_5 = "Price";
    public static final String COL_6 = "ItemQty";
    public static final String COL_7 = "Qty";

    private Context mContext;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ItemID TEXT , BrandName TEXT, ItemName TEXT, Price TEXT, ItemQty TEXT, Qty TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean inertData(String itemId, String brandName, String itemName, String price, String itemqty, String qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, itemId);
        contentValues.put(COL_3, brandName);
        contentValues.put(COL_4, itemName);
        contentValues.put(COL_5, price);
        contentValues.put(COL_6, itemqty);
        contentValues.put(COL_7, qty);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public String getTableAsString() {
        Log.d("TAG", "getTableAsString called");

        SQLiteDatabase db = this.getWritableDatabase();
        String tableString = String.format("Table %s:\n", TABLE_NAME);
        Cursor allRows = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (allRows.moveToFirst()) {
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name : columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";


            } while (allRows.moveToNext());

        }

        return tableString;
    }

    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    public boolean updateDatas(String itemId, String brandName, String itemName, String price, String itemqty, String qty) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, itemId);
        contentValues.put(COL_3, brandName);
        contentValues.put(COL_4, itemName);
        contentValues.put(COL_5, price);
        contentValues.put(COL_6, itemqty);
        contentValues.put(COL_7, qty);
        db.update(TABLE_NAME, contentValues, "ItemID = ?", new String[]{itemId});
        return true;
    }

    public boolean hasObject(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + " =?";
        Cursor cursor = db.rawQuery(selectString, new String[]{id});
        boolean hasObject = false;
        if (cursor.moveToFirst()) {
            hasObject = true;
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
            Log.d("TAG", String.format("%d records found", count));
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }
    public JSONArray getResults()
    {
        Context context = mContext;
        String myPath = String.valueOf(context.getDatabasePath("order.db"));// Set path to your database
        String myTable = DatabaseHelper.TABLE_NAME;//Set name of your table
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        String searchQuery = "SELECT  * FROM " + myTable;
        Cursor cursor = myDataBase.rawQuery(searchQuery, null );
        org.json.JSONArray resultSet     = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();

        return resultSet;
    }
    public Integer deleteDatas(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABLE_NAME,"ItemID = ?",new String[] { id });
    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }

    public List<AdminItemListModel> getItemData() {
        List<AdminItemListModel> model = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AdminItemListModel emodel = new AdminItemListModel();
                emodel.setItemID(cursor.getString(0));
                emodel.setBrandName(cursor.getString(1));
                emodel.setItemName(cursor.getString(2));
                emodel.setPrice(cursor.getString(3));
                emodel.setItemQty(cursor.getString(4));
                emodel.setQty(cursor.getString(5));
                model.add(emodel);
            } while (cursor.moveToNext());
        }

        db.close();
        return model;
    }
}
