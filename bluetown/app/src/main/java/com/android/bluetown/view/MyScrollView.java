package com.android.bluetown.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.bluetown.utils.Log;

/**
 * Created by Dafen on 2018/6/7.
 */

public class MyScrollView extends ScrollView{
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount()>0){
            RelativeLayout mTopLayout = (RelativeLayout) getChildAt(0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float curY = 0;
        boolean interception = false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                curY = ev.getRawY();
                Log.d("onTouch","手势触摸屏幕的位置"+curY);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("onTouch","向上或者向下滑动的距离"+(ev.getRawY()-curY));
                break;
            case MotionEvent.ACTION_UP:
                Log.d("onTouch","手势离开屏幕时的距离"+(ev.getRawY()-curY));
                break;
        }
        return interception;
    }

}
