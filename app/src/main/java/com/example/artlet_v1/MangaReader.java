package com.example.artlet_v1;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class MangaReader extends AppCompatActivity {

    private ViewPager viewpager;
    private FragmentCollectionAdapter adapter;

    private String file_name = "";
    private Boolean flag;

    final String UNZIP_DIR = "imageDir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_reader);
        viewpager = findViewById(R.id.pager);

        String temp = "";
        flag=true;
        Intent intent = getIntent();
        String path = intent.getStringExtra("zipPath");

        for (int i = path.length() - 5; i >= 0; i--) {
            if (path.charAt(i) != '/')
                temp += path.charAt(i);
            else
                break;
        }
        for (int i = temp.length() - 1; i >= 0; i--) {
            file_name += temp.charAt(i);
        }
        Log.d("bruno", file_name);
        Log.d("bruno", path);
        Log.d("bruno", getApplicationContext().getExternalFilesDir("/") + "/" + UNZIP_DIR);


        createFolder(UNZIP_DIR);
        copyFile(path, file_name+".zip", getApplicationContext().getExternalFilesDir("/") + "/" + UNZIP_DIR + "/");
        if(flag)
            unzip(file_name, UNZIP_DIR);
        showManga();
    }

    private void showManga()
    {

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getExternalFilesDir("/" + UNZIP_DIR + "/"+file_name+"/");
        Log.d("bruno", getApplicationContext().getExternalFilesDir("/") + "/" + UNZIP_DIR + "/"+file_name+"/");
        Log.d("bruno", directory.getPath());

        List<File> fileList = new ArrayList<>();
        File [] f = directory.listFiles();
        Log.d("bruno", "length= "+f.length);

        Arrays.sort(f);
        Log.d("bruno", "Sorted");
        for (int i = 0; i < f.length; i++)
        {
            Log.d("bruno", "FileName:" + f[i].getName());
            fileList.add(f[i]);
        }
        ImageFragment.images = fileList;
        adapter = new FragmentCollectionAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.setPageTransformer(false, new ZoomOutPageTransformer());

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

    void unzip(String fileName, String unzipPath)
    {    // unzip file
        File f = new File(getApplicationContext().getExternalFilesDir("/") + "/" + unzipPath + "/" + fileName+"Z");
        if (!f.exists()) {
            Toast.makeText(this, "Unzipped", Toast.LENGTH_LONG).show();
            String zipFile = getApplicationContext().getExternalFilesDir("/")  + "/" + unzipPath + "/" + fileName + ".zip";
            String unzipLocation = getApplicationContext().getExternalFilesDir("/") + "/" + unzipPath  + "/" + fileName +"/";
            Log.d("Hocus", "Zip = "+zipFile);
            Log.d("Hocus", "Loc ="+unzipLocation);
            ZipDecompress df = new ZipDecompress(zipFile, unzipLocation);
            df.unzip();
        } else {
            Toast.makeText(this, "Already Unzipped", Toast.LENGTH_LONG).show();
        }

    }

    private void copyFile(String inputPath, String inputFile, String outputPath) {

        File file = new File (outputPath + inputFile);
        if (file.exists())
        {
            Log.d("Hocus", "Already Exists");
            flag = false;
            return;
        }

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

}

