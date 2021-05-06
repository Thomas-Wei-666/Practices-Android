package com.example.photoalbumactivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.util.Log;
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
    private List<String> photoPaths;
    private Context context;
    private Activity activity;
    private SharedPreferences sharedPreferences;

    public PhotoRvAdapter(List<PhotoData> photoDatas, Activity activity, List<String> photoPaths, SharedPreferences sharedPreferences) {
        this.photoDatas = photoDatas;
        this.activity = activity;
        this.photoPaths = photoPaths;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_photo_item, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(view);
        context = parent.getContext();
        holder.imageView.setOnClickListener(v -> {
            ScaleImageView scaleImageView = new ScaleImageView(activity,sharedPreferences,photoPaths);
            scaleImageView.setFiles(holder.getAbsoluteAdapterPosition());
            scaleImageView.create(holder.getAbsoluteAdapterPosition());
        });
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
