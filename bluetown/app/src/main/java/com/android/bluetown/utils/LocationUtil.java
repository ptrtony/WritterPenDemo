package com.android.bluetown.utils;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.bluetown.bean.LocationInfo;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult.AddressComponent;

/**
 * 定位类
 * 
 * @author shenyz
 * 
 */
public class LocationUtil implements BDLocationListener,
		OnGetGeoCoderResultListener {
	private Context context;
	private static LocationUtil location;
	public LocationClient mLocationClient;
	private LocationInfo info;// 当前定位城市

	GeoCoder mSearch = null; // 搜索模块
	FinalDb db;
	/**
	 * 实例化百度定位
	 * 
	 * LocationMode.Hight_Accuracy;
	 * //高精度定位模式下，会同时使用GPS、Wifi和基站定位，返回的是当前条件下精度最好的定位结果
	 * LocationMode.Battery_Saving;
	 * //低功耗定位模式下，仅使用网络定位即Wifi和基站定位，返回的是当前条件下精度最好的网络定位结果
	 * LocationMode.Device_Sensors;
	 * //仅用设备定位模式下，只使用用户的GPS进行定位。这个模式下，由于GPS芯片锁定需要时间，首次定位速度会需要一定的时间
	 * 
	 * "gcj02"; "bd09ll"; "bd09";
	 * 
	 */
	private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;

	private LocationUtil(Context context) {
		this.context = context;
		// 声明LocationClient类
		mLocationClient = new LocationClient(context);
		initLocation();
		info = new LocationInfo();
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		System.out.println("yanzi 定位实例化");
		db = FinalDb.create(context);

	}

	public static LocationUtil getInstance(Context context) {
		if (location == null) {
			location = new LocationUtil(context);
		}
		return location;

	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		// 设置发起定位请求的间隔时间为5000ms
		option.setScanSpan(5000);
		option.setOpenGps(true);
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType("wgs84");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);

	}

	/**
	 * 开始定位
	 */
	public void startLoc() {
		mLocationClient.registerLocationListener(this);
		mLocationClient.start();
		Log.e("yanzi", "开始定位");
		System.out.println("yanzi 开始定位");

	}

	/**
	 * stopLoc 关闭定位 (这里描述这个方法适用条件 – 可选)
	 *
	 *            void
	 * @exception
	 * @since 1.0.0
	 */

	public void stopLoc() {
		mLocationClient.unRegisterLocationListener(this);
		mLocationClient.stop();
		Log.e("yanzi", "关闭定位");
	}

	/**
	 * 返回当前定位信息
	 * 
	 * @return the info
	 */

	public LocationInfo getInfo() {
		if (info != null && !info.city.isEmpty()) {
			return info;
		}
		return info;
	}

	/*
	 * 获取定位数据
	 * 
	 * @see
	 * com.baidu.location.BDLocationListener#onReceiveLocation(com.baidu.location
	 * .BDLocation)
	 */
	@Override
	public void onReceiveLocation(BDLocation location) {
		// 定位失效,默认是宁波市,会继续定位直到定位成功,或者退出程序不在定位
		if (location == null
				|| location.getLocType() == BDLocation.TypeNetWorkException
				|| location.getLocType() == BDLocation.TypeServerError
				|| location.getLocType() == BDLocation.TypeNone) {
			startLoc();
		} else {
			LocationInfo info = LocationUtil.getInstance(context).getInfo();
			info.latitude = location.getLatitude();
			info.longitude = location.getLongitude();
			LatLng latLng = new LatLng(info.latitude, info.longitude);
			System.out.println("<<<<<<<info latitude"+info.latitude+"<<<<<<longitude is "+info.longitude);
			// 反Geo搜索
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
		}

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(context, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		LocationInfo info = LocationUtil.getInstance(context).getInfo();
		AddressComponent component = arg0.getAddressDetail();
		info.city = component.city;
		info.province = component.province;
		info.addr = arg0.getAddress();
		System.out.println("<<<<<<<info "+info.city+"<<<<<<province is "+info.province);
		List<LocationInfo> infos = db.findAll(LocationInfo.class);
		if (infos == null || infos.size() == 0) {
			LocationInfo selectInfo = new LocationInfo();
			selectInfo.setLatitude(info.latitude);
			selectInfo.setLongitude(info.longitude);
			selectInfo.setCity(info.city);
			selectInfo.setProvince(info.province);
			selectInfo.setAddr(info.addr);
			db.save(selectInfo);
		}else{
		db.deleteAll(LocationInfo.class);
			LocationInfo selectInfo = new LocationInfo();
			selectInfo.setLatitude(info.latitude);
			selectInfo.setLongitude(info.longitude);
			selectInfo.setCity(info.city);
			selectInfo.setProvince(info.province);
			selectInfo.setAddr(info.addr);
			db.save(selectInfo);
		}
		
		System.out.println("<<<<<<<infos111 " + infos.size());
	}

}
