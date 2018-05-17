package com.agroneo.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class Json extends LinkedHashMap<String, Object> {

    public Json() {
        super();
    }

    public Json(String json_string) {
        if (json_string == null || json_string.equals("null")) {
            return;
        }
        try {
            ObjectMapper objmapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat(Fx.ISO_DATE);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            objmapper.setDateFormat(df);
            putAll(objmapper.readValue(json_string, Json.class));
        } catch (Exception e) {
        }
    }

    public Json(Object obj) {
        if (obj == null) {
            return;
        }
        putAll((Map<String, Object>) obj);
    }

    public Json(String key, Object value) {
        super.put(key, value);
    }

    public Json(Map<String, Object> map) {
        super.putAll(map);
    }

    private <T> T get(String key, Class<T> clazz) {
        try {
            if (get(key) == null) {
                return null;
            }
            if (clazz.equals(Integer.class)) {
                return clazz.cast(Number.class.cast(super.get(key)).intValue());
            }
            return clazz.cast(super.get(key));
        } catch (Exception e) {
            return null;
        }
    }

    public Object get(String key) {
        return super.get(key);
    }

    public String getId() {
        if (containsKey("_id")) {
            return getString("_id");
        } else {
            return getString("id");
        }
    }

    public String getString(String key) {
        return get(key, String.class);
    }

    public String getString(String key, String def) {
        if (containsKey(key) && get(key) != null) {
            return getString(key);
        }
        return def;
    }


    public boolean getBoolean(String key, boolean def) {
        try {
            if (containsKey(key) && get(key) != null) {
                return get(key, Boolean.class);
            }
            return def;
        } catch (Exception e) {
            return def;
        }
    }

    public Date getDate(String key) {
        return get(key, Date.class);
    }

    public Date getDate(String key, Date def) {
        if (get(key) == null) {
            return def;
        }
        return get(key, Date.class);
    }

    public Date parseDate(String key) {
        if (getString(key) == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(Fx.ISO_DATE).parse(getString(key));
        } catch (Exception e) {
            return null;
        }
    }

    public int getInteger(String key) {
        if (get(key).getClass().equals(Long.class.getClass())) {
            return get(key, Long.class).intValue();
        }
        return get(key, Integer.class);
    }

    public int getInteger(String key, int def) {
        try {
            if (super.get(key) == null) {
                return def;
            }
            return getInteger(key);
        } catch (Exception e) {
            return def;
        }
    }

    public double getDouble(String key) {

        try {
            return get(key, Double.class);
        } catch (Exception e) {
            return (double) getInteger(key);
        }
    }

    public double getDouble(String key, double def) {
        if (super.get(key) == null) {
            return def;
        }
        return getDouble(key);
    }

    public BigDecimal getBigDecimal(String key) {
        if (get(key) instanceof Double) {
            return BigDecimal.valueOf(get(key, Double.class));
        }
        if (get(key) instanceof Float) {
            return BigDecimal.valueOf(get(key, Float.class));
        }
        if (get(key) instanceof Long) {
            return BigDecimal.valueOf(get(key, Long.class));
        }
        if (get(key) instanceof Integer) {
            return BigDecimal.valueOf(get(key, Integer.class));
        }
        return null;
    }

    public int increment(String key, int qt) {
        put(key, getInteger(key, 0) + qt);
        return getInteger(key);
    }

    public List<String> getList(String key) {
        return getList(key, String.class);
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        return (List<T>) super.get(key);
    }

    public List<Json> getListJson(String key) {
        if (key == null || get(key) == null || !ArrayList.class.isAssignableFrom(get(key).getClass())) {
            return null;
        }
        List<Json> outlist = new ArrayList<>();
        for (Object list : get(key, ArrayList.class)) {
            outlist.add(new Json(list));
        }
        return outlist;
    }

    public Json getJson(String key) {
        if (get(key) == null) {
            return null;
        }
        try {
            if (get(key).getClass().equals(Json.class)) {
                return get(key, Json.class);
            } else {
                return new Json(get(key));
            }
        } catch (Exception e) {
            return null;
        }

    }

    public Json put(String key, Object value) {
        super.remove(key);
        super.put(key, value);
        return this;
    }

    public boolean containsKey(String key) {
        return super.containsKey(key);
    }

    public Json remove(String key) {
        super.remove(key);
        return this;
    }

    public Json add(String key, Object value) {
        List<Object> values = getList(key, Object.class);
        if (values == null) {
            values = new ArrayList<>();
        }
        values.add(value);
        return put(key, values);
    }

    public Json add(String key, Object value, int position) {
        List<Object> values = getList(key, Object.class);
        if (values == null) {
            values = new ArrayList<>();
        }
        values.add(position, value);
        return put(key, values);
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return super.entrySet();
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public Set<String> keySet() {
        return super.keySet();
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean compressed) {
        GsonBuilder gson = new GsonBuilder();
        if (!compressed) {
            gson.setPrettyPrinting();
        }
        return gson.create().toJson(this);
    }


    public String getHash() {
        String hash = BigInteger.valueOf(toString().hashCode()).toString(26).replaceAll("-", "Z");
        while (hash.length() < 8) {
            hash += hash;
        }
        return hash.substring(0, 8).toUpperCase();
    }

    @Override
    public Json clone() {
        return new Json(this);
    }

}
