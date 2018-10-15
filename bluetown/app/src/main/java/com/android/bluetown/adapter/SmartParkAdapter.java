package com.android.bluetown.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.RemindCarport;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.home.main.model.act.GuestAppointmentActivity;
import com.android.bluetown.utils.Constant;


/**
 * 
 * @ClassName: SmartParkAdapter
 * @Description:TODO(SmartParkAdapter)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
public class SmartParkAdapter extends BaseContentAdapter {
	private Activity activity;
	private FinalDb db;
	private String userId;
	private AbHttpUtil httpUtil = null;
	private List<MemberUser> users;

	public SmartParkAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		db = FinalDb.create(context);
		activity=(Activity) context;
		httpUtil = AbHttpUtil.getInstance(context);
		httpUtil.setTimeout(10000);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_smart_parking,
							null);
			mHolder = new ViewHolder();
			mHolder.gardenZone = (TextView) convertView.findViewById(R.id.gardenZone);
			mHolder.carCount = (TextView) convertView.findViewById(R.id.carCount);
			mHolder.yuyue = (TextView) convertView.findViewById(R.id.yuyue);
			mHolder.cant_yuyue = (TextView) convertView.findViewById(R.id.cant_yuyue);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(final ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final RemindCarport item = (RemindCarport) getItem(position);
		mHolder.gardenZone.setText(item.getParkingName());
		mHolder.carCount.setText(item.getRemainingSpaces());
		if(item.getRemainingSpaces().equals("0")){
			mHolder.yuyue.setVisibility(View.GONE);
			mHolder.cant_yuyue.setVisibility(View.VISIBLE);
		}else{
			mHolder.yuyue.setVisibility(View.VISIBLE);
			mHolder.cant_yuyue.setVisibility(View.GONE);
		}
		mHolder.yuyue.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				idadd(item,mHolder);
			}
			
		});
	}
	private void idadd(final RemindCarport item,final ViewHolder mHolder){
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userId);
		params.put("mid", item.getMid());
		httpUtil.post(Constant.HOST_URL
				+ Constant.Interface.MobiMakingAppointAction_isAdd, params,
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								BlueTownApp.ins.CANAPPOINT=item.getVdate();
								Intent intent = new Intent(activity, GuestAppointmentActivity.class);
								intent.putExtra("gardenZone", mHolder.gardenZone.getText().toString());
								intent.putExtra("parkingLotNo", item.getParkingLotNo());
								intent.putExtra("mid", item.getMid());
								intent.putExtra("vdate", item.getVdate());
								activity.startActivityForResult(intent, 0);
							} else {
								new PromptDialog.Builder(context)
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
						}

					}

					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

				});
	}

	static class ViewHolder {
		private TextView gardenZone;
		private TextView carCount;
		private TextView yuyue;
		private TextView cant_yuyue;
	}

}
