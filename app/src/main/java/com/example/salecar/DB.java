package com.example.salecar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {
    private static final String session_id = "_id";

    private static final String user_id = "user_id";
    private static final String userEmail = "email";
    private static final String token = "token";

    private static final String status = "status";
    private static final String created_at = "created_at";
    private static final String updated_at = "updated_at";

    private static final String db = "db_sale";
    private static final String sessions = "sessions";
    private static final int VERSION_DB = 1;

    private BDHelper dbHelper;
    private final Context context;
    private SQLiteDatabase sqLiteDatabase;

    private static class BDHelper extends SQLiteOpenHelper {
        public BDHelper(Context context) {
            super(context, db, null, VERSION_DB);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub

            db.execSQL("CREATE TABLE " + sessions + "(" + session_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + user_id + " INTEGER NOT NULL, "
                    + userEmail + " TEXT NOT NULL, "
                    + token + " TEXT NOT NULL, "
                    + status + " TEXT NOT NULL, "
                    + created_at + " TEXT NOT NULL, "
                    + updated_at + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }
    }

    public DB(Context context) {
        this.context = context;
    }

    public DB open() throws Exception {
        dbHelper = new BDHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        // TODO Auto-generated method stub
        dbHelper.close();
    }

    public void createSession(Integer user_id, String userEmail, String token, String date)
            throws SQLException {
        // TODO Auto-generated method stub
        String status = "Active";

        ContentValues cv = new ContentValues();
        cv.put(this.user_id, user_id);
        cv.put(this.userEmail, userEmail);
        cv.put(this.token, token);
        cv.put(this.status, status);
        cv.put(this.created_at, date);
        cv.put(this.updated_at, date);

        sqLiteDatabase.insert(sessions, null, cv);
    }

    public Cursor searchSessionActive() throws SQLException {
        String selectQuery = "SELECT * FROM " + sessions + " WHERE " + status
                + " = 'Active'";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        return cursor;
    }

    public void closeSession(String userEmail) throws SQLException {
        String status = "Inactive";

        ContentValues cv = new ContentValues();
        cv.put(this.status, status);
        sqLiteDatabase.update(sessions, cv, this.userEmail + "='" + userEmail + "'", null);
    }
}
