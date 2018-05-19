package com.agroneo.app.discuss;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agroneo.app.R;
import com.squareup.picasso.Picasso;

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
        if (cursor.isFirst()) {
            populator.first();
        }
        if (cursor.isLast()) {
            populator.last();
        }

        ((TextView) view.findViewById(R.id.title)).setText(cursor.getString(cursor.getColumnIndex("title")));
        String avatar = cursor.getString(cursor.getColumnIndex("user_avatar"));
        Picasso.get()
                .load(avatar + "@200x200")
                .resizeDimen(R.dimen.avatarDpw, R.dimen.avatarDpw)
                .into((ImageView) view.findViewById(R.id.avatar));

    }

}
