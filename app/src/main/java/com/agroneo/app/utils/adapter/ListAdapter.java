package com.agroneo.app.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.agroneo.app.R;

import java.util.List;

public abstract class ListAdapter<T> extends BaseAdapter implements ListAdapterImpl<T> {

    private int layout;

    private Context context;
    private ListView listView;
    private ProgressBar loading;

    private List<T> data;

    public ListAdapter(Context context, int layout, ListView listView) {
        super();
        this.layout = layout;
        this.context = context;
        this.listView = listView;
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
        }
        showView(convertView, getItem(position), position + 1 == getCount());

        return convertView;
    }

    public Context getContext() {
        return context;
    }


    public void loading(boolean load) {
        if (loading == null) {
            loading = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progress, listView, false);
        }
        if (load) {
            listView.addFooterView(loading);
        } else {
            listView.removeFooterView(loading);
        }
    }
}
