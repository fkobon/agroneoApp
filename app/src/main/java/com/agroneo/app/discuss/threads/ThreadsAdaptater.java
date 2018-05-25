package com.agroneo.app.discuss.threads;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

/*
        TODO: Gestion du pagingNext bien foireuse
 */
public class ThreadsAdaptater extends CursorAdapter {

    private Context context;
    private Populator populator;
    private ListView listView;
    private ProgressBar loading;

    public ThreadsAdaptater(Context context, ListView listView, ProgressBar loading, String url) {
        super(context, null, 0);
        this.context = context;
        this.listView = listView;
        this.loading = loading;
        populator = new Populator(url);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.discuss_thread_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.title)).setText(cursor.getString(cursor.getColumnIndex("title")));
        ImageLoader.setRound(cursor.getString(cursor.getColumnIndex("user_avatar")) + "@200x200", (ImageView) view.findViewById(R.id.avatar), R.dimen.avatarDpw);
        if (cursor.isLast()) {
            String next = cursor.getString(cursor.getColumnIndex("next"));
            if (next != null) {
                populator.update(next);
            }
        }

        final String url = cursor.getString(cursor.getColumnIndex("url"));
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadDiscuss(url);
            }
        });
    }

    private void loadDiscuss(String url) {
        Intent newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("agroneo:" + url));
        context.startActivity(newIntent);

    }


    private class Populator implements ApiResponse {

        private String url;
        private Api api = Api.build(this);

        public Populator(String url) {
            this.url = url;
            if (getCursor() == null || getCursor().getPosition() < 0) {
                update(null);
            } else {
                reloadCursor();
            }

        }


        public void update(String next) {

            String url = this.url;
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
                ThreadsDb.insertDiscuss(context, posts.getListJson("result"), paging);
            }
            reloadCursor();

            listView.removeFooterView(loading);
        }

        @Override
        public void apiError() {

        }

        private void reloadCursor() {
            if (url == null || url.equals("/forum")) {
                changeCursor(AppDatabase.getAppDatabase(context).threadsDao().load());
            } else {
                changeCursor(AppDatabase.getAppDatabase(context).threadsDao().load("%@" + url + "@%"));
            }

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
