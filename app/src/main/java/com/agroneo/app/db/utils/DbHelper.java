package com.agroneo.app.db.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.agroneo.app.discuss.threads.ThreadsDb.Threads;
import com.agroneo.app.discuss.threads.ThreadsDb.ThreadsDao;
import com.agroneo.app.utils.Json;

import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    Context context;

    public DbHelper(Context context) {
        super(context, "agroneo", null, 1);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertDiscuss(List<Json> threadsJson) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        ThreadsDao td = db.threadsDao();
        for (Json threadJson : threadsJson) {
            Threads threadDb = new Threads();
            threadDb._id = threadJson.getId();
            threadDb.title = threadJson.getString("title");
            threadDb.date = threadJson.parseDate("date").getTime();
            Json last = threadJson.getJson("last");
            threadDb.last_date = last.parseDate("date").getTime();
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
            td.insertAll(threadDb);
        }
        db.close();
    }

    public Cursor getDiscuss(String parent) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.threadsDao().load();
    }
}
