package com.example.photoalbumactivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ScaleImageView{
    private Activity activity;
    private ImageView imvClose;
    private ImageView imvDelete;
    private ViewPager viewPager;
    private TextView tvCounter;

    private Dialog dialog;
    private myViewPagerAdapter adapter;

    private int selectedPosition;
    private int startPosition;

    private List<File> files = new ArrayList<>();
    private List<View> views;
    private List<String> photoPaths;

    private SharedPreferences sharedPreferences;


    private final String KEY_PhotoLocals = "photoLocals";


    public ScaleImageView(Activity activity, SharedPreferences sharedPreferences,List<String> photoPaths) {
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
        this.photoPaths = photoPaths;
        init();
    }


    private void init() {
        RelativeLayout relativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.dg_image_viewer, null);
        imvClose = (ImageView) relativeLayout.findViewById(R.id.scale_image_closer);
        imvDelete = (ImageView) relativeLayout.findViewById(R.id.scale_image_delete);
        viewPager = (ViewPager) relativeLayout.findViewById(R.id.scale_image_view_pager);
        tvCounter = (TextView) relativeLayout.findViewById(R.id.scale_image_count);

        dialog = new Dialog(activity, R.style.Dialog_Fullscreen);
        dialog.setContentView(relativeLayout);

        imvClose.setOnClickListener(v -> {
            dialog.dismiss();
            /*MainActivity activity1 = new MainActivity();
            activity1.refreshData();
            会引发空指针异常，，我也不知道为啥。。
             */
        });
        imvDelete.setOnClickListener(v -> {
            int size = views.size();
            files.remove(selectedPosition);
            photoPaths.remove(selectedPosition);
            viewPager.removeView(views.remove(selectedPosition));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(KEY_PhotoLocals+selectedPosition);
            editor.apply();
            if (selectedPosition != size) {
                int position = selectedPosition + 1;
                String text = position + "/" + views.size();
                tvCounter.setText(text);
            }
            adapter.notifyDataSetChanged();
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position;
                String text = ++position + "/" + files.size();
                tvCounter.setText(text);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void findFiles() {
        for (String e : photoPaths) {
            files.add(new File(e));
            Log.i("paths", e);
        }
    }

    public void setFiles(int startPosition) {
        findFiles();

        this.selectedPosition = startPosition++;
        String text = startPosition + "/" + photoPaths.size();
        tvCounter.setText(text);
    }

    public void create(int startPosition) {
        dialog.show();
        views = new ArrayList<>();
        adapter = new myViewPagerAdapter(views, dialog);
        for (File e : files) {
            FrameLayout frameLayout = (FrameLayout) activity.getLayoutInflater().inflate(R.layout.dg_photo_item, null);
            SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) frameLayout.findViewById(R.id.scale_image_view);
            views.add(frameLayout);
            //Log.i("viewsize",views.size()+"m");
            imageView.setImage(ImageSource.uri(Uri.fromFile(e)));
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(startPosition);
    }


    private static class myViewPagerAdapter extends PagerAdapter {

        private List<View> views;
        private Dialog dialog;

        public myViewPagerAdapter(List<View> views, Dialog dialog) {
            this.dialog = dialog;
            this.views = views;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (position == 0 && views.size() == 0) {
                dialog.dismiss();
                return;
            }
            if (position == views.size()) {
                container.removeView(views.get(--position));
            } else {
                container.removeView(views.get(position));
            }
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
