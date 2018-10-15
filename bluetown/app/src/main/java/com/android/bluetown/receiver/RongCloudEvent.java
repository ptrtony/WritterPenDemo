package com.android.bluetown.receiver;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.SentMessageErrorCode;
import io.rong.imkit.model.UIConversation;
//import io.rong.imkit.widget.provider.CameraInputProvider;
//import io.rong.imkit.widget.provider.ImageInputProvider;
//import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
//import io.rong.notification.PushNotificationMessage;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.ImagePagerActivity;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.GroupsBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.home.main.model.act.OthersInfoActivity;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.GroupDetailResult;
import com.android.bluetown.result.GroupDetailResult.GroupDetail;
import com.android.bluetown.result.UserInfoResult;
import com.android.bluetown.utils.Constant;
/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有： 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。 5、群组信息提供者：GetGroupInfoProvider。 蓉c
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 */
@SuppressWarnings("static-access")
public final class RongCloudEvent implements RongIM.GroupInfoProvider,
		RongIM.ConversationBehaviorListener, RongIM.UserInfoProvider,
		RongIM.ConversationListBehaviorListener,
		RongIMClient.ConnectionStatusListener,RongIM.OnSendMessageListener, RongIMClient.OnReceiveMessageListener {
	private static final String TAG = RongCloudEvent.class.getSimpleName();
	private static RongCloudEvent mRongCloudInstance;
	private Context mContext;
	// 好友信息列表
	private List<FriendsBean> friends;
	// 群信息列表
	private List<GroupsBean> groups;

	// Http请求
	protected AbHttpUtil httpInstance;
	// Http请求参数
	protected AbRequestParams params;
	private FinalDb db;
	// 用户信息
	private com.android.bluetown.bean.UserInfo appUserInfo;
	// 群详情
	private GroupDetail detail = null;
	
	private SharePrefUtils prefUtils;
	/**
	 * 初始化 RongCloud.
	 * 
	 * @param context
	 *            上下文。
	 */
	public static void init(Context context) {
		if (mRongCloudInstance == null) {
			synchronized (RongCloudEvent.class) {
				if (mRongCloudInstance == null) {
					mRongCloudInstance = new RongCloudEvent(context);
				}
			}
		}
	}

	/**
	 * 构造方法。
	 * 
	 * @param context
	 *            上下文。
	 */
	private RongCloudEvent(Context context) {
		mContext = context;
		db = FinalDb.create(mContext);
		friends = new ArrayList<FriendsBean>();
		groups = new ArrayList<GroupsBean>();
		params = new AbRequestParams();
		httpInstance = AbHttpUtil.getInstance(context);
		prefUtils = new SharePrefUtils(BlueTownApp.ins);
		initDefaultListener();
	}

	/**
	 * 获取RongCloud 实例。
	 * 
	 * @return RongCloud。
	 */
	public static RongCloudEvent getInstance() {
		return mRongCloudInstance;
	}

	/**
	 * RongIM.init(this) 后直接可注册的Listener。
	 */
	private void initDefaultListener() {
		RongIM.setUserInfoProvider(this, true);// 设置用户信息提供者。
		RongIM.setGroupInfoProvider(this, true);// 设置群组信息提供者。
		// 会话页面操作监听器
		RongIM.setConversationBehaviorListener(this);
		// 会话列表操作监听器
		RongIM.setConversationListBehaviorListener(this);
		RongIM.getInstance().setMessageAttachedUserInfo(true);
	}

	/*
	 * 连接成功注册。 <p/> 在RongIM-connect-onSuccess后调用。
	 */
	public void setOtherListener() {
		RongIM.getInstance().setOnReceiveMessageListener(this);// 设置消息接收监听器。
		RongIMClient.setConnectionStatusListener(this);// 设置连接状态监听器。
		RongIM.getInstance().setSendMessageListener(this);
//		RongIM.getInstance().getRongIMClient()
//				.setOnReceiveMessageListener(this);// 设置消息接收监听器。
//		RongIM.getInstance().getRongIMClient()
//				.setConnectionStatusListener(this);// 设置连接状态监听器。
		// 扩展功能自定义
//		InputProvider.ExtendProvider[] provider = {
//				new ImageInputProvider(RongContext.getInstance()),// 图片
//				new CameraInputProvider(RongContext.getInstance()),// 相机
//		};
//
//		RongIM.getInstance().resetInputExtensionProvider(
//				ConversationType.PRIVATE, provider);
//		RongIM.resetInputExtensionProvider(
//				ConversationType.DISCUSSION, provider);
//		RongIM.resetInputExtensionProvider(ConversationType.GROUP,
//				provider);
//		RongIM.resetInputExtensionProvider(
//				ConversationType.CUSTOMER_SERVICE, provider);
	}

	/**
	 * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
	 * 
	 * @param userId
	 *            用户 Id。
	 * @return 用户信息，（注：由开发者提供用户信息）。
	 */
	@Override
	public UserInfo getUserInfo(String userId) {
		friends = db.findAll(FriendsBean.class);
		// 增强for把所有的用户信息 return 给融云
		for (FriendsBean friend : friends) {
			// 判断返回的userId
			if (friend.getUserId().trim().equals(userId)) {
				return new UserInfo(friend.getUserId(), friend.getNickName(),
						Uri.parse(friend.getHeadImg()));
			}
		}

		return null;

	}

	/**
	 * 群组信息的提供者：GetGroupInfoProvider 的回调方法， 获取群组信息。
	 *
//	 * @param groupId
	 *            群组 Id.
	 * @return 群组信息，（注：由开发者提供群组信息）。
	 */
	@Override
	public Group getGroupInfo(String arg0) {
		// TODO Auto-generated method stub
		groups = db.findAll(GroupsBean.class);
		if (groups != null && groups.size() > 0) {
			// 增强for循环把群信息给融云
			for (GroupsBean bean : groups) {
				// 判断返回的群id
				if (bean.getMid().equals(arg0)) {
					String flockImg = bean.getFlockImg();
					if (TextUtils.isEmpty(flockImg)) {
						flockImg = "";
					}
					return new Group(bean.getMid(), bean.getFlockName(),
							Uri.parse(flockImg));
				}

			}
		}
		return null;
	}

	/**
	 * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
	 * 
	 * @param context
	 *            应用当前上下文。
	 * @param conversationType
	 *            会话类型。
	 * @param user
	 *            被点击的用户的信息。
	 * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
	 */
	@Override
	public boolean onUserPortraitClick(Context context,
			ConversationType conversationType, UserInfo user) {
		Log.e(TAG, "----onUserPortraitClick");
		if (user != null) {
			if (conversationType
					.equals(ConversationType.PUBLIC_SERVICE)
					|| conversationType
							.equals(ConversationType.APP_PUBLIC_SERVICE)) {
				RongIM.getInstance().startPublicServiceProfile(mContext,
						conversationType, user.getUserId());
			} else {

				Intent in = new Intent(context, OthersInfoActivity.class);
				in.putExtra("otherUserId", user.getUserId());
				context.startActivity(in);
			}
		}

		return false;
	}

	@Override
	public boolean onUserPortraitLongClick(Context context,
			ConversationType conversationType, UserInfo userInfo) {
		Log.e(TAG, "----onUserPortraitLongClick");

		return true;
	}

	/**
	 * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
	 * 
	 * @param status
	 *            网络状态。
	 */
	@Override
	public void onChanged(ConnectionStatus status) {
		System.out.println(status);
		switch (status) {
		case CONNECTED:
			Log.d("MSG", "连接成功");
			break;
		case CONNECTING:
			Log.d("MSG", "正在连接");
			break;
		case DISCONNECTED:
			Log.d("MSG", "断开连接");
			break;
		case NETWORK_UNAVAILABLE:
			Log.d("MSG", "网络不可用");
			getUserInfo();
			break;
		// case LOGIN_ON_WEB:
		// Log.d("MSG", "用户在web端登录");
		// break;
		case CONN_USER_BLOCKED:
			Log.d("MSG","该账号被停用，被迫下线");
			Intent intent1 = new Intent("android.bluetown.account.stop");
			prefUtils.setBoolean("isLoging",false);
			mContext.sendBroadcast(intent1);
		case KICKED_OFFLINE_BY_OTHER_CLIENT:
			Log.d("MSG", "用户在其他设备登录，本机即将下线");
			Intent intent = new Intent("android.bluetown.login.status");
			prefUtils.setBoolean("isLoging",false);
			mContext.sendBroadcast(intent);
		}

	}

	/**
	 * 点击会话列表 item 后执行。
	 * 
	 * @param context
	 *            上下文。
	 * @param view
	 *            触发点击的 View。
	 * @param conversation
	 *            会话条目。
	 * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
	 */
	@Override
	public boolean onConversationClick(Context context, View view,
			UIConversation conversation) {
		// Toast.makeText(context, "onConversationClick ", Toast.LENGTH_SHORT)
		// .show();
		return false;
	}

	@Override
	public boolean onConversationPortraitClick(Context context, ConversationType conversationType, String s) {
		return false;
	}

	@Override
	public boolean onConversationPortraitLongClick(Context context, ConversationType conversationType, String s) {
		return false;
	}

	/**
	 * 长按会话列表 item 后执行。
	 * 
	 * @param context
	 *            上下文。
	 * @param view
	 *            触发点击的 View。
	 * @param conversation
	 *            长按会话条目。
	 * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
	 */
	@Override
	public boolean onConversationLongClick(Context context, View view,
			UIConversation conversation) {
		// Toast.makeText(context, "onConversationLongClick ",
		// Toast.LENGTH_SHORT)
		// .show();
		return false;
	}

	@Override
	public boolean onMessageClick(Context arg0, View arg1, Message arg2) {
		// TODO Auto-generated method stub
		// Toast.makeText(arg0, "onMessageClick " + arg2.getContent(),
		// Toast.LENGTH_SHORT).show();
		MessageContent messageContent = arg2.getContent();
		if (messageContent instanceof TextMessage) {// 文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			Log.d(TAG, "onSent-TextMessage:" + textMessage.getContent());
		} else if (messageContent instanceof ImageMessage) {// 图片消息
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
			ArrayList<String> urls = new ArrayList<String>();
			urls.add(imageMessage.getRemoteUri().toString().trim());
			Intent intent = new Intent(arg0, ImagePagerActivity.class);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
			// 不显示下面的下标
			intent.putExtra("indicator", "1");
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
			arg0.startActivity(intent);
		} else if (messageContent instanceof VoiceMessage) {// 语音消息
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onSent-voiceMessage:"
					+ voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// 图文消息
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG,
					"onSent-RichContentMessage:"
							+ richContentMessage.getContent());
		} else {
			Log.d(TAG, "onSent-其他消息，自己来判断处理");
		}
		return false;
	}

	@Override
	public boolean onMessageLinkClick(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		// Toast.makeText(arg0, "onMessageLinkClick " + arg1,
		// Toast.LENGTH_SHORT)
		// .show();
		return false;
	}

	@Override
	public boolean onMessageLongClick(Context arg0, View arg1, Message arg2) {
		// TODO Auto-generated method stub
		// Toast.makeText(arg0, "onMessageLongClick " + arg2.getContent(),
		// Toast.LENGTH_SHORT).show();
		return false;
	}

	/**
	 * 收到消息的处理。
	 * 接收消息监听器的实现，所有接收到的消息、通知、状态都经由此处设置的监听器处理。包括私聊消息、讨论组消息、群组消息、聊天室消息以及各种状态。
	 * 
//	 * @param message
	 *            收到的消息实体。
//	 * @param left
	 *            剩余未拉取消息数目。
	 * @return 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
	 */
