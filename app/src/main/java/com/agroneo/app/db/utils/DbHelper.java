package com.agroneo.app.db.utils;

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
        String selection = "parent = ?";
        String[] args = {parent};
        return getReadableDatabase().query("threads", Threads.getProjection(Threads.class), selection, args, null, null, "date DESC");
    }

    public void insertDiscuss(List<Json> threadsJson) {
        SQLiteDatabase db = getWritableDatabase();
        for (Json threadJson : threadsJson) {
            Threads threadDb = new Threads();
            threadDb._id = threadJson.getId();
            threadDb.title = threadJson.getString("title");
            threadDb.date = threadJson.parseDate("date");
            Json last = threadJson.getJson("last");
            threadDb.last_date = last.parseDate("date");
            threadDb.last_id = last.getId();

            Json user = threadJson.getJson("user");
            threadDb.user_id = user.getId();
            threadDb.user_avatar = user.getString("avatar");

            threadDb.replies = threadJson.getInteger("replies");
            List<Json> parents = threadJson.getListJson("parents");
            if (parents == null || parents.size() == 0) {
                threadDb.parent = "";
            } else {
                threadDb.parent = parents.get(0).getId();
            }

            db.insertWithOnConflict("threads", "_id", threadDb.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
    }
}
