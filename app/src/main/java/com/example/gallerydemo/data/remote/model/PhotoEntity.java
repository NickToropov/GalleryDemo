package com.example.gallerydemo.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoEntity {

    @SerializedName("images")
    private List<PhotoUrl> images;

    @SerializedName("name")
    private String name;

    @SerializedName("user")
    private PhotoAuthor author;

    @SerializedName("camera")
    private String cameraModel;

    public List<PhotoUrl> getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public PhotoAuthor getAuthor() {
        return author;
    }

    public String getCameraModel() {
        return cameraModel;
    }
}
