package com.agroneo.app.db;

import com.agroneo.app.db.utils.DbObject;
import com.agroneo.app.db.utils.Index;
import com.agroneo.app.db.utils.Indexes;
import com.agroneo.app.db.utils.Type;


@Indexes(@Index(name = "_id", keys = {"_id"}))
public class Forums extends DbObject {

    @Type(type = "VARCHAR2(26)")
    String _id;

    @Type(type = "VARCHAR2(255)")
    String title;

    @Type(type = "VARCHAR2(26)")
    String parent;

}
