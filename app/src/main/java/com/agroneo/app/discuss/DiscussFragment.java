package com.agroneo.app.discuss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        SectionsPager adapter = new SectionsPager(getFragmentManager());
        ViewPager pager = new ViewPager(getContext());
        pager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        pager.setId(R.id.content);
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            pager.setPadding(0, TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics()), 0, 0);

        }
        pager.setAdapter(adapter);

        PagerTabStrip title = new PagerTabStrip(getContext());
        title.setGravity(Gravity.TOP);
        title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        pager.addView(title);

        return pager;
    }

    public void load(String path) {
    }
}
