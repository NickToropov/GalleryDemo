package com.example.gallerydemo.ui.photoslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ViewFlipper;

import com.example.gallerydemo.App;
import com.example.gallerydemo.R;
import com.example.gallerydemo.data.model.Photo;
import com.example.gallerydemo.ui.photodetails.PhotoDetailsActivity;

import java.util.List;

public class PhotosListActivity extends AppCompatActivity implements PhotosListView {

    private ViewFlipper viewFlipper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView photosListView;

    private PhotosListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_list);

        presenter = new PhotosListPresenter(App.getInst().getStore());

        viewFlipper = findViewById(R.id.viewFlipper);
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        photosListView = findViewById(R.id.photosListView);
        photosListView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.columns_count)));
        photosListView.setAdapter(new PhotosListAdapter(null, onPhotoInteractionListener));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.reload();
            }
        });

        presenter.bindView(this);
        presenter.loadNextPage();
    }

    @Override
    protected void onDestroy() {
        presenter.unbindView();

        super.onDestroy();
    }

    private PhotosListAdapter.OnPhotoInteractionListener onPhotoInteractionListener =
            new PhotosListAdapter.OnPhotoInteractionListener() {
                @Override
                public void onSelect(View sharedElement, int position, Photo photo) {
                    Intent intent = new Intent(PhotosListActivity.this, PhotoDetailsActivity.class);
                    intent.putExtra("photo", position);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(PhotosListActivity.this, sharedElement, "photo_details");
                    startActivity(intent, options.toBundle());
                }

                @Override
                public void onBottomReached() {
                    if (!presenter.isLoading())
                        presenter.loadNextPage();
                }
            };

    @Override
    public void showLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setData(List<Photo> photos) {
        ((PhotosListAdapter) photosListView.getAdapter()).setData(photos);
        showPhotosList();
    }

    @Override
    public void appendData(List<Photo> photos) {
        ((PhotosListAdapter) photosListView.getAdapter()).appendData(photos);
        showPhotosList();
    }

    @Override
    public void showLoadingError() {
        viewFlipper.setDisplayedChild(1);
    }

    public void showPhotosList() {
        viewFlipper.setDisplayedChild(0);
        hideLoadingIndicator();
    }

    public void reloadClick(View view) {
        presenter.reload();
    }
}
