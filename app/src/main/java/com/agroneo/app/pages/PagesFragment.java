package com.agroneo.app.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agroneo.app.api.ApiAgroneo;
import com.agroneo.app.ui.ActionBarCtl;
import com.agroneo.app.utils.Json;

public class PagesFragment extends Fragment {

    private ActionBarCtl actionbar;
    private PagesView page;

    public PagesFragment setActionbar(ActionBarCtl actionbar) {
        this.actionbar = actionbar;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = new PagesView(getContext(), container);
        loadPage("/plantes/legumes/tubercules/pomme-de-terre/alternariose-de-la-pomme-de-terre");

        return page;
    }

    public void loadPage(String url) {

        page.loading(true);

        new ApiAgroneo(getContext()) {

            @Override
            public void result(Json response) {
                page.setPage(response);
                page.loading(false);
            }

            @Override
            public void error() {
                page.loading(false);
            }
        }.doGet(url);
    }

}
