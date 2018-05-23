package com.agroneo.app.pages;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.agroneo.app.R;
import com.agroneo.app.utils.Json;

import java.util.List;

public class PagesView extends LinearLayout {

    private PagesParser pageparser;
    private ScrollView content;
    private ProgressBar loading;
    private LinearLayout childrens;
    private LinearLayout breadcrumb;


    public PagesView(Context context, ViewGroup container) {
        super(context);

        addView(LayoutInflater.from(getContext()).inflate(R.layout.pages, container, false));

        this.loading = findViewById(R.id.loading);
        this.content = findViewById(R.id.content);
        this.breadcrumb = findViewById(R.id.breadcrumb);

        pageparser = new PagesParser((LinearLayout) findViewById(R.id.text));

        this.childrens = findViewById(R.id.childrens);

    }

    public void intent(String url) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("agroneo://" + url));
        getContext().startActivity(intent);
    }

    public void setPage(Json page) {
        pageparser.parse(page);
        setChildrens(page.getListJson("childrens"));
        setBreadcrumb(page.getListJson("breadcrumb"), page.getString("title"));
    }


    public void loading(boolean load) {

        content.scrollTo(0, 0);
        if (load) {
            content.setVisibility(ProgressBar.GONE);
            loading.setVisibility(ProgressBar.VISIBLE);
        } else {
            content.setVisibility(ProgressBar.VISIBLE);
            loading.setVisibility(ProgressBar.GONE);
        }

    }


    public void setBreadcrumb(List<Json> breadcrumbList, String title) {
        breadcrumb.removeAllViews();
        ((HorizontalScrollView) breadcrumb.getParent()).scrollTo(0, 0);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (breadcrumbList == null) {
            return;
        }

        for (Json bread : breadcrumbList) {
            View br = inflater.inflate(R.layout.bread, this, false);
            TextView item = br.findViewById(R.id.bread);
            item.setText(bread.getString("title"));
            final String url = bread.getString("url");
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    intent(url);
                }
            });
            item.setClickable(true);

            breadcrumb.addView(br);
        }

        TextView item = new TextView(getContext());
        item.setText(title);
        breadcrumb.addView(item);
    }

    public void setChildrens(List<Json> childrensList) {
        childrens.removeAllViews();
        if (childrensList == null) {
            return;
        }

        for (Json children : childrensList) {
            View item = LayoutInflater.from(getContext()).inflate(R.layout.pages_list, this, false);
            TextView text = item.findViewById(R.id.title);
            text.setText(children.getString("title"));
            final String url = children.getString("url");
            text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent(url);
                }
            });
            text.setClickable(true);

            childrens.addView(item);
        }
    }


}
