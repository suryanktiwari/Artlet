package com.example.artlet_v1;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.artlet_v1.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class LeaderBoardActivity extends AppCompatActivity {



    ListView lv;
    CircleImageView first,second,third;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_leaderboard);
        DatabaseHelper dh = new DatabaseHelper(this.getApplicationContext());
        SQLiteDatabase db = dh.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT user.name,  sum(likes) as likeCount from content inner join user on user.id = content.author_id group by content.author_id order by sum(likes) desc", null);
        final String[] title=new String[c.getCount()] ;
        final String[] likes=new String[c.getCount()] ;
        lv = (ListView) findViewById(R.id.list_leader);
        first=(CircleImageView)findViewById(R.id.imageview2);
        second=(CircleImageView)findViewById(R.id.imageview1);
        third=(CircleImageView)findViewById(R.id.imageview3);
        Drawable myDrawabl1 = getResources().getDrawable(R.drawable.manga);
        Drawable myDrawabl2 = getResources().getDrawable(R.drawable.artlet);
        Drawable myDrawabl3 = getResources().getDrawable(R.drawable.epub);
        if(c!=null && c.moveToFirst())
        {
            int i=0;
            do {
                String uname = c.getString(c.getColumnIndex("name"));
                title[i]=uname;
                String like = c.getString(c.getColumnIndex("likeCount"));
                likes[i] = like;

                if(i==0){
                    first.setImageDrawable(myDrawabl2);
                }
                if(i==1){
                    second.setImageDrawable(myDrawabl1);
                }
                if(i==2){
                    third.setImageDrawable(myDrawabl3);
                }
                i=i+1;
            }while(c.moveToNext());
        }


        // getAuthorname();
        CustomAdapter adapter = new CustomAdapter(this,title, likes);
        lv.setAdapter(adapter);
    }

//    public void getAuthorname()
//    {
//
//
//
//    }



}

class CustomAdapter extends ArrayAdapter<String>
{
    Context context;
    String[] title;
    String[] likes;


    CustomAdapter(Context c, String[] title, String[] likes)
    {

        super(c, R.layout.item_list_leader,title);
        this.context = c;
        this.title=title;
        this.likes=likes;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = vi.inflate(R.layout.item_list_leader, parent, false);
        TextView titlee = (TextView) row.findViewById(R.id.textView1);
        int pos = position+1;
        titlee.setText(title[position] + "," + likes[position]);
        pos++;
        return row;
    }

}