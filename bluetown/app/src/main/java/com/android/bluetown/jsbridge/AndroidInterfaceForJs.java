package com.android.bluetown.jsbridge;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;

/**
 * Created by Dafen on 2018/7/10.
 */

public class AndroidInterfaceForJs {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Activity mContext;
    public AndroidInterfaceForJs(Activity activity){
        mContext = activity;
    }
    @JavascriptInterface
    public void successBack(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mContext.finish();
            }
        });
    }
}
