package com.android.bluetown.view.widgets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Dafen on 2018/6/1.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int fristSpace;
    public SpacesItemDecoration(int firstSpace,int space) {
        this.fristSpace = firstSpace;
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.left = space;
//        outRect.right = space;
//        outRect.bottom = space;
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0) {
            outRect.left = fristSpace;
        }else{
            outRect.left = space;
        }
    }
}
