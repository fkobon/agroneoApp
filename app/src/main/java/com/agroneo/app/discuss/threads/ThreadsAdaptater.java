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
import com.agroneo.app.discuss.forums.ForumsDb;
import com.agroneo.app.discuss.threads.ThreadsDb.Threads;
import com.agroneo.app.utils.ImageLoader;
import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.adapter.ListAdapter;
import com.agroneo.app.utils.db.AppDatabase;

public class ThreadsAdaptater extends ListAdapter<Threads> implements ApiResponse, View.OnClickListener {

    private String url;
    private String next;
    private Api api = Api.build(this);

    public ThreadsAdaptater(Context context, ListView listView, String url) {
        super(context, R.layout.discuss_thread_item, listView);
        this.url = url;
        update();
    }

    @Override
    public void showView(View view, Threads thread, boolean isLast) {

        ((TextView) view.findViewById(R.id.title)).setText(thread.title);
        ImageLoader.setRound(thread.user.avatar + "@200x200", (ImageView) view.findViewById(R.id.avatar), R.dimen.avatarDpw);
        if (isLast) {
            update();
        }

        view.setTag(R.id.url, thread.url);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        loadDiscuss(v.getTag(R.id.url).toString());
    }


    public void update() {
        if (next == "") {
            return;
        }
        loading(true);
        api.doGet(url + ((next != null) ? "?paging=" + next : ""));
    }

    @Override
    public void apiResult(Json response) {
        Json posts = response.getJson("posts");
        if (posts != null) {
            next = posts.getJson("paging").getString("next", "");
            ThreadsDb.insertThreads(getContext(), posts.getListJson("result"));
        }

        Json forum = response.getJson("forum");
        if (forum != null) {
            ForumsDb.insertForums(getContext(), forum);
        }
        reloadData();
        loading(false);
    }

    @Override
    public void apiError() {
        loading(false);
    }

    private void loadDiscuss(String url) {
        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("agroneo:" + url)));
    }

    private void reloadData() {
        if (url == null || url.equals("/forum")) {
            setData(AppDatabase.getAppDatabase(getContext()).threadsDao().load());
        } else {
            setData(AppDatabase.getAppDatabase(getContext()).threadsDao().load("%@" + url + "@%"));
        }
    }
}
