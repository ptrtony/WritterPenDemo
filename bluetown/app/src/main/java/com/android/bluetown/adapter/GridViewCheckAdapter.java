package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.DemandTypeBean;

public class GridViewCheckAdapter extends BaseContentAdapter {

	public GridViewCheckAdapter() {
		// TODO Auto-generated constructor stub
	}

	public GridViewCheckAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(final int position, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHoder holder;
		if (convertView == null) {
			holder = new ViewHoder();
			convertView = View.inflate(context, R.layout.item_gridview_check,
					null);
			holder.typeCheck = (CheckBox) convertView
					.findViewById(R.id.typeCheck);
			holder.demandTypeImg = (ImageView) convertView
					.findViewById(R.id.demandTypeImg);
			holder.demandTypeTitle = (TextView) convertView
					.findViewById(R.id.demandTypeTitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHoder) convertView.getTag();
		}
		setData(holder, position);
		return convertView;
	}

	private void setData(ViewHoder holder, final int position) {
		// TODO Auto-generated method stub
		DemandTypeBean modelBean = (DemandTypeBean) getItem(position);
		holder.demandTypeImg.setImageResource(modelBean.getImgResId());
		holder.demandTypeTitle.setText(context.getString(modelBean
				.getModelNameResId()));
		holder.typeCheck.setChecked(modelBean.isCheck());

	}

	static final class ViewHoder {
		private CheckBox typeCheck;
		private ImageView demandTypeImg;
		private TextView demandTypeTitle;

	}

}
