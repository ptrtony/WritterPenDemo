package com.android.bluetown.utils;

import android.content.Context;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by 大灯泡 on 2016/10/28.
 * <p>
 * 字符串工具类
 */

public class StringUtil {

    public static boolean noEmpty(String originStr) {
        return !TextUtils.isEmpty(originStr);
    }


    public static boolean noEmpty(String... originStr) {
        boolean noEmpty = true;
        for (String s : originStr) {
            if (TextUtils.isEmpty(s)) {
                noEmpty = false;
                break;
            }
        }
        return noEmpty;
    }

    /**
     * 从资源文件拿到文字
     */
    public static String getResourceString(Context context,int strId) {
        String result = "";
        if (strId > 0) {
            result = context.getResources().getString(strId);
        }
        return result;
    }

    /**
     * 从资源文件得到文字并format
     */
    public static String getResourceStringAndFormat(Context context,int strId, Object... objs) {
        String result = "";
        if (strId > 0) {
            result = String.format(Locale.getDefault(), context.getResources().getString(strId), objs);
        }
        return result;
    }
}
