package com.agroneo.app.db.utils;

import android.database.Cursor;

import com.agroneo.app.utils.Fx;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DbObject {

    private Cursor cursor;

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public String getString(String key) {
        return cursor.getString(cursor.getColumnIndex(key));
    }

    public String createTable() {

        String query = "CREATE TABLE " + getClass().getSimpleName() + " (";
        List<String> cols = new ArrayList<>();
        for (Field field : getClass().getFields()) {
            Annotation[] annos = field.getDeclaredAnnotations();
            for (Annotation anno : annos) {
                if (anno.toString().startsWith("@" + Type.class.getName())) {
                    cols.add(field.getName() + " " + ((Type) anno).type());
                }
            }
        }
        query += Fx.join(cols, ",") + ")";
        return query;
    }
}
