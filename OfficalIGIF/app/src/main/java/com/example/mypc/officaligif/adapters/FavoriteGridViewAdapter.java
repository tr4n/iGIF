package com.example.mypc.officaligif.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.activities.MainActivity;
import com.example.mypc.officaligif.activities.ViewerActivity;
import com.example.mypc.officaligif.database_dir.TopicDatabaseManager;
import com.example.mypc.officaligif.fragments.FavoriteFragment;
import com.example.mypc.officaligif.fragments.SearchFragment;
import com.example.mypc.officaligif.messages.ViewSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class FavoriteGridViewAdapter extends BaseAdapter {
    public List<MediaModel> favoriteList;
    public Context context;
    private final int WIDTH_SCREEN = Resources.getSystem().getDisplayMetrics().widthPixels;

    public FavoriteGridViewAdapter(List<MediaModel> favoriteList, Context context) {
        this.favoriteList = favoriteList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return favoriteList.size();
    }

    @Override
    public Object getItem(int position) {
        return favoriteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.layout_favorite_item, null);
        ImageView ivMedia = convertView.findViewById(R.id.iv_favorite_media);
        ImageView ivLoadingMedia = convertView.findViewById(R.id.iv_loading_favorite_media);
        final TextView tvTitle = convertView.findViewById(R.id.tv_title_favorite_media);
        ImageView ivRemove = convertView.findViewById(R.id.iv_remove);

        final MediaModel mediaModel = favoriteList.get(position);
        int width = Integer.parseInt(mediaModel.fixed_width_downsampled_width);
        int height = Integer.parseInt(mediaModel.fixed_width_downsampled_height);
        int fixedWidth = WIDTH_SCREEN >> 1;
        int fixedHeight = (fixedWidth * height) / width;
        ivMedia.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Utils.loadImageUrl(ivLoadingMedia, ivMedia, fixedWidth, fixedWidth, mediaModel.fixed_width_downsampled_url, context);
        tvTitle.setText(mediaModel.title);
        tvTitle.setSelected(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewerActivity.class);
                EventBus.getDefault().postSticky(new ViewSticky(null, mediaModel, 2));
                context.startActivity(intent);
            }
        });
        ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setTextColor(Color.RED);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setIcon(Utils.getDrawableResource(R.drawable.bubbletrash, context))
                        .setTitle("Remove from favorites")
                        .setMessage("Do you want to remove this item from your favorites?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "removed! ", Toast.LENGTH_SHORT).show();
                                TopicDatabaseManager.getInstance(context).removeFavoriteItem(mediaModel);
                                Utils.replaceFragmentTag(
                                        ((MainActivity) context).getSupportFragmentManager(),
                                        R.id.cl_main_activity,
                                        new FavoriteFragment(),
                                        "favorite_fragment"
                                );
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tvTitle.setTextColor(Color.WHITE);
                            }
                        })
                        .show();
            }
        });

        return convertView;
    }


}
