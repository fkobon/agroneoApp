package com.agroneo.app.discuss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.agroneo.app.R;
import com.agroneo.app.discuss.threads.ThreadsAdaptater;
import com.agroneo.app.ui.ActionBarCtl;

public class DiscussFragment extends Fragment {
    private ActionBarCtl actionbar;

    public DiscussFragment setActionbar(ActionBarCtl actionbar) {
        this.actionbar = actionbar;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListView listView = (ListView) inflater.inflate(R.layout.discuss, container, false);
        ProgressBar loading = (ProgressBar) inflater.inflate(R.layout.progress, listView, false);

        ThreadsAdaptater adaptater = new ThreadsAdaptater(getContext(), listView, loading, "");
        listView.setAdapter(adaptater);

        return listView;
    }
}
