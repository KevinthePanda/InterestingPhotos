package com.kquicho.interestingphotos.models;

/**
 * Created by kquicho on 16-05-13.
 */
public class InterestingnessResponse {
    //wrapper class for flickr.interestingness.getList response

    FlickrPhotoList photos;

    public FlickrPhotoList getPhotos() {
        return photos;
    }
}
