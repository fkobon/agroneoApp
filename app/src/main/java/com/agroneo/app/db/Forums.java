package com.agroneo.app.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class Forums {
    @PrimaryKey
    @NonNull
    String _id;

    String title;

    String parent;

}
