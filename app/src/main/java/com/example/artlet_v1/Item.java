package com.example.artlet_v1;

import android.database.Cursor;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Item {

    private String genre_name;
    private String num_likes;
    private String content_name;
    private String type;
    private int requestsCount;
    private String date;
    private String time;
    private String content_id;
    private String user_name;
    private String file_path;

    private View.OnClickListener requestBtnClickListener;
    private View.OnClickListener likeBtnClickListener;

    public Item() {
    }

    public Item(String genre_name, String num_likes, String content_name, String type, int requestsCount, String date, String time, String content_id, String user_name, String file_path) {
        this.genre_name = genre_name;
        this.num_likes = num_likes;
        this.content_name = content_name;
        this.type = type;
        this.requestsCount = requestsCount;
        this.date = date;
        this.time = time;
        this.content_id = content_id;
        this.user_name = user_name;
        this.file_path = file_path;
    }

    public String getGenre_name() {
        return genre_name;
    }

    public void setGenre_name(String genre_name) {
        this.genre_name = genre_name;
    }

    public String getNum_likes() {
        return num_likes;
    }

    public void setNum_likes(String num_likes) {
        this.num_likes = num_likes;
    }

    public String getContent_name() {
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRequestsCount() {
        return requestsCount;
    }

    public void setRequestsCount(int requestsCount) {
        this.requestsCount = requestsCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContentId(String id) {
        this.content_id = id;
    }

    public String getContentId() {
        return content_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {

        this.requestBtnClickListener = requestBtnClickListener;
    }

    public void setLikeBtnClickListener(View.OnClickListener likeBtnClickListener) {
        this.likeBtnClickListener = likeBtnClickListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (requestsCount != item.requestsCount) return false;
        if (genre_name != null ? !genre_name.equals(item.genre_name) : item.genre_name != null) return false;
        if (num_likes != null ? !num_likes.equals(item.num_likes) : item.num_likes != null)
            return false;
        if (content_name != null ? !content_name.equals(item.content_name) : item.content_name != null)
            return false;
        if (type != null ? !type.equals(item.type) : item.type != null)
            return false;
        if (date != null ? !date.equals(item.date) : item.date != null) return false;
        return !(time != null ? !time.equals(item.time) : item.time != null);

    }

    @Override
    public int hashCode() {
        int result = genre_name != null ? genre_name.hashCode() : 0;
        result = 31 * result + (num_likes != null ? num_likes.hashCode() : 0);
        result = 31 * result + (content_name != null ? content_name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + requestsCount;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    /**
     * @return List of elements prepared for tests
     */
    public static ArrayList<Item> getContentList(Cursor c, Cursor d) {
        ArrayList<Item> items = new ArrayList<>();
        if (c != null ) {
            if  (c.moveToFirst()) {
                Log.d("before-count", "" + c.getCount());
                if(d!=null && d.moveToFirst())
                {
                    int x = d.getInt(1);
                    do {
                        items.add(new Item(c.getString(c.getColumnIndex("genre_name")), c.getString(c.getColumnIndex("likes")), c.getString(c.getColumnIndex("title")), c.getString(c.getColumnIndex("type")), x, " ", c.getString(c.getColumnIndex("created_at")), c.getString(c.getColumnIndex("content_id")), c.getString(2), c.getString(c.getColumnIndex("file"))));

                    } while (c.moveToNext() && d.moveToNext());

                }
                do {
                    items.add(new Item(c.getString(c.getColumnIndex("genre_name")), c.getString(c.getColumnIndex("likes")), c.getString(c.getColumnIndex("title")), c.getString(c.getColumnIndex("type")), 0, " ", c.getString(c.getColumnIndex("created_at")), c.getString(c.getColumnIndex("content_id")), c.getString(2), c.getString(c.getColumnIndex("file"))));

                } while (c.moveToNext());



            }
        }
//        items.add(new Item("957", "521", "One Piece", "Manga", 300, "TODAY", "05:10 PM", "aaa"));
//        c.close();
        return items;
    }
}