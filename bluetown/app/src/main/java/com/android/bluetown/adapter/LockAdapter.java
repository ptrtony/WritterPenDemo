package com.android.bluetown.adapter;





import com.android.bluetown.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class LockAdapter extends BaseAdapter{
	
	private char keys[];
	public Context context;
	
	public LockAdapter(Context context) {
		super();
		this.context = context;
	}


	public void setKey(String key){
		if (key != null) {
			this.keys = key.toCharArray();
			this.notifyDataSetChanged();
		}
	}
	
	
	@Override
	public int getCount() {
		return 9;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		imageView.setImageResource(R.drawable.eqe);
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				if ((keys[i]-49)==position) {
					imageView.setImageResource(R.drawable.eqd);
					continue;
				}
			}
			
		}
		return imageView;
	}
	
}

