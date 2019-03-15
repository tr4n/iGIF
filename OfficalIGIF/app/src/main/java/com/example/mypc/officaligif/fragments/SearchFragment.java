package com.example.mypc.officaligif.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.adapters.SearchedAdapter;
import com.example.mypc.officaligif.database_dir.TopicDatabaseManager;
import com.example.mypc.officaligif.messages.BackSticky;
import com.example.mypc.officaligif.messages.DataListSticky;
import com.example.mypc.officaligif.messages.SuggestTopicSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.models.PairModel;
import com.example.mypc.officaligif.models.ResponseModel;
import com.example.mypc.officaligif.networks.MediaResponse;
import com.example.mypc.officaligif.networks.RetrofitInstance;
import com.example.mypc.officaligif.networks.iGIPHYService;
import com.example.mypc.officaligif.recyclerview.EndlessRecyclerViewScrollListener;
import com.example.mypc.officaligif.recyclerview.SpeedyStaggeredGridLayoutManager;
import com.example.mypc.officaligif.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    @BindView(R.id.tv_key)
    TextView tvKey;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    Unbinder unbinder;
    @BindView(R.id.tv_number_results)
    TextView tvNumberResults;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.rv_searched_items)
    RecyclerView rvSearchedItems;

    @BindView(R.id.rl_avi)
    RelativeLayout rlAvi;


    DataListSticky dataListSticky;
    SearchedAdapter searchedAdapter;
    ResponseModel responseModel;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);


        Definition();
        Initialization();
        setupUI();

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getTopicMessager(SuggestTopicSticky suggestTopicSticky) {
        tvKey.setText(Utils.getBaseString(suggestTopicSticky.topic));
        responseModel = new ResponseModel(suggestTopicSticky.topic, "eng", 150);
        tvKey.setSelected(true);
        TopicDatabaseManager.getInstance(getContext()).saveSearchedTopic(suggestTopicSticky.topic);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getDataListMessager(DataListSticky dataListSticky1) {
        dataListSticky = dataListSticky1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        Utils.backFragment(getFragmentManager(), 0);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    private void Definition() {

    }

    private void Initialization() {

        staggeredGridLayoutManager = new SpeedyStaggeredGridLayoutManager(2, SpeedyStaggeredGridLayoutManager.VERTICAL);
        rvSearchedItems.setItemAnimator(null);
        rvSearchedItems.setLayoutManager(staggeredGridLayoutManager);
        RetrofitInstance.getRetrofitGifInstance().create(iGIPHYService.class)
                .getMediaResponses(responseModel.key, responseModel.lang, responseModel.limit, responseModel.api_key)
                .enqueue(new Callback<MediaResponse>() {
                    @Override
                    public void onResponse(Call<MediaResponse> call, final Response<MediaResponse> response) {
                        if (response.body() == null || response.body().pagination.count == 0 || response.body().data.isEmpty()) {
                            if (response.body().pagination.count == 0) {
                                tvNumberResults.setText("There isn't any results for " + responseModel.key);
                                if (rlAvi != null) {
                                    rlAvi.setVisibility(View.GONE);
                                }
                            }
                            return;
                        }


                        final List<MediaModel> mediaModelList = new ArrayList<>();

                        List<MediaResponse.DataJSON> dataJSONList = response.body().data;
                        int position = 0;
                        for (MediaResponse.DataJSON dataJSON : dataJSONList) {
                            MediaModel mediaModel = new MediaModel(
                                    dataJSON.id,
                                    dataJSON.images.original.url,
                                    dataJSON.images.original.width,
                                    dataJSON.images.original.height,
                                    dataJSON.source_tld,
                                    dataJSON.title,
                                    dataJSON.caption,
                                    dataJSON.images.fixed_height.url,
                                    dataJSON.images.fixed_height.width,
                                    dataJSON.images.fixed_height.height,
                                    dataJSON.images.fixed_height_small.url,
                                    dataJSON.images.fixed_height_small.width,
                                    dataJSON.images.fixed_height_small.height,
                                    dataJSON.images.fixed_width.url,
                                    dataJSON.images.fixed_width.width,
                                    dataJSON.images.fixed_width.height,
                                    dataJSON.images.fixed_width_small.url,
                                    dataJSON.images.fixed_width_small.width,
                                    dataJSON.images.fixed_width_small.width,
                                    dataJSON.images.fixed_width_still.url,
                                    dataJSON.images.fixed_width_still.width,
                                    dataJSON.images.fixed_width_still.height,
                                    dataJSON.images.preview_gif.url,
                                    dataJSON.images.preview_gif.width,
                                    dataJSON.images.preview_gif.height,
                                    dataJSON.images.fixed_width_downsampled.url,
                                    dataJSON.images.fixed_width_downsampled.width,
                                    dataJSON.images.fixed_width_downsampled.height,
                                    dataJSON.images.fixed_height_downsampled.url,
                                    dataJSON.images.fixed_height_downsampled.width,
                                    dataJSON.images.fixed_height_downsampled.height,
                                    dataJSON.images.original_mp4.mp4,
                                    position++
                            );
                            mediaModelList.add(mediaModel);
                        }


                        dataListSticky = new DataListSticky(mediaModelList);
                        searchedAdapter = new SearchedAdapter(dataListSticky, getContext());
                        if (searchedAdapter != null && rvSearchedItems != null && tvNumberResults != null){
                            rvSearchedItems.setAdapter(searchedAdapter);
                            tvNumberResults.setText(response.body().pagination.total_count + " results");
                            tvNumberResults.setVisibility(View.GONE);
                            setLoadMoreItems();
                        }

                    }

                    @Override
                    public void onFailure(Call<MediaResponse> call, Throwable t) {
                        tvNumberResults.setText("There isn't any results for " + responseModel.key);
                        if (rlAvi != null) {
                            rlAvi.setVisibility(View.GONE);
                        }
                        Toasty.normal(getContext(), "No Internet!").show();

                    }
                });


    }

    private void setupUI() {

    }

    private void setLoadMoreItems() {
        rvSearchedItems.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(final int page, final int totalItemsCount) {

                if (rlAvi != null) {
                    if (dataListSticky.totalList.isEmpty()) {
                        rlAvi.setVisibility(View.GONE);
                        return;
                    } else {
                        rlAvi.setVisibility(View.VISIBLE);
                    }
                }
                final int delayTime = 500;

                CountDownTimer countDownTimer = new CountDownTimer(2 * delayTime, delayTime) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if (rlAvi != null) {
                            rlAvi.setVisibility(View.GONE);

                        }
                        final PairModel pairModel = dataListSticky.addMore(20);
                        searchedAdapter.notifyItemRangeInserted(pairModel.first, pairModel.second - 1);

                        searchedAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onItemRangeInserted(int positionStart, int itemCount) {
                                // super.onItemRangeInserted(positionStart, itemCount);
                                staggeredGridLayoutManager.smoothScrollToPosition(rvSearchedItems, null, positionStart + 5);
                            }
                        });


                    }
                }.start();


            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Utils.backFragment(getFragmentManager(), 0);
                    return true;
                }
                return false;
            }
        });
    }


}
