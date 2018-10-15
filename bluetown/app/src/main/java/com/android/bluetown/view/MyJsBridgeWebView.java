package com.android.bluetown.view;

import android.content.Context;
import android.util.AttributeSet;

import wendu.webviewjavascriptbridge.WVJBWebView;

/**
 * Created by Dafen on 2018/7/31.
 */

public class MyJsBridgeWebView extends WVJBWebView {
    public MyJsBridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyJsBridgeWebView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
