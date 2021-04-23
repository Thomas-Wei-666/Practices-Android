package com.example.photoalbumactivity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PhotoRvAdapter extends RecyclerView.Adapter<PhotoRvAdapter.PhotoViewHolder> {
    private List<PhotoData> photoDatas;
    private Context context;

    public PhotoRvAdapter(List<PhotoData> photoPaths) {
        this.photoDatas = photoPaths;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_photo_item, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        PhotoData photoData = photoDatas.get(position);
        Glide.with(context).load(photoData.getPhotoPaths()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photoDatas.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;


        public PhotoViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.iv_item_photo);
        }
    }
}
