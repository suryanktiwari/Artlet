package com.example.artlet_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UploadActivity extends AppCompatActivity {

    boolean clicked=false;
    private EditText auth_name;
    private EditText title;
    private EditText Genre;
    private EditText FileType;
    private EditText Tags;
    private Button File;
    private Button Upload;
    private  String genre;
    private  static String[] paths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        buttonUpload();
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        getListGenre();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                genre =  spinner.getSelectedItem().toString();
//                System.out.println(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void buttonUpload()
    {
        title=(EditText)findViewById(R.id.edit2);
//        Genre=(EditText) findViewById(R.id.edit3);
        Tags=(EditText)findViewById(R.id.edit5);

        File=(Button)findViewById(R.id.filebutton);

//        Upload=(Button)findViewById(R.id.finalbutton);

        File.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked=true;

                String ttle=title.getText().toString();
//                String gnre=Genre.getText().toString(); //to be changed
                String tgs=Tags.getText().toString();
                if(!(ttle.matches("" )||genre.matches("")||tgs.matches("")))
                {
                    Intent i = new Intent(view.getContext(), FileUploader.class);

                    i.putExtra("genre_id", genre);
                    i.putExtra("title", ttle);
                    i.putExtra("tags", tgs);

                    startActivity(i);
                    Log.d("upload", "onClick: upload file");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"You missed something!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void getListGenre() {
        DatabaseHelper dbHelper = new DatabaseHelper(UploadActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int i = 0;
        Cursor c = db.rawQuery("SELECT name FROM genre", null);
        paths = new String[c.getCount()];
        while (c.moveToNext()) {
            String uname = c.getString(c.getColumnIndex("name"));
            paths[i] = uname;
            i++;
        }
    }
}
