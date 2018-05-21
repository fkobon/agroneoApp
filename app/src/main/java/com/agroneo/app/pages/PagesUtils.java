package com.agroneo.app.pages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.agroneo.app.R;
import com.agroneo.app.api.ApiAgroneo;
import com.agroneo.app.utils.Json;

class PagesUtils {
    private Context context;
    private LinearLayout view;
    private TextView title;
    private TextView intro;
    private TextView text;
    private ListView childrens;

    public PagesUtils(Context context, LayoutInflater inflater, ViewGroup container) {

        this.context = context;
        this.view = (LinearLayout) inflater.inflate(R.layout.pages, container, false);

        title = view.findViewById(R.id.title);
        intro = view.findViewById(R.id.intro);
        text = view.findViewById(R.id.text);
        childrens = view.findViewById(R.id.childrens);


    }

    public void setPage(String url) {

        new ApiAgroneo(context) {

            @Override
            public void result(Json response) {
                title.setText(response.getString("top_title"));
                intro.setText(response.getString("intro"));
                text.setText(response.getString("text"));
            }
        }.doGet(url);
    }

    public LinearLayout getView() {
        return view;
    }
}
