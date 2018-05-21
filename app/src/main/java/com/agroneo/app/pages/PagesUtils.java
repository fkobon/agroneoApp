package com.agroneo.app.pages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.agroneo.app.R;
import com.agroneo.app.api.ApiAgroneo;
import com.agroneo.app.utils.Json;

import java.util.List;

public class PagesUtils {
    private Context context;
    private ScrollView view;
    private LinearLayout content;
    private ProgressBar loading;
    private TextView title;
    private TextView intro;
    private TextView text;
    private ListView childrens;

    public PagesUtils(Context context, LayoutInflater inflater, ViewGroup container) {

        this.context = context;
        this.view = (ScrollView) inflater.inflate(R.layout.pages, container, false);

        this.loading = view.findViewById(R.id.loading);
        this.content = view.findViewById(R.id.content);
        this.title = view.findViewById(R.id.title);
        this.intro = view.findViewById(R.id.intro);
        this.text = view.findViewById(R.id.text);
        this.childrens = view.findViewById(R.id.childrens);


    }

    public void setPage(String url) {
        loading(true);
        new ApiAgroneo(context) {

            @Override
            public void result(Json response) {
                title.setText(response.getString("top_title"));
                intro.setText(response.getString("intro"));
                text.setText(response.getString("text"));
                childrens.setAdapter(new PagesAdapter(context, response.getListJson("childrens")));
                loading(false);
            }

            @Override
            public void error() {
                loading(false);
            }
        }.doGet(url);
    }

    public void loading(boolean load) {
        if (load) {
            content.setVisibility(ProgressBar.GONE);
            loading.setVisibility(ProgressBar.VISIBLE);
        } else {
            content.setVisibility(ProgressBar.VISIBLE);
            loading.setVisibility(ProgressBar.GONE);
        }

    }

    public ScrollView getView() {
        return view;
    }


    public class PagesAdapter extends ArrayAdapter<Json> implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            setPage(v.getTag().toString());
        }

        public PagesAdapter(@NonNull Context context, @NonNull List<Json> items) {
            super(context, R.layout.pages_list, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.pages_list, parent, false);
            }
            Json page = getItem(position);
            TextView text = convertView.findViewById(R.id.title);
            text.setText(page.getString("title"));
            text.setTag(page.getString("url"));
            text.setOnClickListener(this);
            return convertView;
        }
    }

}
