package com.example.gallerydemo.ui.photodetails;

import com.example.gallerydemo.data.Store;
import com.example.gallerydemo.data.model.Photo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PhotoDetailsPresenter {

    private PhotoDetailsView view;
    private final Store store;

    private int photoIndex;
    private Photo currentPhoto;

    private Disposable disposable;

    public PhotoDetailsPresenter(Store store) {
        this.store = store;
    }

    public void bindView(PhotoDetailsView view) {
        this.view = view;
    }

    public void unbindView() {
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();

        this.view = null;
    }

    public boolean isLoading() {
        return disposable != null && !disposable.isDisposed();
    }

    public Photo getCurrentPhoto() {
        return currentPhoto;
    }

    public void loadNextPhoto() {
        loadPhoto(++this.photoIndex);
    }

    public void loadPrevPhoto() {
        if (photoIndex == 0)
            return;

        loadPhoto(--this.photoIndex);
    }

    public void loadPhoto(final int photoIndex) {
        this.photoIndex = photoIndex;

        view.showLoadingIndicator();

        disposable = store
                .getPhotoByNum(photoIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Photo>() {
                    @Override
                    public void onSuccess(Photo photo) {
                        PhotoDetailsPresenter.this.currentPhoto = photo;
                        view.showPhoto(photo);
                        view.hideLoadingIndicator();
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dispose();
                        view.hideLoadingIndicator();
                        view.showLoadingError();
                    }
                });
    }
}
