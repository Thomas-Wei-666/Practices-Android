package com.example.photoalbumactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PhotoRvAdapter extends RecyclerView.Adapter<PhotoRvAdapter.PhotoViewHolder> {
    private List<DataTest> dataTests = new ArrayList<DataTest>();

    public PhotoRvAdapter(List<DataTest> dataTests) {
        this.dataTests = dataTests;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_photo_item, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        DataTest dataTest = dataTests.get(position);
        holder.textView.setText(dataTest.getText());
    }

    @Override
    public int getItemCount() {
        return dataTests.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public PhotoViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.tv_test);
        }
    }
}
