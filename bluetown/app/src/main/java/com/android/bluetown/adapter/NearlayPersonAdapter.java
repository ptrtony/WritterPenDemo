package com.android.bluetown.adapter;

import java.util.List;

import net.tsz.afinal.FinalDb;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.NearLyPersonResult.NearlyPerson;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class NearlayPersonAdapter extends BaseContentAdapter {

	private FinalDb db;
	private String userId = "";
	private List<MemberUser> users;

	public NearlayPersonAdapter() {
		// TODO Auto-generated constructor stub
		super();
	}

	public NearlayPersonAdapter(Context mContext, List<?> data) {
		// TODO Auto-generated constructor stub
		super(mContext, data);
		
		db = FinalDb.create(mContext);
	}
DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_msg_empty) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.ic_msg_empty) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.ic_msg_empty) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.displayer(new RoundedBitmapDisplayer(3)) // 设置成圆角图片
			.build(); // 创建配置过得DisplayImageOption对象

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final NearlyPerson mNearlyPerson = (NearlyPerson) getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_nearly_person, null);
			holder = new ViewHolder();
			holder.nickname = (TextView) convertView.findViewById(R.id.name);
			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);
			holder.add = (TextView) convertView.findViewById(R.id.add);
			holder.userImg = (ImageView) convertView
					.findViewById(R.id.userImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.nickname.setText(mNearlyPerson.getNickName());
		holder.distance.setText(mNearlyPerson.getKm() + "m");
		imageLoader.displayImage(mNearlyPerson.getHeadImg(), holder.userImg,
		    options);
		holder.add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addFriendPop(context, mNearlyPerson);
			}
		});
		return convertView;

	}

	private void addFriendPop(Context context, final NearlyPerson nearlyPerson) {
		// 引入窗口配置文件
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.add_friend_pop_layout, null);
		RoundedImageView mImageView = (RoundedImageView) contentView
				.findViewById(R.id.userImg);
		TextView friendInfo = (TextView) contentView
				.findViewById(R.id.friendInfo);
		TextView addFriendInfo = (TextView) contentView
				.findViewById(R.id.addFriendInfo);
		Button sendAddInfo = (Button) contentView
				.findViewById(R.id.sendAddInfo);
		final Dialog dialog = new Dialog(context, R.style.alert_dialog);
		dialog.setContentView(contentView);
		dialog.show();
		imageLoader.displayImage(nearlyPerson.getHeadImg(), mImageView, options);
		friendInfo.append("加");
		String addFriendNickName = nearlyPerson.getNickName();
		if (!TextUtils.isEmpty(addFriendNickName)) {
			SpannableString spanMiddleStr = new SpannableString(
					addFriendNickName);
			ForegroundColorSpan orangeSpan = new ForegroundColorSpan(context
					.getResources().getColor(R.color.font_orange));
			spanMiddleStr.setSpan(orangeSpan, 0, addFriendNickName.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			friendInfo.append(spanMiddleStr);
		} else {
			friendInfo.append(" ");
		}
		friendInfo.append("为好友");
			String nickname = "";
		String username = "";
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				username = user.getUsername();
				nickname = user.getNickName();
				userId = user.getMemberId();
			}
		
		}
		if (!TextUtils.isEmpty(nickname)) {
			addFriendInfo.setText("我是" + nickname + ",\n想添加你为好友");
		} else {
			addFriendInfo.setText("我是" + username + ",\n想添加你为好友");
		}

		sendAddInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addFriends(0, nearlyPerson,userId,  dialog);
			}
		});
	}

	/**
	 * addFriends userId：当前用户id（必填） friendId:添加的好友id（必填） type:必填(0:添加好友，1：删除好友)
	 */
	private void addFriends(final int type, final NearlyPerson nearlyPerson,
			String userId,final Dialog dialog) {
		// TODO Auto-generated method stub
		
		params.put("userId", userId);
		params.put("friendId", nearlyPerson.getUserId());
		params.put("type", type + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.ADD_FRIEND,
				params, new AbsHttpStringResponseListener(context, null) {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							// 将该用户保存到数据库
							List<FriendsBean> friendList = db.findAllByWhere(
									FriendsBean.class, " userId=\""
											+ nearlyPerson.getUserId() + "\"");
							if (friendList.size() == 0) {
								FriendsBean friend = new FriendsBean();
								friend.setUserId(nearlyPerson.getUserId());
								friend.setHeadImg(nearlyPerson.getHeadImg());
								friend.setNickName(nearlyPerson.getNickName());
								db.save(friend);
							}
							// 发送成功，等待后台审核（推送给前台）
							Toast.makeText(context,
									context.getString(R.string.send_success),
									Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						} else {
							TipDialog.showDialog((Activity) context,
									SweetAlertDialog.ERROR_TYPE,
									result.getRepMsg());
						}

					}

				});

	}

	static class ViewHolder {
		private TextView nickname, distance, add;
		private ImageView userImg;
	}

}
