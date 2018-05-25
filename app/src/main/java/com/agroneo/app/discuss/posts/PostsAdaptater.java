package com.agroneo.app.discuss.posts;

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
import com.agroneo.app.utils.db.AppDatabase;

public class PostsAdaptater extends CursorAdapter {

    private Context context;
    private Populator populator;
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
        View view = LayoutInflater.from(context).inflate(R.layout.discuss_post_item, parent, false);
        return view;
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
            if (getCursor() == null || getCursor().getPosition() < 0) {
                update(url);
            } else {
                reloadCursor();
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
            listView.removeFooterView(loading);

        }

        private void reloadCursor() {
            String thread = url.replaceAll(".*/([A-Z0-9]+)$", "$1");
            changeCursor(AppDatabase.getAppDatabase(context).postsDao().load(thread));
            notifyDataSetChanged();
        }

    }

    @Override
    protected void finalize() throws Throwable {
        if (getCursor() != null && !getCursor().isClosed()) {
            getCursor().close();
        }
        super.finalize();
    }
}
