package hyuse.how_much;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by hwang-gyojun on 2015. 4. 30..
 */
public class DBOpenHelper {
    /* Kyojun Hwang code */
    private static final String DATABASE_NAME = "innerDB.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase db;
    private DatabaseHelper db_helper;
    private Context context;


    private String user =
            "create table user("
                    + "region_do text,"
                    + "region_si text not null);";

    private String preference =
            "create table preference("
                    + "item_id integer primary key, "
                    + "item_name text not null);";

    private String num_of_search =
            "create table num_of_search("
                    + "item_id integer primary key, "
                    + "item_name text not null , "
                    + "count integer default 0);";

    private class DatabaseHelper extends SQLiteOpenHelper {

        // 생성자
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(user);
            db.execSQL(preference);
            db.execSQL(num_of_search);
        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + user);
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

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'", null);
        if(!cursor.moveToNext())
            db.execSQL(user);

        cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='preference'", null);
        if(!cursor.moveToNext())
            db.execSQL(preference);

        cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='num_of_search'", null);
        if(!cursor.moveToNext())
            db.execSQL(num_of_search);

        return this;
    }

    public void insertRegion(String region_do, String region_si) {
        Cursor cursor = db.rawQuery("SELECT * FROM user", null);
        if(cursor.moveToNext()) {
            updateRegion(region_do, region_si);
        }
        else {
            db.execSQL("INSERT INTO user VALUES ('" + region_do + "', '" + region_si + "')");
        }
    }

    public void insertPreference(String item_id, String item_name) {
        Cursor cursor = db.rawQuery("SELECT item_id FROM preference WHERE item_id = " + item_id, null);
        if(cursor.moveToNext()) {
            deletePreference(item_id);
        }
        else {
            db.execSQL("INSERT INTO preference VALUES (" + item_id + ", '" + item_name + "')");
        }
    }

    public void insertSearch(String item_id, String item_name) {
        Cursor cursor = db.rawQuery("SELECT count FROM num_of_search WHERE item_id = " + item_id, null);
        if(cursor.moveToNext())
            updateSearch(item_id);
        else
            db.execSQL("INSERT INTO num_of_search VALUES (" + item_id + ", '" + item_name + "', " + 0 + ")");
    }

    public void updateSearch(String item_id) {
        Cursor cursor = db.rawQuery("SELECT count FROM num_of_search WHERE item_id = " + item_id, null);
        if(!cursor.moveToNext())
            return;

        db.execSQL("UPDATE num_of_search SET count = " + (cursor.getInt(0) + 1)
                    +" WHERE item_id = " + item_id);
    }

    public void updateRegion(String region_do, String region_si) {
        Cursor cursor = db.rawQuery("SELECT * FROM user", null);
        if(!cursor.moveToNext())
            return;

        db.execSQL("UPDATE user SET region_do = '" + region_do
                +"', region_si = '" + region_si + "'");
    }

    public void deletePreference(String item_id) {
        db.execSQL("DELETE FROM preference WHERE item_id = " + item_id);
    }

    public String selectPreference() {
        String str = "";

        Cursor cursor =  db.rawQuery("SELECT item_id FROM preference", null);
        if(cursor.moveToNext())
            str += cursor.getInt(0);

        while(cursor.moveToNext()) {
            str += cursor.getInt(0) + ",";
        }

        return str;
    }

    public String selectSearch() {
        String str = "";

        Cursor cursor =  db.rawQuery("SELECT item_id FROM num_of_search WHERE count >= 5", null);
        if(cursor.moveToNext())
            str += cursor.getInt(0);

        while(cursor.moveToNext()) {
            str += cursor.getInt(0) + ",";
        }

        return str;
    }

    public String selectRegion() {
        String str = "";

        Cursor cursor =  db.rawQuery("SELECT * FROM user", null);
        if(cursor.moveToNext())
            str = cursor.getString(0) + " " + cursor.getString(1);

        return str;
    }

    public boolean checkPreference(String item_id) {
        Cursor cursor = db.rawQuery("SELECT item_id FROM preference WHERE item_id = " + item_id, null);
        if(cursor.moveToNext()) {
            return true;
        }
        else {
            return false;
        }

    }

    public void close(){
        db.close();
    }
    /* Kyojun Hwang code end */
}
