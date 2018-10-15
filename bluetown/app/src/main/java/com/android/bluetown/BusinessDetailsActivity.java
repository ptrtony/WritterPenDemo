package com.android.bluetown;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.BannerInfoBean;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.view.slideview.SlidingPlayView;

//商家详情
public class BusinessDetailsActivity extends TitleBarActivity implements OnClickListener {
	private SlidingPlayView companyDetailShowView;
	private Button searchBtn,search1Btn;
	/** 全局的LayoutInflater对象，已经完成初始化. */
	public LayoutInflater mInflater;
	/** 存放轮播图的ListView */
	private ArrayList<BannerInfoBean> slideList;
	// private TextView mTypeName;
	// private TextView mImagenum;
	private TextView mAddress;
	private TextView mTelephone;
	private TextView mContact;
	private String type;
	private String name;
	private String imagenum;
	private String address;
	private String telephone;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.activity_businessdetails);
		BlueTownExitHelper.addActivity(this);
		type = getIntent().getStringExtra("type");
		name = getIntent().getStringExtra("name");
		imagenum = getIntent().getStringExtra("imagenum");
		address = getIntent().getStringExtra("address");
		telephone = getIntent().getStringExtra("telephone");
		initUIView();
		setSlidePlayView();
		setData();

	}

	private void initUIView() {
		mInflater = LayoutInflater.from(this);
		companyDetailShowView = (SlidingPlayView) findViewById(R.id.companyDetailShowView);
		mAddress = (TextView) findViewById(R.id.tv_address);
		mTelephone = (TextView) findViewById(R.id.tv_telephone);
		mContact = (TextView) findViewById(R.id.tv_contact);
		searchBtn = (Button) findViewById(R.id.search);
		search1Btn = (Button) findViewById(R.id.search1);
		mContact.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		search1Btn.setOnClickListener(this);
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("商家详情");
		setTitleLayoutBg(R.color.title_bg_blue);
		righTextLayout.setVisibility(View.INVISIBLE);
	}

	/**
	 * 
	 * @Title: setSlidePlayView
	 * @Description: TODO(设置轮播图的数据)
	 * @throws
	 */
	private void setSlidePlayView() {
		/**
		 * 图片轮番
		 */
		if (slideList == null) {
			slideList = new ArrayList<BannerInfoBean>();
		}
		slideList.add(new BannerInfoBean("文心雕龙书店#", "#图书#", "http://img0.imgtn.bdimg.com/it/u=3256387150,83586590&fm=21&gp=0.jpg"));
		slideList.add(new BannerInfoBean("文心雕龙书店#", "#图书#", "http://img4.imgtn.bdimg.com/it/u=4216069985,4237480851&fm=21&gp=0.jpg"));
		slideList.add(new BannerInfoBean("文心雕龙书店#", "#图书#", "http://img2.imgtn.bdimg.com/it/u=1003519963,2176779619&fm=21&gp=0.jpg"));
		slideList.add(new BannerInfoBean("文心雕龙书店#", "#图书#", "http://img5.imgtn.bdimg.com/it/u=266267318,3148808817&fm=21&gp=0.jpg"));
		slideList.add(new BannerInfoBean("文心雕龙书店#", "#图书#", "http://img2.imgtn.bdimg.com/it/u=1192044878,1575427818&fm=21&gp=0.jpg"));
		slideList.add(new BannerInfoBean("文心雕龙书店#", "#图书#", "http://img4.imgtn.bdimg.com/it/u=4216069985,4237480851&fm=21&gp=0.jpg"));
		slideList.add(new BannerInfoBean("文心雕龙书店#", "#图书#", "http://img4.imgtn.bdimg.com/it/u=4216069985,4237480851&fm=21&gp=0.jpg"));
		slideList.add(new BannerInfoBean("文心雕龙书店#", "#图书#", "http://img5.imgtn.bdimg.com/it/u=343627748,309697887&fm=21&gp=0.jpg"));
		slideList.add(new BannerInfoBean("文心雕龙书店#", "#图书#", "http://img0.imgtn.bdimg.com/it/u=4041284222,3879594770&fm=21&gp=0.jpg"));
		slideList.add(new BannerInfoBean("文心雕龙书店#", "#图书#", "http://img2.imgtn.bdimg.com/it/u=1003519963,2176779619&fm=21&gp=0.jpg"));

		for (int i = 0; i < slideList.size(); i++) {
			View mPlayView = mInflater.inflate(R.layout.item_play_view, null);
			ImageView slideImg = (ImageView) mPlayView.findViewById(R.id.slideImg);
			TextView companyType = (TextView) mPlayView.findViewById(R.id.companyType);
			TextView companyName = (TextView) mPlayView.findViewById(R.id.companyName);
			TextView imgCount = (TextView) mPlayView.findViewById(R.id.imgCount);
			companyType.setText(slideList.get(i).getBannerType());
			if (slideList.get(i).getBannerTitle().length() >= 15) {
				companyName.setText(slideList.get(i).getBannerTitle().substring(0, 15) + "...");
			} else {
				companyName.setText(slideList.get(i).getBannerTitle());
			}
			imgCount.setText(i + 1 + "/" + slideList.size());
			finalBitmap.display(slideImg, slideList.get(i).getBannerImg());
			companyDetailShowView.addView(mPlayView);
		}
		companyDetailShowView.startPlay();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_contact:
			if (!TextUtils.isEmpty(telephone)) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telephone));
				startActivity(intent);
			}
			break;
		case R.id.search:
			TipDialog.showDialog(BusinessDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE, R.string.book_seat_success);
			break;
		case R.id.search1:
			TipDialog.showDialog(BusinessDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE, R.string.dish_succss);
			break;
		default:
			break;
		}
	}

	private void setData() {
		if (!TextUtils.isEmpty(address)) {
			mAddress.setText("地址：" + address);
		}
		if (!TextUtils.isEmpty(telephone)) {
			mTelephone.setText("电话：" + telephone);
		}
	}

}
