package com.agroneo.app.utils.db;

import android.arch.persistence.room.TypeConverter;

import com.agroneo.app.utils.Fx;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ListTypeConverter {
    @TypeConverter
    public String formList(List<String> list) {
        if (list == null) {
            return null;
        }
        return "@" + Fx.join(list, "@") + "@";
    }

    @TypeConverter
    public List<String> toList(String str) {
        if (str == null) {
            return null;
        }
        if (str.length()<3) {
            return Arrays.asList();
        }
        return Arrays.asList(str.substring(1, str.length() - 2).split("@"));
    }

}