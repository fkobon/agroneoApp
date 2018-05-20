package com.agroneo.app.db.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.agroneo.app.BuildConfig;
import com.agroneo.app.db.Forums;
import com.agroneo.app.db.Threads;
import com.agroneo.app.utils.Json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {

        super(context, "agroneo", null, 1);
        if (BuildConfig.DEBUG) {
            onCreate(getWritableDatabase());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        DbBuilder.createTable(Threads.class, db);
        DbBuilder.createTable(Forums.class, db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getDiscuss(String parent) {
        return query(Threads.class, "parent = ?", new String[]{parent}, "date DESC");
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

    private Cursor query(Class<? extends DbObject> dbobj, String selection, String[] args, String order) {
        return getReadableDatabase().query("threads", getProjection(dbobj), selection, args, null, null, order);
    }

    public static String[] getProjection(Class<? extends DbObject> cls) {
        List<String> projections = new ArrayList<>();
        for (Field field : cls.getFields()) {
            Annotation[] annos = field.getDeclaredAnnotations();
            if (annos.length == 1) {
                if (annos[0].toString().startsWith("@" + Type.class.getName())) {
                    projections.add(field.getName());
                }
            }
        }
        return projections.toArray(new String[0]);
    }
}
