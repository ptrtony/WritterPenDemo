package com.android.bluetown.listener;

import android.annotation.SuppressLint;

import com.ab.http.AbStringHttpResponseListener;

/**
 * Created by gouh on 2/27/2015.
 */
@SuppressLint("ResourceAsColor")
public abstract class AbsHttpResponseListener extends
		AbStringHttpResponseListener {

	public AbsHttpResponseListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onStart() {
		
	}

	@Override
	public void onFinish() {
	}

	@Override
	public void onFailure(int i, String s, Throwable throwable) {

	}
}
