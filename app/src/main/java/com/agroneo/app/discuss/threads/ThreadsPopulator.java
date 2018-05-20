package com.agroneo.app.discuss.threads;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

import com.agroneo.app.api.ApiAgroneo;
import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.db.DbHelper;

public class ThreadsPopulator {

    private CursorAdapter adapter;
    private Cursor cursor;
    private Context context;
    private DbHelper db;
    private String parent;

    public ThreadsPopulator(Context context, CursorAdapter adapter, String parent) {
        this.context = context;
        this.adapter = adapter;
        this.parent = parent;
        this.db = new DbHelper(context);
        cursor = db.getDiscuss(parent);
        adapter.changeCursor(cursor);
        update();
    }

    public void update() {
        update(null);
    }

    public void update(String next) {
        String url = "forum" + parent;
        if (next != null) {
            url += "?paging=" + next;
        }
        new ApiAgroneo(context) {
            @Override
            public void result(Json response) {
                Json posts = response.getJson("posts");
                if (posts != null) {
                    db.insertDiscuss(posts.getListJson("result"), posts.getJson("paging").getString("next"));
                }
                reloadCursor();
            }
        }.doGet(url);

    }

    private void reloadCursor() {
        adapter.changeCursor(db.getDiscuss(parent));

    }


    public void first() {
    }
}
