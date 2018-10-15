package com.android.bluetown.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.android.bluetown.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 
 * @ClassName: BaseContentAdapter
 * @Description:TODO(适配器的父类)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:02:29
 * 
 */
public abstract class BaseContentAdapter extends BaseAdapter {
	protected Context context;
	protected List<?> data;
	protected LayoutInflater mInflater;
	// Http请求
	protected AbHttpUtil httpInstance;
	// Http请求参数
	protected AbRequestParams params;
	protected ImageLoader imageLoader;
	public BaseContentAdapter() {
		// TODO Auto-generated constructor stub
	}
	public void setData(ArrayList<?> lists){
		if (lists!=null){
			this.data = (List<?>) lists.clone();
			notifyDataSetChanged();
		}
	}
	public BaseContentAdapter(Context mContext, List<?> data) {
		// TODO Auto-generated constructor stub
		this.context = mContext;
		this.data = data;
		mInflater = LayoutInflater.from(context);
		initFinalBitmap();
		initHttpRequest();
	}

	public BaseContentAdapter(Context mContext){
		this.context = mContext;
		mInflater = LayoutInflater.from(context);
		initFinalBitmap();
		initHttpRequest();
	}
	private void initFinalBitmap() {
		if (imageLoader==null) {
			imageLoader = ImageLoader.getInstance();
		}
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
			.displayer(new RoundedBitmapDisplayer(3))
			// 设置成圆角图片
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.showStubImage(R.drawable.ic_msg_empty)
			.showImageForEmptyUri(R.drawable.ic_msg_empty)
			.showImageOnFail(R.drawable.ic_msg_empty)
			// 载入图片前稍做延时可以提高整体滑动的流畅度
			.delayBeforeLoading(100).build();
	private void initHttpRequest() {
		// TODO Auto-generated method stub
		if (httpInstance == null) {
			httpInstance = AbHttpUtil.getInstance(context);
			httpInstance.setEasySSLEnabled(true);
		}

		if (params == null) {
			params = new AbRequestParams();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == data ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getContentView(position, convertView, parent);
	}

	public abstract View getContentView(int position, View convertView, ViewGroup parent);
}
