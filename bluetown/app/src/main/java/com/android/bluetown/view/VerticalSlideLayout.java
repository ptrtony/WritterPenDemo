package com.android.bluetown.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dafen on 2018/7/4.
 * 上下拖动切换到上下
 */

public class VerticalSlideLayout extends ViewGroup {
    private static final int THRESHOLD_VEL = 100;
    private static final int THRESHOLD_DISTANCE = 100;
    private ViewDragHelper mDragHelper;
    private PageChangeListener mListener;
    private View mViewOne, mViewTwo;
    private int mViewHeight;
    private int mPageNum;
    private float mOldX, mOldY;

    public VerticalSlideLayout(Context context) {
        this(context, null);
    }

    public VerticalSlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalSlideLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 10f, new DragHelperCallback());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
    }

    @Override
    protected void onFinishInflate() {
        mViewOne = getChildAt(0);
        mViewTwo = getChildAt(1);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mViewOne.getBottom() > 0 && mViewOne.getTop() < 0) {
            return false;
        }
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mOldX = ev.getX();
                mOldY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float adx = Math.abs(ev.getX() - mOldX);
                float ady = Math.abs(ev.getY() - mOldY);
                if (ady < adx) {
                    mDragHelper.cancel();
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mDragHelper.cancel();
                return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mDragHelper.processTouchEvent(e);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mViewOne.getTop() == 0) {
            mViewOne.layout(l, 0, r, b - t);
            mViewTwo.layout(l, 0, r, b - t);
            mViewHeight = mViewOne.getMeasuredHeight();
            mViewTwo.offsetTopAndBottom(mViewHeight);
        } else {
            mViewOne.layout(l, mViewOne.getTop(), r, mViewOne.getBottom());
            mViewTwo.layout(l, mViewTwo.getTop(), r, mViewTwo.getBottom());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0), resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | MEASURED_STATE_TOO_SMALL;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result | (childMeasuredState & MEASURED_STATE_MASK);
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mViewOne) {
                mViewTwo.offsetTopAndBottom(dy);
            } else {
                mViewOne.offsetTopAndBottom(dy);
            }
            invalidate();
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 1;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int finalTop = 0;
            if (releasedChild == mViewOne) {
                if (yvel < -THRESHOLD_VEL || mViewOne.getTop() < -THRESHOLD_DISTANCE) {
                    finalTop = -mViewHeight;
                    mPageNum = 1;
                    if (null != mListener) {
                        mListener.onNextPage();
                    }
                }
            } else {
                if (yvel > THRESHOLD_VEL || releasedChild.getTop() > THRESHOLD_DISTANCE) {
                    finalTop = mViewHeight;
                    mPageNum = 0;
                    if (null != mListener) {
                        mListener.onPrevPage();
                    }
                }
            }
            if (mDragHelper.smoothSlideViewTo(releasedChild, 0, finalTop)) {
                ViewCompat.postInvalidateOnAnimation(VerticalSlideLayout.this);
            }
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (child == mViewOne) {
                if (top > 0) {
                    dy = 0;
                }
            } else {
                if (top < 0) {
                    dy = 0;
                }
            }
            return child.getTop() + dy / 3;
        }
    }

    /**
     * 由外部Activity的onBackPressed实现, 从第二页面返回第一页面
     *
     * @return 为false时可调用finish关闭当前Activity
     */
    public boolean back() {
        if (mPageNum == 1) {
            if (null != mListener) {
                mListener.onPrevPage();
            }
            if (mDragHelper.smoothSlideViewTo(mViewTwo, 0, mViewHeight)) {
                ViewCompat.postInvalidateOnAnimation(VerticalSlideLayout.this);
            }
            mPageNum = 0;
            return true;
        }
        return false;
    }

    /**
     * 外部实现, 用于处理切换页面后的逻辑
     */
    public void setPageChangeListener(PageChangeListener listener) {
        this.mListener = listener;
    }

    public interface PageChangeListener {

        /**
         * 切换到第一页时触发
         */
        void onPrevPage();

        /**
         * 切换到第二页时触发
         */
        void onNextPage();
    }
}
