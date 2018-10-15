package com.android.bluetown.home.main.model.act;


import java.util.List;
import net.tsz.afinal.FinalDb;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CompanyDetailbean;
import com.android.bluetown.bean.CompanyShowItemBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.result.CompanyDetailsData;
import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.Log;
import com.android.bluetown.view.slideview.SlidingPlayViewWithDot;
import com.bumptech.glide.Glide;
import com.cretin.www.externalmaputilslibrary.OpenExternalMapAppUtils;

/**
 * 
 * @ClassName: CompanyDetailsActivity
 * @Description:TODO(企业展示-企业详情)
 * @author: shenyz
 * @date: 2015年8月5日 上午10:45:54
 * 
 */
/**
 * @author shenyz
 * 
 */
public class CompanyDetailsActivity extends TitleBarActivity implements
		OnClickListener {
	private SlidingPlayViewWithDot mSlidingPlayView;
	private Button attentionBtn;
	/** 全局的LayoutInflater对象，已经完成初始化. */
	public LayoutInflater mInflater;
//	private TextView mCompanyType;
	private LinearLayout mLlClickToNevagetionApp;
	private TextView mCompanyName;
	private TextView mAddress;
	private TextView mTelphone;
	private TextView mCompanyTypeTag;
	private WebView mContect;
	private List<String> slideList;
	private String bid, demand;
	private int position;
	private String userId;
	private FinalDb db;
	
	private List<MemberUser> users;
	private CompanyDetailbean detailbean;
	private Handler handler;
	private String latitude;
	private String longitude;
	private String companyName;
	private String describe;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_company_details);
		BlueTownExitHelper.addActivity(this);
		initUIView();

		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		getCompanyDetails();
	}

	private void initUIView() {
		handler = BlueTownApp.getHanler();
	    db = FinalDb.create(this);
		Bundle bundle = getIntent().getExtras();

		bid = bundle.getString("bid");
		try {
			position =bundle.getInt("position");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			demand = "demand";
		}
		latitude = bundle.getString("latitude");
		longitude = bundle.getString("longitude");
		companyName = bundle.getString("companyName");
		describe = bundle.getString("describe");
		mInflater = LayoutInflater.from(this);
		mSlidingPlayView = (SlidingPlayViewWithDot) findViewById(R.id.homeShowView);
		attentionBtn = (Button) findViewById(R.id.attentionBtn);
		attentionBtn.setOnClickListener(this);
//		mCompanyType = (TextView) findViewById(R.id.companyType);
		mLlClickToNevagetionApp = findViewById(R.id.ll_address_click_app);
		mCompanyName = (TextView) findViewById(R.id.companyName);
		mAddress = (TextView) findViewById(R.id.address);
		mTelphone = (TextView) findViewById(R.id.telphone);
		mCompanyTypeTag = (TextView) findViewById(R.id.companyTypeTag);
		mContect = (WebView) findViewById(R.id.tv_contect);
		mTelphone.setOnClickListener(this);
		mLlClickToNevagetionApp.setOnClickListener(this);
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.company_details);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void getCompanyDetails() {
		params.put("bid", bid);
		params.put("userId", userId);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.COMPANY_DETAILS, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						CompanyDetailsData result = (CompanyDetailsData) AbJsonUtil
								.fromJson(s, CompanyDetailsData.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							detailbean = result.getData();
							mAddress.setText(detailbean.getAddress());
							mTelphone.setText(detailbean.getBusinessTell());
							mCompanyTypeTag.setText(detailbean.getTypeName());
							String data = detailbean.getContent();
							if (data==null){
								data = "<div style=\"line-height:150%;font-size:10pt;color:#333333\">"
										+ "暂无简介" + "</div>";
								data = data.replaceAll("color:black;",
										"color:#333333");
							}else{
								data = "<div style=\"line-height:150%;font-size:10pt;color:#666666\">"
										+ data + "</div>";
								data = data.replaceAll("color:black;",
										"color:#666666");
							}
							if (data != null && !data.isEmpty()) {
								mContect.setVisibility(View.VISIBLE);
								Document doc_Dis = Jsoup.parse(data);
								Elements ele_Img = doc_Dis.getElementsByTag("img");
								if (ele_Img.size() != 0) {
									for (Element e_Img : ele_Img) {
										e_Img.attr("width", "100%");
										e_Img.attr("height", "auto");
									}
								}
								String newHtmlContent = doc_Dis.toString();
								mContect.loadDataWithBaseURL("", newHtmlContent,
										"text/html", "UTF-8", "");
							} else {
								mContect.setVisibility(View.GONE);
							}

							slideList = detailbean.getPictruesList();
							String isAttention = detailbean.getIsCollect();
							String companyName = detailbean.getCompanyName();
							if (TextUtils.isEmpty(companyName)
									&& companyName.length() > 14) {
								mCompanyName.setText(companyName.substring(0,
										14) + "...");
							} else {
								mCompanyName.setText(companyName);
							}
//							mCompanyType.setText(detailbean.getTypeName());
							if (!TextUtils.isEmpty(isAttention)) {
								if (isAttention.equals("1")) {
									attentionBtn
											.setText(getString(R.string.attentioned));
								} else {
									attentionBtn
											.setText(getString(R.string.attention_company));
								}

							}
							setSlidePlayView();
						}
					}
				});
	}

	private void setSlidePlayView() {
		if (slideList.size()>0){
			for (int i = 0; i < slideList.size(); i++) {
				ImageView mPlayView = new ImageView(this);
				mPlayView.setScaleType(ScaleType.FIT_XY);
				String bannerUrl = slideList.get(i);
				Log.i("===================", bannerUrl);
				finalBitmap.display(mPlayView, bannerUrl);
				mSlidingPlayView.addView(mPlayView);
			}
			mSlidingPlayView.startPlay();
		}else{
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.FIT_XY);
			Glide.with(this).load(R.drawable.company_default_picture)
					.into(imageView);
			mSlidingPlayView.addView(imageView);
			mSlidingPlayView.stopPlay();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.attentionBtn:
			
			if (TextUtils.isEmpty(userId)) {
				TipDialog.showDialogNoClose(CompanyDetailsActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			} else {
				attentionCompany(userId);
			}
			break;
		case R.id.telphone:
			if (!TextUtils.isEmpty(mTelphone.getText().toString())) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				// url:统一资源定位符
				// uri:统一资源标示符（更广）
				intent.setData(Uri.parse("tel:"
						+ mTelphone.getText().toString()));
				// 开启系统拨号器
				startActivity(intent);
			}

			break;
			case R.id.ll_address_click_app:
				OpenExternalMapAppUtils.openMapNavi(this, longitude, latitude, companyName, describe, "宁波智慧园");
				break;
		default:
			break;
		}
	}

	private void attentionCompany(String userId) {
		/*
		 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞 8加入圈子
		 */
		params.put("userId", userId);
		params.put("actionId", bid);
		params.put("actionType", Constant.ATTENTION + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						UserOperationResult result = (UserOperationResult) AbJsonUtil
								.fromJson(s, UserOperationResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {

							if (result.getData().equals("关注成功")) {
								Toast.makeText(CompanyDetailsActivity.this,
										result.getData(), Toast.LENGTH_SHORT)
										.show();
								attentionBtn
										.setText(getString(R.string.attentioned));
								detailbean.setIsCollect("1");
								if (demand == null || demand.isEmpty()) {
									Message message = handler.obtainMessage();
									message.arg1 = position;
									message.what = CompanyShowItemBean.COLLECT_COMPANY;
									handler.sendMessage(message);
								}

							} else {
								Toast.makeText(CompanyDetailsActivity.this,
										"取消关注成功！", Toast.LENGTH_SHORT).show();
								attentionBtn
										.setText(getString(R.string.attention_company));
								detailbean.setIsCollect("0");
								if (demand == null || demand.isEmpty()) {
									Message message = handler.obtainMessage();
									message.arg1 = position;
									message.what = CompanyShowItemBean.CANCEL_COLLECT_COMPANY;
									handler.sendMessage(message);
								}

							}
						} else {
							Toast.makeText(CompanyDetailsActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
						}

					}
				});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		bid = null;
	}
}
