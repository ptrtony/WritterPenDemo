package com.android.bluetown.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.android.bluetown.R;
import com.android.bluetown.utils.DisplayUtils;

/**
 * 
 * @ClassName: GridViewImgAdapter
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月10日 下午4:33:03
 * 
 */
@SuppressLint("InflateParams")
public class GridViewImgAdapter extends BaseContentAdapter {
	private int width;
	private int height;

	public GridViewImgAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	public GridViewImgAdapter(Context mContext, List<?> data, int width,
			int height) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.width = width;
		this.height = height;
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (width != 0 && height != 0) {
			convertView = mInflater.inflate(R.layout.item_imageview, null);
		} else {
			convertView = mInflater.inflate(R.layout.item_image, null);
		}
		ImageView img = (ImageView) convertView.findViewById(R.id.img);
		if (width!=0&&height!=0) {
			int imageWidth=DisplayUtils.dip2px(context, width);
			int imageHeight=DisplayUtils.dip2px(context, height);
			img.setLayoutParams(new LayoutParams(imageWidth, imageHeight));
		}
		String imgStr = (String) getItem(position);
		if (!TextUtils.isEmpty(imgStr)) {
			imageLoader.displayImage(imgStr, img, defaultOptions);
		} else {
			imageLoader.displayImage("drawable://" + R.drawable.ic_msg_empty, img,
					defaultOptions);
		}

		return convertView;
	}

}
