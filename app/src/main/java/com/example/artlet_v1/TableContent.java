package com.example.artlet_v1;

import android.provider.BaseColumns;

public class TableContent {

    private TableContent()
    {

    }

    public static abstract class TableContentClass implements BaseColumns
    {
        public static final String CONTENT_ID = "id";
        public static final String CONTENT_TITLE = "title";
        public static final String CONTENT_AUTHORID = "author_id";
        public static final String CONTENT_GENREID = "genre_id";
        public static final String CONTENT_TYPE = "type";
        public static final String CONTENT_LIKES = "likes";
        public static final String CONTENT_FILE = "file";
        public static final String CONTENT_CREATED_AT = "created_at";
        public static final String TABLE_Content = "content";

    }

}
