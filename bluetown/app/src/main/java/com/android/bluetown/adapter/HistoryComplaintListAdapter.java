package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.HistoryComplaintSuggestBean;

/**
 * 
 * @ClassName: HistoryComplaintListAdapter
 * @Description:TODO(历史投诉建议)
 * @author: shenyz
 * @date: 2015年8月18日 上午11:29:10
 * 
 */
public class HistoryComplaintListAdapter extends BaseContentAdapter {
	public HistoryComplaintListAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.item_history_complaint_suggest, null);
			mHolder = new ViewHolder();
			mHolder.contentTypeImg = (ImageView) convertView
					.findViewById(R.id.contentTypeImg);
			mHolder.contentType = (TextView) convertView
					.findViewById(R.id.contentType);
			mHolder.dealDate = (TextView) convertView
					.findViewById(R.id.dealTime);
			mHolder.dealStatus = (TextView) convertView
					.findViewById(R.id.dealStatus);
			mHolder.suggestdealReply = (TextView) convertView
					.findViewById(R.id.suggestdealReply);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final HistoryComplaintSuggestBean item = (HistoryComplaintSuggestBean) getItem(position);
		// types：0:投诉，1：建议
		// dispose：0:未处理1:已处理；
		String serviceStatus = item.getDispose();
		String type = item.getTypes();
		mHolder.dealDate.setText(item.getCreateTime());
		// （0：投诉，1建议）
		if (type.equals("0")) {
			mHolder.contentType.setText(R.string.complaint);
		} else {
			mHolder.contentType.setText(R.string.suggest_text);
		}
		// dispose：0:未处理1:已处理；
		if (serviceStatus.equals("1")) {
			mHolder.contentType.setTextColor(context.getResources().getColor(
					R.color.compnay_show_type_tag_text_bg));
			mHolder.dealStatus.setText(R.string.dealed_with);
			mHolder.contentTypeImg.setImageResource(R.drawable.time_gray_dot);
			// 处理反馈的内容固定
			mHolder.suggestdealReply.setVisibility(View.VISIBLE);

		} else {
			mHolder.contentType.setTextColor(context.getResources().getColor(
					R.color.font_black));
			mHolder.contentTypeImg.setImageResource(R.drawable.time_dot);
			mHolder.dealStatus.setText(R.string.un_deal_with);
			mHolder.suggestdealReply.setVisibility(View.GONE);

		}

	}

	static class ViewHolder {
		private ImageView contentTypeImg;
		private TextView contentType;
		private TextView dealDate;
		private TextView dealStatus;
		private TextView suggestdealReply;
	}

}
