package com.example.artlet_v1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.artlet_v1.UserProfile.UserProfileActivity;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;


@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<Item> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private Context context;
    private int num_likes;

    public FoldingCellListAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.genre_name = cell.findViewById(R.id.genre_name);
            viewHolder.time = cell.findViewById(R.id.title_time_label);
            viewHolder.date = cell.findViewById(R.id.title_date_label);
            viewHolder.content_name = cell.findViewById(R.id.title_from_address);
            viewHolder.type = cell.findViewById(R.id.title_to_address);
            viewHolder.requestsCount = cell.findViewById(R.id.content_deadline_time);
            viewHolder.pledgePrice = cell.findViewById(R.id.title_requests_count);
            viewHolder.contentRequestBtn = cell.findViewById(R.id.content_request_btn);
            viewHolder.artistName = cell.findViewById(R.id.content_name_view);
            viewHolder.likeButton = cell.findViewById(R.id.content_like_button);
            viewHolder.content_id = cell.findViewById(R.id.top_content_id);
            viewHolder.authorName = cell.findViewById(R.id.authorName);
            viewHolder.UploadDate = cell.findViewById(R.id.content_delivery_time);
            viewHolder.artistImage = cell.findViewById(R.id.artist_image);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        // bind data from selected element to view through view holder
        String s = item.getTime();
        String[] parts = s.split(" ");
        viewHolder.genre_name.setText(item.getGenre_name());
        viewHolder.time.setText(parts[0]);
        viewHolder.date.setText(item.getDate());
        viewHolder.content_name.setText(item.getContent_name());
        viewHolder.type.setText(item.getType());
        viewHolder.requestsCount.setText(String.valueOf(item.getRequestsCount()));
        //viewHolder.pledgePrice.setText(item.getNum_likes());
        viewHolder.artistName.setText(item.getUser_name());
        viewHolder.authorName.setText(item.getUser_name());
        viewHolder.content_id.setText(item.getContentId());
        viewHolder.UploadDate.setText(parts[0]);
