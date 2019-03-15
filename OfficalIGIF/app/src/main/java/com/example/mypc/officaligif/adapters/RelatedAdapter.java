package com.example.mypc.officaligif.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.activities.MainActivity;
import com.example.mypc.officaligif.fragments.ShareFragment;
import com.example.mypc.officaligif.messages.DataListSticky;
import com.example.mypc.officaligif.messages.MediaSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.RelatedViewHolder> {

    public DataListSticky dataListSticky;
    public int classID;
    public Context context;

    public RelatedAdapter(DataListSticky dataListSticky, int classID, Context context) {
        this.dataListSticky = dataListSticky;
        this.classID = classID;
        this.context = context;
    }

    @NonNull
    @Override
    public RelatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_searched_item, null);
        return new RelatedViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedViewHolder holder, int position) {
        holder.setData(dataListSticky,position, classID);
    }

    @Override
    public int getItemCount() {
        return dataListSticky.relatedList.size();
    }

    public class RelatedViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public View itemView;
        public ImageView ivLoading, ivMedia;


        public RelatedViewHolder(View itemView, Context context) {
            super(itemView);
            this.itemView = itemView ;
            this.context = context;
            this.ivLoading = itemView.findViewById(R.id.iv_loading_media);
            this.ivMedia = itemView.findViewById(R.id.iv_media);
        }

        public void setData(final DataListSticky dataListSticky, int  position, final int classID){
            final MediaModel mediaModel = dataListSticky.relatedList.get(position);
            int width = Integer.parseInt(mediaModel.fixed_height_downsampled_height);
            int height = Integer.parseInt(mediaModel.fixed_height_downsampled_width);
            int fixedWidth =  (int) (Resources.getSystem().getDisplayMetrics().widthPixels*0.6);
            int fixedHeight = (int) (Resources.getSystem().getDisplayMetrics().heightPixels*0.3);
            ivMedia.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Utils.loadImageUrl(ivLoading, ivMedia,fixedWidth, fixedHeight,mediaModel.fixed_height_small_url, context );


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataListSticky childDataListSticky = dataListSticky;
                    childDataListSticky.initializeRelatedList();
                    EventBus.getDefault().postSticky(new MediaSticky(
                            classID + 1,
                            "iGIF",
                            mediaModel,
                            childDataListSticky

                    ));
                    Utils.replaceFragment(
                            ( (MainActivity) context).getSupportFragmentManager(),
                            R.id.cl_main_activity,
                            new ShareFragment()
                    );
                }
            });

        }


    }

}
