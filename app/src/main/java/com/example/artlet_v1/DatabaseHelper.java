package com.example.artlet_v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.artlet_v1.TableContent.TableContentClass;
import com.example.artlet_v1.TableGenre.TableGenreClass;
import com.example.artlet_v1.TableTag.TableTagClass;
import com.example.artlet_v1.TableUser.TableUserClass;
import com.example.artlet_v1.TableUserGenre.TableUserGenreClass;
import com.example.artlet_v1.TableUserProfile.TableUserProfileClass;
import com.example.artlet_v1.TableLike.TableLikesClass;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Name
    private static final String DATABASE_NAME = "artlet";

    // Table Names
    // private static final String TABLE_Users= "user";
    // private static final String TABLE_Genre = "genre";
    // private static final String TABLE_Content = "content";
    // private static final String TABLE_Tags = "tag";
    // private static final String TABLE_User_Genre = "user_genre";

    // user table create statement
    private static final String CREATE_TABLE_User = "CREATE TABLE " + TableUserClass.TABLE_Users + "( " + TableUserClass.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TableUserClass.USER_NAME +" VARCHAR(255)," + TableUserClass.USER_EMAIL +" VARCHAR(255),"+ TableUserClass.USER_PASSWORD + " VARCHAR(255), " + TableUserClass.USER_LOCATION + " VARCHAR(255), " + TableUserClass.USER_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP )";

    // genre table create statement
    private static final String CREATE_TABLE_Genre = "CREATE TABLE " + TableGenreClass.TABLE_Genre + "( " + TableGenreClass.GENRE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + TableGenreClass.GENRE_NAME + " VARCHAR(255), " + TableGenreClass.GENRE_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP )\n ";

    // content table create statement
    private static final String CREATE_TABLE_Content = "CREATE TABLE " + TableContentClass.TABLE_Content + " ( " + TableContentClass.CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + TableContentClass.CONTENT_LIKES + " INTEGER DEFAULT 0, " + TableContentClass.CONTENT_TITLE + " VARCHAR(255), " + TableContentClass.CONTENT_AUTHORID + " INT(11) NOT NULL, " + TableContentClass.CONTENT_GENREID + " INT(11), " + TableContentClass.CONTENT_TYPE + " VARCHAR(255), " + TableContentClass.CONTENT_FILE + " VARCHAR(255), " + TableContentClass.CONTENT_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP )\n";

    //tag tabel create statement
    private static final String CREATE_TABLE_Tag = "CREATE TABLE " + TableTagClass.TABLE_Tags + " ( " + TableTagClass.TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + TableTagClass.TAG_CONTENTID + " INT(11) , " + TableTagClass.TAG_NAME + " VARCHAR(255) NOT NULL, " + TableTagClass.TAG_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (" + TableTagClass.TAG_CONTENTID + ") REFERENCES " + TableContentClass.TABLE_Content + " (" + TableContentClass.CONTENT_ID + "))";

    //user_genre table create statement
    private static final String CREATE_TABLE_User_Genre = "CREATE TABLE " + TableUserGenreClass.TABLE_User_Genre + " ( " + TableUserGenreClass.USERGENRE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + TableUserGenreClass.UG_GENREID + " INT(11), " + TableUserGenreClass.UG_USERID + " INT(11), " + TableUserGenreClass.UG_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (" + TableUserGenreClass.UG_USERID + ") REFERENCES "+ TableUserClass.TABLE_Users + " (" + TableUserClass.USER_ID + "), FOREIGN KEY (" + TableUserGenreClass.UG_GENREID + ") REFERENCES " + TableGenreClass.TABLE_Genre + " (" + TableGenreClass.GENRE_ID + ") )";

    // create user profile table
    private static final String CREATE_TABLE_User_Profile = "CREATE TABLE " + TableUserProfileClass.TABLE_User_Profile + " ( " + TableUserProfileClass.USER_ID + " INTEGER PRIMARY KEY , " + TableUserProfileClass.USER_NAME + " VARCHAR(255), " + TableUserProfileClass.USER_IMAGE + " VARCHAR(255) , " + TableUserProfileClass.USER_FOLLOWERS + " INT(11) , " + TableUserProfileClass.USER_FOLLOWING + " INT(11) , " + TableUserProfileClass.USER_POSTS + " INT(11) )";

    private static final String CREATE_TABLE_Likes = "CREATE TABLE " + TableLikesClass.TABLE_Likes + "( " + TableLikesClass.LIKES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + TableLikesClass.LIKES_USERID + " INT(11), " + TableLikesClass.LIKES_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + TableLikesClass.LIKES_CONTENTID + " INT(11), FOREIGN KEY (" + TableLikesClass.LIKES_USERID + ") REFERENCES " + TableUserClass.TABLE_Users + "(" + TableUserClass.USER_ID + "), FOREIGN KEY (" + TableLikesClass.LIKES_CONTENTID + ") REFERENCES " + TableContentClass.TABLE_Content + "(" + TableContentClass.CONTENT_ID + ") ) ";

    // SQLiteDatabase object to write and read the database created
    protected SQLiteDatabase db;

    public  DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.db = this.getWritableDatabase();
        Log.d("Database Created", "Inside Database helper");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_User);
        db.execSQL(CREATE_TABLE_Genre);
        db.execSQL(CREATE_TABLE_Tag);
        db.execSQL(CREATE_TABLE_Content);
        db.execSQL(CREATE_TABLE_Likes);
        db.execSQL(CREATE_TABLE_User_Genre);
        db.execSQL(CREATE_TABLE_User_Profile);
        Log.d("Tables Created", "Inside OnCreate");
    }

    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
    }

    public long InsertUserData(DatabaseHelper dh,  String user_name, String user_email, String user_password, String user_location)
    {
        db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(TableUserClass.USER_EMAIL, user_email);
        c.put(TableUserClass.USER_NAME, user_name);
        c.put(TableUserClass.USER_PASSWORD, user_password);
        c.put(TableUserClass.USER_LOCATION, user_location);
        long id = db.insert(TableUserClass.TABLE_Users, null, c);
        Log.d("Inside InsertUserData", "One row inserted");
        return id;
    }

    public void InsertUserProfileData(DatabaseHelper dh, long user_id, String user_name, int user_followers, int user_following, int user_posts, String user_image)
    {
        db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(TableUserProfileClass.USER_ID, user_id);
        c.put(TableUserProfileClass.USER_NAME, user_name);
        c.put(TableUserProfileClass.USER_FOLLOWERS, user_followers);
        c.put(TableUserProfileClass.USER_FOLLOWING, user_following);
        c.put(TableUserProfileClass.USER_POSTS, user_posts);
        c.put(TableUserProfileClass.USER_IMAGE, user_image);
        db.insert(TableUserProfileClass.TABLE_User_Profile, null, c);
        Log.d("InsertUserProfileData", "One row inserted");
    }

	public int InsertContentData(DatabaseHelper dh, String title, int authorId, String genreId,
                                  String type, String filePath, String timeStamp) {
        db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(TableContentClass.CONTENT_TITLE, title);
        c.put(TableContentClass.CONTENT_AUTHORID, authorId);
        c.put(TableContentClass.CONTENT_GENREID, genreId);
        c.put(TableContentClass.CONTENT_TYPE, type);
        c.put(TableContentClass.CONTENT_FILE, filePath);
        c.put(TableContentClass.CONTENT_CREATED_AT, timeStamp);
        db.insert(TableContentClass.TABLE_Content, null, c);

        String selectQuery = "SELECT  * FROM content";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        return cursor.getInt(cursor.getColumnIndex("id"));
//        Log.d("InsertContentData", "Inserting file data");
    }

    public void InsertContentData1(DatabaseHelper dh, long user_id, String title, String content_path)
    {
        db = getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(TableContentClass.CONTENT_AUTHORID, user_id);
        c.put(TableContentClass.CONTENT_TITLE, title);
        c.put(TableContentClass.CONTENT_FILE, content_path);
        db.insert(TableContentClass.TABLE_Content, null, c);
        Log.d("InsertContentData", "One row inserted");
    }

    //will be removed later; may be used
    public void InsertGenreData()
    {
        db = getWritableDatabase();
        String insertValues = " INSERT INTO " + TableGenreClass.TABLE_Genre + " ( '" + TableGenreClass.GENRE_NAME + "' ) VALUES ('Narrative'), ('Drama'), ('Novel'), ('Poetry'), ('Science Fiction'), ('Non-Fiction'), ('Short-Story'), ('Autobiography'), ('Historical Fiction'), ('Horror'), ('Crime'), ('Memoir'), ('Comedy'), ('Satire'), ('Romance'), ('Play'), ('Prose'), ('Suspense'), ('Legend'), ('Thriller'), ('Tragedy'), ('Young Adult Fiction'), ('Myth'), ('Occult Fiction'), ('Screenplay'), ('Children Literature'), ('Alternate History'), ('Magical Realism'), ('Mystery'), ('Anthology'), ('Detective Fiction') ";
        db.execSQL(insertValues);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TableUserClass.TABLE_Users);
        db.execSQL("DROP TABLE IF EXISTS " + TableTagClass.TABLE_Tags);
        db.execSQL("DROP TABLE IF EXISTS " + TableGenreClass.TABLE_Genre);
        db.execSQL("DROP TABLE IF EXISTS " + TableContentClass.TABLE_Content);
        db.execSQL("DROP TABLE IF EXISTS " + TableUserGenreClass.TABLE_User_Genre);
        db.execSQL("DROP TABLE IF EXISTS " + TableUserProfileClass.TABLE_User_Profile);

        onCreate(db);

    }

}