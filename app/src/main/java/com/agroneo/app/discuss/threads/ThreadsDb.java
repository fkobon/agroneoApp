package com.agroneo.app.discuss.threads;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.agroneo.app.users.UsersData;
import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.db.AppDatabase;
import com.agroneo.app.utils.db.ListTypeConverter;

import java.util.ArrayList;
import java.util.List;

public class ThreadsDb {


    private Context context;

    public ThreadsDb(Context context) {
        this.context = context;
    }

    public static void insertDiscuss(Context context, List<Json> threadsJson, String next) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        if (threadsJson == null) {
            return;
        }
        ThreadsDao td = db.threadsDao();
        for (Json threadJson : threadsJson) {
            Threads thread = new Threads();
            thread._id = threadJson.getId();
            thread.title = threadJson.getString("title");
            thread.url = threadJson.getString("url");
            thread.date = threadJson.parseDate("date").getTime();
            Json last = threadJson.getJson("last");
            thread.last.date = last.parseDate("date").getTime();
            thread.last._id = last.getId();

            Json last_user = last.getJson("user");
            thread.last.user._id = last_user.getId();
            thread.last.user.name = last_user.getString("name");
            thread.last.user.avatar = last_user.getString("avatar");

            Json user = threadJson.getJson("user");
            thread.user._id = user.getId();
            thread.user.name = user.getString("name");
            thread.user.avatar = user.getString("avatar");

            thread.replies = threadJson.getInteger("replies");
            for (Json parent : threadJson.getListJson("parents")) {
                thread.parents.add(parent.getString("url"));
            }
            thread.next = next;

            td.insert(thread);
        }
    }

    @Dao
    public interface ThreadsDao {

        @Query("SELECT * FROM threads ORDER BY last_date DESC")
        Cursor load();

        @Query("SELECT * FROM threads WHERE parents LIKE :parent ORDER BY last_date DESC")
        Cursor load(String parent);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Threads thread);

        @Delete
        void delete(Threads thread);

    }

    @Entity(indices = {@Index("date"), @Index("parents"), @Index("last_date")})
    public static class Threads {

        @PrimaryKey
        @NonNull
        String _id;

        String title;

        String url;

        long date;

        @Embedded(prefix = "last_")
        ThreadsLast last = new ThreadsLast();

        @Embedded(prefix = "user_")
        UsersData user = new UsersData();

        int replies;
        @TypeConverters(ListTypeConverter.class)
        List<String> parents = new ArrayList<>();

        String next;


    }

    public static class ThreadsLast {

        String _id;
        long date;
        @Embedded(prefix = "user_")
        UsersData user = new UsersData();

    }


}

