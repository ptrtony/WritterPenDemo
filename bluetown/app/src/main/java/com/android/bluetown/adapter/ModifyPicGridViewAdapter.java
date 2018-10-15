package com.android.bluetown.adapter;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.bluetown.R;
import com.android.bluetown.utils.FileUtils;
import com.android.bluetown.utils.UpdateImageUtils;

/**
 * 
 * @ClassName: AppointmentsAdapter
 * @Description: TODO(发布商品的图片)
 * @author scene
 * @date 2015-4-17 上午11:36:50
 * 
 */
public class ModifyPicGridViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private boolean shape;
	private Context context;
	private ModifyPicGridViewAdapter adapter;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public ModifyPicGridViewAdapter(Context context) {
		this.context=context;
		inflater = LayoutInflater.from(context);
		this.adapter=this;
	}

	public void update() {
		loading();
	}

	public int getCount() {
		return (UpdateImageUtils.bmp.size() + 1);
	}

	public Object getItem(int arg0) {

		return null;
	}

	public long getItemId(int arg0) {

		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.item_modify_photo, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.modifyImg);
			holder.delete = (ImageView) convertView.findViewById(R.id.delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == UpdateImageUtils.bmp.size()) {
			holder.image.setImageResource(R.drawable.add_pic);
			holder.delete.setVisibility(View.GONE);
			if (position == 6) {
				holder.image.setVisibility(View.GONE);
			}
		} else {
			holder.delete.setVisibility(View.VISIBLE);
			holder.image.setImageBitmap(UpdateImageUtils.bmp.get(position));
			holder.delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (position != UpdateImageUtils.bmp.size()) {
						UpdateImageUtils.bmp.remove(position);
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				}
			});

		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
		public ImageView delete;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (UpdateImageUtils.max == UpdateImageUtils.drr.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						try {
							String path = UpdateImageUtils.drr.get(UpdateImageUtils.max);
							System.out.println(path);
							Bitmap bm = UpdateImageUtils.revitionImageSize(path);
							UpdateImageUtils.bmp.add(bm);
							String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
							FileUtils.saveBitmap(bm, "" + newStr);
							UpdateImageUtils.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
}
