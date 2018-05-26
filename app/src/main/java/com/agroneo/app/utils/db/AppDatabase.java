package com.agroneo.app.utils.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.agroneo.app.discuss.forums.ForumsDb.Forums;
import com.agroneo.app.discuss.forums.ForumsDb.ForumsDao;
import com.agroneo.app.discuss.posts.PostsDb.Posts;
import com.agroneo.app.discuss.posts.PostsDb.PostsDao;
import com.agroneo.app.discuss.threads.ThreadsDb.Threads;
import com.agroneo.app.discuss.threads.ThreadsDb.ThreadsDao;

@Database(entities = {Forums.class, Threads.class, Posts.class}, version = 26, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "agroneo").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public abstract ThreadsDao threadsDao();

    public abstract ForumsDao forumsDao();

    public abstract PostsDao postsDao();


}
