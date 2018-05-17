package com.agroneo.app.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agroneo.app.ui.ActionBarCtl;

public class PagesFragment extends Fragment {

    private ActionBarCtl actionbar;

    public PagesFragment setActionbar(ActionBarCtl actionbar) {
        this.actionbar = actionbar;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView view = new TextView(getContext());
        view.setText("Documents");
        return view;
    }

}