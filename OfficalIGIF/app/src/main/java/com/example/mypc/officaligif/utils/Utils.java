package com.example.mypc.officaligif.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.fragments.HomeFragment;
import com.example.mypc.officaligif.messages.DataListSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.models.ResponseModel;
import com.example.mypc.officaligif.networks.MediaResponse;
import com.example.mypc.officaligif.networks.RetrofitInstance;
import com.example.mypc.officaligif.networks.iGIPHYService;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.content.ContentValues.TAG;

public class Utils {

    public static int[] idColors = {R.drawable.blue, R.drawable.bluetwo, R.drawable.bluethree, R.drawable.brown, R.drawable.gray, R.drawable.green, R.drawable.greentwo, R.drawable.pink, R.drawable.pink, R.drawable.red};


    public static void openFragment(android.support.v4.app.FragmentManager fragmentManager, int layoutID, Fragment fragment) {

        fragmentManager.beginTransaction()
                .add(layoutID, fragment)
                .addToBackStack(null)
                .commit();

    }

    public static void replaceFragment(android.support.v4.app.FragmentManager fragmentManager, int layoutID, Fragment fragment) {

        fragmentManager.beginTransaction()
                .replace(layoutID, fragment)
                .addToBackStack(null)
                .commit();

    }

    public static void openFragmentTag(FragmentManager fragmentManager, int layoutID, Fragment fragment, String TAG) {
        fragmentManager.beginTransaction()
                .add(layoutID, fragment)
                .addToBackStack(TAG)
                .commit();
    }

    public static void replaceFragmentTag(FragmentManager fragmentManager, int layoutID, Fragment fragment, String TAG) {
        fragmentManager.beginTransaction()
                .replace(layoutID, fragment)
                .addToBackStack(TAG)
                .commit();
    }
    public static void backFragment(FragmentManager fragmentManager, int numberStep) {


        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            if (--numberStep < 0) {
                break;
            }
        }
    }


    public static int gerRandomResourceColor() {
        int position = (new Random()).nextInt(idColors.length);
        return idColors[position];
    }

    public static Drawable getDrawableResource(int idResource, Context context) {
        ImageView temporaryImageView = new ImageView(context);
        temporaryImageView.setImageResource(idResource);
        return temporaryImageView.getDrawable();
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public static void loadImageUrl(final ImageView loadingView, final ImageView view, int width, int height, String url, Context context) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        loadingView.setLayoutParams(layoutParams);
        view.setLayoutParams(layoutParams);
        loadingView.setImageResource(gerRandomResourceColor());
        loadingView.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);

        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loadingView.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(view);

    }

    public static void copyClipBoard(String data, Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, data);
        clipboard.setPrimaryClip(clipData);
    }

    public static String getStringCaps(String string) {
        String result = "";
        if (string == null) return null;
        for (int index = 0; index < string.length(); index++) {
            char charAtIndex = (string.charAt(index));
            char charAddIndex = (charAtIndex >= 'a' && charAtIndex <= 'z') ? (char) (charAtIndex + ('A' - 'a')) : charAtIndex;
            result = result + charAddIndex;

        }
        return result;
    }

    public static String getBaseString(String string) {
        String result = "";

        for (String subString : string.split(" ")) {
            if (subString != null) {
                if (subString.length() < 1) continue;
                char head = subString.charAt(0);
                if (head >= 'a' && head <= 'z') {
                    result += (String) (" " + (char) (head + 'A' - 'a'));
                } else {
                    result += (String) (" " + (char) head);
                }

                result += subString.substring(1);
            }
        }
        return result;
    }

    public static String getHashTagTitle(String title) {
        String result = "#iGIF_";
        for (String subTitle : title.split(" ")) {
            result += subTitle;
        }

        return result.length() == 6 ? result.split("_")[0] : result;
    }

    public static void shareFacebook(MediaModel mediaModel, Activity activity) {

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(mediaModel.original_url))
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag(getHashTagTitle(getBaseString(mediaModel.title)))
                        .build())
                .build();
        ShareDialog shareDialog = new ShareDialog(activity);
        shareDialog.show(content);
    }

    public static void shareStories(MediaModel mediaModel, Activity activity) {

        Log.d(TAG, "shareStories: " + "share Stories");
        // Define photo or video asset URI and attribution link URL
        Uri stickerAssetUri = Uri.parse(mediaModel.original_mp4_url);
        String attributionLinkUrl = mediaModel.original_mp4_url;
        String appId = "942604065922095";

// Instantiate implicit intent with ADD_TO_STORY action,
// sticker asset, background colors, and attribution link
        Intent intent = new Intent("com.facebook.stories.ADD_TO_STORY");
        intent.setType(".mp4");
        intent.putExtra("com.facebook.platform.extra.APPLICATION_ID", appId);
        intent.putExtra("interactive_asset_uri", stickerAssetUri);
        //  intent.putExtra("content_url", attributionLinkUrl);
        intent.putExtra("top_background_color", "#33FF33");
        intent.putExtra("bottom_background_color", "#FF00FF");

// Instantiate activity and verify it will resolve implicit intent
        activity.grantUriPermission(
                "com.facebook.katana", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
            activity.startActivityForResult(intent, 0);
        }
    }

    public static void shareVideoFacebook(MediaModel mediaModel, Activity activity) {
        ShareVideo shareVideo = new ShareVideo.Builder()
                .setLocalUrl(Uri.parse(mediaModel.original_mp4_url))
                .build();
        ShareVideoContent content = new ShareVideoContent.Builder()
                .setVideo(shareVideo)
                .build();
        ShareDialog shareDialog = new ShareDialog(activity);
        shareDialog.show(content);

    }

    public static void shareInstagram(MediaModel mediaModel, Activity activity) {

        Log.d(TAG, "shareInstagram: " + mediaModel.original_mp4_url);
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.instagram.android");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mediaModel.original_mp4_url));
            shareIntent.setType("video/*");
            activity.startActivity(shareIntent);
        } else {
            // bring user to the market to download the app.
            // or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
            activity.startActivity(intent);
        }
    }

    public static void shareMessenger(final MediaModel mediaModel, final Activity activity) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mediaModel.original_url);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.facebook.orca");
        activity.startActivity(sendIntent);


    }




}
