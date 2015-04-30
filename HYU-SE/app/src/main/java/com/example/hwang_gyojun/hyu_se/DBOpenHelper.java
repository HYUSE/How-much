package com.example.hwang_gyojun.hyu_se;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by hwang-gyojun on 2015. 4. 30..
 */
public class DBOpenHelper {
    private static final String DATABASE_NAME = "innerDB.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase db;
    private DatabaseHelper db_helper;
    private Context context;


    private class DatabaseHelper extends SQLiteOpenHelper {
        private String user =
                "create table user("
                        + "region );";

        private String preference =
                "create table preference("
                        + "item_id integer primary key, "
                        + "item_name text not null);";

        private String num_of_search =
                "create table num_of_search("
                        + "item_id integer primary key, "
                        + "item_name text not null , "
                        + "count integer default 0);";


        // 생성자
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(preference);
            db.execSQL(num_of_search);
        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + preference);
            db.execSQL("DROP TABLE IF EXISTS " + num_of_search);
            onCreate(db);
        }
    }

    public DBOpenHelper(Context context){
        this.context = context;
    }

    public DBOpenHelper open() throws SQLException {
        db_helper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = db_helper.getWritableDatabase();
        return this;
    }

    public void insertPreference(int item_id, String item_name) {
        db.execSQL("INSERT INTO preference VALUES (" + item_id + ", " + item_name + ")");
    }

    public void insertSearch(int item_id, String item_name) {
        db.execSQL("INSERT INTO num_of_search VALUES (" + item_id + ", " + item_name + ", " + 0 + ")");
    }

    public void updateSearch(int item_id) {
        Cursor cursor = db.rawQuery("SELECT count FROM num_of_search", null);
        if(!cursor.moveToNext())
            return;

        db.execSQL("UPDATE num_of_search SET count = " + (cursor.getInt(0) + 1)
                    +" WHERE item_id = " + item_id);
    }

    public void deletePreference(int item_id) {
        db.execSQL("DELETE FROM preference WHERE item_id = " + item_id);
    }

    public void close(){
        db.close();
    }
}
