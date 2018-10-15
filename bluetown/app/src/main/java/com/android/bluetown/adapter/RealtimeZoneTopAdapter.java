package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.result.MeaageResult.MessageBean;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.LineInfo;
import com.hikvision.vmsnetsdk.RegionInfo;

/**
 * @author hedi
 * @data:  2016年6月16日 下午3:15:22 
 * @Description:  TODO<监控区域> 
 */
public class RealtimeZoneTopAdapter extends BaseContentAdapter {
	private int clickTemp = -1;
	private boolean flag;
	public RealtimeZoneTopAdapter(Context mContext, List<?> data,boolean flag) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.flag=flag;
	}
//	public String getText(int position) {
//		final MessageBean item = (MessageBean) getItem(position);
//		return item.getUserId();
//	}
	public void setSeclection(int position) {
		clickTemp = position;
	}
	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_realtime_zone_top, null);
			mHolder = new ViewHolder();
			mHolder.text = (TextView) convertView
					.findViewById(R.id.name);
			mHolder.view = convertView
					.findViewById(R.id.view);
//			mHolder.messageTime = (TextView) convertView
//					.findViewById(R.id.messageTime);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position,convertView);
		if(clickTemp==position){
			mHolder.view.setVisibility(View.VISIBLE);
			mHolder.text.setTextColor(context.getResources().getColor(R.color.app_blue));
		}else{
			mHolder.view.setVisibility(View.GONE);
			mHolder.text.setTextColor(0xff616161);
		}
		return convertView;
	}

	private void setData(final ViewHolder mHolder, int position,View convertView) {
		// TODO Auto-generated method stub
		final Object item = getItem(position);
		// finalBitmap.display(mHolder.backgroundImg, item.getCompanyImg());
		String desc = getItemDesc(item);
		if (flag) {
			mHolder.text.setText(desc);
		}else {
			mHolder.text.setText(desc);	
		}
		
	}
	private String getItemDesc(Object itemData) {
		if (itemData instanceof ControlUnitInfo) {
			ControlUnitInfo info = (ControlUnitInfo) itemData;
			return info.name;
		}

		if (itemData instanceof RegionInfo) {
			RegionInfo info = (RegionInfo) itemData;
			return info.name;
		}

		if (itemData instanceof CameraInfo) {
			CameraInfo info = (CameraInfo) itemData;
			return info.name;
		}

		if (itemData instanceof LineInfo) {
			LineInfo info = (LineInfo) itemData;
			return info.lineName;
		}

		return null;
	}
	
	static class ViewHolder {
		private TextView text;
		private View view;
	}

}
