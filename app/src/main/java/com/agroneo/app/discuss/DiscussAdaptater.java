package com.agroneo.app.discuss;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agroneo.app.R;
import com.agroneo.app.db.Threads;
import com.agroneo.app.utils.ImageLoader;

public class DiscussAdaptater extends CursorAdapter {

    private DiscussPopulator populator;

    public DiscussAdaptater(Context context, String parent) {
        super(context, null, 0);
        populator = new DiscussPopulator(context, this, parent);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.discuss_forum, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Threads thread = new Threads();
        thread.setCursor(cursor);

        if (cursor.isFirst()) {
            populator.first();
        }
        if (cursor.isLast()) {
            populator.last();
        }

        ((TextView) view.findViewById(R.id.title)).setText(thread.title);
        ImageLoader.setRound(thread.user_avatar + "@200x200", view.findViewById(R.id.avatar), R.dimen.avatarDpw);


    }

}
