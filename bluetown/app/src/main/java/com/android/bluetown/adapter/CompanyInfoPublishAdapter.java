package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.CompanyInfoPublishBean;

/**
 * 
 * @ClassName: CompanyInfoPublishAdapter
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月10日 下午4:33:03
 * 
 */
public class CompanyInfoPublishAdapter extends BaseContentAdapter {

	public CompanyInfoPublishAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_company_info_publish,
					null);
			mHolder = new ViewHolder();
			mHolder.publishInfoLogo = (ImageView) convertView
					.findViewById(R.id.publishInfoLogo);
			mHolder.publishInfoTitle = (TextView) convertView
					.findViewById(R.id.publishInfoTitle);
			mHolder.publishInfoCompany = (TextView) convertView
					.findViewById(R.id.publishInfoCompany);
			mHolder.publishInfoTime = (TextView) convertView
					.findViewById(R.id.publishInfoTime);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final CompanyInfoPublishBean item = (CompanyInfoPublishBean) getItem(position);
		switch (item.getNeedType()) {
		case 0 + "":
			mHolder.publishInfoLogo.setImageResource(R.drawable.demand_recruit);
			break;
		case 1 + "":
			mHolder.publishInfoLogo.setImageResource(R.drawable.demand_study);
			break;
		case 2 + "":
			mHolder.publishInfoLogo.setImageResource(R.drawable.demand_sale);
			break;
		case 3 + "":
			mHolder.publishInfoLogo.setImageResource(R.drawable.demand_peo);
			break;
		case 4 + "":
			mHolder.publishInfoLogo.setImageResource(R.drawable.demand_outing);
			break;
		case 5 + "":
			mHolder.publishInfoLogo.setImageResource(R.drawable.demand_meeing);
			break;
		case 6 + "":
			mHolder.publishInfoLogo
					.setImageResource(R.drawable.demand_makefriends);
			break;
		case 7 + "":
			mHolder.publishInfoLogo.setImageResource(R.drawable.demand_more);
			break;
		default:
			break;
		}
		mHolder.publishInfoTitle.setText(item.getTitle());
		mHolder.publishInfoCompany.setText(item.getCompanyName());
		mHolder.publishInfoTime.setText(item.getCreateTime());
	}

	static class ViewHolder {
		private TextView publishInfoTitle, publishInfoCompany, publishInfoTime;
		private ImageView publishInfoLogo;
	}

}
