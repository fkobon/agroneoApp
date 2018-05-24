package com.agroneo.app.discuss.posts;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.agroneo.app.users.UsersData;
import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class PostsDb {

    private Context context;

    public PostsDb(Context context) {
        this.context = context;
    }

    public Cursor getPosts(String thread) {

        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.postsDao().load(thread);
    }


    public void insertPosts(List<Json> threadsJson) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        PostsDao td = db.postsDao();
        if (threadsJson == null) {
            return;
        }
        for (Json postJson : threadsJson) {
            Posts postDb = new Posts();
            postDb._id = postJson.getId();
            postDb.url = postJson.getString("url");
            postDb.date = postJson.parseDate("date").getTime();
            postDb.update = postJson.parseDate("update").getTime();
            postDb.changes = postJson.getInteger("changes");
            postDb.text = postJson.getString("text");

            Json user = postJson.getJson("user");
            postDb.user._id = user.getId();
            postDb.user.name = user.getString("name");
            postDb.user.avatar = user.getString("avatar");

            postDb.coins = postJson.getInteger("coins");

            for (Json comment : postJson.getListJson("comments")) {
                Comments com = new Comments();
                com.date = comment.parseDate("date").getTime();
                UsersData usercom = new UsersData();
                Json usercomdb = comment.getJson("user");
                usercom._id = usercomdb.getId();
                usercom.name = usercomdb.getString("name");
                usercom.avatar = usercomdb.getString("avatar");
                com.user = usercom;
                com.text = comment.getString("text");
                postDb.comments.add(com);
            }


            td.insertAll(postDb);
        }
        db.destroyInstance();
    }

    @Dao
    public interface PostsDao {


        @Query("SELECT * FROM posts WHERE url = :url ORDER BY date ASC")
        Cursor load(String url);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(Posts... posts);

        @Delete
        void delete(Posts post);
    }

    @Entity(indices = {@Index("url"), @Index("date")})
    public static class Posts {

        @PrimaryKey
        @NonNull
        String _id;
        String url;
        long date;
        long update;
        int changes;
        String text;
        @Embedded(prefix = "user_")
        UsersData user = new UsersData();
        int coins;
        @Embedded(prefix = "comments_")
        ArrayList<Comments> comments = new ArrayList();

    }

    public class Comments {

        @Embedded(prefix = "user__")
        UsersData user;
        String text;
        long date;
    }


}
