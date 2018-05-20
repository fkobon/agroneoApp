package com.agroneo.app.discuss.threads;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.database.Cursor;
import android.support.annotation.NonNull;

public class ThreadsDb {

    @Dao
    public interface ThreadsDao {

        @Query("SELECT * FROM threads")
        Cursor load();

        @Query("SELECT * FROM threads WHERE parent = :parent ORDER BY date DESC")
        Cursor load(String parent);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(Threads... threads);

        @Delete
        void delete(Threads thread);
    }

    @Entity(indices = {@Index("date")})
    public static class Threads {

        @PrimaryKey
        @NonNull
        public String _id;

        public String title;

        public long date;

        public long last_date;

        public String last_id;

        public String user_id;

        public String user_avatar;

        public int replies;

        public String parent;

        public String next;



    }

}

