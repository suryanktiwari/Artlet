package com.example.artlet_v1.DbProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.artlet_v1.DatabaseHelper;
import com.example.artlet_v1.TableTag;

import java.util.Random;

public class TagTableProvider extends DatabaseHelper {

    SQLiteDatabase db;
    String initial = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public TagTableProvider(Context context) {
        super(context);
    }

    public void populateRowTag(String randomString, int dummyInt) {
        db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(TableTag.TableTagClass.TAG_CONTENTID, dummyInt);
        c.put(TableTag.TableTagClass.TAG_ID, dummyInt);
        c.put(TableTag.TableTagClass.TAG_NAME, randomString);
        db.insert(TableTag.TableTagClass.TABLE_Tags, null, c);
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

    public void populateDataTag() {
        int i = 1;
        while(i <= 50) {
            populateRowTag(randomGenerator(initial), i);
            i=i+1;
        }
    }

}

