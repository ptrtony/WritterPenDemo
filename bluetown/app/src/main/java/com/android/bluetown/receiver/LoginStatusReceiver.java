package com.android.bluetown.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.android.bluetown.LoginActivity;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginStatusReceiver extends BroadcastReceiver {
	private static final int LOGIN_STATUS = 3;
	private static final int LOGIN_INFO = 1;
	private static final int ACCOUT_STOP = 2;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case LOGIN_STATUS:
					Context context = (Context) msg.obj;
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(context, LoginActivity.class);
					intent.putExtra("type", 3);
					context.startActivity(intent);
					break;
				case LOGIN_INFO:
					Context context1 = (Context) msg.obj;
					Intent intent1 = new Intent();
					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent1.setClass(context1, LoginActivity.class);
					intent1.putExtra("type", 1);
					context1.startActivity(intent1);
					break;
				case ACCOUT_STOP:
					Context context2 = (Context) msg.obj;
					Intent intent2 = new Intent();
					intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent2.setClass(context2,LoginActivity.class);
					intent2.putExtra("type",2);
					context2.startActivity(intent2);
					break;
			}
		}
	};
	public LoginStatusReceiver() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals("" +
				"android.bluetown.login.status")) {
			Message message = mHandler.obtainMessage();
			message.obj = context;
			message.what = LOGIN_STATUS;
			mHandler.sendMessage(message);
		}else if(intent.getAction().equals("android.bluetown.login.info")){
			Message message = mHandler.obtainMessage();
			message.obj = context;
			message.what = LOGIN_INFO;
			mHandler.sendMessage(message);
		}else if (intent.getAction().equals("android.bluetown.account.stop")){
			Message message = mHandler.obtainMessage();
			message.obj = context;
			message.what = ACCOUT_STOP;
			mHandler.sendMessage(message);
		}

	}

	

}
