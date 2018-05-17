package com.agroneo.app.discuss;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agroneo.app.R;

public class DiscussAdaptater extends CursorAdapter {


    public DiscussAdaptater(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View convertView = LayoutInflater.from(context).inflate(R.layout.discuss_forum, parent, false);

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor.isFirst()) {
            PopulateDb.updateFirst();
        }
        if (cursor.isLast()) {

            PopulateDb.updateLast();
        }
    }

}
