package com.example.gallerydemo.ui.photoslist;

import com.example.gallerydemo.data.Store;
import com.example.gallerydemo.data.model.Photo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PhotosListPresenter {
    private PhotosListView view;
    private final Store store;

    private int currentPage = 0;

    private Disposable disposable;

    public PhotosListPresenter(Store store) {
        this.store = store;
    }

    public void bindView(PhotosListView view) {
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

    public void reload() {
        if (isLoading())
            disposable.dispose();

        currentPage = 1;
        view.showLoadingIndicator();
        disposable = store
                .reload()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Photo>>() {
                    @Override
                    public void onSuccess(List<Photo> photos) {
                        view.setData(photos);
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dispose();
                        view.showLoadingError();
                    }
                });
    }

    public void loadNextPage() {
        view.showLoadingIndicator();

        disposable = store
                .getPhotos(++currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Photo>>() {
                    @Override
                    public void onSuccess(List<Photo> photos) {
                        view.appendData(photos);
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dispose();
                        view.showLoadingError();
                    }
                });
    }
}
