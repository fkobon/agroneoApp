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
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.agroneo.app.users.UsersData;
import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.db.AppDatabase;

import java.util.List;

public class ThreadsDb {


    private Context context;

    public ThreadsDb(Context context) {
        this.context = context;
    }

    public void insertDiscuss(List<Json> threadsJson, String next) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        ThreadsDao td = db.threadsDao();
        if (threadsJson == null) {
            return;
        }
        for (Json threadJson : threadsJson) {
            Threads threadDb = new Threads();
            threadDb._id = threadJson.getId();
            threadDb.title = threadJson.getString("title");
            threadDb.url = threadJson.getString("url");
            threadDb.date = threadJson.parseDate("date").getTime();
            Json last = threadJson.getJson("last");
            threadDb.last.date = last.parseDate("date").getTime();
            threadDb.last._id = last.getId();

            Json last_user = last.getJson("user");
            threadDb.last.user._id = last_user.getId();
            threadDb.last.user.name = last_user.getString("name");
            threadDb.last.user.avatar = last_user.getString("avatar");

            Json user = threadJson.getJson("user");
            threadDb.user._id = user.getId();
            threadDb.user.name = user.getString("name");
            threadDb.user.avatar = user.getString("avatar");

            threadDb.replies = threadJson.getInteger("replies");
            List<Json> parents = threadJson.getListJson("parents");
            if (parents == null || parents.size() == 0) {
                threadDb.parent = "";
            } else {
                threadDb.parent = parents.get(0).getId();
            }
            threadDb.next = next;

            td.insertAll(threadDb);
        }
        db.destroyInstance();
    }

    public Cursor getDiscuss(String parent) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.threadsDao().load();
    }

    @Dao
    public interface ThreadsDao {

        @Query("SELECT * FROM threads ORDER BY last_date DESC")
        Cursor load();

        @Query("SELECT * FROM threads WHERE parent = :parent ORDER BY last_date DESC")
        Cursor load(String parent);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(Threads... threads);

        @Delete
        void delete(Threads thread);
    }

    @Entity(indices = {@Index("date"), @Index("last_date")})
    public static class Threads {

        @PrimaryKey
        @NonNull
        public String _id;

        public String title;

        public String url;

        public long date;

        @Embedded(prefix = "last_")
        public ThreadsLast last = new ThreadsLast();

        @Embedded(prefix = "user_")
        public UsersData user = new UsersData();

        public int replies;

        public String parent;

        public String next;


    }

    public static class ThreadsLast {

        public String _id;
        public long date;
        @Embedded(prefix = "user_")
        public UsersData user = new UsersData();

    }

}

