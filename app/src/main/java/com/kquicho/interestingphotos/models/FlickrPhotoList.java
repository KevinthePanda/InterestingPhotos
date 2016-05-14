package com.kquicho.interestingphotos.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kquicho on 16-05-13.
 */
public class FlickrPhotoList {

    int page;
    int pages;
    int perpage;
    String stat;
    String total;

    @SerializedName("photo")
    List<FlickrPhoto> mPhotoList;

    public FlickrPhotoList() {
        this.mPhotoList = new ArrayList<>();
    }

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public String getTotal() {
        return total;
    }

    public String getStat() {
        return stat;
    }

    public List<FlickrPhoto> getPhotoList() {
        return mPhotoList;
    }

}
