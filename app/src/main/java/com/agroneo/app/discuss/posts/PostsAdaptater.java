package com.agroneo.app.discuss.posts;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.agroneo.app.R;
import com.agroneo.app.api.Api;
import com.agroneo.app.api.ApiResponse;
import com.agroneo.app.discuss.posts.PostsDb.Posts;
import com.agroneo.app.utils.ImageLoader;
import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.adapter.ListAdapter;
import com.agroneo.app.utils.db.AppDatabase;

public class PostsAdaptater extends ListAdapter<Posts> {

    private Populator populator;

    public PostsAdaptater(Context context, ListView listView, String url) {
        super(context, R.layout.discuss_post_item, listView);
        populator = new Populator(url);
    }

    @Override
    public void showView(View view, Posts post, boolean isLast) {
        ((TextView) view.findViewById(R.id.content)).setText(post.text);
        ImageLoader.setRound(post.user.avatar + "@200x200", (ImageView) view.findViewById(R.id.avatar), R.dimen.avatarDpw);
    }


    private class Populator implements ApiResponse {

        private Api api = Api.build(this);
        private String url;

        public Populator(String url) {
            this.url = url;
            if (getCount() == 0) {
                update(url);
            } else {
                reloadCursor();
            }
        }


        public void update(String url) {
            api.doGet(url);
            loading(true);
        }

        @Override
        public void apiResult(Json response) {
            if (response != null) {
                PostsDb.insertPosts(getContext(), response);
                reloadCursor();
            }
            loading(false);
        }

        @Override
        public void apiError() {
            loading(false);
        }

        private void reloadCursor() {
            String thread = url.replaceAll(".*/([A-Z0-9]+)$", "$1");
            setData(AppDatabase.getAppDatabase(getContext()).postsDao().load(thread));
        }

    }
}
