package com.example.mypc.officaligif.messages;

import com.example.mypc.officaligif.models.MediaModel;
import com.example.mypc.officaligif.models.PairModel;
import com.example.mypc.officaligif.models.SuggestTopicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataListSticky {
    public List<MediaModel> totalList = new ArrayList<>();
    public List<MediaModel> paperList = new ArrayList<>();
    public List<MediaModel> relatedList = new ArrayList<>();

    public DataListSticky(List<MediaModel> totalList, List<MediaModel> paperList, List<MediaModel> relatedList) {
        this.totalList = totalList;
        this.paperList = paperList;
        this.relatedList = relatedList;
    }

    public DataListSticky() {
        this.totalList = new ArrayList<>();
        this.paperList = new ArrayList<>();
        this.relatedList = new ArrayList<>();
    }

    public DataListSticky(List<MediaModel> totalList) {
        this.totalList = new ArrayList<>();
        this.totalList = totalList;
        this.relatedList = new ArrayList<>();
        this.paperList = new ArrayList<>();

        if(totalList.size() <= 2){
            paperList.addAll(totalList);
        }else{
            paperList.addAll(totalList.subList(0, 2));
        }
        initializeRelatedList();
    }

    public void add(MediaModel mediaModel){
        if(totalList == null){
            totalList = paperList = relatedList =  new ArrayList<>();
            totalList.add(mediaModel);
            paperList.add(mediaModel);

        }
    }

    public void initializeRelatedList(){
        relatedList.clear();
        if(totalList.size() <= 10){
            relatedList.addAll(totalList);
        }else{

            int size = totalList.size();
            boolean[] added = new boolean[size+1];
            for(int i = 0 ;i < size ; i++) added[i] = false;
            int count = 0;

            while(count < 10){
                int position = (new Random()).nextInt(size);
                if(!added[position]){
                    added[position] = true;
                    relatedList.add(totalList.get(position));
                    ++count;
                }
            }

        }
    }



    public PairModel addMore(int numberItems){
        int firstIndex = paperList.size();
        int secondIndex = Math.min(paperList.size() + numberItems, totalList.size());
        paperList.addAll(totalList.subList(firstIndex, secondIndex));
        return new PairModel(firstIndex, secondIndex);
    }

}
