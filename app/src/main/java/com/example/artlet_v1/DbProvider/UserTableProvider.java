package com.example.artlet_v1.DbProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.artlet_v1.DatabaseHelper;
import com.example.artlet_v1.TableUser;

import java.util.Random;

public class UserTableProvider extends DatabaseHelper {

    SQLiteDatabase db;
//    String initial = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public UserTableProvider(Context context) {

        super(context);
    }

    public void populateRowUser(String user_email, String user_name, String user_password, String user_loaction) {

        db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(TableUser.TableUserClass.USER_EMAIL, user_email);
        c.put(TableUser.TableUserClass.USER_NAME, user_name);
        c.put(TableUser.TableUserClass.USER_PASSWORD, user_password);
        c.put(TableUser.TableUserClass.USER_LOCATION, user_loaction);
        db.insert(TableUser.TableUserClass.TABLE_Users, null, c);
        db.close();
    }


    String[] user_email = {"zaki@gmail.com", "suryank@gmail.com", "rose@gmail.com", "utsav@gmail.com", "ishani@gmail.com", "sonal@gmail.com", "sonali@gmmail.com", "prateek@gmail.com", "aakanksha@gmail.com"};
    String[] user_name = {"Zaki", "Suryank", "Rose", "Utsav", "Ishani", "Sonal", "Sonali", "Prateek", "Aakanksha"};
    String[] user_password = {"zak", "sur", "ros","uts", "ish", "son","soni","pra","aak"};


   /* public String randomGenerator(String p)
    {
        StringBuilder s = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 7; i++) {
            s.append(p.charAt(r.nextInt(p.length())));
        }

        return s.toString();
    }*/

    public void populateDataUser() {
        int i;
        String loc = "Delhi";
        for(i=0;i<user_email.length;i++)
        {
            populateRowUser(user_email[i], user_name[i], user_password[i], loc);
        }
    }


}
