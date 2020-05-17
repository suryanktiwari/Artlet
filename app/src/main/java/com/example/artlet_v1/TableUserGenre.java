package com.example.artlet_v1;

import android.provider.BaseColumns;

public class TableUserGenre {

    public TableUserGenre()
    {

    }

    public static abstract class TableUserGenreClass implements BaseColumns
    {
        public static final String USERGENRE_ID = "id";
        public static final String UG_GENREID = "genre_id";
        public static final String UG_USERID = "user_id";
        public static final String UG_CREATED_AT = "created_at";
        public static final String TABLE_User_Genre = "user_genre";

    }
}
