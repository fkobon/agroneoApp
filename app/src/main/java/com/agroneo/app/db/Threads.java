package com.agroneo.app.db;

import android.database.Cursor;

import com.agroneo.app.db.utils.DbObject;
import com.agroneo.app.db.utils.Type;

import java.util.Date;

public class Threads extends DbObject {

    @Type(type = "VARCHAR2(26)")
    String _id;

    @Type(type = "VARCHAR2(255)")
    String title;

    @Type(type = "INTEGER")
    Date date;

    @Type(type = "INTEGER")
    Date last_date;

    @Type(type = "VARCHAR2(26)")
    String last_id;

    @Type(type = "VARCHAR2(26)")
    String user_id;

    @Type(type = "VARCHAR2(255)")
    String user_avatar;

    @Type(type = "INTEGER")
    int replies;

    @Type(type = "VARCHAR2(26)")
    String parent;


}
