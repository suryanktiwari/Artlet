package com.example.artlet_v1;

import android.provider.BaseColumns;

public class TableUser {

    private TableUser()
    {

    }

    public static abstract class TableUserClass implements BaseColumns
    {
        public static final String USER_ID = "id";
        public static final String USER_NAME = "name";
        public static final String USER_EMAIL = "email";
        public static final String USER_PASSWORD = "password";
        public static final String USER_LOCATION = "location";
        public static final String USER_CREATED_AT = "created_at";
        public static final String TABLE_Users= "user";

    }
}
