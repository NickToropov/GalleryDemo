package com.example.gallerydemo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
    private String url;
    private String name;
    private String author;
    private String camera;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getCamera() {
        return camera;
    }

    public Photo(String url, String name, String author, String camera) {
        this.url = url;
        this.name = name;
        this.author = author;
        this.camera = camera;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeString(this.camera);
    }

    protected Photo(Parcel in) {
        this.url = in.readString();
        this.name = in.readString();
        this.author = in.readString();
        this.camera = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
