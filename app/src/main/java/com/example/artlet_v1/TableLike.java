package com.example.artlet_v1;

import android.provider.BaseColumns;

public class TableLike {

    private TableLike()
    {

    }

    public static abstract class TableLikesClass implements BaseColumns
    {
        public static final String LIKES_USERID = "user_id";
        public static final String LIKES_ID = "id";
        public static final String LIKES_CREATED_AT = "created_at";
        public static final String TABLE_Likes = "likes";
        public static final String LIKES_CONTENTID = "content_id";
    }

}