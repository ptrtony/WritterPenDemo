package com.android.bluetown.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

public class ToastManager {

	private Toast mToast;
	private static ToastManager mToastManager;
	
	public ToastManager(Context c){
//		mToast = Toast.makeText(c, null, Toast.LENGTH_SHORT);
//		mToast.setGravity(Gravity.CENTER, 0, 0);
	}
	/**
	 * 
	 * @comments  单例模式 获取引用
	 * @param c
	 * @return
	 * @version 1.0
	 */
	public static synchronized ToastManager getInstance(Context c){
		if(mToastManager == null){
			mToastManager = new ToastManager(c);
		}
		return mToastManager;
	}
	/**
	 * 
	 * @comments 纯文字显示 
	 * @param text
	 * @version 1.0
	 */
	public void showText(String text){
		if(TextUtils.isEmpty(text))
			return;
		mToast.setText(text);
		mToast.show();
	}
	/**
	 * 
	 * @comments 纯文字显示 
	 * @param text
	 * @version 1.0
	 */
	public void showText(int text){
		mToast.setText(text);
		mToast.show();		
	}
}