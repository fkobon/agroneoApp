package com.agroneo.app.discuss.forums;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.agroneo.app.R;
import com.agroneo.app.api.Api;
import com.agroneo.app.api.ApiResponse;
import com.agroneo.app.discuss.threads.ThreadsDb;
import com.agroneo.app.discuss.threads.ThreadsDb.Threads;
import com.agroneo.app.utils.ImageLoader;
import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.adapter.ListAdapter;
import com.agroneo.app.utils.db.AppDatabase;

import java.util.List;

public class ForumsFragment extends Fragment {
    private ListView listView;
    private String url;
    private ForumsPager pager;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listView = new ListView(getContext());
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        listView.setAdapter(new ThreadsAdaptater(getContext(), listView, pager, url));
        return listView;
    }


    public void setUrl(String url, ForumsPager pager) {
        this.url = url;
        this.pager = pager;
    }

    private class ThreadsAdaptater extends ListAdapter<ThreadsDb.Threads> implements ApiResponse, View.OnClickListener {

        private String url;
        private String _id;
        private String next;
        private Api api = Api.build(this);
        private ForumsPager pager;

        public ThreadsAdaptater(Context context, ListView listView, ForumsPager pager, String url) {
            super(context, R.layout.discuss_thread_item, listView);
            this.pager = pager;
            this.url = url;
            updateChildrens();
            update();
        }

        public void updateChildrens() {
            List<ForumsDb.Forums> arbo = AppDatabase.getAppDatabase(getContext()).forumsDao().arbo(_id);
            pager.childrens(arbo);
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
            Json forum = response.getJson("forum");
            if (forum != null) {
                _id = forum.getId();
                title = forum.getString("title");
                ForumsDb.insertForums(getContext(), forum);
            }
            Json posts = response.getJson("posts");
            if (posts != null) {
                next = posts.getJson("paging").getString("next", "");
                ThreadsDb.insertThreads(getContext(), posts.getListJson("result"));
            }

            reloadData();
            loading(false);
            updateChildrens();
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
}