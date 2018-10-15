package com.android.bluetown;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.circle.activity.CircleActivity;
import com.android.bluetown.home.main.model.act.SmartRestaurantActivity;
import com.android.bluetown.my.MyActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.LocationUtil;
import com.android.bluetown.utils.StatusBarUtils;
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements OnClickListener {
	private static final String TAG = "UpdateManager";
	public static final int TOKEN_ERROR = 0;
	public static final int SUCCESS = 1;
	public static final int ERROR = 2;
	private static final String TAB_HOME = "HOME_ACTIVITY";
	private static final String TAB_GAME = "SURROUNDING_ACTIVITY";
//	private static final String TAB_FIND = "FIND_ACTIVITY";
	private static final String TAB_PERSONAL = "CIRCLE_ACTIVITY";
	private static final String TAB_SETTING = "MY_ACTIVITY";
	private RelativeLayout homeLayout;
	private RelativeLayout surroundingLayout;
//	private RelativeLayout findLayout;
	private RelativeLayout circleLayout;
	private RelativeLayout myLayout;
	private ImageView homeImg;
	private ImageView surroundingImg;
//	private ImageView findImg;
	private ImageView circleImg;
	private ImageView myImg;
	private TextView homeTxt;
	private TextView surroundingTxt;
//	private TextView findTxt;
	private TextView circleTxt;
	private TextView myTxt;
	private TabHost tabHost;
	private LinearLayout mLlbottomBar;
	// 注册成功和修改密码的标志
	private String flag, username, password;
	private SharePrefUtils prefUtils;
	private ConnectivityManager mConnectivityManager;
	private NetworkInfo netInfo;

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!this.isTaskRoot()) {
			Intent mainIntent = getIntent();
			String action = mainIntent.getAction();
			if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
				finish();
				return;
			}
		}
		setContentView(R.layout.activity_main);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			StatusBarUtils.getInstance().initStatusBar(true, this);
		}

		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(this);
		findWidgets();
		getIntentExtraData();
		initComponent();
		initListener();
		startHomeActivity();
	}

	/**
	 * register network state
	 */
	private void registerNetWorkRecevicer() {
		// TODO Auto-generated method stub
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(myNetReceiver, mFilter);
	}

	/**
	 * 注册成功和修改密码成功后的的用户名和密码
	 */
	private void getIntentExtraData() {
		try {
			// 注册成功和修改密码
			flag = getIntent().getStringExtra("flag");
			username = getIntent().getStringExtra("username");
			password = getIntent().getStringExtra("password");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void findWidgets() {
		homeLayout = (RelativeLayout) findViewById(R.id.layout_main_nav_home);
		surroundingLayout = (RelativeLayout) findViewById(R.id.layout_main_nav_game);
//		findLayout = (RelativeLayout) findViewById(R.id.layout_main_nav_find);
		circleLayout = (RelativeLayout) findViewById(R.id.layout_main_nav_personal);
		myLayout = (RelativeLayout) findViewById(R.id.layout_main_nav_setting);
		homeImg = (ImageView) findViewById(R.id.iv_main_nav_home);
		surroundingImg = (ImageView) findViewById(R.id.iv_main_nav_game);
//		findImg = (ImageView) findViewById(R.id.iv_main_nav_find);
		circleImg = (ImageView) findViewById(R.id.iv_main_nav_personal);
		myImg = (ImageView) findViewById(R.id.iv_main_nav_setting);
		homeTxt = (TextView) findViewById(R.id.tv_main_nav_home);
		surroundingTxt = (TextView) findViewById(R.id.tv_main_nav_game);
//		findTxt = (TextView) findViewById(R.id.tv_main_nav_find);
		circleTxt = (TextView) findViewById(R.id.tv_main_nav_personal);
		myTxt = (TextView) findViewById(R.id.tv_main_nav_setting);
		mLlbottomBar = findViewById(R.id.ll_bottom_bar);
		tabHost = getTabHost();
	}

	private void initComponent() {
		Intent homeAtyIntent = new Intent();
		homeAtyIntent.putExtra("flag", flag);
		homeAtyIntent.putExtra("username", username);
		homeAtyIntent.putExtra("password", password);
		homeAtyIntent.setClass(this,HomeActivity.class);
		Intent gameAtyIntent = new Intent(this,
				SmartRestaurantActivity.class);
//		Intent findAtyIntent = new Intent(this, FindActivity.class);
		Intent personalAtyIntent = new Intent(this, CircleActivity.class);
		Intent settingAtyIntent = new Intent(this, MyActivity.class);
		tabHost.addTab(tabHost.newTabSpec(TAB_HOME).setIndicator(TAB_HOME)
				.setContent(homeAtyIntent));
		tabHost.addTab(tabHost.newTabSpec(TAB_GAME).setIndicator(TAB_GAME)
				.setContent(gameAtyIntent));
//		tabHost.addTab(tabHost.newTabSpec(TAB_FIND).setIndicator(TAB_FIND)
//				.setContent(findAtyIntent));
		tabHost.addTab(tabHost.newTabSpec(TAB_PERSONAL)
				.setIndicator(TAB_PERSONAL).setContent(personalAtyIntent));
		tabHost.addTab(tabHost.newTabSpec(TAB_SETTING)
				.setIndicator(TAB_SETTING).setContent(settingAtyIntent));
	}

	private void initListener() {
		homeLayout.setOnClickListener(this);
		surroundingLayout.setOnClickListener(this);
//		findLayout.setOnClickListener(this);
		circleLayout.setOnClickListener(this);
		myLayout.setOnClickListener(this);
	}

	private void startHomeActivity() {
		if (null!=getIntent().getStringExtra("type")&&getIntent().getStringExtra("type").equals("1")){
			homeActivityToSurroundingCanteenActivity();
			if (getIntent().getBooleanExtra("isNavigationVisible",false)){
				mLlbottomBar.animate().translationY(0).setDuration(1500);
			}else{
				mLlbottomBar.animate().translationY(mLlbottomBar.getHeight()).setDuration(1500);
			}
		}else{
			tabHost.setCurrentTabByTag(TAB_HOME);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//connect(prefUtils.getString(SharePrefUtils.RONG_TOKEN,""));
		super.onResume();
		registerNetWorkRecevicer();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_main_nav_home:
			setBottomNavDrawable(
					R.drawable.iv_main_bottom_navigation_home_press,
					R.drawable.iv_main_bottom_navigation_surround_normal,					
					R.drawable.iv_main_bottom_navigation_circle_normal,
					R.drawable.iv_main_bottom_navigation_mine_normal);
			setBottomNavTxt(R.color.title_bg_blue, R.color.main_content_text,
					R.color.main_content_text, R.color.main_content_text,
					R.color.main_content_text);
			tabHost.setCurrentTabByTag(TAB_HOME);
			break;
		case R.id.layout_main_nav_game:
			homeActivityToSurroundingCanteenActivity();
			break;
//		case R.id.layout_main_nav_find:
//			setBottomNavDrawable(
//					R.drawable.iv_main_bottom_navigation_home_normal,
//					R.drawable.iv_main_bottom_navigation_surround_normal,
//					R.drawable.iv_main_bottom_navigation_find_normal,
//					R.drawable.iv_main_bottom_navigation_circle_normal,
//					R.drawable.iv_main_bottom_navigation_mine_normal);
//			setBottomNavTxt(R.color.main_content_text,
//					R.color.main_content_text, R.color.title_bg_blue,
//					R.color.main_content_text, R.color.main_content_text);
//			tabHost.setCurrentTabByTag(TAB_FIND);
//			break;
		case R.id.layout_main_nav_personal:
			setBottomNavDrawable(
					R.drawable.iv_main_bottom_navigation_home_normal,
					R.drawable.iv_main_bottom_navigation_surround_normal,
					R.drawable.iv_main_bottom_navigation_circle_press,
					R.drawable.iv_main_bottom_navigation_mine_normal);
			setBottomNavTxt(R.color.main_content_text,
					R.color.main_content_text, R.color.main_content_text,
					R.color.title_bg_blue, R.color.main_content_text);
			tabHost.setCurrentTabByTag(TAB_PERSONAL);
			break;
		case R.id.layout_main_nav_setting:
//			String userId = "";
			setBottomNavDrawable(
					R.drawable.iv_main_bottom_navigation_home_normal,
					R.drawable.iv_main_bottom_navigation_surround_normal,
					R.drawable.iv_main_bottom_navigation_circle_normal,
					R.drawable.iv_main_bottom_navigation_mine_press);
			setBottomNavTxt(R.color.main_content_text,
					R.color.main_content_text, R.color.main_content_text,
					R.color.main_content_text, R.color.title_bg_blue);
//			String state = prefUtils.getString(SharePrefUtils.USER_STATE, "");
//			FinalDb db = FinalDb.create(MainActivity.this);
//			List<MemberUser> users = db.findAll(MemberUser.class);
//			if (users != null && users.size() > 0) {
//				MemberUser user = users.get(0);
//				if (user != null) {
//					userId = user.getMemberId();
//				}
//			}
//			if (userId.isEmpty() && !state.isEmpty()) {
//				Intent intent = new Intent();
//				intent.setClass(MainActivity.this, LoginActivity.class);
//				startActivity(intent);
//			} else {
			tabHost.setCurrentTabByTag(TAB_SETTING);
//			}
			break;
		default:
			break;
		}
	}

	private void homeActivityToSurroundingCanteenActivity() {
		setBottomNavDrawable(
                R.drawable.iv_main_bottom_navigation_home_normal,
                R.drawable.iv_main_bottom_navigation_surround_press,
                R.drawable.iv_main_bottom_navigation_circle_normal,
                R.drawable.iv_main_bottom_navigation_mine_normal);
		setBottomNavTxt(R.color.main_content_text, R.color.title_bg_blue,
                R.color.main_content_text, R.color.main_content_text,
                R.color.main_content_text);
		tabHost.setCurrentTabByTag(TAB_GAME);
	}

	private void setBottomNavTxt(int homeTxtRes, int gameTxtRes,
			int findTxtRes, int personalTxtRes, int settingTxtRes) {
		homeTxt.setTextColor(getResources().getColor(homeTxtRes));
		surroundingTxt.setTextColor(getResources().getColor(gameTxtRes));
//		findTxt.setTextColor(getResources().getColor(findTxtRes));
		circleTxt.setTextColor(getResources().getColor(personalTxtRes));
		myTxt.setTextColor(getResources().getColor(settingTxtRes));
	}

	private void setBottomNavDrawable(int homeImgRes, int gameImgRes,
			int findImgRes, int personalImgRes) {
		homeImg.setImageDrawable(getResources().getDrawable(homeImgRes));
		surroundingImg.setImageDrawable(getResources().getDrawable(gameImgRes));
//		findImg.setImageDrawable(getResources().getDrawable(findImgRes));
		circleImg.setImageDrawable(getResources().getDrawable(findImgRes));
		myImg.setImageDrawable(getResources().getDrawable(personalImgRes));
	}

	@Override
	public void onBackPressed() {
		// 退出应用的时候，必须调用我们的disconnect()方法，而不是logout()。这样退出后我们这边才会启动push进程。
		RongIM.getInstance().disconnect();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}


	private void connect(final String token) {

		RongIM.connect(token, new RongIMClient.ConnectCallback() {

			@Override
			public void onTokenIncorrect() {

				Log.d("LoginActivity", "--onTokenIncorrect");
			}

			/**
			 * 链接成功
			 */
			@Override
			public void onSuccess(String userid) {
				prefUtils.setString(SharePrefUtils.RONG_TOKEN,token);
//				RongCloudEvent.getInstance().setOtherListener();
			}

			/**
			 * 连接融云失败
			 * 
//			 * @param errorCodev
			 */
			@Override
			public void onError(RongIMClient.ErrorCode errorCode) {
				Log.d("LoginActivity", "--onError" + errorCode);
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LocationUtil.getInstance(getApplicationContext()).stopLoc();
		if (myNetReceiver != null) {
			unregisterReceiver(myNetReceiver);
		}
	}

	// 监听网络状态变化的广播接收器
	private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				netInfo = mConnectivityManager.getActiveNetworkInfo();
				if (netInfo != null && netInfo.isAvailable()) {
					// 网络连接(WiFi网络/有线网络/3g网络)
					System.out.println("<<<<<<<connenct success");

				} else {
					// 网络断开
					System.out.println("<<<<<<<connenct fail");
					//

				}
			}

		}
	};



}