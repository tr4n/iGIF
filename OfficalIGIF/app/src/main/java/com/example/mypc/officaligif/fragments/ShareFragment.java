package com.example.mypc.officaligif.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.activities.ViewerActivity;
import com.example.mypc.officaligif.adapters.RelatedAdapter;
import com.example.mypc.officaligif.database_dir.TopicDatabaseManager;
import com.example.mypc.officaligif.messages.BackSticky;
import com.example.mypc.officaligif.messages.DataListSticky;
import com.example.mypc.officaligif.messages.MediaSticky;
import com.example.mypc.officaligif.messages.ViewSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.DownloadUtils;
import com.example.mypc.officaligif.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFragment extends Fragment {

    private final int WIDTH_SCREEN = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int HEIGHT_SCREEN = Resources.getSystem().getDisplayMetrics().heightPixels;


    @BindView(R.id.tv_title_media)
    TextView tvTitleMedia;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_source)
    TextView tvSource;
    @BindView(R.id.iv_copy_link)
    ImageView ivCopyLink;
    @BindView(R.id.iv_facebook)
    ImageView ivFacebook;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.iv_download)
    ImageView ivDownload;

    Unbinder unbinder;


    @BindView(R.id.rl_related_title)
    RelativeLayout rlRelatedTitle;
    @BindView(R.id.iv_expanded)
    ImageView ivExpanded;
    @BindView(R.id.rv_related_items)
    RecyclerView rvRelatedItems;
    boolean isExpanded = false;
    int classID = 0;
    String titleFragment;
    MediaModel mediaModel;
    DataListSticky dataListSticky;
    View infalteView;
    Boolean favoriteMedia;
    @BindView(R.id.iv_messenger)
    ImageView ivMessenger;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @BindView(R.id.ll_share_buttons)
    LinearLayout llShareButtons;
    @BindView(R.id.scrollView2)
    ScrollView scrollView2;
    @BindView(R.id.iv_sharing_media)
    GifImageView ivSharingMedia;
    @BindView(R.id.iv_loading_sharing_media)
    ImageView ivLoadingSharingMedia;


    public ShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        infalteView = inflater.inflate(R.layout.fragment_share, container, false);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, infalteView);
        Definition();
        Initialization();
        setupUI();


        return infalteView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMediaSticky(MediaSticky mediaSticky) {
        mediaModel = mediaSticky.mediaModel;
        dataListSticky = mediaSticky.dataListSticky;
        dataListSticky.initializeRelatedList();
        classID = mediaSticky.classID;
        titleFragment = mediaSticky.title;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                    Utils.backFragment(getFragmentManager(), classID);
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.iv_copy_link, R.id.iv_facebook, R.id.iv_favorite, R.id.iv_download, R.id.rl_related_title, R.id.iv_messenger, R.id.iv_loading_sharing_media})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                Utils.backFragment(getFragmentManager(), classID);
                break;
            case R.id.iv_copy_link:
                Utils.copyClipBoard(mediaModel.original_url, getContext());

                Toasty.normal(getContext(), "Copied link", Utils.getDrawableResource(R.drawable.ic_content_copy_white_24dp, getContext())).show();
                break;
            case R.id.iv_facebook:
                Toasty.normal(getContext(), "Sharing ... ", Utils.getDrawableResource(R.drawable.facebookwhite, getContext())).show();
                Utils.shareFacebook(mediaModel, getActivity());
                break;
            case R.id.iv_messenger:
                Toasty.normal(getContext(), "Sharing ... ", Utils.getDrawableResource(R.drawable.messenger_bubble_large_white, getContext())).show();
                Utils.shareMessenger(mediaModel, getActivity());
                break;
            case R.id.iv_favorite:
                ivFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                if (TopicDatabaseManager.getInstance(getContext()).addFavoriteItem(mediaModel)) {
                    Toasty.normal(getContext(), "This item is added in the favorites", Utils.getDrawableResource(R.drawable.ic_favorite_white_24dp, getContext())).show();

                }
                break;
            case R.id.iv_download:
                Toasty.normal(getContext(), "Downloading...", Utils.getDrawableResource(R.drawable.ic_file_download_black_24dp, getContext())).show();
                DownloadUtils.getInstance(getContext()).downloadMedia(mediaModel, getContext());
                break;
            case R.id.rl_related_title:
                if (!isExpanded) {

                    CountDownTimer countDownTimer = new CountDownTimer(1000, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Toasty.normal(getContext(), "Initialize related medias ...", Utils.getDrawableResource(R.drawable.ic_bubble_chart_white_24dp, getContext())).show();
                        }

                        @Override
                        public void onFinish() {
                            setExpanedRelatedList(true);
                        }
                    }.start();
                } else {
                    setExpanedRelatedList(false);
                }

                break;
            case R.id.iv_loading_sharing_media:
            Toasty.normal(getContext(), "loading...", Utils.getDrawableResource(R.drawable.ic_bubble_chart_white_24dp, getContext())).show();
            break;
        }
    }

    private void Definition() {
        EventBus.getDefault().postSticky(new BackSticky(classID));

    }

    private void Initialization() {


        setExpanedRelatedList(false);
        if (titleFragment.contains(" GIF")) {
            titleFragment = titleFragment.split("GIF")[0].trim();
        }

        tvTitleMedia.setText(Utils.getBaseString(mediaModel.title));
        tvTitleMedia.setSelected(true);
        tvSource.setText("SOURCE: " + mediaModel.source_tld);

        int width = Integer.parseInt(mediaModel.original_width);
        int height = Integer.parseInt(mediaModel.original_height);
        int fixedWidth = (int) (WIDTH_SCREEN * 0.8);
        int fixedHeight = (fixedWidth * height) / width;
        DownloadUtils.getInstance(getContext()).load(ivSharingMedia, ivLoadingSharingMedia, fixedWidth, fixedHeight, mediaModel, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RelatedAdapter relatedAdapter = new RelatedAdapter(dataListSticky, classID, getContext());
        rvRelatedItems.setLayoutManager(linearLayoutManager);
        rvRelatedItems.setAdapter(relatedAdapter);
    }

    private void setupUI() {
        favoriteMedia = TopicDatabaseManager.getInstance(getContext()).inFavoriteList(mediaModel);
        if (favoriteMedia) {
            ivFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            ivFavorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }

        ivSharingMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewerActivity.class);
                EventBus.getDefault().postSticky(new ViewSticky(null, mediaModel, 3));
                getContext().startActivity(intent);
            }
        });


    }

    private void setExpanedRelatedList(boolean expanded) {
        isExpanded = expanded;
        if (expanded) {
            rvRelatedItems.setVisibility(View.VISIBLE);
            ivExpanded.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
        } else {
            rvRelatedItems.setVisibility(View.GONE);
            ivExpanded.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
        }
    }


}
