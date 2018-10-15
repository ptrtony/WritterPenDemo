package com.android.bluetown.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.ReplyPostBean;
import com.android.bluetown.view.RoundedImageView;

/**
 * 
 * @ClassName: PostReplyAdapter
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月19日 下午6:12:42
 * 
 */
public class PostReplyAdapter extends BaseContentAdapter {

	public PostReplyAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_post_reply, null);
			mHolder = new ViewHolder();
			mHolder.postReplyUserImg = (RoundedImageView) convertView
					.findViewById(R.id.postReplyUserImg);
			mHolder.postReplyUserName = (TextView) convertView
					.findViewById(R.id.postReplyUserName);
			mHolder.postReplyDate = (TextView) convertView
					.findViewById(R.id.postReplyDate);
			mHolder.postReplyContent = (TextView) convertView
					.findViewById(R.id.postReplyContent);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final ReplyPostBean item = (ReplyPostBean) getItem(position);
		if (!TextUtils.isEmpty(item.getHeadImg())) {
			imageLoader.displayImage(item.getHeadImg(),
					mHolder.postReplyUserImg, defaultOptions);
		} else {
			imageLoader.displayImage("drawable://" + R.drawable.ic_msg_empty,

			mHolder.postReplyUserImg, defaultOptions);
		}
		mHolder.postReplyUserName.setText(item.getNickName());
		mHolder.postReplyContent.setText(item.getCommentContent());
		setCommentTime(mHolder, item);

	}

	/**
	 * 设置评论时间
	 * 
	 * @param mHolder
	 * @param item
	 */
	private void setCommentTime(ViewHolder mHolder, final ReplyPostBean item) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String todayDateTime = format.format(new Date());
		try {
			Date nowTime = format.parse(todayDateTime);
			Date commentTime = format.parse(item.getCommentTime());
			// 相差的毫秒数
			long diff = nowTime.getTime() - commentTime.getTime();
			// 秒
			long timeInterval = diff / 1000;
			if (timeInterval < 60) {
				mHolder.postReplyDate.setText("刚刚");
			} else if ((timeInterval / 60) < 60) {
				mHolder.postReplyDate.setText((timeInterval / 60) + "分钟前");
			} else if ((timeInterval / (60 * 60)) < 24) {
				mHolder.postReplyDate.setText((timeInterval / (60 * 60))
						+ "小时前");
			} else {
				mHolder.postReplyDate.setText(item.getCommentTime());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mHolder.postReplyDate.setText(item.getCommentTime());
		}

	}

	static class ViewHolder {
		private TextView postReplyUserName, postReplyDate, postReplyContent;
		private RoundedImageView postReplyUserImg;
	}

}
