package com.mcvm_app.gamekeyprices_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavDB extends SQLiteOpenHelper {

    //sets what table contains
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "FavGamesDB";
    private static String TABLE_NAME = "favoriteTable";
    public static String KEY_ID = "plain";
    public static String GAME_TITLE = "title";
    public static String ITEM_IMAGE = "image";
    public static String FAVORITE_STATUS = "fStatus";

    //creates table
    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " TEXT," + GAME_TITLE+ " TEXT,"
            + ITEM_IMAGE + " TEXT," + FAVORITE_STATUS+" TEXT)";

    public FavDB(Context context) { super(context,DATABASE_NAME,null,DB_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    // create empty table
    public void insertEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
    }

    // insert data into database
    public void insertIntoTheDatabase(String plain, String item_title, String item_image, String fav_status) {
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, plain);
        cv.put(GAME_TITLE, item_title);
        cv.put(ITEM_IMAGE, item_image);
        cv.put(FAVORITE_STATUS, fav_status);
        db.insert(TABLE_NAME,null, cv);
        Log.d("FavDB Status", item_title + ", favstatus - "+fav_status+" - . " + cv);
    }

    //read all data
    public Cursor read_all_data(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
           String sql = "select * from " + TABLE_NAME + " where " + KEY_ID +"='"+id+"'";
        return db.rawQuery(sql,null,null);
    }

    // remove line from database
    public void remove_fav(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String sql = "UPDATE " + TABLE_NAME + " SET  "+ FAVORITE_STATUS+" ='0' WHERE "+KEY_ID+"='"+id+"'";
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE "+KEY_ID+"='"+id+"'";
        db.execSQL(sql);
        Log.d("remove", id.toString());

    }

    // select all favorite list
    public Cursor select_all_favorite_list() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+FAVORITE_STATUS+" ='1'";
        return db.rawQuery(sql,null,null);
    }

}
