package com.android.bluetown;
import java.util.Set;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.LocationUtil;
import com.android.bluetown.utils.StatusBarUtils;

public class LoginStatusDialogActivity extends Activity implements
		OnClickListener {
	private SharePrefUtils prefUtils;
	private TextView title, dialogContent;
	private Button cancelBtn, comfirmBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_login_status_dialog);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			StatusBarUtils.getInstance().initStatusBar(true, this);
		}
		prefUtils = new SharePrefUtils(this);
		title = (TextView) findViewById(R.id.title_text);
		dialogContent = (TextView) findViewById(R.id.content_text);
		cancelBtn = (Button) findViewById(R.id.cancel_button);
		comfirmBtn = (Button) findViewById(R.id.confirm_button);
		cancelBtn.setOnClickListener(this);
		comfirmBtn.setOnClickListener(this);
		title.setText(R.string.tip);
		if(getIntent().getIntExtra("type", 0)==1){
			dialogContent.setText("您的账号已被锁定");
		}else if (getIntent().getIntExtra("type",0)==2){
			dialogContent.setText("您的账户已被禁用，请联系园区中心!");
		}else if (getIntent().getIntExtra("type",0)==3){
			dialogContent.setText("您的账号在别的设备上登录，您被迫下线！");	
		}		
		// 点击空白处不起作用
		this.setFinishOnTouchOutside(false);
	}

	/**
	 * 禁止返回键
	 */
	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK: {
			return false;
		}
		}
		return true;
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancel_button:
			finish();
			break;
		case R.id.confirm_button:
			LocationUtil.getInstance(getApplicationContext()).stopLoc();
			FinalDb db = FinalDb.create(LoginStatusDialogActivity.this);
			// 删除登录保存的登录信息
			db.deleteAll(MemberUser.class);
			prefUtils.setString(SharePrefUtils.GARDEN, "");
			prefUtils.setString(SharePrefUtils.GARDEN_ID, "");
			prefUtils.setString(SharePrefUtils.TOKEN, "");
			prefUtils.setString(SharePrefUtils.PAYPASSWORD, "");
			prefUtils.setString(SharePrefUtils.HANDPASSWORD, "");
			prefUtils.setString(SharePrefUtils.PASSWORD, "");
			prefUtils.setString(SharePrefUtils.NOPASSWORDPAY_COUNT, "");
			prefUtils.setString(SharePrefUtils.NOPASSWORDPAY, "");
			prefUtils.setString(SharePrefUtils.COMPANYNAME, "");
			prefUtils.setString(SharePrefUtils.REALNAME, "");
			prefUtils.setString(SharePrefUtils.IDCARD, "");
			prefUtils.setString(SharePrefUtils.PPID, "");
			prefUtils.setString(SharePrefUtils.OOID, "");
			prefUtils.setString(SharePrefUtils.CHECKSTATE, "");
			prefUtils.setString(SharePrefUtils.STAMPIMG, "");
			prefUtils.setString(SharePrefUtils.HEAD_IMG, "");
			prefUtils.setString(SharePrefUtils.NICK_NAME, "");
			prefUtils.setString(SharePrefUtils.LL_ID, "");
			prefUtils.setString(SharePrefUtils.KEY_LIST, "");
			prefUtils.setString(SharePrefUtils.KEY_LIST_TIME, "");
			prefUtils.setString(SharePrefUtils.CODE, "");
			prefUtils.setString(SharePrefUtils.RONG_TOKEN, "");
			setDirectPush("");
			// 设置用户退出了登录（退出状态）
			prefUtils.setString(SharePrefUtils.USER_STATE, "1");
			// 好友验证消息未读消息的状态
			BlueTownApp.isScanUnReadMsg = false;
			// 好友验证消息未读消息数(交友)
			BlueTownApp.unReadMsgCount = 0;
			// 活动中心消息未读消息数
			BlueTownApp.actionMsgCount = 0;
			// 跳蚤市场消息未读消息数
			BlueTownApp.fleaMarketMsgCount = 0;
//			Intent refreshintent = new Intent(
//					"com.android.bm.refresh.new.msg.action");
//			sendBroadcast(refreshintent);
//			Intent intent = new Intent();
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//					| Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.setClass(LoginStatusDialogActivity.this, LoginActivity.class);
//			// 退出登录 为游客身份
//			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
	private void setDirectPush(final String userId) {
		JPushInterface.setAlias(getApplicationContext(), userId,
				new TagAliasCallback() {

					@Override
					public void gotResult(int arg0, String arg1,
							Set<String> arg2) {
						// TODO Auto-generated method
						// stub
						// 写入标签名

					}
				});
	}

}
