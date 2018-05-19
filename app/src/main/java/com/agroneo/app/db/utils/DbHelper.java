package com.agroneo.app.db.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.agroneo.app.db.Forums;
import com.agroneo.app.db.Threads;
import com.agroneo.app.utils.Json;

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
        try {
            db.execSQL("DROP TABLE Threads");
        } catch (Exception e) {
        }
        try {
            db.execSQL("DROP TABLE Forums");
        } catch (Exception e) {
        }
        db.execSQL((new Threads().createTable()));
        db.execSQL((new Forums().createTable()));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getDiscuss(String parent) {
        String[] projection = Threads.getProjection(Threads.class);
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
