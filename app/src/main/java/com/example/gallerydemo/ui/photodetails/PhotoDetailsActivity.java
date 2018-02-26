package com.example.gallerydemo.ui.photodetails;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gallerydemo.App;
import com.example.gallerydemo.R;
import com.example.gallerydemo.data.model.Photo;
import com.example.gallerydemo.ui.utils.PixelsColorTransformation;
import com.github.chrisbanes.photoview.OnSingleFlingListener;
import com.github.chrisbanes.photoview.PhotoView;

public class PhotoDetailsActivity extends AppCompatActivity implements PhotoDetailsView {

    private PhotoView imgView;
    private TextView imgNameTextView;
    private TextView authorTextView;
    private TextView cameraModelTextView;

    private View loadingIndicatorFrame;

    private GestureDetectorCompat gestureDetector;

    private PhotoDetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        presenter = new PhotoDetailsPresenter(App.getInst().getStore());
        presenter.bindView(this);

        imgView = findViewById(R.id.img);
        imgNameTextView = findViewById(R.id.imgName);
        authorTextView = findViewById(R.id.author);
        cameraModelTextView = findViewById(R.id.cameraModel);
        loadingIndicatorFrame = findViewById(R.id.loadingIndicatorFrame);

        imgView.setOnSingleFlingListener(new OnSingleFlingListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return gestureListener.onFling(e1, e2, velocityX, velocityY);
            }
        });
        gestureDetector = new GestureDetectorCompat(this, gestureListener);
        imgNameTextView.setTypeface(imgNameTextView.getTypeface(), Typeface.BOLD);

        presenter.loadPhoto(getIntent().getIntExtra("photo", 0));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        presenter.unbindView();

        super.onDestroy();
    }

    private final GestureDetector.SimpleOnGestureListener gestureListener =
            new GestureDetector.SimpleOnGestureListener() {

                private static final int SWIPE_THRESHOLD = 100;
                private static final int SWIPE_VELOCITY_THRESHOLD = 100;

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    boolean result = false;
                    try {
                        float diffY = e2.getY() - e1.getY();
                        float diffX = e2.getX() - e1.getX();
                        if (Math.abs(diffX) > Math.abs(diffY)) {
                            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffX > 0) {
                                    onSwipeRight();
                                } else {
                                    onSwipeLeft();
                                }
                                result = true;
                            }
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return result;
                }
            };

    @Override
    public void showLoadingIndicator() {
        loadingIndicatorFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPhoto(Photo photo) {
        imgNameTextView.setText(photo.getName());
        authorTextView.setText(photo.getAuthor());
        cameraModelTextView.setText(photo.getCamera());

        Glide.with(PhotoDetailsActivity.this)
                .asBitmap()
                .load(photo.getUrl())
                .into(new PixelsColorTransformation(PixelsColorTransformation.BLUE_COLOR, Color.GREEN, imgView));
    }

    @Override
    public void hideLoadingIndicator() {
        loadingIndicatorFrame.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingError() {
        new AlertDialog.Builder(PhotoDetailsActivity.this)
                .setMessage(R.string.error_photo_loading)
                .show();
    }

    private void onSwipeLeft() {
        if (presenter.isLoading())
            return;
        presenter.loadNextPhoto();
    }

    private void onSwipeRight() {
        if (presenter.isLoading())
            return;
        presenter.loadPrevPhoto();
    }

    public void onShareClick(View view) {
        if (presenter.getCurrentPhoto() == null)
            return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, presenter.getCurrentPhoto().getUrl());
        startActivity(Intent.createChooser(intent, getString(R.string.share_photo_choose_title)));
    }
}
