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
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.GroupsBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MakeFriendsAdapter extends BaseContentAdapter {
	private int type;

	public MakeFriendsAdapter() {
		// TODO Auto-generated constructor stub
	}

	public MakeFriendsAdapter(Context mContext, List<?> data, int type) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.type = type;
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
		if (type == 0) {
			FriendsBean friendsBean = (FriendsBean) getItem(position);
			holder.nickname.setText(friendsBean.getNickName());
			holder.userStatus.setVisibility(View.VISIBLE);
			holder.publishDate.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(friendsBean.getStatu())) {
				if (friendsBean.getStatu().contains("1")) {
					holder.userStatus
							.setImageResource(R.drawable.friend_list_online_image);
				} else {
					holder.userStatus
							.setImageResource(R.drawable.friend_list_outline_image);
				}
			}
			imageLoader.displayImage(friendsBean.getHeadImg(), holder.userImg,
				options);	
			} else {
			GroupsBean groupsBean = (GroupsBean) getItem(position);
			holder.userStatus.setVisibility(View.GONE);
			holder.publishDate.setVisibility(View.VISIBLE);
			holder.nickname.setText(groupsBean.getFlockName());
			holder.publishDate.setText("发起于" + groupsBean.getCreateDate());
			imageLoader.displayImage(groupsBean.getFlockImg(), holder.userImg,
					options);
			}

		return convertView;
	}

	static class ViewHolder {
		private TextView nickname, publishDate;
		private ImageView userImg, userStatus;
	}
}