//	@Override
//	public boolean onReceived(Message arg0, int arg1) {
//		// TODO Auto-generated method stub
//		System.out.println("<<<<<<<<<<<<onReceived");
//		// 根据用户id获取用户信息,刷新当前用户的信息
//		final ConversationType conversationType = arg0.getConversationType();
//		String targetId = arg0.getSenderUserId();
//
//		if (conversationType.equals(ConversationType.PRIVATE)) {
//			// 好友
//			List<FriendsBean> list_user = db.findAllByWhere(FriendsBean.class,
//					" userId=\"" + targetId + "\"");
//			if (list_user.size() == 0) {
//				getOtherUserInfo(arg0);
//			}
//		} else if (conversationType.equals(ConversationType.GROUP)) {
//			// 群
//			List<GroupsBean> list_group = db.findAllByWhere(GroupsBean.class,
//					" mid=\"" + targetId + "\"");
//			if (list_group.size() == 0) {
//				getOtherGroupInfo(arg0);
//			}
//		}
//
//		return false;
//	}

	/**
	 * 获取陌生用户的用户信息
	 */
	private void getOtherUserInfo(final Message arg0) {
		// TODO Auto-generated method stub
		Looper.prepare();
		final String targetId = arg0.getTargetId();
		final int status = arg0.getSentStatus().getValue();
		params.put("userId", targetId);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.USER_INFO,
				params, new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						UserInfoResult result = (UserInfoResult) AbJsonUtil
								.fromJson(s, UserInfoResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							appUserInfo = result.getData();
							FriendsBean user = new FriendsBean();
							user.setUserId(targetId);
							user.setNickName(appUserInfo.getNickName());
							user.setStatu(status + "");
							user.setHeadImg(appUserInfo.getHeadImg());
							db.save(user);
						}
						UserInfo in = new UserInfo(targetId, appUserInfo
								.getNickName(), Uri.parse(appUserInfo
								.getHeadImg()));
						// 需要更新的用户缓存数据
						RongIM.getInstance().refreshUserInfoCache(in);

					}
				});
		Looper.loop();

	}

	/**
	 * 获取（首次接收该群的消息）群信息（陌生群的信息）
	 */
	private void getOtherGroupInfo(final Message arg0) {
		// TODO Auto-generated method stub
		Looper.prepare();
		final String targetId = arg0.getTargetId();
		final String userId = arg0.getSenderUserId();
		// mid:群id（必填）
		// userId:当前登录用户(必填)
		params.put("userId", userId);
		params.put("mid", targetId);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.GROUP_DETAIL,
				params, new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						final GroupDetailResult result = (GroupDetailResult) AbJsonUtil
								.fromJson(s, GroupDetailResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							// TODO Auto-generated method stub
							detail = result.getData();
							GroupsBean group = new GroupsBean();
							group.setMid(targetId);
							group.setFlockImg(detail.getFlockImg());
							group.setFlockName(detail.getFlockName());
							group.setCreateDate(detail.getCreateDate());
							group.setFlockNumber(detail.getFlockNumber());
							group.setScale(detail.getScale());
							group.setHeadImg(detail.getHeadImg());
							group.setUserId(detail.getUserId());
							db.save(group);
						}
						Group in = new Group(targetId, detail.getFlockName(),
								Uri.parse(detail.getFlockImg()));
						// 需要更新的用户缓存数据
						RongIM.getInstance().refreshGroupInfoCache(in);
					}

				});
		Looper.loop();

	}
	private void getUserInfo() {
		// TODO Auto-generated method stub
//		Looper.prepare();
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				params.put("userId", user.getMemberId());
			}
		}
		httpInstance.post(Constant.HOST_URL + Constant.Interface.MobiMemberAction_checkStateByUserId,
				params, new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							System.out.println(repCode);
							if (repCode.equals("000000")) {		
								Intent intent1 = new Intent();
								intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent1.setClass(mContext, LoginActivity.class);
								intent1.putExtra("type", 1);
								mContext.startActivity(intent1);
//								Intent intent1 = new Intent("android.bluetown.login.info");
//								mContext.sendBroadcast(intent1);
								System.out.println("-----------------");
							} else if (repCode.equals("31011")){
								Intent intent1 = new Intent();
								intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent1.setClass(mContext, LoginActivity.class);
								intent1.putExtra("type", 2);
								mContext.startActivity(intent1);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					

				});
