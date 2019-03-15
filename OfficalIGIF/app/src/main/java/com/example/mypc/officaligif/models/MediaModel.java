package com.example.mypc.officaligif.models;

import java.util.List;

public class MediaModel {
    public String id;
    public String original_url ;
    public String original_width;
    public String original_height;
    public String source_tld;
    public String title;
    public String caption;
    public String fixed_height_url;
    public String fixed_height_width;
    public String fixed_height_height;
    public String fixed_height_small_url;
    public String fixed_height_small_width;
    public String fixed_height_small_height;
    public String fixed_width_url;
    public String fixed_width_width;
    public String fixed_width_height;
    public String fixed_width_small_url;
    public String fixed_width_small_width;
    public String fixed_width_small_height;
    public String fixed_width_still_url;
    public String fixed_width_still_width;
    public String fixed_width_still_height;
    public String preview_gif_url;
    public String preview_gif_width;
    public String preview_gif_height;
    public String fixed_width_downsampled_url;
    public String fixed_width_downsampled_width;
    public String fixed_width_downsampled_height;
    public String fixed_height_downsampled_url;
    public String fixed_height_downsampled_width;
    public String fixed_height_downsampled_height;
    public String original_mp4_url;
    public int position;

    public MediaModel(String id, String original_url, String original_width, String original_height, String source_tld, String title, String caption, String fixed_height_url, String fixed_height_width, String fixed_height_height, String fixed_height_small_url, String fixed_height_small_width, String fixed_height_small_height, String fixed_width_url, String fixed_width_width, String fixed_width_height, String fixed_width_small_url, String fixed_width_small_width, String fixed_width_small_height, String fixed_width_still_url, String fixed_width_still_width, String fixed_width_still_height, String preview_gif_url, String preview_gif_width, String preview_gif_height, String fixed_width_downsampled_url, String fixed_width_downsampled_width, String fixed_width_downsampled_height, String fixed_height_downsampled_url, String fixed_height_downsampled_width, String fixed_height_downsampled_height, String original_mp4_url, int position) {
        if(title != null){
            if(title.contains(" GIF")){
                title = title.split(" GIF")[0].trim();
            }
            if(title.length() > 0){
                char head = title.charAt(0);
                if(head >= 'a' && head <= 'a'){
                    head = (char) (head + 'A' - 'a');
                    title = head + title.substring(1);
                }
            }
        }
        this.id = id;
        this.original_url = original_url;
        this.original_width = original_width;
        this.original_height = original_height;
        this.source_tld = source_tld;
        this.title = title;
        this.caption = caption;
        this.fixed_height_url = fixed_height_url;
        this.fixed_height_width = fixed_height_width;
        this.fixed_height_height = fixed_height_height;
        this.fixed_height_small_url = fixed_height_small_url;
        this.fixed_height_small_width = fixed_height_small_width;
        this.fixed_height_small_height = fixed_height_small_height;
        this.fixed_width_url = fixed_width_url;
        this.fixed_width_width = fixed_width_width;
        this.fixed_width_height = fixed_width_height;
        this.fixed_width_small_url = fixed_width_small_url;
        this.fixed_width_small_width = fixed_width_small_width;
        this.fixed_width_small_height = fixed_width_small_height;
        this.fixed_width_still_url = fixed_width_still_url;
        this.fixed_width_still_width = fixed_width_still_width;
        this.fixed_width_still_height = fixed_width_still_height;
        this.preview_gif_url = preview_gif_url;
        this.preview_gif_width = preview_gif_width;
        this.preview_gif_height = preview_gif_height;
        this.fixed_width_downsampled_url = fixed_width_downsampled_url;
        this.fixed_width_downsampled_width = fixed_width_downsampled_width;
        this.fixed_width_downsampled_height = fixed_width_downsampled_height;
        this.fixed_height_downsampled_url = fixed_height_downsampled_url;
        this.fixed_height_downsampled_width = fixed_height_downsampled_width;
        this.fixed_height_downsampled_height = fixed_height_downsampled_height;
        this.original_mp4_url = original_mp4_url;
        this.position = position;
    }


    public MediaModel() {
    }
}
