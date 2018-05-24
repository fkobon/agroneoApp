package com.agroneo.app.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agroneo.app.api.Api;
import com.agroneo.app.api.ApiResponse;
import com.agroneo.app.ui.ActionBarCtl;
import com.agroneo.app.utils.Json;

public class PagesFragment extends Fragment implements ApiResponse {

    private ActionBarCtl actionbar;
    private PagesView page;
    private Api api = Api.build(this);

    public PagesFragment setActionbar(ActionBarCtl actionbar) {
        this.actionbar = actionbar;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = new PagesView(getContext(), container);

        loadPage("/documents");
        return page;
    }

    public void loadPage(String url) {
        if (page!=null) {
            page.loading(true);
        }
        api.doGet(url);
    }

    @Override
    public void apiResult(Json response) {
        page.setPage(response);
        page.loading(false);
    }

    @Override
    public void apiError() {
        page.loading(false);
    }
}
