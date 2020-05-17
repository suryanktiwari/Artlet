package com.example.artlet_v1;

import android.provider.BaseColumns;

public class TableUserProfile {

    public TableUserProfile()
    {

    }

    public static abstract class TableUserProfileClass implements BaseColumns
    {
        public static final String USER_ID = "id";
        public static final String USER_NAME = "user_name";
        public static final String USER_IMAGE = "user_image";
        public static final String USER_FOLLOWERS = "user_followers";
        public static final String USER_FOLLOWING = "user_following";
        public static final String USER_POSTS = "user_posts";
        public static final String TABLE_User_Profile = "user_profile";
    }
}
