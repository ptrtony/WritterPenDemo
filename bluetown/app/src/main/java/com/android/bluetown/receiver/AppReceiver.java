package com.android.bluetown.receiver;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation.ConversationType;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.android.bluetown.MainActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.home.main.model.act.ActionCenterActivity;
import com.android.bluetown.home.main.model.act.FleaMarketActivity;
import com.android.bluetown.home.main.model.act.HistoryServiceActivity;
import com.android.bluetown.home.main.model.act.MakeFriendsActivity;
import com.android.bluetown.home.main.model.act.ProductDetailActivity;
import com.android.bluetown.my.AuthenticationActivity;
import com.android.bluetown.my.AuthenticationSuccessActivity;
import com.android.bluetown.mywallet.activity.BliiListActivity;
import com.android.bluetown.mywallet.activity.TransferHistoryActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.JushConstant;

public class AppReceiver extends BroadcastReceiver {
	/**
	 * type(1：拒绝加好友2:同意加好友3:加好友提醒,4:自助报修,5:活动中心,6商品发布,7评论,8回复)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SharePrefUtils prefUtils = new SharePrefUtils(context);
		String type = null;
		String friendId = null;
		String relId = null;
		String action = intent.getAction();
		Bundle bundle = intent.getExtras();
		String Value = bundle.getString(JPushInterface.EXTRA_EXTRA);
		if(action.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)){
			try {
				JSONObject jsonObject = new JSONObject(Value);
				type = jsonObject.getString("type");
				try {
					friendId = jsonObject.getString("friendId");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				relId = jsonObject.getString("relId");
				// String title = jsonObject.getString("title");
				// String userId = jsonObject.getString("userId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (JushConstant.PAY_SUCCESS.equals(type)) {
//				Intent jushIntent = new Intent();
//				jushIntent.putExtra("push", "push");
//				jushIntent.setClass(context, TransferSuccessActivity.class);
//				jushIntent.putExtra("title", "支付");
//				jushIntent.putExtra("money", "¥"+relId);
//				jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//						| Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(jushIntent);
			}else if (JushConstant.RENZHEN_SUCCESS.equals(type)) {
				prefUtils.setString(SharePrefUtils.CHECKSTATE, "1");
				
			}
		}
		if (action.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
			try {
				JSONObject jsonObject = new JSONObject(Value);
				type = jsonObject.getString("type");
				try {
					friendId = jsonObject.getString("friendId");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				relId = jsonObject.getString("relId");
				// String title = jsonObject.getString("title");
				// String userId = jsonObject.getString("userId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (JushConstant.ADD_FRIEND.equals(type)
					|| JushConstant.REFUSE_FRIEND.equals(type)
					|| JushConstant.AGREE_FRIEND.equals(type)
					|| JushConstant.DELETE_FRIEND.equals(type)) {
				if (JushConstant.DELETE_FRIEND.equals(type)) {
					// RongIM.getInstance().getRongIMClient().clearMessages删除融云上该用户的所有消息内容
					// 删除会话列表中的会话 removeConversation
					RongIM.getInstance()
							.getRongIMClient()
							.removeConversation(ConversationType.PRIVATE,
									friendId);
				}
				Intent jushIntent = new Intent();
				jushIntent.setClass(context, MakeFriendsActivity.class);
				jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(jushIntent);
			} else if (JushConstant.SELF_SERVICE.equals(type)) {
				Intent jushIntent = new Intent();
				jushIntent.setClass(context, HistoryServiceActivity.class);
				jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(jushIntent);
			} else if (JushConstant.ACTION_CENTER.equals(type)) {
				Intent jushIntent = new Intent();
				jushIntent.putExtra("push", "push");
				jushIntent.setClass(context, ActionCenterActivity.class);
				jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(jushIntent);
			} else if (JushConstant.PUBLISH_PRODUCT.equals(type)
					|| JushConstant.COMMENT.equals(type)
					|| JushConstant.REPLY.equals(type)) {
				if (JushConstant.PUBLISH_PRODUCT.equals(type)) {
					Intent jushIntent = new Intent();
					jushIntent.putExtra("push", "push");
					jushIntent.setClass(context, FleaMarketActivity.class);
					jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(jushIntent);
				} else if (JushConstant.COMMENT.equals(type)
						|| JushConstant.REPLY.equals(type)) {
					Intent jushIntent = new Intent();
					jushIntent.putExtra("push", "push");
					jushIntent.setClass(context, ProductDetailActivity.class);
					jushIntent.putExtra("cid", relId);
					jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(jushIntent);
				}

			} else if (JushConstant.REFUND.equals(type)) {
				Intent jushIntent = new Intent();
				jushIntent.putExtra("push", "push");
				jushIntent.setClass(context, BliiListActivity.class);
				jushIntent.putExtra("cid", relId);
				jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(jushIntent);
			}else if (JushConstant.TRANSFER.equals(type)) {
				Intent jushIntent = new Intent();
				jushIntent.putExtra("push", "push");
				jushIntent.setClass(context, TransferHistoryActivity.class);
				jushIntent.putExtra("cid", relId);
				jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(jushIntent);
			}else if (JushConstant.PAY_SUCCESS.equals(type)) {
//				Intent jushIntent = new Intent();
//				jushIntent.putExtra("push", "push");
//				jushIntent.setClass(context, TransferSuccessActivity.class);
//				jushIntent.putExtra("title", "支付");
//				jushIntent.putExtra("money", relId);
//				jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//						| Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(jushIntent);
			}else if (JushConstant.RENZHEN_SUCCESS.equals(type)) {
				prefUtils.setString(SharePrefUtils.CHECKSTATE, "1");
				Intent jushIntent = new Intent();
				jushIntent.putExtra("push", "push");
				jushIntent.setClass(context, AuthenticationSuccessActivity.class);
				jushIntent.putExtra("cid", relId);
				jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(jushIntent);
			}else if (JushConstant.RENZHEN_FAIL.equals(type)) {
				Intent jushIntent = new Intent();
				jushIntent.putExtra("push", "push");
				jushIntent.setClass(context, AuthenticationActivity.class);
				jushIntent.putExtra("cid", relId);
				jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(jushIntent);
			}else {
				Intent jushIntent = new Intent();
				jushIntent.setClass(context, MainActivity.class);
				jushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(jushIntent);
			}
			Intent refreshintent = new Intent(
					"com.android.bm.refresh.new.msg.action");
			context.sendBroadcast(refreshintent);
		} else if (action.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)
				|| action.equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
			try {
				JSONObject jsonObject = new JSONObject(Value);
				type = jsonObject.getString("type");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (JushConstant.ADD_FRIEND.equals(type)
					|| JushConstant.REFUSE_FRIEND.equals(type)
					|| JushConstant.AGREE_FRIEND.equals(type)
					|| JushConstant.DELETE_FRIEND.equals(type)) {
				// 收到你好友加入或邀请消息，未读取推送消息
				BlueTownApp.isScanUnReadMsg = false;
				// 重置未读取消息数
				BlueTownApp.unReadMsgCount = BlueTownApp.unReadMsgCount + 1;
				if (JushConstant.DELETE_FRIEND.equals(type)) {
					// RongIM.getInstance().getRongIMClient().clearMessages删除融云上该用户的所有消息内容
					// 删除会话列表中的会话 removeConversation
					RongIM.getInstance()
							.getRongIMClient()
							.removeConversation(ConversationType.PRIVATE,
									friendId);
				}
			} else if (JushConstant.ACTION_CENTER.equals(type)) {
				// 活动中心消息未读消息数
				// 重置未读取消息数
				BlueTownApp.actionMsgCount = BlueTownApp.actionMsgCount + 1;
			} else if (JushConstant.PUBLISH_PRODUCT.equals(type)
					|| JushConstant.COMMENT.equals(type)
					|| JushConstant.REPLY.equals(type)) {
				// 跳蚤市场消息未读消息数
				// 重置未读取消息数
				BlueTownApp.fleaMarketMsgCount = BlueTownApp.fleaMarketMsgCount + 1;
			}

			Intent refreshintent = new Intent(
					"com.android.bm.refresh.new.msg.action");
			context.sendBroadcast(refreshintent);
		}

	}

}
