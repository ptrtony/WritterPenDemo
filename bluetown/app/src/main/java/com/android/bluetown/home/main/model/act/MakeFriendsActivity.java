package com.android.bluetown.home.main.model.act;

/**
 * 
 * @ClassName:  MakeFriendsActivity   
 * @Description:TODO(HomeActivity-交友)   
 * @author: shenyz   
 * @date:   2015年7月31日 上午10:28:15   
 *
 */
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.home.makefriends.activity.AddFriendActivity;
import com.android.bluetown.home.makefriends.activity.CreateGroupActivity;
import com.android.bluetown.home.makefriends.activity.JoinGroupActivity;
import com.android.bluetown.home.makefriends.activity.MakeFriendsActivity1;
import com.android.bluetown.home.makefriends.activity.MessageActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.receiver.RongCloudEvent;
import com.android.bluetown.utils.StatusBarUtils;

@SuppressWarnings("deprecation")
public class MakeFriendsActivity extends TabActivity implements OnClickListener {
	private static final String TAB_MESSAGEACTIVITY = "MESSAGEACTIVITY";
	private static final String TAB_MAKEFRIENDSACTIVITY1 = "MAKEFRIENDSACTIVITY1";
	private static final String TAB_ADDFRIENDACTIVITY = "ADDFRIENDACTIVITY";
	private static final String TAB_JOINGROUPACTIVITY = "JOINGROUPACTIVITY";
	private static final String TAB_CREATEGROUPACTIVITY = "CREATEGROUPACTIVITY";
	private FrameLayout messageLayout;
	private RelativeLayout makefriendsLayout;
	private RelativeLayout addfriendLayout;
	private RelativeLayout joingroupLayout;
	private RelativeLayout creategroupLayout;
	private ImageView messageImg;
	private ImageView makefriendsImg;
	private ImageView addfriendImg;
	private ImageView joingroupImg;
	private ImageView creategroupImg;
	private TextView messageTxt;
	private TextView makefriendsTxt;
	private TextView addfriendTxt;
	private TextView joingroupTxt;
	private TextView creategroupTxt;
	private TextView unReadMsgCount;
	private TabHost tabHost;
	private String group;
	private SharePrefUtils prefUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_make_friends);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			StatusBarUtils.getInstance().initStatusBar(true, this);
		}
		BlueTownExitHelper.addActivity(this);
		initTitle();
		findWidgets();
		initComponent();
		initListener();
		prefUtils = new SharePrefUtils(this);
		connect(prefUtils.getString(SharePrefUtils.RONG_TOKEN,""));
		if (group != null && !group.isEmpty()) {
			setFriendHomeActivity();
		
		} else {
			setMsgHomeActivity();
		}
		final Conversation.ConversationType[] conversationTypes = {
				Conversation.ConversationType.PRIVATE,
				Conversation.ConversationType.DISCUSSION,
				Conversation.ConversationType.GROUP,
				Conversation.ConversationType.SYSTEM,
				Conversation.ConversationType.APP_PUBLIC_SERVICE,
				Conversation.ConversationType.PUBLIC_SERVICE };
		if (RongIM.getInstance() != null) {
			/**
			 * 设置接收未读消息的监听器。
			 * 
			 * @param listener
			 *            接收未读消息消息的监听器。
			 * @param conversationTypes
			 *            接收指定会话类型的未读消息数。
			 */
			RongIM.getInstance().setOnReceiveUnreadCountChangedListener(
					mCountListener, conversationTypes);
		}

	}
	private void connect(final String token) {
		if (getApplicationInfo().packageName.equals(BlueTownApp.getCurProcessName(getApplicationContext()))){
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
					RongCloudEvent.getInstance().setOtherListener();
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

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initTitle() {
		TextView textView = (TextView) findViewById(R.id.titleMiddle);
		textView.setText(getString(R.string.make_friends));
		TextView textBack = (TextView) findViewById(R.id.titleLeft);
		textBack.setVisibility(View.VISIBLE);
		textBack.setText("");
		textBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void findWidgets() {
	try {
			group = getIntent().getStringExtra("group");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			group = "";
		}
		messageLayout = (FrameLayout) findViewById(R.id.layout_nav_msg);
		makefriendsLayout = (RelativeLayout) findViewById(R.id.layout_nav_users);
		addfriendLayout = (RelativeLayout) findViewById(R.id.layout_nav_add_user);
		joingroupLayout = (RelativeLayout) findViewById(R.id.layout_nav_add_group);
		creategroupLayout = (RelativeLayout) findViewById(R.id.layout_nav_create_group);
		messageImg = (ImageView) findViewById(R.id.iv_nav_msg_img);
		makefriendsImg = (ImageView) findViewById(R.id.iv_nav_users_img);
		addfriendImg = (ImageView) findViewById(R.id.iv_nav_add_user_img);
		joingroupImg = (ImageView) findViewById(R.id.iv_nav_add_group_img);
		creategroupImg = (ImageView) findViewById(R.id.iv_nav_create_group_img);
		messageTxt = (TextView) findViewById(R.id.iv_nav_msg);
		makefriendsTxt = (TextView) findViewById(R.id.iv_nav_users);
		addfriendTxt = (TextView) findViewById(R.id.iv_nav_add_user);
		joingroupTxt = (TextView) findViewById(R.id.iv_nav_add_group);
		creategroupTxt = (TextView) findViewById(R.id.iv_nav_create_group);
		unReadMsgCount = (TextView) findViewById(R.id.iv_nav_msg_count);
		tabHost = getTabHost();
	}

	private void initComponent() {
		Intent homeAtyIntent = new Intent(this, MessageActivity.class);
		Intent gameAtyIntent = new Intent(this, MakeFriendsActivity1.class);
		Intent personalAtyIntent = new Intent(this, AddFriendActivity.class);
		Intent settingAtyIntent = new Intent(this, JoinGroupActivity.class);
		Intent createAtyIntent = new Intent(this, CreateGroupActivity.class);
		tabHost.addTab(tabHost.newTabSpec(TAB_MESSAGEACTIVITY)
				.setIndicator(TAB_MESSAGEACTIVITY).setContent(homeAtyIntent));
		tabHost.addTab(tabHost.newTabSpec(TAB_MAKEFRIENDSACTIVITY1)
				.setIndicator(TAB_MAKEFRIENDSACTIVITY1)
				.setContent(gameAtyIntent));
		tabHost.addTab(tabHost.newTabSpec(TAB_ADDFRIENDACTIVITY)
				.setIndicator(TAB_ADDFRIENDACTIVITY)
				.setContent(personalAtyIntent));
		tabHost.addTab(tabHost.newTabSpec(TAB_JOINGROUPACTIVITY)
				.setIndicator(TAB_JOINGROUPACTIVITY)
				.setContent(settingAtyIntent));
		tabHost.addTab(tabHost.newTabSpec(TAB_CREATEGROUPACTIVITY)
				.setIndicator(TAB_CREATEGROUPACTIVITY)
				.setContent(createAtyIntent));
	}

	private void initListener() {
		messageLayout.setOnClickListener(this);
		makefriendsLayout.setOnClickListener(this);
		addfriendLayout.setOnClickListener(this);
		joingroupLayout.setOnClickListener(this);
		creategroupLayout.setOnClickListener(this);
	}

	

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_nav_msg:
			setMsgHomeActivity();
			break;
		case R.id.layout_nav_users:
			setFriendHomeActivity();
			break;
		case R.id.layout_nav_add_user:
			setBottomNavDrawable(R.drawable.msg_n, R.drawable.users_n,
					R.drawable.heart_p, R.drawable.user_add_n,
					R.drawable.folder_create_n);
			setBottomNavTxt(R.color.font_gray, R.color.font_gray,
					R.color.chat_tab_join_friend_color, R.color.font_gray,
					R.color.font_gray);
			tabHost.setCurrentTabByTag(TAB_ADDFRIENDACTIVITY);
			break;
		case R.id.layout_nav_add_group:
			setBottomNavDrawable(R.drawable.msg_n, R.drawable.users_n,
					R.drawable.heart_n, R.drawable.user_add_p,
					R.drawable.folder_create_n);
			setBottomNavTxt(R.color.font_gray, R.color.font_gray,
					R.color.font_gray, R.color.chat_tab_join_group_color,
					R.color.font_gray);
			tabHost.setCurrentTabByTag(TAB_JOINGROUPACTIVITY);
			break;
		case R.id.layout_nav_create_group:
			setBottomNavDrawable(R.drawable.msg_n, R.drawable.users_n,
					R.drawable.heart_n, R.drawable.user_add_n,
					R.drawable.folder_create_p);
			setBottomNavTxt(R.color.font_gray, R.color.font_gray,
					R.color.font_gray, R.color.font_gray,
					R.color.chat_tab_create_group_color);
			tabHost.setCurrentTabByTag(TAB_CREATEGROUPACTIVITY);
			break;
		default:
			break;
		}
	}

	private void setMsgHomeActivity() {
		setBottomNavDrawable(R.drawable.msg_p, R.drawable.users_n,
				R.drawable.heart_n, R.drawable.user_add_n,
				R.drawable.folder_create_n);
		setBottomNavTxt(R.color.chat_tab_message_color, R.color.font_gray,
				R.color.font_gray, R.color.font_gray, R.color.font_gray);
		tabHost.setCurrentTabByTag(TAB_MESSAGEACTIVITY);
	}

	private void setFriendHomeActivity() {
		setBottomNavDrawable(R.drawable.msg_n, R.drawable.users_p,
				R.drawable.heart_n, R.drawable.user_add_n,
				R.drawable.folder_create_n);
		setBottomNavTxt(R.color.font_gray, R.color.chat_tab_friend_color,
				R.color.font_gray, R.color.font_gray, R.color.font_gray);
		tabHost.setCurrentTabByTag(TAB_MAKEFRIENDSACTIVITY1);
	
	}

	private void setBottomNavTxt(int homeTxtRes, int gameTxtRes,
			int personalTxtRes, int settingTxtRes, int creategroupTxtRes) {
		messageTxt.setTextColor(getResources().getColor(homeTxtRes));
		makefriendsTxt.setTextColor(getResources().getColor(gameTxtRes));
		addfriendTxt.setTextColor(getResources().getColor(personalTxtRes));
		joingroupTxt.setTextColor(getResources().getColor(settingTxtRes));
		creategroupTxt.setTextColor(getResources().getColor(creategroupTxtRes));
	}

	private void setBottomNavDrawable(int homeImgRes, int gameImgRes,
			int personalImgRes, int settingImgRes, int creategroupImgRes) {
		messageImg.setImageDrawable(getResources().getDrawable(homeImgRes));
		makefriendsImg.setImageDrawable(getResources().getDrawable(gameImgRes));
		addfriendImg.setImageDrawable(getResources()
				.getDrawable(personalImgRes));
		joingroupImg
				.setImageDrawable(getResources().getDrawable(settingImgRes));
		creategroupImg.setImageDrawable(getResources().getDrawable(
				creategroupImgRes));
	}

	/**
	 * 接收未读消息的监听器。
	 */
	public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
		@Override
		public void onMessageIncreased(int count) {
			if (count == 0) {
				unReadMsgCount.setVisibility(View.GONE);
			} else {
				unReadMsgCount.setVisibility(View.VISIBLE);
				unReadMsgCount.setText(count + "");
			}
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}