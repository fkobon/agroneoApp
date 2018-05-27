package com.agroneo.app.discuss.forums;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
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

import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.db.AppDatabase;
import com.agroneo.app.utils.db.ListTypeConverter;

import java.util.ArrayList;
import java.util.List;

public class ForumsDb {
    public static void insertForums(Context context, Json forumJson) {
        ForumsDao td = AppDatabase.getAppDatabase(context).forumsDao();
        Forums forum = new Forums();

        forum._id = forumJson.getId();
        forum.title = forumJson.getString("title");
        forum.url = forumJson.getString("url");

        for (Json childrenJson : forumJson.getListJson("childrens")) {
            Forums children = new Forums();
            children._id = childrenJson.getId();
            children.title = childrenJson.getString("title");
            children.url = childrenJson.getString("url");
            td.insert(children);

            forum.childrens.add(childrenJson.getId());
        }

        td.insert(forum);

    }

    @Dao
    public interface ForumsDao {

        @Query("SELECT * FROM forums")
        Cursor load();

        @Query("SELECT * FROM forums WHERE _id LIKE :id ORDER BY title ASC")
        List<Forums> load(String id);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Forums... forums);

        @Delete
        void delete(Forums forum);
    }


    @Entity(indices = {@Index("title")})
    public static class Forums {

        @PrimaryKey
        @NonNull
        String _id;
        String title;
        String url;
        @TypeConverters(ListTypeConverter.class)
        List<String> childrens = new ArrayList<>();

    }

}

