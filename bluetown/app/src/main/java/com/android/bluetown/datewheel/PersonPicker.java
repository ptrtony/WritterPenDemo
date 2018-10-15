package com.android.bluetown.datewheel;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.bean.GardenBean;
import com.android.bluetown.bean.MakingIncreaseBean;
import com.android.bluetown.bean.ParkingLotsBean;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.result.GardenListResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.ParseJSONTools;

/**
 * 访客人数Picker
 * 
 * @author zd
 * 
 */
public class PersonPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker personPicker;
	private Context context;
	private List<ParkingLotsBean> gardens=new ArrayList<ParkingLotsBean>();
	private ArrayList<String> personList = new ArrayList<String>();
	private String personStr;
	private String type;
	// Http请求
	protected AbHttpUtil httpInstance;
	// Http请求参数
	protected AbRequestParams params;

	public PersonPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context);
	}

	public PersonPicker(Context context) {
		super(context);
		this.context = context;
		init(context);

	}

	private void initHttpRequest() {
		// TODO Auto-generated method stub
		if (httpInstance == null) {
			httpInstance = AbHttpUtil.getInstance(context);
		}

		if (params == null) {
			params = new AbRequestParams();
		}
	}

	private void getPickList() {
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.MobiCollectAction_getVersionXml, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONObject data = json.optJSONObject("data");
								JSONArray parkingLots = data.optJSONArray("parkingLots");
								for (int j = 0; j < parkingLots.length(); j++) {
									JSONObject itemObj = parkingLots.optJSONObject(j);
									ParkingLotsBean bb = (ParkingLotsBean) ParseJSONTools
											.getInstance().fromJsonToJava(
													itemObj, ParkingLotsBean.class);
									gardens.add(bb);
									personList.add(itemObj.optString("name")+"¥"+itemObj.optString("amount"));
								}
								personPicker.setData(personList);
								personPicker.setDefault(0);
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

	/**
	 * @param context
	 */
	private void init(Context context) {

		initHttpRequest();

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.person_picker, this);
		// 获取控件引用
		personPicker = (ScrollerNumberPicker) findViewById(R.id.personPicker);
		getPickList();
	}

	public String[] getPersonCount() {
		personStr = personPicker.getSelectedText();
		if (personList != null && personList.size() > 0) {
			int position = personList.indexOf(personStr);
			type = gardens.get(position).type;
		}
		
		String[] data = new String[] { personStr, type };
		return data;
	}
}
