package com.agroneo.app.db.utils;

import android.database.sqlite.SQLiteDatabase;

import com.agroneo.app.utils.Fx;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DbBuilder {

    public static void createTable(Class<? extends DbObject> cls, SQLiteDatabase db) {

        try {
            db.execSQL("DROP TABLE " + cls.getSimpleName());
        } catch (Exception e) {
        }

        String query = "CREATE TABLE " + cls.getSimpleName() + " (";
        List<String> cols = new ArrayList<>();
        for (Field field : cls.getFields()) {
            Annotation[] annos = field.getDeclaredAnnotations();
            for (Annotation anno : annos) {
                if (anno.toString().startsWith("@" + Type.class.getName())) {
                    cols.add(field.getName() + " " + ((Type) anno).type());
                }
            }
        }
        query += Fx.join(cols, ",") + ")";
        db.execSQL(query);

        for (Annotation anno : cls.getAnnotations()) {
            if (anno.toString().startsWith("@" + Indexes.class.getName())) {
                for (Index indexanno : ((Indexes) anno).value()) {
                    String index = "CREATE INDEX " + cls.getSimpleName() + "_" + indexanno.name() + " ON " + cls.getSimpleName() + "(";
                    index += Fx.join(indexanno.keys(), ",");
                    index += ")";
                    db.execSQL(index);
                }
            }
        }

    }
}
