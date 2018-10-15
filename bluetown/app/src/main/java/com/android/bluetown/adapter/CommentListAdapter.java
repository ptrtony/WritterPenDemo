package com.android.bluetown.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.CommentBean;
import com.android.bluetown.view.RoundedImageView;

/**
 * 
 * @ClassName: CommentListAdapter
 * @Description:TODO(点评列表的Adapter)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
@SuppressLint("SimpleDateFormat") public class CommentListAdapter extends BaseContentAdapter {
	private Context context;

	public CommentListAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.context = mContext;
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_comment, null);
			mHolder = new ViewHolder();
			mHolder.headImg = (RoundedImageView) convertView
					.findViewById(R.id.commentHeadImg);
			mHolder.ivStars[0] = (ImageView) convertView
					.findViewById(R.id.iv_star_1);
			mHolder.ivStars[1] = (ImageView) convertView
					.findViewById(R.id.iv_star_2);
			mHolder.ivStars[2] = (ImageView) convertView
					.findViewById(R.id.iv_star_3);
			mHolder.ivStars[3] = (ImageView) convertView
					.findViewById(R.id.iv_star_4);
			mHolder.ivStars[4] = (ImageView) convertView
					.findViewById(R.id.iv_star_5);
			mHolder.nickname = (TextView) convertView
					.findViewById(R.id.nickname);
			mHolder.commentTime = (TextView) convertView
					.findViewById(R.id.commentTime);
			mHolder.contentComment = (TextView) convertView
					.findViewById(R.id.contentComment);
			mHolder.commentImageList = (GridView) convertView
					.findViewById(R.id.commentImgList);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final CommentBean item = (CommentBean) getItem(position);
		imageLoader.displayImage(item.getHeadImg(), mHolder.headImg,defaultOptions);
		mHolder.nickname.setText(item.getNickName());
		setCreateTime(mHolder.commentTime, item.getCommentTime());
		mHolder.contentComment.setText(item.getContent());
		List<String> commentImgList = item.getCommentImgList();
		if (commentImgList != null && commentImgList.size() > 0) {
			mHolder.commentImageList.setAdapter(new GridViewImgAdapter(context,
					commentImgList, 120, 80));
		}
		String grade = item.getStar();
		if (!TextUtils.isEmpty(grade)) {
			showStars(mHolder, Integer.parseInt(grade));
		}
	}

	private void showStars(ViewHolder holder, int stars) {
		for (int i = 0; i < holder.ivStars.length; i++) {
			if (i < stars) {
				holder.ivStars[i].setImageResource(R.drawable.ic_comment_start);
			} else {
				holder.ivStars[i].setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置评论时间
	 * 
	 * @param mHolder
	 * @param item
	 */
	private void setCreateTime(TextView dateTextView, String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String todayDateTime = format.format(new Date());
		try {
			Date nowTime = format.parse(todayDateTime);
			Date createTime = format.parse(date);
			// 相差的毫秒数
			long diff = nowTime.getTime() - createTime.getTime();
			// 秒
			long timeInterval = diff / 1000;
			if (timeInterval < 60) {
				dateTextView.setText("刚刚");
			} else if ((timeInterval / 60) < 60) {
				dateTextView.setText((timeInterval / 60) + "分钟前");
			} else if ((timeInterval / (60 * 60)) < 24) {
				dateTextView.setText((timeInterval / (60 * 60)) + "小时前");
			} else {
				dateTextView.setText(date);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dateTextView.setText(date);
		}
	}

	static class ViewHolder {
		private RoundedImageView headImg;
		private TextView nickname;
		private TextView commentTime;
		private TextView contentComment;
		private GridView commentImageList;
		private ImageView[] ivStars = new ImageView[5];
	}

}
