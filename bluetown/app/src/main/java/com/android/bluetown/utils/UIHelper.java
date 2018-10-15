package com.android.bluetown.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.FloatRange;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/5/19.
 */

public class UIHelper {

    /**
     * 获取屏幕分辨率：宽
     */
    public static int getScreenWidthPix(Context context) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    /**
     * 获取屏幕分辨率：高
     */
    public static int getScreenHeightPix(Context context) {
        int height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources()
                    .getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 隐藏软键盘
     */
    public static void hideInputMethod(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideInputMethod(final View view, long delayMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideInputMethod(view);
            }
        }, delayMillis);
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(View view) {
        if (view == null) return;
        if (view instanceof EditText) view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            boolean success = imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 多少时间后显示软键盘
     */
    public static void showInputMethod(final View view, long delayMillis) {
        if (view != null)
        // 显示输入法
        {
            view.postDelayed(new Runnable() {

                @Override
                public void run() {
                    UIHelper.showInputMethod(view);
                }
            }, delayMillis);
        }
    }


    /**
     * Toast封装
     */
    public static void ToastMessage(Context context,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 调整窗口的透明度
     *
     * @param from
     * @param to
     */
    private void dimBackground(Activity act, @FloatRange(from = 0.0f, to = 1.0f) final float from, @FloatRange(from = 0.0f, to = 1.0f) final float to) {
        if (act == null || act.isFinishing()) return;
        final Window window = act.getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });

        valueAnimator.start();
    }


}
