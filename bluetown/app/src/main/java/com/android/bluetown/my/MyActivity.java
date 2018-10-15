package com.android.bluetown.my;
import io.rong.imkit.RongIM;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.HuodongActivity;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.MygoodsActivity;
import com.android.bluetown.R;
import com.android.bluetown.R.drawable;
import com.android.bluetown.R.id;
import com.android.bluetown.R.layout;
import com.android.bluetown.R.string;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.BlueAnthenResponse;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.UserInfo;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.mywallet.activity.GesturePSWCheckActivity;
import com.android.bluetown.mywallet.activity.MyWalletActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.UserInfoResult;
import com.android.bluetown.surround.WebOrderListActivity;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.LocationUtil;
import com.android.bluetown.utils.StatusBarUtils;
import com.android.bluetown.view.RoundImageView;
import com.bumptech.glide.Glide;
import static com.android.bluetown.utils.Constant.HOST_URL1;
import static com.android.bluetown.utils.Constant.Interface.BLUE_APP_AUTHRIENTY;

/**
 * @author hedi
 * @data: 2016年4月22日 下午3:06:18
 * @Description: 我的
 */
public class MyActivity extends AppCompatActivity implements OnClickListener {
	private TextView userNameView, telnum;
	private RoundImageView userImg;
	private long exitTime = 0;
	private RelativeLayout guestAppointLy;
	private RelativeLayout mRlBlueIdentity;
	private SharePrefUtils prefUtils;
	private UserInfo userInfo;
	/** 加载图片的FinalBitmap类 */
	protected FinalBitmap finalBitmap;
	// Http请求
	protected AbHttpUtil httpInstance;
	// Http请求参数
	protected AbRequestParams params;
	// 用户类型（普通用户和企业用户）
	private int userType;
	private FinalDb db;
	private String userId = "";
	private String comName = "";
	private String gardenName = "";
	private String tell_number = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			StatusBarUtils.getInstance().initStatusBar(true,this);
		}
		BlueTownExitHelper.addActivity(this);
		EventBus.getDefault().register(this);
		prefUtils = new SharePrefUtils(this);
		initTitle();
		initView();
		initHttpRequest();
		initImageLoader();
		verifyBlueUser(tell_number);
		getJudgeUserInfo();
	}

	private void initView() {
		userImg = findViewById(R.id.touxing);
		userNameView = (TextView) findViewById(R.id.userName);
		mRlBlueIdentity = findViewById(R.id.rl_blue_identity);
		userImg.setDrawingCacheEnabled(true);
		db = FinalDb.create(MyActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				tell_number = user.getUsername();
				userType = user.getUserType();
				comName = user.getCompanyName();
				gardenName = user.getHotRegion();
			}
		}
		findViewById(id.rl_personal).setOnClickListener(this);
		findViewById(id.mywallet).setOnClickListener(this);
		findViewById(id.collect).setOnClickListener(this);
		findViewById(id.guanzhu).setOnClickListener(this);
		findViewById(id.mymessage).setOnClickListener(this);
		findViewById(id.huodong).setOnClickListener(this);
		findViewById(id.mygoods).setOnClickListener(this);
		findViewById(id.bookOrderTv).setOnClickListener(this);
	}

	private void initTitle() {
		findViewById(id.titleImageRight).setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: initImageLoader
	 * @Description: TODO(初始化加载图片的类)
	 * @throws
	 */
	private void initImageLoader() {
		if (finalBitmap == null) {
			finalBitmap = FinalBitmap.create(this);
		}
		finalBitmap.configBitmapLoadThreadSize(3);// 定义线程数量
		finalBitmap.configDiskCachePath(this.getApplicationContext()
				.getFilesDir().toString());// 设置缓存目录；
		finalBitmap.configDiskCacheSize(1024 * 1024 * 50);// 设置缓存大小
		finalBitmap.configLoadingImage(drawable.ic_msg_empty);
		finalBitmap.configLoadfailImage(drawable.ic_msg_empty);
	}

	private void initHttpRequest() {
		// TODO Auto-generated method stub
		if (httpInstance == null) {
			httpInstance = AbHttpUtil.getInstance(this);
			httpInstance.setEasySSLEnabled(true);
		}

		if (params == null) {
			params = new AbRequestParams();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	public void getJudgeUserInfo(){
		if (!TextUtils.isEmpty(userId)) {

			if (userType == Constant.COMPANY_USER) {
				// guestAppointLy.setVisibility(View.VISIBLE);
				// companyName.setVisibility(View.VISIBLE);
				// userTypeView.setText(gardenName);
				// companyName.setText(comName);
			} else if (userType == Constant.NORMAL_USER) {
				// guestAppointLy.setVisibility(View.GONE);
				// companyName.setVisibility(View.GONE);
			}
			getUserInfo(userId);
		} else {
			String state = prefUtils.getString(SharePrefUtils.USER_STATE, "");
			if (state.isEmpty()) {
				TipDialog.showDialogNoClose(MyActivity.this, string.tip,
						string.confirm, string.login_info_tip,
						LoginActivity.class);
			}
		}
	}
	/**
	 * 获取用户信息
	 */
	private void getUserInfo(String userId) {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.USER_INFO,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						UserInfoResult result = (UserInfoResult) AbJsonUtil
								.fromJson(s, UserInfoResult.class);

						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							String headImg = result.getData().getHeadImg();
							if (TextUtils.isEmpty(headImg)) {
								userImg.setImageResource(R.drawable.ic_msg_empty);
							} else {
								Glide.with(MyActivity.this).load(headImg).into(userImg);
							}
							if (TextUtils.isEmpty(result.getData()
									.getNickName())) {
								String username = "";
								List<MemberUser> users = db
										.findAll(MemberUser.class);
								if (users != null && users.size() != 0) {
									username = users.get(0).getUsername();
								}
								userNameView.setText(username);
							} else {
								userNameView.setText(result.getData()
										.getNickName());
							}
							userInfo = result.getData();
							try {
								JSONObject json = new JSONObject(s);
								JSONObject data= json.optJSONObject("data");
								prefUtils.setString(SharePrefUtils.CHECKSTATE, data.optString("checkState"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							Toast.makeText(MyActivity.this, result.getRepMsg(),
									Toast.LENGTH_LONG).show();

						}

					}
				});

	}
	@Override
	public void onClick(View v) {
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		switch (v.getId()) {
		case id.rl_personal:
			if (!TextUtils.isEmpty(userId)) {
				Intent intent = new Intent();
				intent.putExtra("userInfo", userInfo);
				if (userImg.getDrawable().getMinimumWidth()!=0){
					intent.putExtra("bitmap", getBitmapFromView(userImg));
				}
				intent.setClass(this, MyInfoActivity.class);
				startActivity(intent);
			} else {
				TipDialog.showDialogNoClose(MyActivity.this, string.tip,
						string.confirm, string.login_info_tip,
						LoginActivity.class);
			}

			break;
			case R.id.titleImageRight:
				Intent intent1 = new Intent(MyActivity.this,
						SettingActivity.class);
				startActivity(intent1);
				break;
		case id.huodong:
			if (!TextUtils.isEmpty(userId)) {
				startActivity(new Intent(this, HuodongActivity.class));
			} else {
				TipDialog.showDialogNoClose(MyActivity.this, string.tip,
						string.confirm, string.login_info_tip,
						LoginActivity.class);
			}
			break;
		case id.mygoods:
			if (!TextUtils.isEmpty(userId)) {
				startActivity(new Intent(this, MygoodsActivity.class));
			} else {
				TipDialog.showDialogNoClose(MyActivity.this, string.tip,
						string.confirm, string.login_info_tip,
						LoginActivity.class);
			}
			break;
		case id.bookOrderTv:
			// 预订点菜
			if (!TextUtils.isEmpty(userId)) {
				Intent intent = new Intent();
				intent.setClass(this, WebOrderListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(Constant.LOAD_URL,Constant.WEB_BASE_URL+"/#/orderList");
				intent.putExtras(bundle);
//				intent.putExtra("flag", "1");
				startActivity(intent,bundle);
			} else {
				TipDialog.showDialogNoClose(MyActivity.this, string.tip,
						string.confirm, string.login_info_tip,
						LoginActivity.class);
			}
			break;
		case id.mymessage:
			if (!TextUtils.isEmpty(userId)) {
				startActivity(new Intent(this, SettingMessageActivity.class));
			} else {
				TipDialog.showDialogNoClose(MyActivity.this, string.tip,
						string.confirm, string.login_info_tip,
						LoginActivity.class);
			}
			break;
		case id.mywallet:
			if (!TextUtils.isEmpty(userId)) {
				if(prefUtils.getString(SharePrefUtils.CHECKSTATE, "").equals("1")){
					if (prefUtils.getString(SharePrefUtils.HANDPASSWORD, "")
							.equals("")
							|| prefUtils.getString(SharePrefUtils.HANDPASSWORD, "") == null
							|| prefUtils.getString(SharePrefUtils.HANDPASSWORD, "")
									.equals("null")) {
						startActivity(new Intent(this, MyWalletActivity.class));
					} else {
						Intent intent=(new Intent(this,
								GesturePSWCheckActivity.class));
						intent.putExtra("type", 1);
						startActivity(intent);
					}
				}else if(prefUtils.getString(SharePrefUtils.CHECKSTATE, "").equals("3")){
					TipDialog.showDialogNoClose(this,
							string.tip, string.AuthenticationIng,
							string.Authenticationinfo,
							AuthenticationIngActivity.class);
				}else{
					TipDialog.showDialogNoClose(this,
							string.tip, string.gotoAuthentication,
							string.Authenticationinfo, AuthenticationActivity.class);
				}
			} else {
				TipDialog.showDialogNoClose(MyActivity.this, string.tip,
						string.confirm, string.login_info_tip,
						LoginActivity.class);
			}
			break;
		case id.guanzhu:
			if (!TextUtils.isEmpty(userId)) {
				startActivity(new Intent(this, MyGuanzhuActivity.class));
			} else {
				TipDialog.showDialogNoClose(MyActivity.this, string.tip,
						string.confirm, string.login_info_tip,
						LoginActivity.class);
			}
			break;
		case id.collect:
			if (!TextUtils.isEmpty(userId)) {
				startActivity(new Intent(this, MyCollectActivity.class));
			} else {
				TipDialog.showDialogNoClose(MyActivity.this, string.tip,
						string.confirm, string.login_info_tip,
						LoginActivity.class);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// 退出应用的时候，必须调用我们的disconnect()方法，而不是logout()。这样退出后我们这边才会启动push进程。
		RongIM.getInstance().disconnect();
		LocationUtil.getInstance(getApplicationContext()).stopLoc();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	public static Bitmap getBitmapFromView(View view) {
		Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
				view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(returnedBitmap);
		Drawable bgDrawable = view.getBackground();
		if (bgDrawable != null)
			bgDrawable.draw(canvas);
		else
			canvas.drawColor(Color.TRANSPARENT);
		view.draw(canvas);
		return returnedBitmap;
	}


	/**
	 * 验证是否为深蓝公寓用户
	 */
	private void verifyBlueUser(String telPhoneNum){
		params.put("param",telPhoneNum);
		httpInstance.post(HOST_URL1 + BLUE_APP_AUTHRIENTY, params, new AbsHttpStringResponseListener(MyActivity.this) {
			@Override
			public void onSuccess(int i, String s) {
				BlueAnthenResponse response = (BlueAnthenResponse) AbJsonUtil.fromJson(s,BlueAnthenResponse.class);
				if (response.rep_code.equals(Constant.HTTP_SUCCESS)&&response.result){
					mRlBlueIdentity.setVisibility(View.VISIBLE);
				}else{
					mRlBlueIdentity.setVisibility(View.GONE);
				}
			}
			@Override
			public void onFailure(int i, String s, Throwable throwable) {
				super.onFailure(i, s, throwable);
				throwable.printStackTrace();
//				TipDialog.showDialog(MyActivity.this, R.string.confirm,throwable.getMessage().toString());
			}
		});
	}

	/*** 重新选择头像刷新上一页面使用广播的方式*/
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(Bitmap bitmap){
		userImg.setImageBitmap(bitmap);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
