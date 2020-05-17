package com.example.artlet_v1.DbProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.artlet_v1.DatabaseHelper;
import com.example.artlet_v1.TableGenre;

import java.util.Random;


public class GenreTableProvider extends DatabaseHelper {

    SQLiteDatabase db;
    String initial = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public GenreTableProvider(Context context) {
        super(context);
    }

    public void populateRowGenre(String randomString, int dummyInt) {
        db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(TableGenre.TableGenreClass.GENRE_NAME, randomString);
        db.insert(TableGenre.TableGenreClass.TABLE_Genre, null, c);
        db.close();
    }

    /*public String randomGenerator(String p)
    {
        StringBuilder s = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 7; i++) {
            s.append(p.charAt(r.nextInt(p.length())));
        }

        return s.toString();
    }

    public void populateDataGenre() {
        int i = 1;
        while(i <= 50) {
            populateRowGenre(randomGenerator(initial), i);
            i=i+1;
        }
    }*/



}

