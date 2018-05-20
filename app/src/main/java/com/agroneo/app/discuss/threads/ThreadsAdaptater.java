package com.agroneo.app.discuss.threads;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agroneo.app.R;
import com.agroneo.app.utils.ImageLoader;

public class ThreadsAdaptater extends CursorAdapter {

    private ThreadsPopulator populator;

    public ThreadsAdaptater(Context context, String parent) {
        super(context, null, 0);
        populator = new ThreadsPopulator(context, this, parent);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.discuss_forum, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        try {
            if (cursor.isLast()) {
                String next = cursor.getString(cursor.getColumnIndex("next"));
                if (next != null) {
                    populator.update(next);
                }
            }
            ((TextView) view.findViewById(R.id.title)).setText(cursor.getString(cursor.getColumnIndex("title")));
            ImageLoader.setRound(cursor.getString(cursor.getColumnIndex("useravatar")) + "@200x200", view.findViewById(R.id.avatar), R.dimen.avatarDpw);

        } catch (Exception e) {

        }


    }

}
