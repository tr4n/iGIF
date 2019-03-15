package com.example.mypc.officaligif.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.constraint.Constraints;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.activities.MainActivity;

import java.util.Calendar;

import butterknife.BindView;

import static org.greenrobot.eventbus.EventBus.TAG;

public class BubbleService extends Service implements View.OnClickListener, View.OnTouchListener {


    ImageView ivBubble;
    ImageView ivTrash;
    ImageView ivTrashContainer;
    LinearLayout llColapsedWidget;
    private WindowManager windowManager;
    private View bubbleLayout;
    private View trashLayout;
    private WindowManager.LayoutParams lpBubble, lpTrash;
    private final int WIDTH_PIXELS = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int HEIGHT_PIXELS = Resources.getSystem().getDisplayMetrics().heightPixels;

    private boolean isExpanded = false;


    public BubbleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Definition();
        Initialization();
        setupUI();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bubbleLayout != null) windowManager.removeView(bubbleLayout);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private void Definition() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(BubbleService.this);
        localBroadcastManager.sendBroadcast(new Intent("finish_activity"));
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        bubbleLayout = LayoutInflater.from(this).inflate(R.layout.layout_bubble, null);
        trashLayout = LayoutInflater.from(this).inflate(R.layout.layout_trash, null);
        ivBubble = bubbleLayout.findViewById(R.id.iv_bubble);
        ivTrashContainer = trashLayout.findViewById(R.id.iv_trash);
        ivTrash = trashLayout.findViewById(R.id.iv_trash);
        llColapsedWidget = bubbleLayout.findViewById(R.id.ll_colapsed_widget);


    }

    private void Initialization() {
        Log.d(TAG, "Initialization: ");
        isExpanded = false;
        // Init Bubble View

        lpBubble = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        ivBubble.setImageResource(R.drawable.igif);

        // Init Trash View

        lpTrash = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        ivTrash.setImageResource(R.drawable.bubbletrash);

        // window Manager add View
        windowManager.addView(bubbleLayout, lpBubble);

    }

    private void setupUI() {


        llColapsedWidget.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private long startClick = 0;
            private final long LIMIT_TIME_CLICK = 125;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = lpBubble.x;
                        initialY = lpBubble.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        startClick = Calendar.getInstance().getTimeInMillis();
                        windowManager.addView(trashLayout, lpTrash);
                        break;

                    case MotionEvent.ACTION_UP:
                        long timeClick = Calendar.getInstance().getTimeInMillis() - startClick;
                        if (timeClick < LIMIT_TIME_CLICK) {
                            setOnCustomClick();
                        } else {
                            if (isNearTrash(lpBubble)) {
                                onDestroy();
                                // Toast.makeText(BubbleService.this, "near ", Toast.LENGTH_SHORT).show();
                            }

                        }
                        windowManager.removeViewImmediate(trashLayout);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        lpBubble.x = initialX + (int) (event.getRawX() - initialTouchX);
                        lpBubble.y = initialY + (int) (event.getRawY() - initialTouchY);
                        if(isNearTrash(lpBubble)){
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            if (vibrator.hasVibrator()) {
                                vibrator.vibrate(50); // for 500 ms
                            }
                            ivTrash.setVisibility(View.GONE);
                            ivTrashContainer.setVisibility(View.VISIBLE);
                            windowManager.updateViewLayout(trashLayout, lpTrash);
                        }else{
                            ivTrashContainer.setVisibility(View.GONE);
                            ivTrash.setVisibility(View.VISIBLE);

                        }

                        windowManager.updateViewLayout(bubbleLayout, lpBubble);

                        break;

                }

                return true;
            }

        });

    }

    private void setOnCustomClick() {
        onDestroy();
        Intent intent = new Intent(BubbleService.this, MainActivity.class);
        startActivity(intent);
        //  onDestroy();

    }

    private boolean isNearTrash(WindowManager.LayoutParams layoutParams) {

        return (layoutParams.x < 100 && Math.abs(layoutParams.y - (int) (HEIGHT_PIXELS * 0.5)) < 300);

    }

    private void warnNearing(boolean warning) {
        if (warning) {
            ivTrashContainer.setVisibility(View.VISIBLE);
            ivTrash.setVisibility(View.GONE);
        } else {
            ivTrash.setVisibility(View.VISIBLE);
            ivTrashContainer.setVisibility(View.GONE);
        }


    }


}
