package com.android.bluetown.view.widgets;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bluetown.R;


/**
 * Created by Dafen on 2018/6/9.
 */

public class SimpleHomeRefreshView extends LinearLayout implements IHeaderWrapper {

    ImageView mIvRefresh;
    TextView mTvRefresh;

    public SimpleHomeRefreshView(Context context) {
        this(context, null);
    }

    public SimpleHomeRefreshView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleHomeRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_home_refresh_header, this, false);
        mIvRefresh = (ImageView) view.findViewById(R.id.iv_refresh_image);
        mTvRefresh = (TextView) view.findViewById(R.id.tv_refresh_text);
        mIvRefresh.setImageResource(R.drawable.ic_home_loading);

        Animation rotateAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.widget_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        rotateAnimation.setInterpolator(interpolator);
        mIvRefresh.startAnimation(rotateAnimation);
        LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(-1,-2);
        params.gravity= Gravity.BOTTOM;
        addView(view,params);
    }

    @Override
    public View getHeaderView() {
        return this;
    }

    @Override
    public void pullDown() {
        mTvRefresh.setText("下拉刷新");
    }

    @Override
    public void pullDownReleasable() {
        mTvRefresh.setText("松手可刷新");
    }

    @Override
    public void pullDownRelease() {
        mTvRefresh.setText("正在刷新");
    }
}
