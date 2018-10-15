package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.ProcedureHistoryBean.DetailBean;

/**
 * 
 * @ClassName: ProcedureApprovalHistoryListAdapter
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月21日 下午1:20:06
 * 
 */
public class ProcedureApprovalHistoryListAdapter extends BaseContentAdapter {
	public ProcedureApprovalHistoryListAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.item_procedureapprovalhistory, null);
			mHolder = new ViewHolder();
			mHolder.itemDot = (ImageView) convertView
					.findViewById(R.id.itemDot);
			mHolder.zoneInnerName = (TextView) convertView
					.findViewById(R.id.zoneInnerName);
			mHolder.approvalDate = (TextView) convertView
					.findViewById(R.id.approvalDate);
			mHolder.approvalContent = (TextView) convertView
					.findViewById(R.id.approvalContent);
			mHolder.approvalStatus = (TextView) convertView
					.findViewById(R.id.approvalStatus);
			mHolder.approvalRefuseContent = (TextView) convertView
					.findViewById(R.id.approvalRefuseContent);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);
		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final DetailBean item = (DetailBean) getItem(position);
		mHolder.zoneInnerName.setText(item.title);
		mHolder.approvalDate.setText(item.createTime);
		mHolder.approvalContent.setText(item.content);
		// status:(0审批通过，1审批拒绝);
		if (item.status.equals("0")) {
			mHolder.approvalStatus.setText(R.string.pass);
			mHolder.approvalStatus.setTextColor(context.getResources()
					.getColor(R.color.font_orange));
			mHolder.itemDot.setImageResource(R.drawable.time_dot);
			mHolder.approvalRefuseContent.setVisibility(View.GONE);
		} else if (item.status.equals("1")) {
			mHolder.approvalStatus.setText(R.string.refuse);
			mHolder.approvalStatus.setTextColor(context.getResources()
					.getColor(R.color.compnay_show_type_tag_text_bg));
			mHolder.itemDot.setImageResource(R.drawable.time_gray_dot);
			mHolder.approvalRefuseContent.setVisibility(View.VISIBLE);
			mHolder.approvalRefuseContent.setText(context
					.getString(R.string.approval_refuse_reason) + item.repulse);
		} else {
			mHolder.approvalStatus.setText(R.string.auditing);
			mHolder.approvalStatus.setTextColor(context.getResources()
					.getColor(R.color.compnay_show_type_tag_text_bg));
			mHolder.itemDot.setImageResource(R.drawable.time_dot);
			mHolder.approvalRefuseContent.setVisibility(View.GONE);
		}

	}

	static class ViewHolder {
		private ImageView itemDot;
		private TextView zoneInnerName;
		private TextView approvalDate;
		private TextView approvalContent;
		private TextView approvalStatus;
		private TextView approvalRefuseContent;
	}

}
