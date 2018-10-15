package com.android.bluetown.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.android.bluetown.R;

/**
 * Created by Administrator on 2018/5/19.
 */

public class DotView extends View {
    private static final String TAG = "DotView";

    //正常状态下的dot
    Drawable mDotNormal;
    //选中状态下的dot
    Drawable mDotSelected;

    private boolean isSelected;

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDotNormal = context.getResources().getDrawable(R.drawable.ic_viewpager_dot_indicator_normal);
        mDotSelected = context.getResources().getDrawable(R.drawable.ic_viewpager_dot_indicator_selected);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width=getWidth();
        int height=getHeight();

        if (isSelected) {
            mDotSelected.setBounds(0,0,width,height);
            mDotSelected.draw(canvas);
        }
        else {
            mDotNormal.setBounds(0,0,width,height);
            mDotNormal.draw(canvas);
        }
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        invalidate();
    }

    public boolean getSelected() {
        return isSelected;
    }
}
