package com.example.gallerydemo.ui.photodetails;

import com.example.gallerydemo.data.model.Photo;

public interface PhotoDetailsView {
    void showLoadingIndicator();

    void showPhoto(Photo photo);

    void hideLoadingIndicator();

    void showLoadingError();
}
