package com.example.artlet_v1;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.artlet_v1.DbProvider.ContentTableProvider;
import com.example.artlet_v1.DbProvider.GenreTableProvider;
import com.example.artlet_v1.DbProvider.TagTableProvider;
import com.example.artlet_v1.DbProvider.UserGenreTableProvider;
import com.example.artlet_v1.DbProvider.UserTableProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FirstTime extends Application {

    DatabaseHelper db;

    final String UNZIP_DIR = "imageDir";
    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences ins  = PreferenceManager.getDefaultSharedPreferences(this);
        if (!ins.getBoolean("firstTime", false)) {

            Log.d("Inside if","bool is false");
            this.db = new DatabaseHelper(getApplicationContext());
            DatabaseHelper d1 = new DatabaseHelper(this);
            d1.InsertGenreData();
            GenreTableProvider g1 = new GenreTableProvider(this);
            UserTableProvider u0 = new UserTableProvider(this);
            ContentTableProvider c1 = new ContentTableProvider(this);
            TagTableProvider t1 = new TagTableProvider(this);
            UserGenreTableProvider u1 = new UserGenreTableProvider(this);
            u0.populateDataUser();
            //g1.populateDataGenre();

//            u1.populateDataUserGenre();
//            t1.populateDataTag();


            Log.d("Inside if","bool is still false");

            SharedPreferences.Editor editor = ins.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
            Log.d("Inside if","bool is true now");

            createFolder(UNZIP_DIR);
            copyToAssets(UNZIP_DIR, c1);
        }
    }

    public void createFolder(String fname) {

        //make directory
        String myfolder = getApplicationContext().getExternalFilesDir("/") + "/" + fname;
        File f = new File(myfolder);
        if (!f.exists())
            if (!f.mkdir()) {
                Toast.makeText(this, myfolder + " can't be created.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, myfolder + " can be created.", Toast.LENGTH_LONG).show();
                f.mkdirs();
            }
        else
            Toast.makeText(this, myfolder + " already exits.", Toast.LENGTH_LONG).show();
    }


    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void copyToAssets(String folder, ContentTableProvider c1)
    {
        String myfolder = getApplicationContext().getExternalFilesDir("/") + "/" + folder;

        ///////////////////////////////////////////////////////////////////
        // Copy file from assets to folder
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);

                File outFile = new File(myfolder, filename);

                out = new FileOutputStream(outFile);
                String filePath = myfolder + "/" + filename;

                if(filename.equals("tut8_sol.pdf")) {
                    c1.populateDataContent("pdffile","pdf",filePath);
                }

                if(filename.equals("NOTICE.txt")) {
                    c1.populateDataContent("textfile","doc",filePath);
                }

                if(filename.equals("epubfile.epub")) {
                    c1.populateDataContent("epub-content","epub",filePath);
                }


//                if(filename.equals("Blep.zip")) {
//                    c1.populateDataContent("manga","manga",filePath);
//                }

                Log.d("file", filename);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
        ///////////////////////////////////////////////////////////////////////////

    }
}

