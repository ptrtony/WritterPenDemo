package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.TakePartPerson;
import com.android.bluetown.view.RoundedImageView;

/**
 * 
 * @ClassName: LimiteGridViewAdapter
 * @Description:TODO(限制item显示的GridView)
 * @author: shenyz
 * @date: 2015年7月21日 下午4:56:11
 * 
 */
public class LimiteGridViewAdapter extends BaseContentAdapter {
	private Context context;
	private List<TakePartPerson> persons;
	public LimiteGridViewAdapter() {
		// TODO Auto-generated constructor stub
	}

	public LimiteGridViewAdapter(Context context, List<TakePartPerson> persons) {
		// TODO Auto-generated constructor stub
		super(context, persons);
		this.context = context;
		this.persons = persons;
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.item_image_text, null);
			holder.applyPersonImg = (RoundedImageView) convertView
				.findViewById(R.id.applyImg);
			holder.applyPersonName = (TextView) convertView
				.findViewById(R.id.applyName);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		setData(holder, position);
		return convertView;
	}

	/**
	 * 
	 * @Title: setData
	 * @Description: TODO(给Item设置数据)
	 * @param holder
	 * @param position
	 * @throws
	 */
	private void setData(Holder holder, int position) {
		// TODO Auto-generated method stub
		if (persons.size()>10) {
			if (position==persons.size()-1) {
				holder.applyPersonImg.setImageResource(R.drawable.more_person);
				holder.applyPersonName.setVisibility(View.GONE);
			}else {
				TakePartPerson person = (TakePartPerson) getItem(position);
				holder.applyPersonName.setText(person.getNickName());	
				imageLoader.displayImage(person.getHeadImg(), 
				holder.applyPersonImg, defaultOptions);
			}
		}else {
			TakePartPerson person = (TakePartPerson) getItem(position);
			holder.applyPersonName.setText(person.getNickName());	
			imageLoader.displayImage(person.getHeadImg(), 
			holder.applyPersonImg, defaultOptions);
		}
		
	
	}

	static class Holder {
		private RoundedImageView applyPersonImg;
		private TextView applyPersonName;
	}

	

}
