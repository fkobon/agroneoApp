package com.agroneo.app.discuss;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

import com.agroneo.app.api.ApiAgroneo;
import com.agroneo.app.utils.DbHelper;
import com.agroneo.app.utils.Json;

public class DiscussPopulator {

    private CursorAdapter adapter;
    private Cursor cursor;
    private Context context;
    private DbHelper db;
    private String parent;

    public DiscussPopulator(Context context, CursorAdapter adapter, String parent) {
        this.context = context;
        this.adapter = adapter;
        this.parent = parent;
        this.db = new DbHelper(context);
        cursor = db.getDiscuss(parent);
        adapter.changeCursor(cursor);
        update();
    }

    public void update() {
        new ApiAgroneo(context) {
            @Override
            public void result(Json response) {
                Json posts = response.getJson("posts");
                if (posts != null) {
                    for (Json post : posts.getListJson("result")) {
                        db.insertDiscuss(post);
                    }
                }
                reloadCursor();
            }
        }.doGet("forum" + parent);

    }

    private void reloadCursor() {
        Cursor newcursor = db.getDiscuss(parent);
        adapter.changeCursor(newcursor);
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = newcursor;
    }

    public void last() {
    }

    public void first() {
    }
}
