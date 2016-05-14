package com.kquicho.interestingphotos.models;

import org.parceler.Parcel;

/**
 * Created by kquicho on 16-05-13.
 */
@Parcel
public class FlickrPhoto {
    String id;
    String owner;
    String secret;
    String server;
    String farm;
    String title;
    String ispublic;
    String isfriend;
    String isfamily;

    public FlickrPhoto(){ }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public String getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public String getIspublic() {
        return ispublic;
    }

    public String getIsfriend() {
        return isfriend;
    }

    public String getIsfamily() {
        return isfamily;
    }
}
