package com.example.gallerydemo.ui.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class PixelsColorTransformation extends SimpleTarget<Bitmap> {

    public final static int BLUE_COLOR = Color.rgb(50, 100, 180);

    private final ImageView imageView;

    private final int sourceRed;
    private final int sourceGreen;
    private final int sourceBlue;
    private final int targetColor;

    public PixelsColorTransformation(int sourceColor, int targetColor, ImageView imageView) {
        this.sourceRed = Color.red(sourceColor);
        this.sourceGreen = Color.green(sourceColor);
        this.sourceBlue = Color.blue(sourceColor);
        this.targetColor = targetColor;
        this.imageView = imageView;
    }
    @Override
    public void onResourceReady(@NonNull Bitmap bmp, @Nullable Transition<? super Bitmap> transition) {
        for (int x = 0; x < bmp.getWidth(); x++) {
            for (int y = 0; y < bmp.getHeight(); y++) {
                int pixelColor = bmp.getPixel(x, y);
                int b = Color.blue(pixelColor);
                int r = Color.red(pixelColor);
                int g = Color.green(pixelColor);
                if (b > sourceBlue && r < sourceRed && g < sourceGreen) {
                    bmp.setPixel(x,y, targetColor);
                }
            }
        }

        imageView.setImageBitmap(bmp);
    }
}
