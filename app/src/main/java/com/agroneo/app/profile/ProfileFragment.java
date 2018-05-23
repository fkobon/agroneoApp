package com.agroneo.app.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agroneo.app.ui.ActionBarCtl;

public class ProfileFragment extends Fragment {

    private ActionBarCtl actionbar;

    public ProfileFragment setActionbar(ActionBarCtl actionbar) {
        this.actionbar = actionbar;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView view = new TextView(getContext());
        view.setText("Profile");
        return view;
    }

}
