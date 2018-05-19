package com.agroneo.app.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {

        super(context, "agroneo", null, 1);
        onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE threads");
        } catch (Exception e) {
        }
        try {
            db.execSQL("DROP TABLE forums");
        } catch (Exception e) {
        }

        String threads = "CREATE TABLE threads ( " +
                " _id VARCHAR2(26)," +
                " title VARCHAR2(255), " +
                " date INT," +
                " last_date INT," +
                " last_id VARCHAR2(26)," +
                " last TEXT," +
                " user_id VARCHAR2(26)," +
                " user_avatar TEXT," +
                " user TEXT," +
                " replies INT," +
                " parent VARCHAR2(26) )";
        db.execSQL(threads);


        String forums = "CREATE TABLE forums ( " +
                " _id VARCHAR2(26)," +
                " title VARCHAR2(255), " +
                " parent VARCHAR2(26) )";
        db.execSQL(forums);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getDiscuss(String parent) {
        String[] projection = {"_id", "title", "date", "last_date", "last_id", "last", "user_id","user_avatar", "user", "replies", "parent"};
        String selection = "parent = ?";
        String[] args = {parent};
        return getReadableDatabase().query("threads", projection, selection, args, null, null, "date DESC");
    }

    public void insertDiscuss(List<Json> threads) {
        SQLiteDatabase db = getWritableDatabase();
        for (Json thread : threads) {
            ContentValues values = new ContentValues();
            values.put("_id", thread.getId());
            values.put("title", thread.getString("title"));
            values.put("date", thread.parseDate("date").getTime());
            Json last = thread.getJson("last");
            values.put("last_date", last.parseDate("date").getTime());
            values.put("last_id", last.getId());
            values.put("last", last.toString());

            Json user = thread.getJson("user");
            values.put("user_id", user.getId());
            values.put("user_avatar", user.getString("avatar"));
            values.put("user", user.toString());

            values.put("replies", thread.getInteger("replies"));
            List<Json> parents = thread.getListJson("parents");
            if (parents == null || parents.size() == 0) {
                values.put("parent", "");
            } else {
                values.put("parent", parents.get(0).getId());
            }

            db.insertWithOnConflict("threads", "_id", values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
    }
}
