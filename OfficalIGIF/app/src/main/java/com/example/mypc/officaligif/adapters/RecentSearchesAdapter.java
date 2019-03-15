package com.example.mypc.officaligif.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mypc.officaligif.R;

import java.util.List;

public class RecentSearchesAdapter extends BaseAdapter {

    List<String> recentTopicList ;


    public RecentSearchesAdapter(List<String> recentTopicList) {
        this.recentTopicList = recentTopicList;
    }

    @Override
    public int getCount() {
        return recentTopicList.size();
    }

    @Override
    public Object getItem(int position) {
        return recentTopicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recent_search, null);
        TextView tvRecentTopic = convertView.findViewById(R.id.tv_recent_topic);
        tvRecentTopic.setText(recentTopicList.get(position));
        tvRecentTopic.setSelected(true);





        return convertView;
    }
}
