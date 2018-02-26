package com.example.gallerydemo;

import android.app.Application;

import com.example.gallerydemo.data.Store;
import com.example.gallerydemo.data.remote.RestClient;

public class App extends Application {

    private static App inst;
    private RestClient restClient;
    private Store store;

    @Override
    public void onCreate() {
        super.onCreate();
        inst = this;
        restClient = new RestClient();
        store = new Store(restClient);
    }

    public static App getInst() {
        return inst;
    }

    public RestClient getRestClient() {
        return restClient;
    }

    public Store getStore() {
        return store;
    }
}
