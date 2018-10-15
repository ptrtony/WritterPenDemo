package com.android.bluetown.datewheel;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.bean.GardenBean;
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.result.GardenListResult;
import com.android.bluetown.utils.Constant;

/**
 * 园区Picker
 * 
 * @author zd
 * 
 */
public class GardenPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker gardenPicker;
	private Context context;
	private List<GardenBean> gardens;
	private ArrayList<String> gardenList = new ArrayList<String>();
	// Http请求
	protected AbHttpUtil httpInstance;
	// Http请求参数
	protected AbRequestParams params;
	private String city, province, gardenStr, gardenId;
	private FinalDb db;

	public GardenPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context);
	}

	public GardenPicker(Context context) {
		super(context);
		this.context = context;
		init(context);

	}

	/**
	 * @param context
	 */
	private void init(Context context) {
		db = FinalDb.create(context);
		List<LocationInfo> infos = db.findAll(LocationInfo.class);
		LocationInfo info = null;
		if (infos != null && infos.size() != 0) {
			info = infos.get(0);
			if (info != null) {
				city = info.getCity();
				province = info.getProvince();
			}
		}
		initHttpRequest();
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

	/**
	 * 获取园区列表
	 * 
	 * @param province
	 *            :商家省（ 传实值）
	 * @param (必填) city：市 （传实值）(必填)
	 */
	private void getGardenList() {
		params.put("province", province);
		params.put("city", city);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.GARDEN_LIST,
				params, new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						GardenListResult result = (GardenListResult) AbJsonUtil
								.fromJson(s, GardenListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							gardens = (ArrayList<GardenBean>) result.getData();
							for (int j = 0; j < gardens.size(); j++) {
								gardenList.add(gardens.get(j).getHotRegion());
							}
							if (gardenList != null && gardenList.size() > 0) {
								gardenPicker.setData(gardenList);
								gardenPicker.setDefault(0);
							}
						}

					}
				});
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.garden_picker, this);
		// 获取控件引用
		gardenPicker = (ScrollerNumberPicker) findViewById(R.id.gardenPicker);
		getGardenList();
	}

	public String[] getGarden() {
		gardenStr = gardenPicker.getSelectedText();
		if (gardenList != null && gardenList.size() > 0) {
			int position = gardenList.indexOf(gardenStr);
			gardenId = gardens.get(position).getHid();
		}
		String[] data = new String[] { gardenStr, gardenId };
		return data;
	}
}
