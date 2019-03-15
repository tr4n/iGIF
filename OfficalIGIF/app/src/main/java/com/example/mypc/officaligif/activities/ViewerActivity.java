package com.example.mypc.officaligif.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.database_dir.TopicDatabaseManager;
import com.example.mypc.officaligif.messages.ViewSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.DownloadUtils;
import com.example.mypc.officaligif.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class ViewerActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title_media)
    TextView tvTitleMedia;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.iv_copy_link)
    ImageView ivCopyLink;
    @BindView(R.id.iv_facebook)
    ImageView ivFacebook;
    @BindView(R.id.iv_messenger)
    ImageView ivMessenger;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.giv_media)
    GifImageView givMedia;
    @BindView(R.id.ll_share_buttons)
    LinearLayout llShareButtons;

    ViewSticky viewSticky;
    @BindView(R.id.iv_loading_media)
    ImageView ivLoadingMedia;

    MediaModel mediaModel;
    @BindView(R.id.iv_zoom)
    ImageView ivZoom;
    GestureDetector gestureDetector;

    boolean isZoomed = false;
    int width =200;
    int height = 200;
    final int widthScreen = Resources.getSystem().getDisplayMetrics().widthPixels;
    final int heightScreen = Resources.getSystem().getDisplayMetrics().heightPixels;
    @BindView(R.id.ll_viewer)
    LinearLayout llViewer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viewer);
        getSupportActionBar().hide();
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        Definition();
        Initialization();
        setupUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getViewSticky(ViewSticky viewSticky1) {
        viewSticky = viewSticky1;
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void Definition() {
        gestureDetector = new GestureDetector(this, new MyGesture());
        isZoomed = false;

    }

    public void Initialization() {

        if (viewSticky != null) {
            if (viewSticky.TAG == 1) {
                llShareButtons.setVisibility(View.GONE);
                File file = viewSticky.file;
                String path = file.getAbsolutePath().toString();
                width = Integer.parseInt(file.getName().split("---")[0]);
                height = Integer.parseInt(file.getName().split("---")[1]);

                try {

                    givMedia.setImageDrawable(new GifDrawable(path));
                    givMedia.setLayoutParams(getZoomLayoutParams(width, height, false));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                llShareButtons.setVisibility(View.VISIBLE);
                mediaModel = viewSticky.mediaModel;
                width = Integer.parseInt(mediaModel.original_width);
                height = Integer.parseInt(mediaModel.original_height);
                LinearLayout.LayoutParams layoutParams = getZoomLayoutParams(width, height, false);
                tvTitleMedia.setText(Utils.getBaseString(mediaModel.title));
                DownloadUtils.getInstance(this).load(givMedia, ivLoadingMedia, layoutParams.width, layoutParams.height, mediaModel, this);
            }
        }

    }

    public void setupUI() {
        setZoom();
       givMedia.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               gestureDetector.onTouchEvent(event);
               return true;
           }
       });

    }


    @OnClick({R.id.iv_copy_link, R.id.iv_facebook, R.id.iv_messenger, R.id.iv_download, R.id.iv_favorite, R.id.iv_zoom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_copy_link:
                Utils.copyClipBoard(mediaModel.original_url, this);
                Toasty.normal(ViewerActivity.this, "Copied link", Utils.getDrawableResource(R.drawable.ic_content_copy_white_24dp, this)).show();
                break;
            case R.id.iv_facebook:
                Toasty.normal(ViewerActivity.this, "Sharing ... ", Utils.getDrawableResource(R.drawable.facebookwhite, this)).show();
                Utils.shareFacebook(mediaModel, ViewerActivity.this);
            case R.id.iv_messenger:
                Toasty.normal(ViewerActivity.this, "Sharing ... ", Utils.getDrawableResource(R.drawable.messenger_bubble_large_white, this)).show();
                Utils.shareMessenger(mediaModel, ViewerActivity.this);
                break;
            case R.id.iv_download:
                Toasty.normal(ViewerActivity.this, "Downloading ... ", Utils.getDrawableResource(R.drawable.ic_file_download_black_24dp, this)).show();
                DownloadUtils.getInstance(this).downloadMedia(mediaModel, this);
                break;
            case R.id.iv_favorite:
                if (TopicDatabaseManager.getInstance(this).inFavoriteList(mediaModel)) {
                    Toasty.normal(ViewerActivity.this, "Liked! ", Utils.getDrawableResource(R.drawable.ic_favorite_white_24dp, this)).show();
                    ivFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                    break;
                } else if (viewSticky.TAG == 3) {
                    if (TopicDatabaseManager.getInstance((this)).addFavoriteItem(mediaModel)) {
                        ivFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                        Toasty.normal(ViewerActivity.this, "", Utils.getDrawableResource(R.drawable.ic_favorite_white_24dp, this)).show();
                    }
                }
                break;
            case R.id.iv_zoom:
               setZoom();
               break;

        }
    }


    public LinearLayout.LayoutParams getZoomLayoutParams(int widthZ, int heightZ, boolean zoom) {
        int fixedWidth = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.8);
        int fixedHeight = (heightZ * fixedWidth) / widthZ;
        return (!zoom && widthZ < fixedWidth) ? new LinearLayout.LayoutParams(widthZ, heightZ) : new LinearLayout.LayoutParams(fixedWidth, fixedHeight);

    }

    public void setZoom(){
        if (givMedia.getVisibility() == View.GONE) return;
        if (isZoomed) {
            Toasty.normal(ViewerActivity.this, "Zoom out", Utils.getDrawableResource(R.drawable.ic_zoom_out_white_24dp, this)).show();
            givMedia.setLayoutParams(getZoomLayoutParams(width, height, false));
            isZoomed = false;
            ivZoom.setImageResource(R.drawable.ic_zoom_in_white_24dp);
        } else {
            Toasty.normal(ViewerActivity.this, "Zoom in", Utils.getDrawableResource(R.drawable.ic_zoom_in_white_24dp, this)).show();
            givMedia.setLayoutParams(getZoomLayoutParams(width, height, true));
            isZoomed = true;
            ivZoom.setImageResource(R.drawable.ic_zoom_out_white_24dp);
        }

    }


    class MyGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            setZoom();
            return super.onDoubleTap(e);
        }
    }
}
