package com.agroneo.app.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agroneo.app.ui.ActionBarCtl;

public class PagesFragment extends Fragment {

    private ActionBarCtl actionbar;
    private PagesUtils page;

    public PagesFragment setActionbar(ActionBarCtl actionbar) {
        this.actionbar = actionbar;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = new PagesUtils(getContext(), inflater, container);
        loadPage("/plantes/legumes/feuilles/poireau");
        return page.getView();
    }

    public void loadPage(String url) {
        page.load(url);
    }

}
