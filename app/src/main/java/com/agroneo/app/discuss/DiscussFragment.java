package com.agroneo.app.discuss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

        ListView view = (ListView) inflater.inflate(R.layout.discuss, container, false);
        ThreadsAdaptater adaptater = new ThreadsAdaptater(getContext(), "");
        view.setAdapter(adaptater);
        return view;
    }
}
