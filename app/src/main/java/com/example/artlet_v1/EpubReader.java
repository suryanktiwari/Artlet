package com.example.artlet_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.folioreader.FolioReader;
import com.folioreader.ui.activity.FolioActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EpubReader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epub_reader);
        Intent intent = getIntent();
        String path = intent.getStringExtra("epubPath");
        Log.d("bruno", path);
        FolioReader folioReader = FolioReader.get();
        folioReader.openBook(path);
        finish();
    }
}
