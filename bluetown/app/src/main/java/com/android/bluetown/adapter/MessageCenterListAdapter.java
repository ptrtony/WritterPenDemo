package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.IndexBanner;
import com.android.bluetown.view.RoundAngleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 
 * @ClassName: MessageCenterListAdapter
 * @Description:TODO(消息中心列表的Adapter)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
public class MessageCenterListAdapter extends BaseContentAdapter {

	public MessageCenterListAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_message_list, null);
			mHolder = new ViewHolder();
			mHolder.messageImg = (RoundAngleImageView) convertView
					.findViewById(R.id.iv_company);
			mHolder.msgName = (TextView) convertView
					.findViewById(R.id.publishTitle);
			mHolder.msgPubishDate = (TextView) convertView
					.findViewById(R.id.publishDate);
			mHolder.msgDeadLineDate = (TextView) convertView
					.findViewById(R.id.deadLineDate);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	public DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			// 设置下载的图片是否缓存在内存中
			.cacheInMemory(true)
			// 设置下载的图片是否缓存在SD卡中
			.cacheOnDisc(true)
			// 设置图片的解码类型
			.bitmapConfig(Bitmap.Config.RGB_565)
			// 设置图片的质量(图片以如何的编码方式显示 ),其中，imageScaleType的选择值:
			// EXACTLY :图像将完全按比例缩小的目标大小
			// EXACTLY_STRETCHED:图片会缩放到目标大小完全
			// IN_SAMPLE_INT:图像将被二次采样的整数倍
			// IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
			// NONE:图片不会调整
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.showStubImage(R.drawable.ic_msg_empty)
			.showImageForEmptyUri(R.drawable.ic_msg_empty)
			.showImageOnFail(R.drawable.ic_msg_empty)
			// 载入图片前稍做延时可以提高整体滑动的流畅度
			.delayBeforeLoading(100).build();

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final IndexBanner item = (IndexBanner) getItem(position);
		String pimg = item.getPimg();
		if (pimg != null && !pimg.isEmpty()) {
			imageLoader.displayImage(pimg, mHolder.messageImg, defaultOptions);
		} else {
			mHolder.messageImg.setBackgroundResource(R.drawable.icon_message);
		}

		mHolder.msgName.setText(item.getTitle());
		if (!TextUtils.isEmpty(item.getCreateDate())) {
			mHolder.msgPubishDate.setText(item.getCreateDate());
		} else {
			mHolder.msgPubishDate.setVisibility(View.GONE);
		}
		mHolder.msgDeadLineDate.setText("过期时间：" + item.getPastDate());
	}

	static class ViewHolder {
		private RoundAngleImageView messageImg;
		private TextView msgName;
		private TextView msgPubishDate;
		private TextView msgDeadLineDate;
	}

}
