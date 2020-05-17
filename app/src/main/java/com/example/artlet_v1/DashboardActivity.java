package com.example.artlet_v1;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.artlet_v1.UserProfile.UserProfileActivity;
import com.folioreader.FolioReader;
import com.google.android.material.navigation.NavigationView;
import com.ramotion.foldingcell.FoldingCell;
import android.widget.Button;
import java.util.ArrayList;


public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper db;
    MangaReader mr;
    private DrawerLayout drawer;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ListView theListView = findViewById(R.id.mainListView);

        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());

        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT content.id as content_id , genre.name as genre_name, user.name as user_name, title, type, likes ,content.created_at as created_at,file FROM content INNER JOIN user ON content.author_id = user.id INNER JOIN genre ON content.genre_id = genre.id ORDER BY content.id DESC", null);

        Cursor d = db.rawQuery("SELECT user_id, COUNT(id) FROM likes GROUP BY user_id, content_id ", null);
        // prepare elements to display
        final ArrayList<Item> items = Item.getContentList(c,d);

//        String type = c.getString(c.getColumnIndex("type"));

        int count = 0;
        if(c.getCount() > 0) {
            if (c != null) {

                if (c.moveToFirst()) {
                    Log.d("before-count", "" + c.getCount());
                    do {
                        final String temp = c.getString(c.getColumnIndex("type"));

                        final String file_path = c.getString(c.getColumnIndex("file"));

                        items.get(count).setRequestBtnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                            testManga();
                                switch (temp) {
                                    case "pdf":
                                        openPdf(file_path);
                                        break;
                                    case "epub":
                                        openEpub(file_path);
                                        break;
                                    case "manga":
                                        openManga(file_path);
                                        break;
                                    case "doc":
                                        openDoc(file_path);
                                        break;
                                }
                            }
                        });
//
//                        }
//                    });
                        items.get(count).setLikeBtnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("inside like", "onClick: liked");
//                            testManga();
                                int user_id = 1;

                            }
                        });
                        count++;
                    } while (c.moveToNext());
                }
            }


            // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
            final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, items);

            // add default btn handler for each request btn on each item if custom handler not found
            adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
                }
            });

            // set elements to adapter
            theListView.setAdapter(adapter);

            // set on click event listener to list view
            theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    // toggle clicked cell state
                    ((FoldingCell) view).toggle(false);
                    // register in adapter that state for selected cell is toggled
                    adapter.registerToggle(pos);
                }
            });
        }
//        getSupportActionBar().setLogo(R.drawable.ic_hamburger);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        SpannableString s = new SpannableString(settingsItemTitle);
//
//        s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_LEFT), 0, s.length(), 0);
//
//        item.setTitle(s);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.drawer=findViewById(R.id.dashboardactivity);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,
                this.drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().show();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().hide();
                TextView username = findViewById(R.id.usernameNav);
                TextView useremail = findViewById(R.id.useremailNav);
                String uname = getIntent().getStringExtra("key_username");
                String uemail = getIntent().getStringExtra("key_email");
                if(uname!=null && uname.length()>0){
                    username.setText(uname.substring(0, 1).toUpperCase() + uname.substring(1));
                    useremail.setText(uemail);
                }

            }
        };
        this.drawer.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
//                Toast.makeText(this, "click..!!", Toast.LENGTH_SHORT).show();
                this.drawer.openDrawer(Gravity.LEFT);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.d("In DashboardActivity", "Above::Going to profiles");
        switch (menuItem.getItemId()){
            case R.id.nav_leaderboard:
                Intent intent1 = new Intent(this, LeaderBoardActivity.class);
                startActivity(intent1);
                break;

            case R.id.nav_feedback:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.nav_profile:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragmentt()).commit();

                Log.d("In DashboardActivity", "Going to profiles");
                String userId = getIntent().getStringExtra("key_userid");
                Intent goToUserProfileIntent = new Intent(this, UserProfileActivity.class);
                goToUserProfileIntent.putExtra("key_userId", userId);
                goToUserProfileIntent.putExtra("view_userid", userId);
                startActivity(goToUserProfileIntent);
                break;

            // upload
            case R.id.nav_polls:
//
                Intent intent2 = new Intent(this, UploadActivity.class);
                startActivity(intent2);
                break;
//
//            case R.id.nav_send:
//                Toast.makeText(this,"SEND",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.nav_share:
//                Toast.makeText(this,"SHARE",Toast.LENGTH_SHORT).show();
//                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    // add in every common activity
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo( searchManager.getSearchableInfo(new
                ComponentName(this,SearchResultsActivity.class)));

//        MenuItem appBar = findViewById(R.id.navBar);
        MenuItem appBar = menu.getItem(0);

        appBar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getSupportActionBar().hide();
                getActionBar().hide();
                openNavBar();
                return true;
            }
        });
        return true;

    }

    public void openNavBar() {
        this.drawer.openDrawer(Gravity.LEFT);
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

    public void openPdf(String docFilePath) {
        Intent intent = new Intent(this, PdfReader.class);
        intent.putExtra("pdfPath", docFilePath);
        startActivity(intent);
    }

    public void openDoc(String docFilePath) {
        Intent intent = new Intent(this, DocActivity.class);
        intent.putExtra("docPath", docFilePath);
        intent.putExtra("editable", false);
//                intent.putExtra("editable", true);
        startActivity(intent);
    }
}