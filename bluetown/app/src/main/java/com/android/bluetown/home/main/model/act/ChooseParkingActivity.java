package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ChooseParkingAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.ParkingSpaceBean;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.ParseJSONTools;

public class ChooseParkingActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener {
	private GridView girdview;
	private List<ParkingSpaceBean> list;
	private ChooseParkingAdapter adapter;
	private FinalDb db;
	private String userId;
	private String garden;
	private String phoneNumber;
	private SharePrefUtils prefUtils;
	private String parkingSpace;
	private TextView carNumber,tv_parkingSpace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_choose_parking);
		BlueTownExitHelper.addActivity(this);
		initView();
		setData();
		getCarport();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		adapter.setSeclection(position);		
		adapter.notifyDataSetChanged();
		ParkingSpaceBean item=new ParkingSpaceBean();
		item=list.get(position);
		if(item.isRentable.equals("0")){
			parkingSpace = adapter.getText(position);
			tv_parkingSpace.setTextColor(0xffFFAB53);
			tv_parkingSpace.setText("选择车位  "+parkingSpace);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.can_choose:
			if(parkingSpace==null||parkingSpace.equals("")){
				new PromptDialog.Builder(ChooseParkingActivity.this)
				.setViewStyle(
						PromptDialog.VIEW_STYLE_NORMAL)
				.setMessage("请选择车位")
				.setButton1(
						"确定",
						new PromptDialog.OnClickListener() {

							@Override
							public void onClick(
									Dialog dialog,
									int which) {
								// TODO Auto-generated
								// method stub
								dialog.cancel();
							}
						}).show();
				return;
			}
			Intent intent = (new Intent(ChooseParkingActivity.this,
					ParkingConfirmOrderActivity.class));
			intent.putExtra("userName", getIntent().getStringExtra("userName"));
			intent.putExtra("amount", getIntent().getStringExtra("amount"));
			intent.putExtra("carNumber", getIntent()
					.getStringExtra("carNumber"));
			intent.putExtra("parkingType",
					getIntent().getStringExtra("parkingType"));
			intent.putExtra("mouthNumber",
					getIntent().getStringExtra("mouthNumber"));
			intent.putExtra("region", getIntent().getStringExtra("region"));
			intent.putExtra("parkingSpace",parkingSpace);
			intent.putExtra("mid", getIntent().getStringExtra("mid"));
			intent.putExtra("url", getIntent().getStringExtra("url"));
			intent.putExtra("parkingLotNo", getIntent().getStringExtra("parkingLotNo"));
			intent.putExtra("parkingName", getIntent().getStringExtra("parkingName"));
			startActivity(intent);

			break;
		}

	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(getIntent().getStringExtra("parkingName"));
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void initView() {
		findViewById(R.id.can_choose).setOnClickListener(this);
		girdview = (GridView) findViewById(R.id.gridview);
		carNumber=(TextView)findViewById(R.id.carNumber);
		tv_parkingSpace=(TextView)findViewById(R.id.tv_parkingSpace);
		
		carNumber.setText(getIntent().getStringExtra("carNumber"));
		girdview.setOnItemClickListener(this);
		girdview.setSelector(new BitmapDrawable());
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(ChooseParkingActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				garden = user.getHotRegion();
				phoneNumber = user.getUsername();
			}
		}
	}

	private void setData() {
		list = new ArrayList<ParkingSpaceBean>();
		adapter = new ChooseParkingAdapter(this, list);
		girdview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	private void getCarport() {
		// TODO Auto-generated method stub
		params.put("mid", getIntent().getStringExtra("mid"));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.ParkingOrderAction_getRulerInfo, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONArray data = json.optJSONArray("data");
								for (int j = 0; j < data.length(); j++) {
									ParkingSpaceBean bb = new ParkingSpaceBean();
									JSONObject itemObj = data.optJSONObject(j);
									bb = (ParkingSpaceBean) ParseJSONTools
											.getInstance().fromJsonToJava(
													itemObj, ParkingSpaceBean.class);
									if(bb.parkingSpaceType.equals(getIntent().getStringExtra("parkingSpaceType"))){
										list.add(bb);
									}									
								}
								adapter.notifyDataSetChanged();
							} else {
								new PromptDialog.Builder(ChooseParkingActivity.this)
										.setViewStyle(
												PromptDialog.VIEW_STYLE_NORMAL)
										.setMessage(json.optString("repMsg"))
										.setButton1(
												"确定",
												new PromptDialog.OnClickListener() {

													@Override
													public void onClick(
															Dialog dialog,
															int which) {
														// TODO Auto-generated
														// method stub
														dialog.cancel();
													}
												}).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					
				});
	}

}
