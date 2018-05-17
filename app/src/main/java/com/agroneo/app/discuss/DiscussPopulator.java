package com.agroneo.app.discuss;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

import com.agroneo.app.utils.DbHelper;

public class DiscussPopulator {

    private CursorAdapter adapter;
    private Cursor cursor;

    public DiscussPopulator(Context context, CursorAdapter adapter) {
        this.adapter = adapter;
        cursor = new DbHelper(context).getDiscuss("ROOT");
        adapter.changeCursor(cursor);
    }


    public void last() {
    }

    public void first() {
    }
}
