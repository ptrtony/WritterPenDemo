package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.LineInfo;
import com.hikvision.vmsnetsdk.RegionInfo;

/**
 * 
 * @ClassName: CompanyShowListAdapter
 * @Description:TODO(企业展示列表的Adapter)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
public class RealTimeZoneListAdapter extends BaseContentAdapter {
	private boolean flag;
	public RealTimeZoneListAdapter(Context mContext, List<?> data,boolean flag) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.flag=flag;
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			mHolder = new ViewHolder();
			if (flag) {
				convertView = mInflater.inflate(R.layout.item_textview, null);
				mHolder = new ViewHolder();
				mHolder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
			}else {
				convertView = mInflater.inflate(R.layout.item_realtimezone, null);
//				mHolder.backgroundImg = (RelativeLayout) convertView.findViewById(R.id.rl_background);
				mHolder.direction = (TextView) convertView.findViewById(R.id.tv_direction);	
			}
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(convertView, mHolder, position);
		return convertView;
	}

	private void setData(View convertView, ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final Object item = getItem(position);
		// finalBitmap.display(mHolder.backgroundImg, item.getCompanyImg());
		String desc = getItemDesc(item);
		if (flag) {
			mHolder.nameTextView.setText(desc);
		}else {
			mHolder.direction.setText(desc);	
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
		private RelativeLayout backgroundImg;
		private TextView direction;
		private TextView nameTextView;
	}

}
