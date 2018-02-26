package com.example.gallerydemo.ui.photoslist;

import com.example.gallerydemo.data.model.Photo;

import java.util.List;

public interface PhotosListView {

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void setData(List<Photo> photos);

    void appendData(List<Photo> photos);

    void showLoadingError();
}
