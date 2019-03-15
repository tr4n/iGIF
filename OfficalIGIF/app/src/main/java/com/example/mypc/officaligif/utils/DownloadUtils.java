package com.example.mypc.officaligif.utils;

import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mypc.officaligif.models.MediaModel;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static org.greenrobot.eventbus.EventBus.TAG;

public class DownloadUtils {

    public Context context;
    private static DownloadUtils downloadUtils;
    private final String pathFolderDownload ;
    private final String pathFolderLoading;


    public static DownloadUtils getInstance(Context context) {
        if (downloadUtils == null) {
            downloadUtils = new DownloadUtils(context);
        }
        return downloadUtils;
    }

    public DownloadUtils(Context context) {
        this.context = context;
        this.pathFolderDownload = context.getExternalCacheDir() + "/iGIF/downloadss";
        this.pathFolderLoading = context.getExternalCacheDir() + "/iGIF/preLoading";
    }

    public void downloadMedia(MediaModel mediaModel, final Context context) {

        File folder = new File(pathFolderDownload);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String nameFile = mediaModel.original_width + "---"+mediaModel.original_height+ "---" + mediaModel.id + "-iGIF.GIF";
        File downloadFile = new File(folder, nameFile);
        if (downloadFile.exists()) {
            Toast.makeText(context, "File have already downloaded", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri downloadUri = Uri.parse(mediaModel.original_url);
        Uri destinationUri = Uri.parse(pathFolderDownload+ "/" +nameFile);
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setDownloadContext(context)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        Toast.makeText(context, "downloaded!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {

                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {

                    }
                });
        new ThinDownloadManager().add(downloadRequest);

    }



    public void load(final GifImageView gifImageView, final ImageView loadingView, final int width, final int height, final MediaModel mediaModel, final Context context) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        loadingView.setLayoutParams(layoutParams);
        gifImageView.setLayoutParams(layoutParams);
        loadingView.setImageResource(Utils.gerRandomResourceColor());

        loadingView.setVisibility(View.VISIBLE);
        gifImageView.setVisibility(View.GONE);
        final String nameFile = mediaModel.original_width + "---"+mediaModel.original_height+ "---" + mediaModel.id + "-iGIF.GIF";

        File folder = new File(pathFolderLoading);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File downloadFile = new File(folder, nameFile);
        if (downloadFile.exists()) {
            try {
                gifImageView.setImageDrawable(new GifDrawable(pathFolderLoading + "/" + nameFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            gifImageView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
            return;
        }

        Uri downloadUri = Uri.parse(mediaModel.original_url);

        Uri destinationUri = Uri.parse(pathFolderLoading + "/" + nameFile);
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setDownloadContext(context)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        try {
                            gifImageView.setImageDrawable(new GifDrawable(pathFolderLoading + "/"+ nameFile));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gifImageView.setVisibility(View.VISIBLE);
                        loadingView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {

                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {

                    }
                });
        new ThinDownloadManager().add(downloadRequest);
    }

    public List<File> getDownloadedFile(Context context) {
        List<File> fileList = new ArrayList<>();
        File folder = new File(pathFolderDownload);
        if (!folder.exists()) folder.mkdirs();
        if (folder.listFiles() == null) return null;
        for (File file : folder.listFiles()) {
            Log.d(TAG, "getDownloadedFile: " + file.getName().toString());
            if (file.getName().toString().contains("-iGIF.GIF")) {
                fileList.add(file);
            }
        }
        return fileList;
    }


}
