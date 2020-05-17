package com.example.artlet_v1;

import android.provider.BaseColumns;

public class TableTag {

    private TableTag()
    {

    }

    public static abstract class TableTagClass implements BaseColumns
    {
        public static final String TAG_ID = "id";
        public static final String TAG_CONTENTID = "content_id";
        public static final String TAG_NAME = "name";
        public static final String TAG_CREATED_AT = "created_at";
        public static final String TABLE_Tags = "tag";
    }
}
