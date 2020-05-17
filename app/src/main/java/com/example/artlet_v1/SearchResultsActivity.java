package com.example.artlet_v1;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.artlet_v1.Adapters.SearchViewAdapter;
import com.folioreader.FolioReader;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultsActivity extends AppCompatActivity {

    private  ArrayList<HashMap<String, String>> results= new ArrayList<>();
    private String query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        handleIntent(getIntent());
        String query = getIntent().getStringExtra(SearchManager.QUERY);
        this.query = query;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryLiveSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                queryLiveSearch(query);
                return false;
            }
        });
        searchView.setIconified(false);
        searchView.setQuery(this.query, false);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        Log.d("search", "onNewIntent");
        handleIntent(intent);
    }

    private void queryLiveSearch(String query) {
        Intent intent = new Intent(SearchResultsActivity.this, SearchResultsActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
    }

    private void handleIntent(Intent intent) {
//        Log.d("search", "intentReceived");
        String query = intent.getStringExtra(SearchManager.QUERY);
        this.query = query;
        showResults();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            showResults();
        }
    }

    private void showResults() {
//        Log.d("search", query);
        fetchDatabaseResults();
        displayResultList();

    }

    private void fetchDatabaseResults() {

        this.results.clear();

        String tableName = "content";

        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            String lowerQuery = this.query.toLowerCase();

            Cursor c = db.rawQuery("SELECT c1.id, title, type , file, c1.created_at  FROM content c1 INNER JOIN tag ON c1.id=tag.content_id WHERE LOWER(tag.name) LIKE ?  UNION \n" +
                    "SELECT c1.id, title, type , file, c1.created_at  FROM content c1 INNER JOIN genre ON c1.genre_id=genre.id WHERE LOWER(genre.name) LIKE ?  UNION\n" +
                    "SELECT c1.id, c1.title, c1.type , c1.file, c1.created_at  FROM content c1 INNER JOIN content c2 ON c1.id=c2.id WHERE LOWER(c1.title) LIKE ? ORDER BY c1.created_at DESC", new String[] {"%" + lowerQuery + "%", "%" + lowerQuery + "%", "%" + lowerQuery + "%"});

            if (c != null ) {
                if  (c.moveToFirst()) {
//                    Log.d("before-count", "" + c.getCount());
                    do {
                        HashMap<String,String> data = new HashMap<>();
                        String title = c.getString(c.getColumnIndex("title"));
                        String type = c.getString(c.getColumnIndex("type"));
                        String file = c.getString(c.getColumnIndex("file"));

                        data.put("title", title);
                        data.put("type", type);
                        data.put("file", file);
                        this.results.add(data);

                    }while (c.moveToNext());

                }
            }
            c.close();
            db.close();
        } catch (SQLiteException se ) {

            Log.e(getClass().getSimpleName(), "Could'nt Open the database");

        }
    }

    private void displayResultList() {
        ListView lv = (ListView) findViewById(R.id.user_list);
        if(this.results.size() == 0) {
            lv.setEmptyView(this.findViewById(R.id.empty));
        } else {

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap<String, String>  data = getItem(position);
                    String fileType = data.get("type");
                    String filePath = data.get("file");
                    openContentReader(fileType, filePath);

                }
            });
            ListAdapter adapter = new SearchViewAdapter(SearchResultsActivity.this, this.results);
            lv.setAdapter(null);
            lv.setAdapter(adapter);
        }
    }

    //use filePath when upload work will be completed by manga and pdf teams
    public void openContentReader(String fileType, String filePath) {
        Log.d("ABC", "opencontent");
        switch (fileType) {
            case "pdf" : openPdfReader(filePath);
                break;
            case "epub" : openEpub(filePath);
                break;
            case "manga" : openManga(filePath);
                break;
            case "doc" : openDocReader(filePath);
                break;
        }
    }

    public HashMap<String, String> getItem(int position) {
        return this.results.get(position);
    }

    public void openManga(String docFilePath) {
        Intent intent = new Intent(this, MangaReader.class);
        intent.putExtra("zipPath", docFilePath);
        startActivity(intent);
    }

    public void openEpub(String docFilePath) {
        Intent intent = new Intent(this, EpubReader.class);
        intent.putExtra("epubPath", docFilePath);
        startActivity(intent);
    }

    public void openPdfReader(String docFilePath) {
        Intent intent = new Intent(this, PdfReader.class);
        intent.putExtra("pdfPath", docFilePath);
        startActivity(intent);
    }

    public void openDocReader(String docFilePath) {
        Intent intent = new Intent(this, DocActivity.class);
        intent.putExtra("docPath", docFilePath);
        intent.putExtra("editable", false);
//                intent.putExtra("editable", true);
        startActivity(intent);
    }
}




