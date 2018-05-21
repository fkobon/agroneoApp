package com.agroneo.app.pages;

import com.agroneo.app.api.ApiAgroneo;
import com.agroneo.app.utils.Json;

class PagesLoader {
    public static void load(String url, final PagesUtils utils) {

        utils.loading(true);
        new ApiAgroneo(utils.getContext()) {

            @Override
            public void result(Json response) {
                utils.setPage(response);
                utils.loading(false);
            }

            @Override
            public void error() {
                utils.loading(false);
            }
        }.doGet(url);
    }
}
