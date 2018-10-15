package com.android.bluetown.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.utils.DisplayUtils;

/**
 * Created by Dafen on 2018/6/9.
 */

public class NestRefreshScrollView extends ScrollView{

    private LinearLayout inner;
    private RelativeLayout topLayout;
    private View topBigMark;
    private RelativeLayout topSmallLayout;
    private RelativeLayout contentLayout;
    private ImageView mLoadingTopImg;
    private TextView mLoadingText;
    private boolean isRefresh;
    private RefreshListener listener;
    private int oldY;
    private MyListView mListView;
    private ScrollView mScrollView;
    private float y;
    private LinearLayout mMoveLayout;
    private Rect normal = new Rect();
    private Rect topLayoutNormal = new Rect();
    private int height;
    public NestRefreshScrollView(Context context) {
        super(context);
        init();
    }

    public NestRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestRefreshScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mScrollView = new ScrollView(getContext());
    }
    @Override
    protected void onFinishInflate() {
        if(getChildCount()>0){
            inner = (LinearLayout) getChildAt(0);
            topLayout = (RelativeLayout) inner.getChildAt(0);
            topBigMark = topLayout.getChildAt(2);
            topSmallLayout = (RelativeLayout) topLayout.getChildAt(1);
            contentLayout = (RelativeLayout) inner.getChildAt(1);
            RelativeLayout mLoadingTop = (RelativeLayout) contentLayout.getChildAt(0);
            mLoadingText = (TextView) mLoadingTop.getChildAt(1);
            setOverScrollMode(OVER_SCROLL_NEVER);
            mLoadingTopImg = inner.findViewById(R.id.iv_load_image);
            mMoveLayout = (LinearLayout) contentLayout.getChildAt(1);
            RelativeLayout flowLayout = (RelativeLayout) mMoveLayout.getChildAt(0);
            mListView = (MyListView) mMoveLayout.getChildAt(1);
            initAnimation();
        }
    }

    private void initAnimation() {
        mLoadingTopImg.setImageResource(R.drawable.progress_anim_frame);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingTopImg.getDrawable();
        animationDrawable.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (inner==null){
            return super.onTouchEvent(ev);
        }else {
            if (isRefresh && listener != null) {
                return super.onTouchEvent(ev);
            }
            commOnTouchEvent(ev);
        }

        return super.onTouchEvent(ev);
    }

    private void commOnTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLoadingText.setText("下拉刷新");
                listener.onActionDown();
                if (topLayoutNormal.isEmpty()){
                    topLayoutNormal.set(topLayout.getLeft(),topLayout.getTop(),topLayout.getRight(),topLayout.getBottom());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mMoveLayout.getTop()> DisplayUtils.dip2px(getContext(),240)&&listener!=null){
                    refreshAnimation();
                    mLoadingText.setText("努力加载中...");
                    isRefresh = true;
                    listener.onRefresh();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float preY = y;
                float nowY = ev.getY();
                int deltaY;
                deltaY = (int) Math.sqrt(Math.abs((nowY - preY))*2);
                y = nowY;
                if (isNeedMove(nowY)){
                    if (normal.isEmpty()){
                        normal.set(mMoveLayout.getLeft(),mMoveLayout.getTop(),mMoveLayout.getRight(),mMoveLayout.getBottom());
                        return;
                    }
                    if (nowY>preY){
                        mMoveLayout.layout(mMoveLayout.getLeft(),mMoveLayout.getTop()+deltaY,mMoveLayout.getRight(),mMoveLayout.getBottom()+deltaY);
                    }else if (nowY<preY){
                        mMoveLayout.layout(mMoveLayout.getLeft(),mMoveLayout.getTop()-deltaY,mMoveLayout.getRight(),mMoveLayout.getBottom()-deltaY);
                    }
                    if (mMoveLayout.getTop()>DisplayUtils.dip2px(getContext(),240)&&listener!=null){
                        mLoadingText.setText("松开即可刷新");
                    }
                }

                break;
        }
    }

    private void refreshAnimation() {
        //开启动画
        TranslateAnimation ta = new TranslateAnimation(0,0,mMoveLayout.getTop() - DisplayUtils.dip2px(getContext(),240),normal.top-DisplayUtils.dip2px(getContext(),193));
        Interpolator in = new DecelerateInterpolator();
        ta.setInterpolator(in);
        ta.setDuration(300);
        mMoveLayout.startAnimation(ta);
        mMoveLayout.layout(normal.left,normal.top+DisplayUtils.dip2px(getContext(),53),normal.right,normal.bottom+DisplayUtils.dip2px(getContext(),53));

    }

    //是否需要开启动画
    public boolean isNeedAniamtion(){
        return !normal.isEmpty();
    }

    //是否需要移动布局
    public boolean isNeedMove(float nowY){
        int scrollY = getScrollY();
        return scrollY == 0 && nowY > topLayoutNormal.bottom ||getMeasuredHeight() == getScrollY();
    }
    public interface RefreshListener{
        void onActionDown();
        void onActionUp();
        void onRefresh();
        void onRefreshFinish();
        void onLoadMore();
        void onScroll(int y);
        void onAlphaActionBar(float a);
    }
    public void setRefreshListener(RefreshListener listener){
        this.listener = listener;
    }
}
