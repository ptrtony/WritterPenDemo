package com.android.bluetown.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Dafen on 2018/7/18.
 */

public class CircleBigShowAdapter extends PagerAdapter{
    private ArrayList<View> PictureView;

    public void setData(ArrayList<View> showBigPictureView){
        if (showBigPictureView!=null&&showBigPictureView.size()>0){
            PictureView = (ArrayList<View>) showBigPictureView.clone();
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return PictureView.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = PictureView.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = PictureView.get(position);
        container.removeView(view);
    }
}
