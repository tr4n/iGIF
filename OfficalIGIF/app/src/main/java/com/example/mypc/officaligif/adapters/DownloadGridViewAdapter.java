package com.example.mypc.officaligif.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class DownloadGridViewAdapter extends BaseAdapter {
    public List<File> downloadList;
    public Context context;
    private final int WIDTH_SCREEN = Resources.getSystem().getDisplayMetrics().widthPixels;

    public DownloadGridViewAdapter(List<File> downloadList, Context context) {
        this.downloadList = downloadList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return downloadList.size();
    }

    @Override
    public Object getItem(int position) {
        return downloadList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.layout_downloaded_item, null);
        GifImageView ivMedia = convertView.findViewById(R.id.giv_download);

        try {
            GifDrawable gifDrawable = new GifDrawable(downloadList.get(position));
            ivMedia.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
