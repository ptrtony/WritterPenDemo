package com.android.bluetown.home.makefriends.activity;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.UserInfo;
import java.util.List;
import java.util.Locale;
import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.home.main.model.act.OthersInfoActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.receiver.RongCloudEvent;
import com.android.bluetown.utils.AndroidBug5497Workaround;
import com.android.bluetown.utils.InputSoftUtil;

/**
 * 加载会话界面
 * 
 * @author shenyz
 * 
 */
public class ConversationActivity extends TitleBarActivity implements
		OnClickListener, View.OnTouchListener {
	private String title;
	/**
	 * 会话类型
	 */
	private ConversationType mConversationType;
	/**
	 * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
	 */
	private String mTargetIds;
	private String mTargetId;
	private FinalDb db;
	private SharePrefUtils prefUtils;
	private LinearLayout mLlConversation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.conversation);
		mLlConversation = findViewById(R.id.ll_conversation);
		mLlConversation.setOnTouchListener(this);
		AndroidBug5497Workaround.assistActivity(this);
		initViewContent();
		prefUtils = new SharePrefUtils(ConversationActivity.this);
		db = FinalDb.create(ConversationActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		MemberUser user = users.get(0);
		if (user != null) {
			String userId = user.getMemberId();
			String nickname = user.getNickName();
			RongIM.getInstance().setCurrentUserInfo(
					new UserInfo(userId, nickname, Uri
							.parse(prefUtils.getString(
									SharePrefUtils.HEAD_IMG,
									""))));
			RongIM.getInstance().setMessageAttachedUserInfo(true);
		}
		connect(prefUtils.getString(SharePrefUtils.RONG_TOKEN,""));
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.app_blue);

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
				RongCloudEvent.getInstance().setOtherListener();
			}

			/**
			 * 连接融云失败
			 * 
			 * @param errorCodev
			 */
			@Override
			public void onError(RongIMClient.ErrorCode errorCode) {
				Log.d("LoginActivity", "--onError" + errorCode);
			}
		});
	}
	private void initViewContent() {
		Intent intent = getIntent();
		mTargetId = intent.getData().getQueryParameter("targetId");
		title = intent.getData().getQueryParameter("title");
		mConversationType = ConversationType.valueOf(intent
				.getData().getLastPathSegment()
				.toUpperCase(Locale.getDefault()));
		setRightImageView(R.drawable.user_chat);

		if (title.equals("社区交友")){
			rightImageLayout.setVisibility(View.INVISIBLE);
		}
		rightImageLayout.setOnClickListener(this);
		enterFragment(mConversationType, mTargetId);
		setTitleView(title);

	}

	/**
	 * 加载会话页面 ConversationFragment
	 * 
	 * @param mConversationType
	 *            会话类型
	 * @param mTargetId
	 *            目标 Id
	 */
	private void enterFragment(ConversationType mConversationType,
			String mTargetId) {

		ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager()
				.findFragmentById(R.id.conversation);

		Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName)
				.buildUpon().appendPath("conversation")
				.appendPath(mConversationType.getName().toLowerCase())
				.appendQueryParameter("targetId", mTargetId).build();

		fragment.setUri(uri);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightImageLayout:
			if (mConversationType.equals(ConversationType.GROUP)) {
				Intent intent = new Intent();
				intent.setClass(ConversationActivity.this,
						GroupDataActivity.class);
				intent.putExtra("mid", mTargetId);
				intent.putExtra("flcokName", title);
				startActivity(intent);
			} else if (mConversationType.equals(ConversationType.PRIVATE)) {
				Intent intent = new Intent();
				intent.setClass(ConversationActivity.this,
						OthersInfoActivity.class);
				intent.putExtra("otherUserId", mTargetId);
				startActivity(intent);
			}

			break;

		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		InputSoftUtil.hideSoftInput(this);
		return true;
	}
}
