package com.example.mypc.officaligif.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.activities.MainActivity;
import com.example.mypc.officaligif.fragments.SearchFragment;
import com.example.mypc.officaligif.messages.SuggestTopicSticky;
import com.example.mypc.officaligif.models.SuggestTopicModel;
import com.example.mypc.officaligif.models.TopicModel;
import com.example.mypc.officaligif.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import static android.view.View.GONE;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> implements View.OnClickListener {

    public SuggestTopicModel suggestTopicModel;
    public Context context;

    public TopicAdapter(SuggestTopicModel suggestTopicModel, Context context) {
        this.suggestTopicModel = suggestTopicModel;
        this.context = context;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_topic_model, null);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        holder.setData(suggestTopicModel, position, context);

    }

    @Override
    public int getItemCount() {
        return suggestTopicModel.topicList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView tvTitleTopic;
        private ImageView ivTopic, ivLoadingTopic;

        public TopicViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.tvTitleTopic = itemView.findViewById(R.id.tv_title_topic);
            this.ivTopic = itemView.findViewById(R.id.iv_topic);
            this.ivLoadingTopic = itemView.findViewById(R.id.iv_loading_topic);


        }

        public void setData(SuggestTopicModel suggestTopicModel, int position, final Context context) {
            //  Utils.setTopicModelData(itemView, suggestTopicModel, position, context);
            final TopicModel topicModel = suggestTopicModel.topicList.get(position);
            int widthScreen = Resources.getSystem().getDisplayMetrics().widthPixels;
            int width = (int) (widthScreen * 0.4);
            int height = (int) (widthScreen * 0.4);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            ivLoadingTopic.setLayoutParams(layoutParams);
            ivLoadingTopic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivTopic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivTopic.setLayoutParams(layoutParams);

            tvTitleTopic.setText(topicModel.key);
            tvTitleTopic.setSelected(true);
            tvTitleTopic.setBackgroundColor(Color.parseColor(suggestTopicModel.color));
            ivLoadingTopic.setImageResource(Utils.gerRandomResourceColor());
            ivLoadingTopic.setVisibility(View.VISIBLE);
            ivTopic.setVisibility(GONE);

            Glide.with(context)
                    .load(topicModel.url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            ivTopic.setVisibility(View.VISIBLE);
                            ivLoadingTopic.setVisibility(View.GONE);

                            return false;
                        }
                    })
                    .into(ivTopic);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EventBus.getDefault().postSticky(new SuggestTopicSticky(topicModel.key));

                    Utils.replaceFragmentTag(
                            ( (MainActivity) context).getSupportFragmentManager(),
                            R.id.cl_main_activity,
                            new SearchFragment(),
                            "search_fragment_base"
                    );

                }
            });


        }
    }
}
