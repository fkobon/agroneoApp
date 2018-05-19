package com.agroneo.app.db.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.agroneo.app.utils.Fx;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbObject {
    
    public void setCursor(Cursor cursor) {
        for (Field field : getClass().getFields()) {

            Annotation[] annos = field.getDeclaredAnnotations();
            if (annos.length == 1) {
                if (annos[0].toString().startsWith("@" + Type.class.getName())) {
                    try {
                        if (field.getType() == Date.class) {
                            field.set(this, new Date(cursor.getLong(cursor.getColumnIndex(field.getName()))));
                        } else if (field.getType() == String.class) {
                            field.set(this, cursor.getString(cursor.getColumnIndex(field.getName())));
                        } else if (field.getType() == Integer.class) {
                            field.set(this, cursor.getInt(cursor.getColumnIndex(field.getName())));
                        } else if (field.getType() == Long.class) {
                            field.set(this, cursor.getLong(cursor.getColumnIndex(field.getName())));
                        } else if (field.getType() == Float.class) {
                            field.set(this, cursor.getFloat(cursor.getColumnIndex(field.getName())));
                        } else if (field.getType() == Double.class) {
                            field.set(this, cursor.getDouble(cursor.getColumnIndex(field.getName())));
                        } else if (field.getType() == byte[].class) {
                            field.set(this, cursor.getBlob((cursor.getColumnIndex(field.getName()))));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    public static String[] getProjection(Class cls) {
        List<String> projections = new ArrayList<>();
        for (Field field : cls.getFields()) {
            Annotation[] annos = field.getDeclaredAnnotations();
            if (annos.length == 1) {
                if (annos[0].toString().startsWith("@" + Type.class.getName())) {
                    projections.add(field.getName());
                }
            }
        }
        return projections.toArray(new String[0]);
    }

    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();
        for (Field field : getClass().getFields()) {

            Annotation[] annos = field.getDeclaredAnnotations();
            if (annos.length == 1) {
                if (annos[0].toString().startsWith("@" + Type.class.getName())) {
                    try {
                        if (field.getType() == Date.class) {
                            values.put(field.getName(), ((Date) field.get(this)).getTime());
                        } else if (field.getType() == String.class) {
                            values.put(field.getName(), (String) field.get(this));
                        } else if (field.getType() == Integer.class) {
                            values.put(field.getName(), (Integer) field.get(this));
                        } else if (field.getType() == Long.class) {
                            values.put(field.getName(), (Long) field.get(this));
                        } else if (field.getType() == Float.class) {
                            values.put(field.getName(), (Float) field.get(this));
                        } else if (field.getType() == Double.class) {
                            values.put(field.getName(), (Double) field.get(this));
                        } else if (field.getType() == byte[].class) {
                            values.put(field.getName(), (byte[]) field.get(this));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        return values;
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
