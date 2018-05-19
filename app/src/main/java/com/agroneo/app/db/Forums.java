package com.agroneo.app.db;

import com.agroneo.app.db.utils.DbObject;
import com.agroneo.app.db.utils.Type;

public class Forums extends DbObject {

    @Type(type = "VARCHAR2(26)")
    String _id;

    @Type(type = "VARCHAR2(255)")
    String title;


    @Type(type = "VARCHAR2(26)")
    String parent;
}
