package com.android.bluetown.home.hot.model.act;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.bean.HistoryComplaintSuggestBean;

/**
 * 
 * @ClassName: HistoryComlaintSuggestActivity
 * @Description:TODO(历史投诉建议)
 * @author: shenyz
 * @date: 2015年8月18日 上午10:34:08
 * 
 */
public class HistoryComlaintSuggestDetailsActivity extends TitleBarActivity {
	private TextView complaintDate, complaintStatus;
	private TextView complaintAddress;
	private TextView complaintIntro;
	private TextView complaintImg;
	private TextView dealFeedbackDate, dealFeedbackContent;
	private TextView complaintAddressTitle;
	private TextView complaintIntroTitle;
	private RelativeLayout dealFeedbackLy;
	private HistoryComplaintSuggestBean suggestBean;
	/** 处理状态 */
	private String dealStatus, types;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_history_complaint_suggest_details);
		suggestBean = (HistoryComplaintSuggestBean) getIntent()
				.getSerializableExtra("details");
		dealStatus = suggestBean.getDispose();
		types = suggestBean.getTypes();
		complaintAddressTitle = (TextView) findViewById(R.id.complaintAddressTitle);
		complaintIntroTitle = (TextView) findViewById(R.id.complaintIntroTitle);
		complaintImg = (TextView) findViewById(R.id.complaintImg);
		complaintDate = (TextView) findViewById(R.id.complaintSuggestDate);
		complaintStatus = (TextView) findViewById(R.id.dealStatus);
		complaintAddress = (TextView) findViewById(R.id.complaintAddress);
		complaintIntro = (TextView) findViewById(R.id.complaintIntro);
		dealFeedbackDate = (TextView) findViewById(R.id.dealFeedbackDate);
		dealFeedbackContent = (TextView) findViewById(R.id.dealFeedbackContent);
		dealFeedbackLy = (RelativeLayout) findViewById(R.id.dealFeedbackLy);
		// types：0:投诉，1：建议
		if (!TextUtils.isEmpty(types)) {
			if (types.equals("0")) {
				complaintAddressTitle.setText(R.string.complaint_address);
				complaintIntroTitle.setText(R.string.complaint_intro);
			} else {
				complaintAddressTitle.setText(R.string.advise_address);
				complaintIntroTitle.setText(R.string.advise_intro);
			}
		}
		// dispose：0:未处理1:已处理；
		// 已处理
		if (dealStatus.equals("1")) {
			complaintStatus.setText(R.string.dealed_with);
			dealFeedbackLy.setVisibility(View.VISIBLE);
			dealFeedbackDate.setText(suggestBean.getManageDate());
		} else {
			// 未处理
			dealFeedbackLy.setVisibility(View.GONE);
			complaintStatus.setText(R.string.un_deal_with);

		}
		if (suggestBean.getPicturesList() != null
				&& suggestBean.getPicturesList().size() > 0) {
			complaintImg.setText(R.string.unloaded);
		} else {
			complaintImg.setText(R.string.un_unload);
		}
		complaintDate.setText(suggestBean.getCreateTime());
		complaintAddress.setText(suggestBean.getAddress());
		complaintIntro.setText(suggestBean.getContent());
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.history_complaint_suggest);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

}