//        viewHolder.artistImage.set;

        // set custom btn handler for list item from that item
        if (item.getRequestBtnClickListener() != null) {
            viewHolder.contentRequestBtn.setOnClickListener(item.getRequestBtnClickListener());
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            final SQLiteDatabase db = dbHelper.getReadableDatabase();
            String content_id = viewHolder.content_id.getText().toString();
            SharedPreferences settings = context.getSharedPreferences("YOUR_USER_ID", 0);
            final int user_id = settings.getInt("user_id", 1); //0 is the default value
            viewHolder.likeButton.setText("Like");
            if(checkIfAlreadyLiked(db, content_id, String.valueOf(user_id)))
            {
                viewHolder.likeButton.setBackgroundResource(R.color.highlight_blue);
                viewHolder.likeButton.setText("Unlike");
            }

            viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    final SQLiteDatabase db = dbHelper.getReadableDatabase();
                    String content_id = viewHolder.content_id.getText().toString();
                    SharedPreferences settings = context.getSharedPreferences("YOUR_USER_ID", 0);
                    int user_id = settings.getInt("user_id", 1); //0 is the default value

                    if(checkIfAlreadyLiked(db, content_id, String.valueOf(user_id))) {
//                        v.setBackgroundResource(R.drawable.round);

                        decrementLikesCount(db,content_id, viewHolder);
                        removeLike(db, content_id, user_id);
                        v.setBackgroundResource(R.color.highlight_yellow);
                    }
                    else {
                        insertIntoLikeTable(db, user_id, content_id);
                        incrementLikesCount(db, content_id, viewHolder);
                        v.setBackgroundResource(R.color.highlight_blue);
                    }
                }
            });

            final String contentId = viewHolder.content_id.getText().toString();
            viewHolder.artistName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cursor c  = db.rawQuery("SELECT author_id FROM content where content.id = ?", new String[]{contentId});
                    if(c!=null && c.moveToFirst())
                    {
                        int content_author_id = c.getInt(0);
                        Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra("key_userId", ""+content_author_id);
                    intent.putExtra("view_userid", ""+user_id);
                        context.startActivity(intent);
                    }
                }
            });

            viewHolder.artistName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cursor c  = db.rawQuery("SELECT author_id FROM content where content.id = ?", new String[]{contentId});
                    if(c!=null && c.moveToFirst())
                    {
                        int content_author_id = c.getInt(0);
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra("key_userId", ""+content_author_id);
                        intent.putExtra("view_userid", ""+user_id);
                        context.startActivity(intent);
                    }
                }
            });

            viewHolder.artistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cursor c  = db.rawQuery("SELECT author_id FROM content where content.id = ?", new String[]{contentId});
                    if(c!=null && c.moveToFirst())
                    {
                        int content_author_id = c.getInt(0);
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra("key_userId", ""+content_author_id);
                        intent.putExtra("view_userid", ""+user_id);
                        context.startActivity(intent);
                    }
                }
            });



        } else {

            viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
            viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("inside like", "onClick: liked");
                }
            });

        }

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView genre_name;
        TextView contentRequestBtn;
        TextView artistName;
        TextView pledgePrice;
        TextView content_name;
        TextView type;
        TextView requestsCount;
        TextView date;
        TextView time;
        TextView content_id;
        TextView likeButton;
        TextView authorName;
        TextView UploadDate;
        ImageView artistImage;
    }

    public void insertIntoLikeTable(SQLiteDatabase db, int user_id, String content_id) {
        db.execSQL("INSERT INTO 'likes' ('user_id', 'content_id' ) VALUES (?, ?)", new String[]{String.valueOf(user_id), String.valueOf(content_id)});
//        Cursor c = db.rawQuery("INSERT INTO like values(")
        Log.d("aaa", "insertIntoLikeTable: insert like");
    }

    public boolean checkIfAlreadyLiked(SQLiteDatabase db, String content_id, String user_id) {
        Cursor c  = db.rawQuery("SELECT * FROM likes where content_id = ? and user_id= ?", new String[]{content_id, user_id});
        if(c.getCount() > 0) {
            return true;
        }
        return false;
    }

    public void removeLike(SQLiteDatabase db, String content_id, int user_id) {
//        Cursor c  = db.rawQuery("DELETE FROM likes where content_id = ? and user_id= ?", new String[]{content_id, "1"});
        db.execSQL("delete from likes where content_id="+content_id+ " and " + user_id);

    }

    public void decrementLikesCount(SQLiteDatabase db, String content_id, ViewHolder viewHolder) {

        Log.d("aaa", "decrement likes");
        Cursor c  = db.rawQuery("SELECT likes FROM content where id = ?", new String[]{content_id});
        if(c!=null && c.moveToFirst())
        {
            num_likes = c.getInt(c.getColumnIndex("likes"));
        }
        num_likes = num_likes-1;
        ContentValues cv = new ContentValues();
        cv.put(TableContent.TableContentClass.CONTENT_LIKES, num_likes );
        db.update(TableContent.TableContentClass.TABLE_Content, cv, TableContent.TableContentClass.CONTENT_ID + " = ?", new String[]{content_id} );
        viewHolder.requestsCount.setText(String.valueOf(num_likes));
        viewHolder.likeButton.setText("Like");

    }

    public void incrementLikesCount(SQLiteDatabase db, String content_id, ViewHolder viewHolder) {

        Cursor c  = db.rawQuery("SELECT likes FROM content where id = ?", new String[]{content_id});
        if(c!=null && c.moveToFirst())
        {
            num_likes = c.getInt(c.getColumnIndex("likes"));
        }

        num_likes = num_likes+1;
        ContentValues cv = new ContentValues();
        cv.put(TableContent.TableContentClass.CONTENT_LIKES, num_likes );
        db.update(TableContent.TableContentClass.TABLE_Content, cv, TableContent.TableContentClass.CONTENT_ID + " = ?", new String[]{content_id} );
        viewHolder.requestsCount.setText(String.valueOf(num_likes));
        viewHolder.likeButton.setText("Unlike");
    }
}
