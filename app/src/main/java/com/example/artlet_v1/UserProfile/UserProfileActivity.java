package com.example.artlet_v1.UserProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artlet_v1.DatabaseHelper;
import com.example.artlet_v1.ExampleAdapter;
import com.example.artlet_v1.MangaReader;
import com.example.artlet_v1.R;
import com.example.artlet_v1.SomeActivity;
import com.example.artlet_v1.TableContent;
import com.example.artlet_v1.TableUserProfile.TableUserProfileClass;
import com.example.artlet_v1.UploadActivity;
import com.example.artlet_v1.examplepost;
import com.example.artlet_v1.DocActivity;
import com.example.artlet_v1.EpubReader;
import com.example.artlet_v1.PdfReader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity implements ExampleAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayout;
    ArrayList<examplepost> exampleList= new ArrayList<>();
    ArrayList<String> titleList;
    ArrayList<String> typeList;
    ArrayList<String> pathList;
    TextView followersTV;
    TextView followingTV;
    TextView userPostsTV;
    DatabaseHelper db;
    private Button b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        db = new DatabaseHelper(this);

        ImageButton editButton = findViewById(R.id.editButton);

        Button button = findViewById(R.id.load);
        String userId = getIntent().getStringExtra("key_userId");
        String view_userId = getIntent().getStringExtra("view_userid");
//        Log.d("aaa", "onCreate: "+view_userId);
        if(view_userId.equals(userId)){
            editButton.setVisibility(View.VISIBLE);
            editButton.setClickable(true);
            button.setVisibility(View.VISIBLE);
            button.setClickable(true);
        }
        TextView userNameTV= findViewById(R.id.username);


//        String query = "SELECT "+ TableUserProfileClass.USER_IMAGE+ " FROM "+ TableUserProfileClass.TABLE_User_Profile +" WHERE "+TableUserProfileClass.USER_ID +" = "+userId;
        String query = "SELECT "+ TableUserProfileClass.USER_IMAGE+","+ TableUserProfileClass.USER_NAME+ " FROM "+ TableUserProfileClass.TABLE_User_Profile +" WHERE "+TableUserProfileClass.USER_ID +" = "+userId;
        Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
        if (cursor!= null && cursor.moveToFirst() && cursor.getCount()>0) {
            String picturePath = cursor.getString(0).trim();
            String uname = cursor.getString(1).trim();
            userNameTV.setText(uname.substring(0, 1).toUpperCase() + uname.substring(1));
            if(picturePath.length()>0){
                CircleImageView profileImage = findViewById(R.id.imageview);
                profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }

/*
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
        exampleList.add(new examplepost("Line 1"));
*/

        fetchUserPosts();

        mRecyclerView= findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayout= new LinearLayoutManager(this);
        mAdapter=new ExampleAdapter(titleList,this);
        mRecyclerView.setLayoutManager(mLayout);
        mRecyclerView.setAdapter(mAdapter);

        fetchDatabaseResults();
    }

    @Override
    public void onItemClick(int position) {
        if(typeList.get(position).equalsIgnoreCase("pdf")) {
            openPdf(pathList.get(position));
        } else if(typeList.get(position).equalsIgnoreCase("manga")) {
            openManga(pathList.get(position));
        } else if(typeList.get(position).equalsIgnoreCase("epub")) {
            openEpub(pathList.get(position));
        } else if(typeList.get(position).equalsIgnoreCase("txt") || typeList.get(position).equalsIgnoreCase("text")) {
            openTxt(pathList.get(position));
        } else{
//            Log.d(TAG,"Wrong File Type present in the List");
        }

    }

    public void openTxt(String filePath) {
        Intent intent = new Intent(this, DocActivity.class);
        intent.putExtra("docPath", filePath);
        intent.putExtra("editable", false);
        startActivity(intent);
    }

    public void openPdf(String filePath) {
        Intent intent = new Intent(this, PdfReader.class);
        intent.putExtra("pdfPath", filePath);
        startActivity(intent);
    }

    public void openManga(String filePath) {
        Intent intent = new Intent(this, MangaReader.class);
        intent.putExtra("zipPath", filePath);
        startActivity(intent);
    }

    public void openEpub(String filePath) {
        Intent intent = new Intent(this, EpubReader.class);
        intent.putExtra("epubPath", filePath);
        startActivity(intent);
    }

    private void fetchDatabaseResults() {
        String tableName = TableUserProfileClass.TABLE_User_Profile;

        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String userId = getIntent().getStringExtra("key_userId");

        followersTV = findViewById(R.id.followersId);
        followingTV = findViewById(R.id.followingId);
        userPostsTV = findViewById(R.id.postsId);
        try {
            Log.d("UserProfileActivity",userId);
            String query = "SELECT "+ TableUserProfileClass.USER_FOLLOWERS + " , " + TableUserProfileClass.USER_FOLLOWING + " , "
                            + TableUserProfileClass.USER_POSTS + " FROM " + tableName + " WHERE " + TableUserProfileClass.USER_ID +" = "+userId;

            Cursor c = db.rawQuery(query,null);

            if (c != null ) {
                if  (c.moveToFirst()) {

                    String followers = c.getString(c.getColumnIndex(TableUserProfileClass.USER_FOLLOWERS));
                    String following = c.getString(c.getColumnIndex(TableUserProfileClass.USER_FOLLOWING));
                    String userPosts = c.getString(c.getColumnIndex(TableUserProfileClass.USER_POSTS));
                    followersTV.setText(followers);
                    followingTV.setText(following);
                    userPostsTV.setText(userPosts);

                    Log.d("In UserProfileActivity", "Successfully Printed followers and following");
                }
            }
            c.close();
            db.close();
        } catch (SQLiteException se ) {

            Log.e(getClass().getSimpleName(), "Couldn't open the database");

        }
    }
    private void fetchUserPosts() {

        String tableName = TableContent.TableContentClass.TABLE_Content;

        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String userId = getIntent().getStringExtra("key_userId");

        // List of paths retrieved
        titleList = new ArrayList<String>();
        pathList = new ArrayList<String>();
        typeList = new ArrayList<String>();

        try {
            Log.d("fetchUserPosts","Fetching user posts from content");
            String query = "SELECT "+ TableContent.TableContentClass.CONTENT_FILE + " , " + TableContent.TableContentClass.CONTENT_TITLE + " , " + TableContent.TableContentClass.CONTENT_TYPE + " FROM " + tableName + " WHERE " + TableContent.TableContentClass.CONTENT_AUTHORID +" = "+userId;

            Cursor c = db.rawQuery(query,null);

            if (c != null ) {
                if  (c.moveToFirst()) {

                    do {
                        String path = c.getString(c.getColumnIndex(TableContent.TableContentClass.CONTENT_FILE));
                        String title = c.getString(c.getColumnIndex(TableContent.TableContentClass.CONTENT_TITLE));
                        String type = c.getString(c.getColumnIndex(TableContent.TableContentClass.CONTENT_TYPE));

//                        Log.d(TAG, "type:"+type);
//                        Log.d(TAG, "path:"+path);
                        titleList.add(title);
                        typeList.add(type);
                        pathList.add(path);
                        Log.d("In fetchuserposts", "Added titles to list");
                    }while(c.moveToNext());
                }
            }
            c.close();
            db.close();
        } catch (SQLiteException se ) {

            Log.e(getClass().getSimpleName(), "Couldn't open the database");

        }
    }

    public void editButtonOnClick(View v){

        browseFiles();

        /*CircleImageView profileImage = findViewById(R.id.imageview);
        String picturePath = "/storage/emulated/0/WhatsApp/Media/WhatsApp Images/IMG-20191101-WA0011.jpg";
        profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));*/
    }

    private void browseFiles() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf + application/zip + application/epub");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

