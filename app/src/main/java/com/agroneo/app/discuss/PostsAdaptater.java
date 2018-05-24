package com.agroneo.app.discuss;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agroneo.app.R;
import com.agroneo.app.api.Api;
import com.agroneo.app.api.ApiResponse;
import com.agroneo.app.discuss.posts.PostsDb;
import com.agroneo.app.discuss.threads.ThreadsDb;
import com.agroneo.app.utils.ImageLoader;
import com.agroneo.app.utils.Json;

import java.util.List;

public class PostsAdaptater extends CursorAdapter {

    private Populator populator;
    private ListView listView;
    private ProgressBar loading;
    private Context context;

    public PostsAdaptater(Context context, ListView listView, ProgressBar loading, String url) {
        super(context, null, 0);
        this.context = context;
        this.listView = listView;
        this.loading = loading;
        populator = new Populator(url);

    }

    public PostsAdaptater(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.discuss_post_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.content)).setText(cursor.getString(cursor.getColumnIndex("text")));
        ImageLoader.setRound(cursor.getString(cursor.getColumnIndex("user_avatar")) + "@200x200", (ImageView) view.findViewById(R.id.avatar), R.dimen.avatarDpw);

    }

    private class Populator implements ApiResponse {

        private Cursor cursor;
        private PostsDb db;
        private Api api = Api.build(this);
        String url;

        public Populator(String url) {
            this.url = url;
            this.db = new PostsDb(context);
            cursor = db.getPosts(url);
            if (cursor == null || cursor.getPosition() < 0) {
                update(url);
            } else {
                PostsAdaptater.this.changeCursor(cursor);
            }

        }


        public void update(String url) {
            listView.removeFooterView(loading);
            api.doGet(url);
            listView.addFooterView(loading);
        }

        @Override
        public void apiResult(Json response) {
            List<Json> posts = response.getListJson("posts");
            if (posts != null) {
                db.insertPosts(posts);
            }
            reloadCursor();

            listView.removeFooterView(loading);
        }

        @Override
        public void apiError() {

        }

        private void reloadCursor() {
            PostsAdaptater.this.changeCursor(db.getPosts(url));

        }

    }

}
