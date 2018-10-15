package com.android.bluetown.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by Dafen on 2018/5/18.
 * 设置屏幕透明度工具类运用到弹窗中 并兼容华为手机
 */

public class AlphaUtil {
    /*
    * 设置页面的透明度
    * @param bgAlpha 1表示不透明
    */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }
}
