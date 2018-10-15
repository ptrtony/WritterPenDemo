package com.android.bluetown.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Dafen on 2018/6/15.
 */

public class DecoratorViewPager extends ViewPager{
    private int preX=0;
    public DecoratorViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public DecoratorViewPager(Context context) {
        super(context);

    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////        int height = 0;
////        for (int i = 0; i < getChildCount(); i++) {
////            View child = getChildAt(i);
////            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
////            int h = child.getMeasuredHeight();
////            if (h > height)height = h;
////        }
////        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
////
////        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, heightSpec);
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent even) {

        if(even.getAction()==MotionEvent.ACTION_DOWN)
        {
            preX=(int) even.getX();
        }else
        {
            if(Math.abs((int)even.getX()-preX)>4)
            {
                return true;
            }else
            {
                preX=(int) even.getX();
            }
        }
        return super.onInterceptTouchEvent(even);


    }
}
