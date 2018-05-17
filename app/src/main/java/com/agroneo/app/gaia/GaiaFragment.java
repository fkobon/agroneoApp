package com.agroneo.app.gaia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agroneo.app.ui.ActionBarCtl;

public class GaiaFragment extends Fragment {


    private ActionBarCtl actionbar;

    public GaiaFragment setActionbar(ActionBarCtl actionbar) {

        this.actionbar = actionbar;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GaiaMapView map = new GaiaMapView(container.getContext(), savedInstanceState);

        if (actionbar != null) {
            map.setActionbar(actionbar);
        }
        return map;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}