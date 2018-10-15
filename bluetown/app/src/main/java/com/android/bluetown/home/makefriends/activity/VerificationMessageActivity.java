package com.android.bluetown.home.makefriends.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.VerificationMessageListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.WaitFriendListResult;
import com.android.bluetown.result.WaitFriendListResult.WaitFriend;
import com.android.bluetown.utils.Constant;

/**
 * 验证消息列表
 * 
 * @author shenyz
 * 
 */
public class VerificationMessageActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterLoadListener {
	private AbPullToRefreshView mAbPullToRefreshView;
	private ListView mListView;
	private VerificationMessageListAdapter adapter;
	private List<WaitFriend> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_verificationmessage);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		getWaitFriendList();

	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description:初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.verfy_msg);
		setTitleLayoutBg(R.color.chat_tab_message_color);
		setRighTextView(R.string.clear_msg);
		righTextLayout.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		list = new ArrayList<WaitFriend>();
		mListView = (ListView) findViewById(R.id.verfyInfoList);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
	}

	/**
	 * 获取好友添加或邀请的消息列表
	 */
	private void getWaitFriendList() {
		// TODO Auto-generated method stub
		params.put("userId",userId);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.WAIT_FRIEND_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						WaitFriendListResult result = (WaitFriendListResult) AbJsonUtil
								.fromJson(s, WaitFriendListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else {
							Toast.makeText(VerificationMessageActivity.this,
									result.getRepMsg(), Toast.LENGTH_LONG)
									.show();

						}

					}
				});

	}

	protected void dealResult(WaitFriendListResult result) {
		switch (listStatus) {
		// case Constant.ListStatus.LOAD:
		// list.addAll(result.getData().getRows());
		// mAbPullToRefreshView.onFooterLoadFinish();
		// break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getRows());
			mAbPullToRefreshView.onHeaderRefreshFinish();
			break;
		}

		if (adapter == null) {
			adapter = new VerificationMessageListAdapter(this, list);
			mListView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
			// showDialog(VerificationMessageActivity.this, R.string.verfy_msg,
			// R.string.dialog_ok,R.string.cancel, R.string.clear_msg_success);
			if (list != null && list.size() > 0) {
				// 清空验证消息列表
				clearVerifyMsgList();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 清空验证消息列表
	 */
	private void clearVerifyMsgList() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.CLEAR_ADD_MESSAGE, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						WaitFriendListResult result = (WaitFriendListResult) AbJsonUtil
								.fromJson(s, WaitFriendListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							if (list != null) {
								list.clear();
							}
							if (adapter != null) {
								adapter.notifyDataSetChanged();
							}
							Toast.makeText(VerificationMessageActivity.this,
									R.string.clear_success, Toast.LENGTH_SHORT)
									.show();
							// TipDialog.showDialog(context, R.string.tip,
							// R.string.dialog_ok, R.string.clear_success);
						} else {
							Toast.makeText(VerificationMessageActivity.this,
									result.getRepMsg(), Toast.LENGTH_LONG)
									.show();

						}

					}
				});

	}

	// private void showDialog(final Context context, int titleId,
	// int confirmTextId,int cancelTextId, int contentStr) {
	// SweetAlertDialog dialog = new SweetAlertDialog(context)
	// .setContentText(context.getString(contentStr));
	// dialog.setTitleText(context.getString(titleId));
	// dialog.setConfirmText(context.getString(confirmTextId));
	// dialog.setCancelText(context.getString(cancelTextId));
	// dialog.setContentText(context.getString(contentStr));
	// dialog.setConfirmClickListener(new OnSweetClickListener() {
	//
	// @Override
	// public void onClick(SweetAlertDialog sweetAlertDialog) {
	// // TODO Auto-generated method stub
	// // 清空验证消息列表
	// clearVerifyMsgList();
	// sweetAlertDialog.dismiss();
	// }
	// });
	// dialog.setCancelClickListener(new OnSweetClickListener() {
	//
	// @Override
	// public void onClick(SweetAlertDialog sweetAlertDialog) {
	// // TODO Auto-generated method stub
	// sweetAlertDialog.dismiss();
	// }
	// });
	// dialog.show();
	// }

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		// list.clear();
		// listStatus = Constant.ListStatus.REFRESH;
		// page = 1;
		// getWaitFriendList();
		mAbPullToRefreshView.onFooterLoadFinish();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		// page = 1;
		getWaitFriendList();

	}

}
