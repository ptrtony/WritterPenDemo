package com.android.bluetown.view.widgets;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.bluetown.R;
/**
 * <p>公司   tsingning</p>
 * <p>作者   dengzq</p>
 * <p>时间   2017/7/4 11:33</p>
 * <p>包名   com.dengzq.refresh.widgets.simpleRefresh</p>
 * <p>描述   TODO</p>
 */

public class SimpleRefreshView extends LinearLayout implements IHeaderWrapper {

    ImageView mIvRefresh;
    TextView mTvRefresh;

    public SimpleRefreshView(Context context) {
        this(context, null);
    }

    public SimpleRefreshView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_refresh_header, this, false);
        mIvRefresh = (ImageView) view.findViewById(R.id.iv_refresh_image);
        mTvRefresh = (TextView) view.findViewById(R.id.tv_refresh_text);
        mIvRefresh.setImageResource(R.drawable.progress_anim_frame);
        LayoutParams params =new LayoutParams(-1,-2);
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
        AnimationDrawable drawable = (AnimationDrawable) mIvRefresh.getDrawable();
        drawable.start();
    }


}
