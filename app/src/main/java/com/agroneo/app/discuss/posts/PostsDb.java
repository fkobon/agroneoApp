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
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.agroneo.app.users.UsersData;
import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class PostsDb {

    public static void insertPosts(Context context, Json thread) {
        AppDatabase db = AppDatabase.getAppDatabase(context);

        List<Json> posts = thread.getListJson("posts");
        if (posts == null) {
            return;
        }
        PostsDao td = db.postsDao();
        for (Json postJson : posts) {
            Posts post = new Posts();
            post._id = postJson.getId();
            post.thread = thread.getId();
            post.url = thread.getString("url");
            post.date = postJson.parseDate("date").getTime();

            post.update = postJson.containsKey("update") ? postJson.parseDate("update").getTime() : postJson.parseDate("date").getTime();
            post.changes = postJson.getInteger("changes");
            post.text = postJson.getString("text");

            Json user = postJson.getJson("user");
            post.user._id = user.getId();
            post.user.name = user.getString("name");
            post.user.avatar = user.getString("avatar");

            post.coins = postJson.getInteger("coins");

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
                post.comments.add(com);
            }

            td.insert(post);
        }

    }

    @Dao
    public interface PostsDao {


        @Query("SELECT * FROM posts WHERE thread = :thread ORDER BY date ASC")
        Cursor load(String thread);


        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Posts post);

        @Delete
        void delete(Posts post);
    }

    @Entity(indices = {@Index("thread"), @Index("date")})
    public static class Posts {

        @PrimaryKey
        @NonNull
        String _id;

        String url;

        @NonNull
        String thread;

        long date;

        long update;

        int changes;

        int coins;

        String text;

        @Embedded(prefix = "user_")
        UsersData user = new UsersData();

        @Embedded(prefix = "comments_")
        ArrayList<Comments> comments = new ArrayList();

    }

    public static class Comments {

        @Embedded(prefix = "user_")
        UsersData user;
        String text;
        long date;
    }


}

