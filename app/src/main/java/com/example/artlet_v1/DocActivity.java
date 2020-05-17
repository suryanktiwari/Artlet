package com.example.artlet_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.microedition.khronos.egl.EGLDisplay;

public class DocActivity extends AppCompatActivity {

    EditText docTitle;
    EditText editDoc;
    TextView txtDocName, showText;
    Button btnSave;
    String docName = "";

    String filePath, filePathNew="";
    Boolean editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);

        docTitle = (EditText)findViewById(R.id.editDoc);
        //editDoc = (EditText)findViewById(R.id.editDoc);
        txtDocName = (TextView)findViewById(R.id.docName);
        showText = (TextView)findViewById(R.id.showText);
        Intent intent = getIntent();
        filePath = intent.getStringExtra("docPath");
        editable = intent.getBooleanExtra("editable", false);
        btnSave = (Button)findViewById(R.id.btn_save);
        File file = new File(filePath);
        for(int i = 0; i < filePath.length(); i++) {
            if(filePath.charAt(i) == '/') {
                docName = "";
            } else {
                docName += filePath.charAt(i);
            }
        }
        txtDocName.setText(docName);
        if(editable)
        {
            txtDocName.setVisibility(View.VISIBLE);
        }
        else
        {
            showText.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
        }

        int lastindex = filePath.lastIndexOf('/');
        filePathNew = filePath.substring(0,lastindex);
        readFileInternalStorage();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeFileInternalStorage();
//                setupTextFieldsByContact();
            }
        });
    }

    public void readFileInternalStorage() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuffer sb = new StringBuffer();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = reader.readLine();
            }
            if(editable)
                docTitle.setText(sb.toString());
            else
                showText.setText(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeFileInternalStorage(

    ) {
       // String value = editDoc.getText().toString().trim();
        createUpdateFile();
    }

    private void createUpdateFile() {
        File file = new File(filePathNew+'/'+txtDocName.getText().toString().trim());
        FileOutputStream outputStream = null;
        try {
            String new_value = editDoc.getText().toString().trim();
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            outputStream = new FileOutputStream(file, false);
            outputStream.write(new_value.getBytes());
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tts(View v)
    {
        Intent newIntent = new Intent(this, TTS.class);
        if(editable)
            newIntent.putExtra("toSpeak", docTitle.getText().toString());
        else
            newIntent.putExtra("toSpeak", showText.getText().toString());
        startActivity(newIntent);
    }

}