// get file path

    private String getFileNameByUri(Context context, Uri uri) {
        String filepath = "";//default fileName
        File file;
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION}, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;

        } else if (uri.getScheme().compareTo("file") == 0) {
            try {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            filepath = uri.getPath();
        }
        return filepath;
    }


    protected void onActivityResult ( int req, int result, Intent data)
    {
//         TODO Auto-generated method stub
        super.onActivityResult(req, result, data);
        if (result == RESULT_OK) {
            Uri fileuri = data.getData();
            String   docFilePath = getFileNameByUri(this, fileuri);
            Log.d("FilePath", "onActivityResult: " + docFilePath);
//            private static final String CREATE_TABLE_Content = "CREATE TABLE " + TableContentClass.TABLE_Content + " ( "
//                    + TableContentClass.CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
//                    + TableContentClass.CONTENT_TITLE + " VARCHAR(255), "
//                    + TableContentClass.CONTENT_AUTHORID + " INT(11) NOT NULL, "
//                    + TableContentClass.CONTENT_GENREID + " INT(11), "
//                    + TableContentClass.CONTENT_TYPE + " VARCHAR(255), "
//                    + TableContentClass.CONTENT_FILE + " VARCHAR(255), "
//                    + TableContentClass.CONTENT_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP )\n";
            String revTitle = "", revType = "", title = "", type = "";
            for(int i = docFilePath.length() - 1; i >= 0; i--) {
                if(docFilePath.charAt(i) == '/') {
                    break;
                } else {
                    title += docFilePath.charAt(i);
                    if(docFilePath.charAt(i) == '.') {
                        revType = title;
                    }
                }
            }
            for(int i = revTitle.length() - 1; i >= 0; i--) {
                title += revTitle.charAt(i);
            }
            for(int i = revType.length() - 1; i >= 0; i--) {
                type += revType.charAt(i);
            }

            Log.d("bruno", docFilePath);
            Log.d("title", title);

            String userId = getIntent().getStringExtra("key_userId");
            if(type.equals(".png") || type.equals(".jpg") || type.equals(".jpeg"))
            {
                //public void InsertUserProfileData(DatabaseHelper dh, long user_id, String user_name, int user_followers, int user_following, int user_posts, String user_image)
                //db.InsertUserProfileData(db,userId, );
                String updateValues = " UPDATE " + TableUserProfileClass.TABLE_User_Profile + " SET " + TableUserProfileClass.USER_IMAGE + " = '"+docFilePath+"' WHERE "+ TableUserProfileClass.USER_ID+" =  "+userId;
                Log.d("In updation",updateValues);
                Cursor cursor = db.getReadableDatabase().rawQuery(updateValues,null);

                cursor.moveToFirst();

                /*if (cursor!= null && cursor.moveToFirst() && cursor.getCount()>0) {
                    CircleImageView profileImage = findViewById(R.id.imageview);
                    String picturePath = docFilePath;
                    profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                else{
                    Log.d("Db Error", "Database error in updation");
                }*/
                cursor.close();
                CircleImageView profileImage = findViewById(R.id.imageview);
                String picturePath = docFilePath;
                profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
            else
            {
                Toast.makeText(getApplicationContext(), "File Type not Supported", Toast.LENGTH_LONG).show();
                finish();
            }
            //db.InsertContentData(db, title, "someId", "someGenreId", type, docFilePath, new Date().toString());
        }
        //finish();
    }

    public void upload(View v)
    {
        b=(Button)findViewById(R.id.load);
        Intent newIntent = new Intent(this, UploadActivity.class);
        startActivity(newIntent);
    }
}
