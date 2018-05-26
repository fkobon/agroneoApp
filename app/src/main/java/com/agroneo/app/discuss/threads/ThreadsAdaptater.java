package com.agroneo.app.discuss.threads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.agroneo.app.R;
import com.agroneo.app.api.Api;
import com.agroneo.app.api.ApiResponse;
import com.agroneo.app.discuss.threads.ThreadsDb.Threads;
import com.agroneo.app.utils.ImageLoader;
import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.adapter.ListAdapter;
import com.agroneo.app.utils.db.AppDatabase;

/*
        TODO: Gestion du pagingNext bien foireuse
 */
public class ThreadsAdaptater extends ListAdapter<Threads> {

    private Populator populator;

    public ThreadsAdaptater(Context context, ListView listView, String url) {
        super(context, R.layout.discuss_thread_item, listView);
        populator = new Populator(url);

    }

    @Override
    public void showView(View view, Threads thread, boolean isLast) {

        ((TextView) view.findViewById(R.id.title)).setText(thread.title);
        ImageLoader.setRound(thread.user.avatar + "@200x200", (ImageView) view.findViewById(R.id.avatar), R.dimen.avatarDpw);
        if (isLast) {
            populator.update();
        }

        final String url = thread.url;
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadDiscuss(url);
            }
        });
    }


    private void loadDiscuss(String url) {
        Intent newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("agroneo:" + url));
        getContext().startActivity(newIntent);
    }

    private class Populator implements ApiResponse {

        private String url;
        private String next;
        private Api api = Api.build(this);

        public Populator(String url) {
            this.url = url;
            if (getCount() == 0) {
                update();
            } else {
                reloadData();
            }
        }

        public void update() {
            String url = this.url;
            if (next == "") {
                return;
            }
            api.doGet(url + ((next != null) ? "?paging=" + next : ""));
            loading(true);
        }

        @Override
        public void apiResult(Json response) {
            Json posts = response.getJson("posts");
            next = posts.getJson("paging").getString("next", "");
            if (posts != null) {
                ThreadsDb.insertDiscuss(getContext(), posts.getListJson("result"));
            }
            reloadData();
            loading(false);
        }

        @Override
        public void apiError() {
            loading(false);
        }

        private void reloadData() {
            if (url == null || url.equals("/forum")) {
                setData(AppDatabase.getAppDatabase(getContext()).threadsDao().load());
            } else {
                setData(AppDatabase.getAppDatabase(getContext()).threadsDao().load("%@" + url + "@%"));
            }
        }
    }
}
