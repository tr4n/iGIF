package com.example.mypc.officaligif.models;

import java.util.ArrayList;
import java.util.List;

public class SuggestTopicModel {
    public int id;
    public String key;
    public int count;
    public String color;
    public List<TopicModel> topicList = new ArrayList<>();

    public SuggestTopicModel(int id, String key, int count, String color, List<TopicModel> topicList) {
        this.id = id;
        this.key = key;
        this.count = count;
        this.color = color;
        this.topicList = topicList;
    }


}
