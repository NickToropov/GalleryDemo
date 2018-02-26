package com.example.gallerydemo.data;

import com.example.gallerydemo.data.model.Photo;
import com.example.gallerydemo.data.remote.RestClient;
import com.example.gallerydemo.data.remote.model.PhotoEntity;
import com.example.gallerydemo.data.remote.model.PhotosResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.observers.DisposableSingleObserver;

public class Store {
    private final static int PAGE_SIZE = 20;
    private final RestClient restClient;
    private final List<Photo> photos = new ArrayList<>();

    public Store(RestClient restClient) {
        this.restClient = restClient;
    }

    private synchronized void resetPhotos(List<Photo> photos) {
        this.photos.clear();
        if (photos != null && photos.size() > 0) {
            this.photos.addAll(photos);
        }
    }

    private synchronized void appendPhotos(List<Photo> photos) {
        this.photos.addAll(photos);
    }

    private synchronized List<Photo> getPhotosByPage(int page) {
        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = startIndex + PAGE_SIZE - 1;
        if (this.photos.size() - 1 < endIndex)
            return null;

        List<Photo> ret = new ArrayList<>(PAGE_SIZE);
        for (int i = startIndex; i <= endIndex; i++) {
            ret.add(this.photos.get(i));
        }
        return ret;
    }

    private static Photo toModelPhoto(PhotoEntity photoEntity) {
        return new Photo(photoEntity.getImages().get(0).getHttpsUrl(),
                photoEntity.getName(),
                photoEntity.getAuthor().getFullName(),
                photoEntity.getCameraModel());
    }

    public Single<List<Photo>> reload() {
        return Single.create(new SingleOnSubscribe<List<Photo>>() {
            @Override
            public void subscribe(final SingleEmitter<List<Photo>> emitter) throws Exception {
                restClient.getPhotos(1)
                        .subscribe(new DisposableSingleObserver<PhotosResponse>() {
                            @Override
                            public void onSuccess(PhotosResponse photosResponse) {
                                List<Photo> newPhotos = new ArrayList<>(photosResponse.getPhotos().size());
                                for(PhotoEntity photoEntity: photosResponse.getPhotos()) {
                                    newPhotos.add(toModelPhoto(photoEntity));
                                }

                                resetPhotos(newPhotos);
                                emitter.onSuccess(newPhotos);
                                dispose();
                            }

                            @Override
                            public void onError(Throwable e) {
                                emitter.onError(e);
                                dispose();
                            }
                        });
            }
        });
    }

    public Single<List<Photo>> getPhotos(final int page) {
        List<Photo> storedPhotos = getPhotosByPage(page);
        if (storedPhotos != null) {
            return Single.just(storedPhotos);
        }

        return Single.create(new SingleOnSubscribe<List<Photo>>() {
            @Override
            public void subscribe(final SingleEmitter<List<Photo>> emitter) throws Exception {
                restClient.getPhotos(page)
                        .subscribe(new DisposableSingleObserver<PhotosResponse>() {
                            @Override
                            public void onSuccess(PhotosResponse photosResponse) {
                                List<Photo> newPhotos = new ArrayList<>(photosResponse.getPhotos().size());
                                for(PhotoEntity photoEntity: photosResponse.getPhotos()) {
                                    newPhotos.add(toModelPhoto(photoEntity));
                                }

                                appendPhotos(newPhotos);
                                emitter.onSuccess(newPhotos);
                                dispose();
                            }

                            @Override
                            public void onError(Throwable e) {
                                emitter.onError(e);
                                dispose();
                            }
                        });
            }
        });
    }

    public Single<Photo> getPhotoByNum(final int photoIndex) {
        if (photos.size() > photoIndex)
            return Single.just(photos.get(photoIndex));

        return Single.create(new SingleOnSubscribe<Photo>() {
            @Override
            public void subscribe(final SingleEmitter<Photo> emitter) throws Exception {
                int pagesCount = Store.this.photos.size() / PAGE_SIZE;
                getPhotos(pagesCount + 1).subscribe(new DisposableSingleObserver<List<Photo>>() {
                    @Override
                    public void onSuccess(List<Photo> photos) {
                        emitter.onSuccess(Store.this.photos.get(photoIndex));
                    }

                    @Override
                    public void onError(Throwable e) {
                        emitter.onError(e);
                    }
                });
            }
        });
    }
}
