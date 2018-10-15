package com.android.bluetown.mywallet.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bluetown.MainActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.MD5Util;
import com.android.bluetown.view.GestureLockView;
import com.android.bluetown.view.RoundedImageView;
import com.android.bluetown.view.GestureLockView.OnGestureFinishListener;

public class GesturePSWCheckActivity extends TitleBarActivity implements
		OnClickListener {
	private GestureLockView gestureLockView;

	private TextView textview;

	private Animation animation;

	private int errorNum;// 错误数量

	private int limitErrorNum = 5;// 限制错误数次
	private FinalDb db;
	private SharePrefUtils prefUtils;
	private RoundedImageView userImg;
	private String headImg;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_gesture_psw_check);
		BlueTownExitHelper.addActivity(this);
		db = FinalDb.create(this);
		initView();
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		switch (getIntent().getIntExtra("type", 0)) {
		case 0:
			setBackImageView();
			setTitleView("验证手势密码");
			setTitleLayoutBg(R.color.title_bg_blue);
			rightImageLayout.setVisibility(View.INVISIBLE);
			break;
		case 1:
			setBackImageView();
			setTitleView("验证手势密码");
			setTitleLayoutBg(R.color.title_bg_blue);
			rightImageLayout.setVisibility(View.INVISIBLE);
			break;
		case 2:
			setBackImageView();
			backImageLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(GesturePSWCheckActivity.this, MainActivity.class));
				}
			});
			setTitleView("验证手势密码");
			setTitleLayoutBg(R.color.title_bg_blue);
			rightImageLayout.setVisibility(View.INVISIBLE);
			break;
		case 3:
			setBackImageView();
			setTitleView("验证手势密码");
			setTitleLayoutBg(R.color.title_bg_blue);
			rightImageLayout.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}

	}

	private void initView() {
		prefUtils = new SharePrefUtils(this);
		userImg = (RoundedImageView) findViewById(R.id.touxing);
		gestureLockView = (GestureLockView) findViewById(R.id.gestureLockView);
		textview = (TextView) findViewById(R.id.textview);
		findViewById(R.id.fogot_password).setOnClickListener(this);
		finalBitmap.display(userImg,
				prefUtils.getString(SharePrefUtils.HEAD_IMG, ""));
		animation = new TranslateAnimation(-20, 20, 0, 0);
		animation.setDuration(50);
		animation.setRepeatCount(2);
		animation.setRepeatMode(Animation.REVERSE);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		// 设置密码
		String key = prefUtils.getString(SharePrefUtils.HANDPASSWORD, "");
		if (TextUtils.isEmpty(key)) {
			GesturePSWCheckActivity.this.finish();
		} else {
			gestureLockView.setKey(key);
		}

		// 手势完成后回
		gestureLockView
				.setOnGestureFinishListener(new OnGestureFinishListener() {
					@Override
					public void OnGestureFinish(boolean success, String key) {
						if (success) {
							textview.setTextColor(Color.parseColor("#424242"));
							textview.setVisibility(View.VISIBLE);
							textview.setText("密码正确");
							textview.startAnimation(animation);							
							switch (getIntent().getIntExtra("type", 0)) {
							case 0:
								startActivity(new Intent(
										GesturePSWCheckActivity.this,
										GesturePSWActivity.class));
								break;
							case 1:
								startActivity(new Intent(
										GesturePSWCheckActivity.this,
										MyWalletActivity.class));
								break;
							case 2:
								Intent resultIntent = GesturePSWCheckActivity.this
										.getIntent();
								GesturePSWCheckActivity.this.setResult(
										Activity.RESULT_OK, resultIntent);
								GesturePSWCheckActivity.this.finish();
								break;
							case 3:
								startActivity(new Intent(
										GesturePSWCheckActivity.this,
										BliiListActivity.class));
								break;

							default:
								break;

							}
							GesturePSWCheckActivity.this.finish();
						} else {
							errorNum++;
							if (errorNum >= limitErrorNum) {
								db.deleteAll(MemberUser.class);
								prefUtils.setString(SharePrefUtils.GARDEN, "");
								prefUtils.setString(SharePrefUtils.GARDEN_ID,
										"");
								prefUtils.setString(SharePrefUtils.TOKEN, "");
								prefUtils.setString(SharePrefUtils.PAYPASSWORD,
										"");
								prefUtils.setString(
										SharePrefUtils.HANDPASSWORD, "");
								prefUtils.setString(
										SharePrefUtils.HANDPASSWORD, "");
								prefUtils.setString(
										SharePrefUtils.NOPASSWORDPAY_COUNT, "");
								prefUtils.setString(
										SharePrefUtils.NOPASSWORDPAY, "");
								prefUtils.setString(SharePrefUtils.COMPANYNAME,
										"");
								prefUtils
										.setString(SharePrefUtils.REALNAME, "");
								prefUtils.setString(SharePrefUtils.IDCARD, "");
								prefUtils.setString(SharePrefUtils.PPID, "");
								prefUtils.setString(SharePrefUtils.OOID, "");
								prefUtils.setString(SharePrefUtils.CHECKSTATE,
										"");
								prefUtils
										.setString(SharePrefUtils.STAMPIMG, "");
								prefUtils
										.setString(SharePrefUtils.HEAD_IMG, "");
								prefUtils.setString(SharePrefUtils.NICK_NAME,
										"");
								prefUtils.setString(SharePrefUtils.LL_ID, "");
								prefUtils.setString(SharePrefUtils.KEY_LIST, "");
								// 好友验证消息未读消息的状态
								BlueTownApp.isScanUnReadMsg = false;
								// 好友验证消息未读消息数(交友)
								BlueTownApp.unReadMsgCount = 0;
								// 活动中心消息未读消息数
								BlueTownApp.actionMsgCount = 0;
								// 跳蚤市场消息未读消息数
								BlueTownApp.fleaMarketMsgCount = 0;
								// 设置用户退出了登录（退出状态）
								prefUtils.setString(SharePrefUtils.USER_STATE,
										"1");
								Intent intent = new Intent();
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
										| Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setClass(GesturePSWCheckActivity.this,
										MainActivity.class);
								// 退出登录 为游客身份
								GesturePSWCheckActivity.this
										.startActivity(intent);
							}
							textview.setTextColor(Color.parseColor("#F26666"));
							textview.setVisibility(View.VISIBLE);
							textview.setText("剩余次数"
									+ (limitErrorNum - errorNum) + "次");
							textview.startAnimation(animation);

						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fogot_password:

			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = (View) inflater.inflate(R.layout.item_edittext,
					null);
			new PromptDialog.Builder(GesturePSWCheckActivity.this)
					.setView(layout)
					.setViewStyle(PromptDialog.VIEW_STYLE_NORMAL)
					.setButton1("确定", new PromptDialog.OnClickListener() {

						@Override
						public void onClick(Dialog dialog, int which) {
							// TODO Auto-generated method stub
							EditText password = (EditText) layout
									.findViewById(R.id.password);
							if (MD5Util.encoder(password.getText().toString())
									.equals(prefUtils.getString(
											SharePrefUtils.PASSWORD, ""))) {
								setpassword("");
								switch (getIntent().getIntExtra("type", 0)) {
								case 0:
									startActivity(new Intent(
											GesturePSWCheckActivity.this,
											GesturePSWActivity.class));
									break;
								case 1:
									startActivity(new Intent(
											GesturePSWCheckActivity.this,
											MyWalletActivity.class));
									break;
								case 2:
									Intent resultIntent = GesturePSWCheckActivity.this
											.getIntent();
									GesturePSWCheckActivity.this.setResult(
											Activity.RESULT_OK, resultIntent);
									GesturePSWCheckActivity.this.finish();
									break;
								case 3:
									startActivity(new Intent(
											GesturePSWCheckActivity.this,
											BliiListActivity.class));
									break;

								default:
									break;

								}
								GesturePSWCheckActivity.this.finish();
							} else {
								Toast.makeText(GesturePSWCheckActivity.this,
										"密码错误", Toast.LENGTH_SHORT).show();
							}

						}
					}).setButton2("取消", new PromptDialog.OnClickListener() {

						@Override
						public void onClick(Dialog dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					}).show();

			break;

		default:
			break;
		}
	}
	private void setpassword(final String passWord) {
		// TODO Auto-generated method stub
		params.put("uid", userId);
		params.put("handPassword", (passWord));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.MobiMemberAction_handPassword, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								prefUtils.setString(SharePrefUtils.HANDPASSWORD, (passWord));
								
							} else {
								Toast.makeText(GesturePSWCheckActivity.this,
										json.optString("repMsg"),
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode)
			return false;
		return super.onKeyDown(keyCode, event);
	}
}
