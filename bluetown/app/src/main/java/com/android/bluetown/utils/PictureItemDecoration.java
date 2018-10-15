package com.android.bluetown.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Dafen on 2018/7/6.
 */

public class PictureItemDecoration extends RecyclerView.ItemDecoration{
    private int space;
    private int centerSpace;
    public PictureItemDecoration(int space,int centerSpace){
        this.space = space;
        this.centerSpace = centerSpace;

    }
    private ArrayList<Bitmap> bitmaps;
    public void getPictureLists(ArrayList<Bitmap> arrayList){
        this.bitmaps = arrayList;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (bitmaps!=null&&bitmaps.size()>0){
            for (int i=0;i<bitmaps.size();i++){
                if (i==0){
                    outRect.left = space;
                }else if (i==3){
                    outRect.right = space;
                }else if (i==4){
                    outRect.left = space;
                }else{
                    outRect.right = centerSpace;
                }
            }
        }
        outRect.top = space;
    }
}
