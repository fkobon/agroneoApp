package com.agroneo.app.discuss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.agroneo.app.R;

public class DiscussAdaptater extends BaseAdapter {

    private Context context;

    public DiscussAdaptater(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1005;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.discuss_forum, parent, false);
        }

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
