package com.android.bluetown.adapter;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.home.main.model.act.OthersInfoActivity;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.WaitFriendListResult;
import com.android.bluetown.result.WaitFriendListResult.WaitFriend;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: VerificationMessageListAdapter
 * @Description:TODO(好友请求列表的Adapter)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
public class VerificationMessageListAdapter extends BaseContentAdapter {
	private String loginUserId;
	private List<MemberUser> users;
	private FinalDb db;
	public VerificationMessageListAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		db = FinalDb.create(mContext);
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_verification_message,
					null);
			mHolder = new ViewHolder();
			mHolder.verfyMsgLy = (LinearLayout) convertView
					.findViewById(R.id.verfyMsgLy);
			mHolder.userImg = (ImageView) convertView
					.findViewById(R.id.userImage);
			mHolder.name = (TextView) convertView.findViewById(R.id.name);
			mHolder.contents = (TextView) convertView
					.findViewById(R.id.content);
			mHolder.time = (TextView) convertView.findViewById(R.id.time);
			mHolder.refuse = (TextView) convertView.findViewById(R.id.refuse);
			mHolder.agree = (TextView) convertView.findViewById(R.id.agree);
			mHolder.requestStatus = (TextView) convertView
					.findViewById(R.id.requestStatus);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(final ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final WaitFriend item = (WaitFriend) getItem(position);
		if (!TextUtils.isEmpty(item.getHeadImg())) {
			imageLoader.displayImage(item.getHeadImg(), mHolder.userImg,
					defaultOptions);		
		}
		mHolder.name.setText(item.getNickName());
		mHolder.time.setText(item.getCreateDate());
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				loginUserId = user.getMemberId();
			}
		}		// 好友
		if (item.getType().equals("0")) {
			mHolder.userImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("otherUserId", item.getUserId());
					intent.setClass(context, OthersInfoActivity.class);
					context.startActivity(intent);
				}
			});
			if (item.getStatus().equals("0")) {
				mHolder.verfyMsgLy.setVisibility(View.VISIBLE);
				mHolder.refuse.setVisibility(View.VISIBLE);
				mHolder.agree.setVisibility(View.VISIBLE);
				mHolder.requestStatus.setVisibility(View.GONE);
				mHolder.contents.setText(item.getNickName() + "申请加您为好友");
				mHolder.refuse.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-gener.eqated method stub
						acceptOrNotFriend(mHolder, item, loginUserId,"1");
					}
				});
				mHolder.agree.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						acceptOrNotFriend(mHolder, item,loginUserId, "0");
					}
				});
			} else if (item.getStatus().equals("1")) {
				// 发起方friendId 目标方userid
				if (item.getFriendId().equals(loginUserId)) {
					mHolder.verfyMsgLy.setVisibility(View.VISIBLE);
					mHolder.refuse.setVisibility(View.GONE);
					mHolder.agree.setVisibility(View.GONE);
					mHolder.requestStatus.setVisibility(View.GONE);
					mHolder.contents.setText("TA已拒绝加您为好友!");
				} else {
					mHolder.verfyMsgLy.setVisibility(View.VISIBLE);
					mHolder.refuse.setVisibility(View.GONE);
					mHolder.agree.setVisibility(View.GONE);
					mHolder.requestStatus.setVisibility(View.VISIBLE);
					mHolder.requestStatus.setTextColor(context.getResources()
							.getColor(R.color.chat_tab_friend_color));
					mHolder.contents.setText(item.getNickName() + "申请加您加入好友!");
					mHolder.requestStatus.setText("已拒绝");
				}
			} else {
				// 发起方friendId 目标方《登录用户的》userid
				if (item.getFriendId().equals(loginUserId)) {
					mHolder.verfyMsgLy.setVisibility(View.VISIBLE);
					mHolder.refuse.setVisibility(View.GONE);
					mHolder.agree.setVisibility(View.GONE);
					mHolder.requestStatus.setVisibility(View.GONE);
					mHolder.contents.setText("TA已同意加您为好友!");
				} else {
					mHolder.verfyMsgLy.setVisibility(View.VISIBLE);
					mHolder.refuse.setVisibility(View.GONE);
					mHolder.agree.setVisibility(View.GONE);
					mHolder.requestStatus.setVisibility(View.VISIBLE);
					mHolder.requestStatus.setTextColor(context.getResources()
							.getColor(R.color.chat_tab_message_color));
					mHolder.contents.setText(item.getNickName() + "申请加您加入好友!");
					mHolder.requestStatus.setText("已同意");
				}
			}
		} else {
			// 群 state：进去标示(0申请进群1邀请进群)
			if (item.getState().equals("0")) {
				mHolder.userImg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.putExtra("otherUserId", item.getMemberId());
						intent.setClass(context, OthersInfoActivity.class);
						context.startActivity(intent);
					}
				});
				// status：是否同意状态（0：审核中1：拒绝2：通过）
				if (item.getStatus().equals("0")) {
					mHolder.verfyMsgLy.setVisibility(View.VISIBLE);
					mHolder.refuse.setVisibility(View.VISIBLE);
					mHolder.agree.setVisibility(View.VISIBLE);
					mHolder.requestStatus.setVisibility(View.GONE);
					// 群主ID和登录用户Id相同，说明该用户是申请加入该群
					if (item.getQzid().equals(loginUserId)) {
						mHolder.contents.setText(item.getNickName() + "申请加入"
								+ item.getFlockName());
						mHolder.refuse
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-gener.eqated method stub
										acceptOrNotGroup(mHolder, item, "1",
												"0");
									}
								});
						mHolder.agree.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								acceptOrNotGroup(mHolder, item, "0", "0");
							}
						});
					} else {
						mHolder.verfyMsgLy.setVisibility(View.GONE);
					}
				} else if (item.getStatus().equals("1")) {
					mHolder.verfyMsgLy.setVisibility(View.VISIBLE);
					mHolder.refuse.setVisibility(View.GONE);
					mHolder.agree.setVisibility(View.GONE);
					mHolder.requestStatus.setVisibility(View.VISIBLE);
					// 群主ID和登录用户Id相同，说明该用户是申请加入该群
					if (item.getQzid().equals(loginUserId)) {
						mHolder.requestStatus.setTextColor(context
								.getResources().getColor(
										R.color.chat_tab_friend_color));
						mHolder.contents.setText(item.getNickName() + "申请加入"
								+ item.getFlockName());
						mHolder.requestStatus.setText("已拒绝");
					} else {
						mHolder.verfyMsgLy.setVisibility(View.GONE);
					}
				} else {
					mHolder.verfyMsgLy.setVisibility(View.VISIBLE);
					mHolder.refuse.setVisibility(View.GONE);
					mHolder.agree.setVisibility(View.GONE);
					mHolder.requestStatus.setVisibility(View.VISIBLE);
					// 群主ID和登录用户Id相同，说明该用户是申请加入该群
					if (item.getQzid().equals(loginUserId)) {
						mHolder.requestStatus.setTextColor(context
								.getResources().getColor(
										R.color.chat_tab_message_color));
						mHolder.contents.setText(item.getNickName() + "申请加入"
								+ item.getFlockName());
						mHolder.requestStatus.setText("已同意");
					} else {
						mHolder.verfyMsgLy.setVisibility(View.GONE);
					}
				}

			} else {
				mHolder.userImg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.putExtra("otherUserId", item.getUserId());
						intent.setClass(context, OthersInfoActivity.class);
						context.startActivity(intent);
					}
				});
				// status：是否同意状态（0：审核中1：拒绝2：通过）
				if (item.getStatus().equals("0")) {
					mHolder.refuse.setVisibility(View.VISIBLE);
					mHolder.agree.setVisibility(View.VISIBLE);
					mHolder.requestStatus.setVisibility(View.GONE);
					// 该用户被邀请加入该群
					mHolder.contents.setText(item.getNickName() + "邀请您加入"
							+ item.getFlockName());
					mHolder.refuse.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-gener.eqated method stub
							acceptOrNotGroup(mHolder, item, "1", "1");
						}
					});
					mHolder.agree.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							acceptOrNotGroup(mHolder, item, "0", "1");
						}
					});
				} else if (item.getStatus().equals("1")) {
					mHolder.refuse.setVisibility(View.GONE);
					mHolder.agree.setVisibility(View.GONE);
					mHolder.requestStatus.setVisibility(View.VISIBLE);
					mHolder.requestStatus.setTextColor(context.getResources()
							.getColor(R.color.chat_tab_friend_color));
					// 该用户被邀请加入该群
					mHolder.contents.setText(item.getNickName() + "邀请您加入"
							+ item.getFlockName());
					mHolder.requestStatus.setText("已拒绝");
				} else {
					mHolder.refuse.setVisibility(View.GONE);
					mHolder.agree.setVisibility(View.GONE);
					mHolder.requestStatus.setVisibility(View.VISIBLE);

					mHolder.requestStatus.setTextColor(context.getResources()
							.getColor(R.color.chat_tab_message_color));
					// 该用户被邀请加入该群
					mHolder.contents.setText(item.getNickName() + "邀请您加入"
							+ item.getFlockName());
					mHolder.requestStatus.setText("已同意");
				}
			}
		}
	}

	/**
	 * 同意 / 拒绝进群 userId:申请人id（必填） mid：群id（必填） type:必填（0：同意1：拒绝）
	 * state:必填（0：邀请1：申请）
	 * 
	 * @param mHolder
	 * @param item
	 * @param type
	 *            type:必填（0：同意1：拒绝）
	 */
	private void acceptOrNotGroup(final ViewHolder mHolder,
			final WaitFriend item, final String type, String state) {
		// TODO Auto-generated method stub
		params.put("userId", item.getMemberId());
		params.put("mid", item.getFlockId());
		params.put("type", type);
		params.put("state", state);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.ACCEPT_OR_NOT_GROUP, params,
				new AbsHttpStringResponseListener(context, null) {
					@Override
					public void onSuccess(int i, String s) {
						WaitFriendListResult result = (WaitFriendListResult) AbJsonUtil
								.fromJson(s, WaitFriendListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							mHolder.refuse.setVisibility(View.GONE);
							mHolder.agree.setVisibility(View.GONE);
							mHolder.requestStatus.setVisibility(View.VISIBLE);
							if (type.equals("0")) {
								mHolder.requestStatus
										.setTextColor(context
												.getResources()
												.getColor(
														R.color.chat_tab_message_color));
								mHolder.requestStatus.setText("已同意");
							} else {
								mHolder.requestStatus.setTextColor(context
										.getResources().getColor(
												R.color.chat_tab_friend_color));
								mHolder.requestStatus.setText("已拒绝");
							}
						} else {
							Toast.makeText(context, result.getRepMsg(),
									Toast.LENGTH_LONG).show();

						}

					}
				});

	}

	/**
	 * 同意 / 拒绝 添加好友 userId：当前用户id（必填） friendId:添加的好友id（必填） type:必填(0:同意，1：拒绝)
	 * 
	 * @param mHolder
	 * @param item
	 * @param type
	 *            type:必填（0：同意1：拒绝） 发起方friendId 目标方 userid
	 */
	private void acceptOrNotFriend(final ViewHolder mHolder,
			final WaitFriend item, String loginUserId,final String type) {
		// TODO Auto-generated method stub
		
		params.put("userId", loginUserId);
		params.put("friendId", item.getUserId());
		params.put("type", type);
		httpInstance.post(
				Constant.HOST_URL + Constant.Interface.ACCEPT_REQUEST, params,
				new AbsHttpStringResponseListener(context, null) {
					@Override
					public void onSuccess(int i, String s) {
						WaitFriendListResult result = (WaitFriendListResult) AbJsonUtil
								.fromJson(s, WaitFriendListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							mHolder.refuse.setVisibility(View.GONE);
							mHolder.agree.setVisibility(View.GONE);
							mHolder.requestStatus.setVisibility(View.VISIBLE);
							if (type.equals("0")) {
								mHolder.requestStatus
										.setTextColor(context
												.getResources()
												.getColor(
														R.color.chat_tab_message_color));
								mHolder.contents.setText(item.getNickName()
										+ "申请添加您为好友!");
								mHolder.requestStatus.setText("已同意");
							} else {
								mHolder.requestStatus.setTextColor(context
										.getResources().getColor(
												R.color.chat_tab_friend_color));
								mHolder.contents.setText(item.getNickName()
										+ "申请添加您为好友!");
								mHolder.requestStatus.setText("已拒绝");
							}
						} else {
							Toast.makeText(context, result.getRepMsg(),
									Toast.LENGTH_LONG).show();

						}

					}
				});

	}

	static class ViewHolder {
		private ImageView userImg;
		private TextView name;
		private TextView time;
		private TextView contents;
		private TextView refuse;
		private TextView agree;
		private TextView requestStatus;
		private LinearLayout verfyMsgLy;

	}

}
