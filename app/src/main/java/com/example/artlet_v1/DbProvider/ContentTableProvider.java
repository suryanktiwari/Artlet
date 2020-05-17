package com.example.artlet_v1.DbProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.artlet_v1.DatabaseHelper;
import com.example.artlet_v1.TableContent;

import java.util.Random;

public class ContentTableProvider extends DatabaseHelper {

    SQLiteDatabase db;
    String initial = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    String[] types = {
            "pdf",
            "epub",
            "doc",
            "manga"
    };


    public ContentTableProvider(Context context) {

        super(context);
    }

    public void populateRowContent(String randomString, int dummyInt, String type) {

        db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(TableContent.TableContentClass.CONTENT_AUTHORID, dummyInt);
        c.put(TableContent.TableContentClass.CONTENT_TITLE, randomString);
        c.put(TableContent.TableContentClass.CONTENT_GENREID, dummyInt);
        c.put(TableContent.TableContentClass.CONTENT_AUTHORID, dummyInt);
        c.put(TableContent.TableContentClass.CONTENT_TYPE, type);
        c.put(TableContent.TableContentClass.CONTENT_FILE, randomString);
        db.insert(TableContent.TableContentClass.TABLE_Content, null, c);
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

    public void populateDataContent(String title, String type, String filePath) {
        db = getWritableDatabase();
        db.execSQL("INSERT INTO content (author_id, title, genre_id, type, file) VALUES(1, '"+title+"', 1, '"+ type +"', '"+ filePath + "')");
//        int i = 1;
//        String[] rep = new String[10];
//        for (int j=0;j<rep.length;j++)
//        {
//            rep[j]=randomGenerator(initial);
//            System.out.println(rep[j]);
//        }
//
//        while(i <= 100) {
//            if(i<=50)
//            {
//                populateRowContent(randomGenerator(initial), i, this.types[new Random().nextInt(4)]);
//            }
//
//            if(i>50)
//            {
//                int index = new Random().nextInt(rep.length);
//                populateRowContent(rep[index], i, this.types[new Random().nextInt(4)]);
//
//            }
//            i=i+1;
//            }

        }
    }





