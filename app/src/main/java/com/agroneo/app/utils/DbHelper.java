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
        String[] projection = {"_id", "title", "date", "last_date", "last_id", "last", "user_id", "user", "replies", "parent"};
        String selection = "parent = ?";
        String[] args = {parent};
        return getReadableDatabase().query("threads", projection, selection, args, null, null, "date DESC");
    }

    public void insertDiscuss(List<Json> posts) {
        SQLiteDatabase db = getWritableDatabase();
        for (Json post : posts) {
            ContentValues values = new ContentValues();
            values.put("_id", post.getId());
            values.put("title", post.getString("title"));
            values.put("date", post.parseDate("date").getTime());
            Json last = post.getJson("last");
            values.put("last_date", last.parseDate("date").getTime());
            values.put("last_id", last.getId());
            values.put("last", last.toString());

            Json user = post.getJson("user");
            values.put("user_id", user.getId());
            values.put("user", user.toString());

            values.put("replies", post.getInteger("replies"));
            List<Json> parents = post.getListJson("parents");
            if (parents == null || parents.size() == 0) {
                values.put("parent", "");
            } else {
                values.put("parent", parents.get(0).getId());
            }

            db.insertWithOnConflict("threads", "id", values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
    }
}
