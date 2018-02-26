package com.example.gallerydemo.ui.photoslist;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.gallerydemo.R;
import com.example.gallerydemo.data.model.Photo;
import com.example.gallerydemo.ui.utils.PixelsColorTransformation;

import java.util.ArrayList;
import java.util.List;


public class PhotosListAdapter extends RecyclerView.Adapter<PhotosListAdapter.ViewHolder> {

    private List<Photo> data;
    private OnPhotoInteractionListener listener;

    public List<Photo> getData() {
        return this.data;
    }

    interface OnPhotoInteractionListener {
        void onSelect(View sharedElement, int position, Photo photo);
        void onBottomReached();
    }

    PhotosListAdapter(List<Photo> data, OnPhotoInteractionListener onPhotoInteractionListener) {
        this.data = data;
        this.listener = onPhotoInteractionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onSelect(viewHolder.img, viewHolder.getAdapterPosition(), data.get(viewHolder.getAdapterPosition()));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Photo item = this.data.get(position);
        Glide.with(viewHolder.itemView.getContext())
                .asBitmap()
                .load(item.getUrl())
                .into(new PixelsColorTransformation(PixelsColorTransformation.BLUE_COLOR, Color.GREEN, viewHolder.img));

        if (position == this.data.size() - 1 && listener != null) {
            listener.onBottomReached();
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void setData(List<Photo> data) {
        if (data != null) {
            this.data = new ArrayList<>(data.size());
            for (int i = 0; i < data.size(); i++) {
                this.data.add(data.get(i));
            }
        }
        else
            this.data = new ArrayList<>(0);

        notifyDataSetChanged();
    }

    public void appendData(List<Photo> data) {
        if (data == null || data.size() < 1)
            return;

        if (this.data == null) {
            this.data = new ArrayList<>(0);
        }
        int startPosition = getItemCount();
        this.data.addAll(data);
        notifyItemRangeInserted(startPosition, getItemCount());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
}
