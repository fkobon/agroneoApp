package com.agroneo.app.discuss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.agroneo.app.R;
import com.agroneo.app.discuss.posts.PostsAdaptater;
import com.agroneo.app.discuss.threads.ThreadsAdaptater;
import com.agroneo.app.ui.ActionBarCtl;
import com.agroneo.app.utils.adapter.ListAdapter;

public class DiscussFragment extends Fragment {
    private ActionBarCtl actionbar;
    private ListAdapter adaptater;
    private ListView listView;

    public DiscussFragment setActionbar(ActionBarCtl actionbar) {
        this.actionbar = actionbar;
        return this;
    }

    //TODO: pas bon avec un seul fragment, il faut que le back ne ramène pas en haut

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listView = (ListView) inflater.inflate(R.layout.discuss, container, false);
        adaptater = new ThreadsAdaptater(getContext(), listView, "/forum");
        listView.setAdapter(adaptater);

        return listView;
    }

    public void load(String url) {

        if (url.matches(".*/([0-9A-Z]+)$")) {
            adaptater = new PostsAdaptater(getContext(), listView, url);
        } else {
            adaptater = new ThreadsAdaptater(getContext(), listView, url);
        }

        listView.setAdapter(adaptater);

    }
}
