package com.example.artlet_v1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.artlet_v1.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> results ;
    ArrayList<String> UserName;

    public SearchViewAdapter(
            Context context,
            ArrayList<HashMap<String, String>> results
    )
    {

        this.context = context;
        this.results = results;

    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.list_view_item, null);

            holder = new Holder();

            holder.textviewname = (TextView) child.findViewById(R.id.title);
            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }

        HashMap<String, String> value = this.results.get(position);

        holder.textviewname.setText(value.get("title"));
//      holder.textviewfile.setText(file.get(position));

        return child;
    }

    public class Holder {
        TextView textviewid;
        TextView textviewname;
    }

    public int getCount() {
        return results.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
}
