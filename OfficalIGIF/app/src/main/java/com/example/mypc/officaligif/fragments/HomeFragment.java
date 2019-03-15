package com.example.mypc.officaligif.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.adapters.GridViewAdapter;
import com.example.mypc.officaligif.database_dir.TopicDatabaseManager;
import com.example.mypc.officaligif.messages.BackSticky;
import com.example.mypc.officaligif.messages.SuggestTopicSticky;
import com.example.mypc.officaligif.models.SuggestTopicModel;
import com.example.mypc.officaligif.utils.Utils;
import com.txusballesteros.bubbles.BubblesManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {



    @BindView(R.id.iv_search_title)
    ImageView ivSearchTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.title)
    LinearLayout title;
    Unbinder unbinder;
    @BindView(R.id.title_search)
    LinearLayout titleSearch;
    @BindView(R.id.grid_view)
    GridView gridView;
    private BubblesManager bubblesManager;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        Definition();
        Initialization();
        setupUI();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //   bubblesManager.recycle();
    }



    private void setupUI() {
        editSearch(true, false);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    editSearch(true, true);
                    return true;
                }
                return false;
            }
        });



    }

    private void Initialization() {
             List<SuggestTopicModel> suggestTopicModelList = TopicDatabaseManager.getInstance(getContext()).getSuggestTopicModelList();
             List<SuggestTopicModel> filterTopicList = new ArrayList<>();
             filterTopicList.add(suggestTopicModelList.get(0));
             filterTopicList.add(suggestTopicModelList.get(1));
             filterTopicList.add(suggestTopicModelList.get(4));
             filterTopicList.add(suggestTopicModelList.get(5));
        gridView.setAdapter(new GridViewAdapter(filterTopicList, getContext()));

    }

    private void Definition() {

        EventBus.getDefault().postSticky(new BackSticky(0));


    }





    private void editSearch(boolean done, boolean searchTopic) {


        if (!done) {

            CountDownTimer countDownTimer = new CountDownTimer(300, 150) {
                @Override
                public void onTick(long millisUntilFinished) {
                    rlTitle.setVisibility(View.GONE);
                    titleSearch.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                    // Show soft keyboard automaticly
                    etSearch.dispatchTouchEvent(
                            MotionEvent.obtain(SystemClock.uptimeMillis(),
                                    SystemClock.uptimeMillis(),
                                    MotionEvent.ACTION_DOWN,
                                    0,
                                    0,
                                    0)
                    );
                    etSearch.dispatchTouchEvent(
                            MotionEvent.obtain(SystemClock.uptimeMillis(),
                                    SystemClock.uptimeMillis(),
                                    MotionEvent.ACTION_UP,
                                    0,
                                    0,
                                    0)
                    );
                }
            }.start();


        } else {
            titleSearch.setVisibility(View.GONE);
            rlTitle.setVisibility(View.VISIBLE);
            etSearch.clearFocus();
            InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
            if (searchTopic) {
                String topic = etSearch.getText().toString();
                Searching(topic);

            }

            etSearch.setText("");


        }
    }

    public void Searching(String topic) {
        titleSearch.setVisibility(View.GONE);
        rlTitle.setVisibility(View.VISIBLE);
        EventBus.getDefault().postSticky(new SuggestTopicSticky(topic));
        Utils.replaceFragmentTag(getFragmentManager(), R.id.cl_main_activity, new SearchFragment(), "search_fragment");
    }

    @OnClick({R.id.iv_search_title, R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search_title:
                editSearch(false, false);
                break;
            case R.id.iv_back:
                editSearch(true, false);

                break;
            case R.id.iv_search:
                editSearch(true, true);
                break;
        }
    }


}
