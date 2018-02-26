package com.example.gallerydemo.data.remote;

import com.example.gallerydemo.data.remote.model.PhotosResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Single;

public class RestClient {
    private static final String BASE_URL = "https://api.500px.com/v1/";

    interface Service {
        @GET("photos?feature=popular&consumer_key=wB4ozJxTijCwNuggJvPGtBGCRqaZVcF6jsrzUadF")
        Single<PhotosResponse> getPhotos(@Query("page") int pageNum);
    }

    private final Service apiService;

    public RestClient() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient okHttpClient =  new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .build();

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = restAdapter.create(Service.class);
    }

    public Single<PhotosResponse> getPhotos(int pageNum) {
        return apiService.getPhotos(pageNum);
    }
}