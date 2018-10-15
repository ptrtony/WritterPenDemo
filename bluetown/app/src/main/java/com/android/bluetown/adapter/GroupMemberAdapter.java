package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.result.GroupMemberResult.GroupMember;

public class GroupMemberAdapter extends BaseContentAdapter {
	public GroupMemberAdapter() {
		// TODO Auto-generated constructor stub
	}

	public GroupMemberAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupMember groupMember = (GroupMember) getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_makefriends, null);
			holder = new ViewHolder();
			holder.nickname = (TextView) convertView.findViewById(R.id.name);
			holder.publishDate = (TextView) convertView
					.findViewById(R.id.publishDate);
			holder.userStatus = (ImageView) convertView
					.findViewById(R.id.userstatus);
			holder.userImg = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nickname.setText(groupMember.getNickName());
		holder.userStatus.setVisibility(View.VISIBLE);
		holder.publishDate.setVisibility(View.GONE);
		if (!TextUtils.isEmpty(groupMember.getStatu())) {
			if (groupMember.getStatu().contains("1")) {
				holder.userStatus
						.setImageResource(R.drawable.friend_list_online_image);
			} else {
				holder.userStatus
						.setImageResource(R.drawable.friend_list_outline_image);
			}
		}
		if (!TextUtils.isEmpty(groupMember.getHeadImg())) {
			imageLoader.displayImage(groupMember.getHeadImg(), holder.userImg,
					defaultOptions);
			}else {
			imageLoader.displayImage("drawable://" + R.drawable.ic_msg_empty,
			     holder.userImg, defaultOptions);
			
		}
		

		return convertView;
	}

	static class ViewHolder {
		private TextView nickname, publishDate;
		private ImageView userImg, userStatus;
	}
}