package com.agroneo.app.discuss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.agroneo.app.R;
import com.agroneo.app.discuss.posts.PostsAdaptater;
import com.agroneo.app.discuss.threads.ThreadsAdaptater;
import com.agroneo.app.ui.ActionBarCtl;

public class DiscussFragment extends Fragment {
    private ActionBarCtl actionbar;
    private CursorAdapter adaptater;
    private ListView listView;
    private ProgressBar loading;

    public DiscussFragment setActionbar(ActionBarCtl actionbar) {
        this.actionbar = actionbar;
        return this;
    }

    //TODO: pas bon avec un seul fragment, il faut que le back ne ramène pas en haut

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listView = (ListView) inflater.inflate(R.layout.discuss, container, false);
        loading = (ProgressBar) inflater.inflate(R.layout.progress, listView, false);

        adaptater = new ThreadsAdaptater(getContext(), listView, loading, "/forum");
        listView.setAdapter(adaptater);

        return listView;
    }

    public void load(String url) {

        if (url.matches(".*/([0-9A-Z]+)$")) {
            adaptater = new PostsAdaptater(getContext(), listView, loading, url);
        } else {
            adaptater = new ThreadsAdaptater(getContext(), listView, loading, url);
        }

        listView.setAdapter(adaptater);

    }
}
