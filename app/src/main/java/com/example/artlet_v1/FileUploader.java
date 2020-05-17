package com.example.artlet_v1;

    import android.content.Context;
import android.content.Intent;
    import android.content.SharedPreferences;
    import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
    import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class FileUploader extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_file_uploader);

        db = new DatabaseHelper(this);
        browseFiles();
    }


    private void browseFiles() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf + application/zip + application/epub + application/plain");
//        intent.setType("images/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult ( int req, int result, Intent data)
    {
//         TODO Auto-generated method stub
        super.onActivityResult(req, result, data);
        if (result == RESULT_OK) {
            Uri fileuri = data.getData();
            String   docFilePath = getFileNameByUri(this, fileuri);
            Log.d("FilePath", "onActivityResult: " + docFilePath);

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

            title = getIntent().getStringExtra("title");
            SharedPreferences settings = getApplicationContext().getSharedPreferences("YOUR_USER_ID", 0);
            int author_id = settings.getInt("user_id", 2); //0 is the default value
            String genre = getIntent().getStringExtra("genre_id");
            Log.d("genre", "onActivityResult: " + genre);
            String genre_id = getGenreId(genre, db);
            String tags = getIntent().getStringExtra("tags");
            ArrayList<String> tagsList = this.getTagsList(tags);


            if(type.equals(".zip"))
            {
//                Intent intent = new Intent(this, MangaReader.class);
//                intent.putExtra("zipPath", docFilePath);
//                startActivity(intent);
              type = "manga";
            }
            else if(type.equals(".epub"))
            {
//                Intent intent = new Intent(this, EpubReader.class);
//                intent.putExtra("epubPath", docFilePath);
//                startActivity(intent);
                type = "epub";
            }
            else if(type.equals(".pdf"))
            {
//                Intent intent = new Intent(this, PdfReader.class);
//                intent.putExtra("pdfPath", docFilePath);
//                startActivity(intent);
                type = "pdf";
            }
            else if(type.equals(".txt"))
            {
//                Intent intent = new Intent(this, DocActivity.class);
//                intent.putExtra("docPath", docFilePath);
//                intent.putExtra("editable", false);
////                intent.putExtra("editable", true);
//                startActivity(intent);
                type = "doc";
            }
            else
            {
                Log.d("bruno", docFilePath);
                Toast.makeText(getApplicationContext(), "File Type not Supported", Toast.LENGTH_LONG).show();
                finish();
            }


            int content_id = db.InsertContentData(db, title, author_id, genre_id, type, docFilePath, new Date().toString());
            this.insertTagsForContent(content_id, tagsList, db);
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private String getGenreId(String genre, DatabaseHelper db) {
//        return "1";
        genre = genre.toLowerCase();
        String id=null;

        Cursor c = db.getReadableDatabase().rawQuery("SELECT id FROM genre WHERE LOWER(name) LIKE ?", new String[]{genre+"%"});
        if(c!=null && c.moveToFirst())
        {
            id = c.getString(c.getColumnIndex("id"));
        }

        return id;
    }

    private void insertTagsForContent(int content_id, ArrayList<String> tagsList, DatabaseHelper db) {
        Log.d("tags", "insertTagsForContent: "+ content_id);
        Cursor c;
        for(int i = 0; i<tagsList.size(); i++) {
            c = db.getWritableDatabase().rawQuery("INSERT INTO tag (content_id, name) values (?,?)", new String[]{String.valueOf(content_id), String.valueOf(tagsList.get(i))});
        }

    }

    private ArrayList<String> getTagsList(String tags) {
        ArrayList<String> tagsList = new ArrayList<> (Arrays.asList(tags.split(",")));
        return  tagsList;
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
}
