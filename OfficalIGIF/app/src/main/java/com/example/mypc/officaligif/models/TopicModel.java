package com.example.mypc.officaligif.models;

public class TopicModel{
    public int id;
    public String key;
    public String url ;
    public int parent_id;

    public TopicModel(int id, String key, String url, int parent_id) {
        this.id = id;
        this.key = key;
        this.url = url;
        this.parent_id = parent_id;
    }
}