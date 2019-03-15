package com.example.mypc.officaligif.messages;

import com.example.mypc.officaligif.models.MediaModel;

import java.io.File;

public class ViewSticky {
    public File file;
    public MediaModel mediaModel;
    public int TAG;

    public ViewSticky(File file, MediaModel mediaModel, int TAG) {
        this.file = file;
        this.mediaModel = mediaModel;
        this.TAG = TAG;
    }

}
