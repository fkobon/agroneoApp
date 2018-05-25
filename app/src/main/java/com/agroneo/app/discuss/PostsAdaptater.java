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
import com.agroneo.app.utils.ImageLoader;
import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.db.AppDatabase;

public class PostsAdaptater extends CursorAdapter {

    private Context context;
    private Populator populator;
    private Cursor cursor;
    private ListView listView;
    private ProgressBar loading;

    public PostsAdaptater(Context context, ListView listView, ProgressBar loading, String url) {
        super(context, null, 0);
        this.context = context;
        this.listView = listView;
        this.loading = loading;

        populator = new Populator(url);

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

        private Api api = Api.build(this);
        private String url;

        public Populator(String url) {
            this.url = url;
            reloadCursor();
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
            if (response != null) {
                PostsDb.insertPosts(context, response);

                reloadCursor();
            }

            listView.removeFooterView(loading);
        }

        @Override
        public void apiError() {

        }

        private void reloadCursor() {
            closeCursor();
            cursor = AppDatabase.getAppDatabase(context).postsDao().load(url);
            changeCursor(cursor);
        }

    }

    private void closeCursor() {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        closeCursor();
        super.finalize();
    }
}
