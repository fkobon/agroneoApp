package com.agroneo.app.utils.db;

import android.content.Context;
import android.database.Cursor;

import com.agroneo.app.discuss.threads.ThreadsDb.Threads;
import com.agroneo.app.discuss.threads.ThreadsDb.ThreadsDao;
import com.agroneo.app.utils.Json;

import java.util.List;

public class DbHelper {

    private Context context;

    public DbHelper(Context context) {
        this.context = context;
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
        db.destroyInstance();
    }

    public Cursor getDiscuss(String parent) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.threadsDao().load();
    }
}
