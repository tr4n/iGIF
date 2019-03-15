package com.example.mypc.officaligif.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.fragments.DownloadFragment;
import com.example.mypc.officaligif.fragments.FavoriteFragment;
import com.example.mypc.officaligif.fragments.HomeFragment;
import com.example.mypc.officaligif.messages.BackSticky;
import com.example.mypc.officaligif.services.BubbleService;
import com.example.mypc.officaligif.utils.Utils;
import com.txusballesteros.bubbles.BubblesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_PERMISSION = 2048;
    public boolean exit = false;


    BubblesManager bubblesManager;
    int classID = 0;
    @BindView(R.id.iv_float_icon)
    ImageView ivFloatIcon;

    ImageView ivFloatFavorite, ivFloatDownload, ivFloatHome, ivUpdropIcon, ivUpdropFavorite, ivUpdropDownload, ivUpdropHome;

    LocalBroadcastManager mLocalBroadcastManager;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("finish_activity")) {
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("finish_activity");
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);

        Definition();
        Initialization();
        setupUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getBackSticky(BackSticky backSticky) {
        classID = backSticky.numberStep;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode != RESULT_OK) {

                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            } else {

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment searchFragment = getFragmentManager().findFragmentByTag("search_fragment");
        if (!exit) {
            exit = true;
            Toasty.normal(MainActivity.this, "Press back again to close app!", Utils.getDrawableResource(R.drawable.ic_close_black_24dp, this)).show();
        } else {
            this.finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_float_icon:
                Toasty.normal(MainActivity.this, "Collapsed to floating icon").show();
                Intent intent = new Intent(MainActivity.this, BubbleService.class);
                startService(intent);
                break;
            case R.id.iv_float_favorite:
                setSelectedFloatItem(ivFloatFavorite, ivUpdropFavorite);
                Utils.replaceFragmentTag(getSupportFragmentManager(), R.id.cl_main_activity, new FavoriteFragment(), "favorite_fragment");
                break;
            case R.id.iv_float_download:
                setSelectedFloatItem(ivFloatDownload, ivUpdropDownload);
                Utils.replaceFragmentTag(getSupportFragmentManager(), R.id.cl_main_activity, new DownloadFragment(), "download_fragment");
                break;
            case R.id.iv_float_home:
                setSelectedFloatItem(ivFloatHome, ivUpdropHome);
                Utils.replaceFragmentTag(getSupportFragmentManager(), R.id.cl_main_activity, new HomeFragment(), "home_fragment");
                break;

        }
    }

    private void setupPermission() {
        final Handler handler = new Handler();
        final Runnable checkOverlaySetting = new Runnable() {
            @Override
            @TargetApi(23)
            public void run() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    return;
                }
                if (Settings.canDrawOverlays(MainActivity.this)) {
                    //You have the permission, re-launch MainActivity
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    return;
                }
                handler.postDelayed(this, 1000);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning!")
                    .setMessage("Without permission you can not use this app. " +
                            "Do you want to grant permission?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, REQUEST_PERMISSION);
                            handler.postDelayed(checkOverlaySetting, 1000);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.this.finish();
                        }
                    })
                    .show();

        }


    }

    private void Definition() {
        setupPermission();
        ivFloatDownload = findViewById(R.id.iv_float_download);
        ivFloatFavorite = findViewById(R.id.iv_float_favorite);
        ivUpdropDownload = findViewById(R.id.iv_updrop_download);
        ivUpdropFavorite = findViewById(R.id.iv_updrop_favorite);
        ivUpdropIcon = findViewById(R.id.iv_updrop_float);
        ivFloatHome = findViewById(R.id.iv_float_home);
        ivUpdropHome = findViewById(R.id.iv_updrop_home);

    }

    private void Initialization() {
        exit = false;
        setSelectedFloatItem(ivFloatHome, ivUpdropHome);
        Utils.openFragmentTag(getSupportFragmentManager(), R.id.cl_main_activity, new HomeFragment(), "home_fragment");

    }


    private void setupUI() {
        ivFloatFavorite.setOnClickListener(this);
        ivFloatDownload.setOnClickListener(this);
        ivFloatIcon.setOnClickListener(this);
        ivFloatHome.setOnClickListener(this);


    }


    private void setSelectedFloatItem(View firstView, View secondView) {
        exit = false;
        Utils.backFragment(getSupportFragmentManager(), 0);
        int fixedWith = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.14);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                fixedWith,
                fixedWith
        );
        ivFloatHome.setLayoutParams(layoutParams);
        ivFloatIcon.setLayoutParams(layoutParams);
        ivFloatDownload.setLayoutParams(layoutParams);
        ivFloatFavorite.setLayoutParams(layoutParams);
        ivUpdropIcon.setVisibility(View.GONE);
        ivUpdropFavorite.setVisibility(View.GONE);
        ivUpdropDownload.setVisibility(View.GONE);
        ivUpdropHome.setVisibility(View.GONE);
        ivFloatFavorite.setBackgroundResource(R.drawable.background_unselected);
        ivFloatHome.setBackgroundResource(R.drawable.background_unselected);
        ivFloatFavorite.setBackgroundResource(R.drawable.background_unselected);
        ivFloatDownload.setBackgroundResource(R.drawable.background_unselected);
        int padding = 25;
        ivFloatDownload.setPadding(padding, padding, padding, padding);
        ivFloatHome.setPadding(padding, padding, padding, padding);
        ivFloatFavorite.setPadding(padding, padding, padding, padding);


        fixedWith = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.16);
        layoutParams = new LinearLayout.LayoutParams(
                fixedWith,
                fixedWith
        );

        firstView.setLayoutParams(layoutParams);
        secondView.setVisibility(View.VISIBLE);
        firstView.setBackgroundResource(R.drawable.background_round);
        firstView.setPadding(padding, padding, padding, padding);
    }


}
