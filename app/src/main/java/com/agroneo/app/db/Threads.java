package com.agroneo.app.db;

import com.agroneo.app.db.utils.DbObject;
import com.agroneo.app.db.utils.Index;
import com.agroneo.app.db.utils.Indexes;
import com.agroneo.app.db.utils.Type;

import java.util.Date;


@Indexes(@Index(name = "_id", keys = {"_id"}))
public class Threads extends DbObject {

    @Type(type = "VARCHAR2(26)")
    public String _id;

    @Type(type = "VARCHAR2(255)")
    public String title;

    @Type(type = "INTEGER")
    public Date date;

    @Type(type = "INTEGER")
    public Date last_date;

    @Type(type = "VARCHAR2(26)")
    public String last_id;

    @Type(type = "VARCHAR2(26)")
    public String user_id;

    @Type(type = "VARCHAR2(255)")
    public String user_avatar;

    @Type(type = "INTEGER")
    public int replies;

    @Type(type = "VARCHAR2(26)")
    public String parent;

}
