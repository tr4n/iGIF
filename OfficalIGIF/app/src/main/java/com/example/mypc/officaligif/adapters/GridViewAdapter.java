package com.example.mypc.officaligif.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.activities.MainActivity;
import com.example.mypc.officaligif.fragments.SearchFragment;
import com.example.mypc.officaligif.messages.SuggestTopicSticky;
import com.example.mypc.officaligif.models.SuggestTopicModel;
import com.example.mypc.officaligif.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    List<SuggestTopicModel> suggestTopicModelList = new ArrayList<>();
    Context context;

    public GridViewAdapter(List<SuggestTopicModel> suggestTopicModelList, Context context) {
        this.suggestTopicModelList = suggestTopicModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return suggestTopicModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestTopicModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.layout_main_topic_item, null);

        TextView tvMainTopic = convertView.findViewById(R.id.tv_main_topic);
        RecyclerView recyclerView = convertView.findViewById(R.id.rv_topic_list);

        final SuggestTopicModel suggestTopicModel = suggestTopicModelList.get(position);

        tvMainTopic.setText(suggestTopicModel.key);
      //tvMainTopic.setBackgroundColor(Color.parseColor(suggestTopicModel.color));

        TopicAdapter topicAdapter = new TopicAdapter(suggestTopicModel, context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(topicAdapter);

        tvMainTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().postSticky(new SuggestTopicSticky(suggestTopicModel.key));

                Utils.replaceFragmentTag(
                        ( (MainActivity) context).getSupportFragmentManager(),
                        R.id.cl_main_activity,
                        new SearchFragment(),
                        "search_fragment_base"
                );
            }
        });


        return convertView;
    }
}
