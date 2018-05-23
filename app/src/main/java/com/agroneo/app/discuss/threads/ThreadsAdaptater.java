package com.agroneo.app.discuss.threads;

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
import com.agroneo.app.utils.ImageLoader;
import com.agroneo.app.utils.Json;

public class ThreadsAdaptater extends CursorAdapter {

    private Populator populator;
    private ListView listView;
    private ProgressBar loading;
    private Context context;

    public ThreadsAdaptater(Context context, ListView listView, ProgressBar loading, String parent) {
        super(context, null, 0);
        this.context = context;
        this.listView = listView;
        this.loading = loading;
        populator = new Populator(parent);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.discuss_forum, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.title)).setText(cursor.getString(cursor.getColumnIndex("title")));
        ImageLoader.setRound(cursor.getString(cursor.getColumnIndex("user_avatar")) + "@200x200", (ImageView) view.findViewById(R.id.avatar), R.dimen.avatarDpw);
        if (cursor.isLast()) {
            String next = cursor.getString(cursor.getColumnIndex("next"));
            if (next != null) {
                populator.update(next);
            }
        }
    }

    private class Populator implements ApiResponse {

        private Cursor cursor;
        private ThreadsDb db;
        private String parent;
        private Api api;

        public Populator(String parent) {
            this.parent = parent;
            this.db = new ThreadsDb(context);
            cursor = db.getDiscuss(parent);
            if (cursor == null || cursor.getPosition() < 0) {
                update(null);
            } else {
                ThreadsAdaptater.this.changeCursor(cursor);
            }

        }


        public void update(String next) {

            String url = "forum" + parent;
            if (next != null) {
                url += "?paging=" + next;
            }


            listView.removeFooterView(loading);
            if (api != null) {
                api.cancel(true);
            }
            api = Api.build(this);
            api.doGet(url);
            listView.addFooterView(loading);
        }

        @Override
        public void apiResult(Json response) {
            Json posts = response.getJson("posts");
            String paging = posts.getJson("paging").getString("next");
            if (posts != null) {
                db.insertDiscuss(posts.getListJson("apiResult"), paging);
            }
            reloadCursor();

            listView.removeFooterView(loading);
        }

        @Override
        public void apiError() {

        }

        private void reloadCursor() {
            ThreadsAdaptater.this.changeCursor(db.getDiscuss(parent));

        }

    }

}
