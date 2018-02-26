package com.example.gallerydemo.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotosResponse {
    @SerializedName("photos")
    private List<PhotoEntity> photos;

    public List<PhotoEntity> getPhotos() {
        return photos;
    }
}
