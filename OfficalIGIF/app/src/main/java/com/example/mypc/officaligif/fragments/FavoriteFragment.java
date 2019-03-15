package com.example.mypc.officaligif.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mypc.officaligif.R;
import com.example.mypc.officaligif.adapters.FavoriteGridViewAdapter;
import com.example.mypc.officaligif.database_dir.TopicDatabaseManager;
import com.example.mypc.officaligif.messages.BackSticky;
import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    View fragmentView;
    Unbinder unbinder;
    List<MediaModel> favoriteList;
    FavoriteGridViewAdapter favoriteGridViewAdapter;
    @BindView(R.id.gv_favorites)
    GridView gvFavorites;


    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_favorite, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);

        Definition();
        Initialization();
        setupUI();


        return fragmentView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void Definition() {
        EventBus.getDefault().postSticky(new BackSticky(0));
    }

    ;

    private void Initialization() {
        favoriteList = TopicDatabaseManager.getInstance(getContext()).getFavoriteList();
        favoriteGridViewAdapter = new FavoriteGridViewAdapter(favoriteList, getContext());
        gvFavorites.setAdapter(favoriteGridViewAdapter);

    }

    private void setupUI() {

    }



}
