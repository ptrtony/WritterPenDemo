package com.android.bluetown.home.makefriends.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.receiver.RongCloudEvent;
import com.android.bluetown.result.WaitFriendListResult;
import com.android.bluetown.result.WaitFriendListResult.WaitFriend;
import com.android.bluetown.utils.Constant;

public class MessageActivity extends TitleBarActivity implements
		OnClickListener{
	private ImageView verifyMsgImg;
	private LinearLayout verfiyMsgLy;
	private TextView latestMsg, verifyMsgTime, unReadmsgCount;
	private String userId;
	private FinalDb db;
	private SharePrefUtils prefUtils;
	
	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.conversation_list);
		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(MessageActivity.this);
//		connect(prefUtils.getString(SharePrefUtils.RONG_TOKEN,""));
		initViews();
	}
	

	/**
	 * 初始化组件
	 */
	private void initViews() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
			
		}
		verifyMsgImg = (ImageView) findViewById(R.id.verifyMsgImg);
		verfiyMsgLy = (LinearLayout) findViewById(R.id.verfiyMsgLy);
		latestMsg = (TextView) findViewById(R.id.latestMsg);
		verifyMsgTime = (TextView) findViewById(R.id.verifyMsgTime);
		unReadmsgCount = (TextView) findViewById(R.id.unReadmsgCount);
		verfiyMsgLy.setOnClickListener(this);
		imageLoader.displayImage("drawable://" + R.drawable.verify_msg_bg,
				verifyMsgImg, defaultOptions);
	}


	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("消息");
		setTitleLayoutBg(R.color.chat_tab_message_color);
		righTextLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		connect(prefUtils.getString(SharePrefUtils.RONG_TOKEN,""));
		enterFragment();
		// 获取好友添加或邀请的消息列表
		getWaitFriendList(userId);
	}

	/**
	 * 加载 会话列表 ConversationListFragment
	 */
	private void enterFragment() {
		ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.conversationlist);

		Uri uri = Uri
				.parse("rong://" + getApplicationInfo().packageName)
				.buildUpon()
				.appendPath("conversationlist")
				.appendQueryParameter(
						Conversation.ConversationType.PRIVATE.getName(),
						"false") // 设置私聊会话非聚合显示
				.appendQueryParameter(
						Conversation.ConversationType.GROUP.getName(), "true")// 设置群组会话聚合显示
				.appendQueryParameter(
						Conversation.ConversationType.DISCUSSION.getName(),
						"false")// 设置讨论组会话非聚合显示
				.appendQueryParameter(
						Conversation.ConversationType.SYSTEM.getName(), "true")// 设置系统会话聚合显示
				.build();

		fragment.setUri(uri);

	}
	
	private void connect(final String token) {
		if (getApplicationInfo().packageName.equals(BlueTownApp.getCurProcessName(getApplicationContext()))) {
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
				public void onError(ErrorCode errorCode) {
					Log.d("LoginActivity", "--onError" + errorCode);
				}
			});
		}
	}

	ResultCallback<Message> callback = new ResultCallback<Message>() {
		@Override
		public void onError(ErrorCode arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess(Message arg0) {
			// TODO Auto-generated method stub
			arg0.setSentStatus(Message.SentStatus.SENT);
			RongIM.getInstance()
					.getRongIMClient()
					.setMessageSentStatus(arg0.getMessageId(),
							Message.SentStatus.SENT, null);
		}
	};

	/**
	 * 获取好友添加或邀请的消息列表
	 */
	private void getWaitFriendList(final String userId) {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.WAIT_FRIEND_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						WaitFriendListResult result = (WaitFriendListResult) AbJsonUtil
								.fromJson(s, WaitFriendListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							verfiyMsgLy.setVisibility(View.VISIBLE);
							// 设置未读取消息组件的显示状态
							if (!BlueTownApp.isScanUnReadMsg) {
								if (BlueTownApp.unReadMsgCount != 0) {
									unReadmsgCount.setVisibility(View.VISIBLE);
									unReadmsgCount
											.setText(BlueTownApp.unReadMsgCount
													+ "");
								} else {
									unReadmsgCount.setVisibility(View.GONE);
								}
							} else {
								unReadmsgCount.setVisibility(View.GONE);
							}
							WaitFriend friend = result.getData().getRows()
									.get(0);
				
							String msgType = friend.getType();
							if (!TextUtils.isEmpty(msgType)
									&& !TextUtils.isEmpty(friend.getStatus())) {
								// 好友
								if (friend.getType().equals("0")) {
									if (friend.getStatus().equals("0")) {
										latestMsg.setText(friend.getNickName()
												+ "申请加您为好友");
									} else if (friend.getStatus().equals("1")) {
										// 发起方friendId 目标方userid
										if (friend.getFriendId().equals(userId)) {
											latestMsg.setText("TA已拒绝加您为好友!");
										} else {
											latestMsg.setText(friend
													.getNickName()
													+ "申请加您加入好友!");
										}
									} else {
										// 发起方friendId 目标方《登录用户的》userid
										if (friend.getFriendId().equals(userId)) {
											latestMsg.setText("TA已同意加您为好友!");
										} else {
											latestMsg.setText(friend
													.getNickName()
													+ "申请加您加入好友!");
										}
									}
								} else {
									// 群 state：进去标示(0申请进群1邀请进群)
									if (friend.getState().equals("0")) {
										// status：是否同意状态（0：审核中1：拒绝2：通过）
										// 群主ID和登录用户Id相同，说明该用户是申请加入该群
										if (friend.getQzid().equals(userId)) {
											latestMsg.setText(friend
													.getNickName()
													+ "申请加入"
													+ friend.getFlockName());
										}
									} else {
										// status：是否同意状态（0：审核中1：拒绝2：通过）
										// 该用户被邀请加入该群
										latestMsg.setText(friend.getNickName()
												+ "邀请您加入"
												+ friend.getFlockName());
									}
								}
								verifyMsgTime.setText(friend.getCreateDate());
							}
						} else {
							// verfiyMsgLy.setVisibility(View.GONE);
							latestMsg.setText("暂无消息！");
						}

					}
				});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.verfiyMsgLy:
			startActivity(new Intent(MessageActivity.this,
					VerificationMessageActivity.class));
			// 重置推送的好友加入或邀请信息的查看状态
			BlueTownApp.isScanUnReadMsg = true;
			// 重置推送的好友加入或邀请信息的数目
			BlueTownApp.unReadMsgCount = 0;
			Intent refreshintent = new Intent(
					"com.android.bm.refresh.new.msg.action");
			sendBroadcast(refreshintent);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


//	@Override
//	public boolean onReceived(Message message, int i) {
//		enterFragment();
//		return false;
//	}

}
