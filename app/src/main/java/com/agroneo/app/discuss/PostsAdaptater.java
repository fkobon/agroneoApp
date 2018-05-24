package com.agroneo.app.discuss;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.agroneo.app.api.Api;
import com.agroneo.app.api.ApiResponse;
import com.agroneo.app.discuss.threads.ThreadsDb;
import com.agroneo.app.utils.Json;

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
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    private class Populator implements ApiResponse {

        private Cursor cursor;
        private ThreadsDb db;
        private String parent;
        private Api api = Api.build(this);

        public Populator(String parent) {
            this.parent = parent;
            this.db = new ThreadsDb(context);
            cursor = db.getDiscuss(parent);
            if (cursor == null || cursor.getPosition() < 0) {
                update(null);
            } else {
                PostsAdaptater.this.changeCursor(cursor);
            }

        }


        public void update(String next) {

            String url = "forum" + parent;
            if (next != null) {
                url += "?paging=" + next;
            }


            listView.removeFooterView(loading);

            api.doGet(url);
            listView.addFooterView(loading);
        }

        @Override
        public void apiResult(Json response) {
            Json posts = response.getJson("posts");
            String paging = posts.getJson("paging").getString("next");
            if (posts != null) {
                db.insertDiscuss(posts.getListJson("result"), paging);
            }
            reloadCursor();

            listView.removeFooterView(loading);
        }

        @Override
        public void apiError() {

        }

        private void reloadCursor() {
            PostsAdaptater.this.changeCursor(db.getDiscuss(parent));

        }

    }

}
