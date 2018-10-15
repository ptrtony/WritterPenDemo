package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.result.MeaageResult.MessageBean;

/**
 * 
 * @ClassName: MessageListAdapter
 * @Description:TODO(消息列表的Adapter)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
public class MessageListAdapter extends BaseContentAdapter {

	public MessageListAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_message, null);
			mHolder = new ViewHolder();
			mHolder.messageContent = (TextView) convertView
					.findViewById(R.id.messageContent);
			mHolder.messageTime = (TextView) convertView
					.findViewById(R.id.messageTime);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(final ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final MessageBean item = (MessageBean) getItem(position);
		mHolder.messageContent.setText(item.getPushContent());
		mHolder.messageTime.setText(item.getPushDate());
	}
	
	static class ViewHolder {
		private TextView messageContent;
		private TextView messageTime;
	}

}