//		Looper.loop();
	}
	/**
	 * push 消息。
	 */
//	@Override
//	public boolean onReceivePushMessage(PushNotificationMessage arg0) {
//		// TODO Auto-generated method stub
//		connect(prefUtils.getString(SharePrefUtils.RONG_TOKEN,""));
//		return false;
//	}

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
				//prefUtils.setString(SharePrefUtils.RONG_TOKEN,token);
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
	public Message onSend(Message arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public boolean onSent(Message arg0, SentMessageErrorCode arg1) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public boolean onReceived(Message message, int i) {
		System.out.println("<<<<<<<<<<<<onReceived");
		// 根据用户id获取用户信息,刷新当前用户的信息
		final ConversationType conversationType = message.getConversationType();
		String targetId = message.getSenderUserId();

		if (conversationType.equals(ConversationType.PRIVATE)) {
			// 好友
			List<FriendsBean> list_user = db.findAllByWhere(FriendsBean.class,
					" userId=\"" + targetId + "\"");
			if (list_user.size() == 0) {
				getOtherUserInfo(message);
			}
		} else if (conversationType.equals(ConversationType.GROUP)) {
			// 群
			List<GroupsBean> list_group = db.findAllByWhere(GroupsBean.class,
					" mid=\"" + targetId + "\"");
			if (list_group.size() == 0) {
				getOtherGroupInfo(message);
			}
		}

		return false;
	}

}
