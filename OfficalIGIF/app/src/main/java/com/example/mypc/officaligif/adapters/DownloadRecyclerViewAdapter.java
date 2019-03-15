package com.example.mypc.officaligif.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.activities.ViewerActivity;
import com.example.mypc.officaligif.messages.ViewSticky;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class DownloadRecyclerViewAdapter extends RecyclerView.Adapter<DownloadRecyclerViewAdapter.DownloadViewHolder>{

    public List<File> downloadFileList = new ArrayList<>();
    public Context context;

    public DownloadRecyclerViewAdapter(List<File> downloadFileList, Context context) {
        this.downloadFileList = downloadFileList;
        this.context = context;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_downloaded_item, null);
        return new DownloadViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {
        holder.setData(downloadFileList, position);
    }

    @Override
    public int getItemCount() {
        return downloadFileList.size();
    }

    public class DownloadViewHolder extends RecyclerView.ViewHolder{

        private View itemView;
        private Context context;
        public GifImageView gifImageView;


        public DownloadViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            this.itemView = itemView;
            gifImageView = itemView.findViewById(R.id.giv_download);


        }

        public void setData(List<File> downloadFileList, int position){
            final File file = downloadFileList.get(position);
            if(!file.exists()) return;
            String path = file.getAbsolutePath().toString();
            int width = Integer.parseInt(file.getName().split("---")[0]);
            int height = Integer.parseInt(file.getName().split("---")[1]);

            try {

                gifImageView.setImageDrawable(new GifDrawable(path));
                int fixedWidth = Resources.getSystem().getDisplayMetrics().widthPixels >> 1;
                int fixedHeight = (fixedWidth * height)/ width;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(fixedWidth, fixedHeight);
                gifImageView.setLayoutParams(layoutParams);
            } catch (IOException e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewerActivity.class);
                    EventBus.getDefault().postSticky(new ViewSticky(file, null, 1));
                    context.startActivity(intent);
                }
            });
        }
    }

}
