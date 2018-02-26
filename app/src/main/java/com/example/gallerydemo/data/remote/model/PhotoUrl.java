package com.example.gallerydemo.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class PhotoUrl {

    @SerializedName("https_url")
    private String httpsUrl;

    @SerializedName("url")
    private String url;

    public String getHttpsUrl() {
        return httpsUrl;
    }

    public String getUrl() {
        return url;
    }
}
