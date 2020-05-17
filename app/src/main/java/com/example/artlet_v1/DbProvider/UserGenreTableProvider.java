package com.example.artlet_v1.DbProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.artlet_v1.DatabaseHelper;
import com.example.artlet_v1.TableUserGenre;

import java.util.Random;

public class UserGenreTableProvider extends DatabaseHelper {

    SQLiteDatabase db;
    String initial = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public UserGenreTableProvider(Context context) {
        super(context);
    }

    public void populateRowUserGenre(String randomString, int dummyInt) {
        db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(TableUserGenre.TableUserGenreClass.UG_GENREID, dummyInt);
        c.put(TableUserGenre.TableUserGenreClass.UG_USERID, dummyInt);
        db.insert(TableUserGenre.TableUserGenreClass.TABLE_User_Genre, null, c);
        db.close();
    }

    public String randomGenerator(String p)
    {
        StringBuilder s = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 7; i++) {
            s.append(p.charAt(r.nextInt(p.length())));
        }

        return s.toString();
    }

    public void populateDataUserGenre() {
        int i = 1;
        while(i <= 50) {
            populateRowUserGenre(randomGenerator(initial), i);
            i=i+1;
        }
    }

}

