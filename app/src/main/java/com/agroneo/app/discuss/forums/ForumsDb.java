package com.agroneo.app.discuss.forums;

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

public class ForumsDb {

    @Dao
    public interface ForumsDao {

        @Query("SELECT * FROM forums")
        Cursor load();

        @Query("SELECT * FROM forums WHERE parent = :parent ORDER BY title DESC")
        Cursor load(String parent);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(Forums... forums);

        @Delete
        void delete(Forums forum);
    }

    @Entity(indices = {@Index("parent"), @Index("title")})
    public static class Forums {

        @PrimaryKey
        @NonNull
        String _id;

        String title;

        String parent;

    }

}

