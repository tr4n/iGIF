package com.example.mypc.officaligif.messages;

import com.example.mypc.officaligif.models.MediaModel;

import java.util.List;

public class MediaSticky {
    public int classID;
    public String title;
    public MediaModel mediaModel;
    public DataListSticky dataListSticky;

    public MediaSticky(int classID, String title, MediaModel mediaModel, DataListSticky dataListSticky) {
        this.classID = classID;
        this.title = title;
        this.mediaModel = mediaModel;
        this.dataListSticky = dataListSticky;
    }
}
