package com.agroneo.app.discuss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.agroneo.app.R;
import com.agroneo.app.ui.ActionBarCtl;

public class DiscussFragment extends Fragment {
    private ActionBarCtl actionbar;

    public DiscussFragment setActionbar(ActionBarCtl actionbar) {
        this.actionbar = actionbar;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.discuss, container, false);
        SectionsPager adapter = new SectionsPager(getFragmentManager());
        ViewPager pager = view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        return view;
    }

    public void load(String path) {
    }
}
