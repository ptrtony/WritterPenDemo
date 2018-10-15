package com.android.bluetown.view.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.bluetown.R;

/**
 * Created by Dafen on 2018/7/9.
 */

public class SimpleHomeLoadView extends LinearLayout implements IFooterWrapper{
    public SimpleHomeLoadView(Context context) {
        this(context,null);
    }

    public SimpleHomeLoadView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimpleHomeLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.include_my_bottom_line,null);
        addView(view);
    }

    @Override
    public View getFooterView() {
        return this;
    }

    @Override
    public void pullUp() {
    }

    @Override
    public void pullUpReleasable() {

    }

    @Override
    public void pullUpRelease() {

    }

    @Override
    public void pullUpFinish() {

    }
}
