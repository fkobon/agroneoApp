package com.agroneo.app.discuss;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

import com.agroneo.app.api.ApiAgroneo;
import com.agroneo.app.db.utils.DbHelper;
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
                    db.insertDiscuss(posts.getListJson("result"));
                }
                reloadCursor();
            }
        }.doGet("forum" + parent);

    }

    private void reloadCursor() {
        adapter.changeCursor(db.getDiscuss(parent));

    }

    public void last() {
    }

    public void first() {
    }
}
