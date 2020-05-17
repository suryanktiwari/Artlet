package com.example.artlet_v1;

import android.provider.BaseColumns;

public class TableGenre {

    private TableGenre()
    {

    }

    public static abstract class TableGenreClass implements BaseColumns
    {
        public static final String GENRE_ID = "id";
        public static final String GENRE_NAME = "name";
        public static final String GENRE_CREATED_AT = "created_at";
        public static final String TABLE_Genre = "genre";

    }

}
