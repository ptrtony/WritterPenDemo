package com.android.bluetown.surround;

import java.util.List;
import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.bean.OtherMerchant;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.result.CanteenFoodDetailsResult.CanteenMerchant;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

/**
 * 地图页面
 * 
 * @author shenyz
 * 
 */
public class MapViewActivity extends TitleBarActivity implements
		OnClickListener {
	private LinearLayout startNav;
	// 商家的经度、纬度
	private double longitude, latitude;
	// 商家城市，用户城市
	private String merchantCity, userCity;
	BitmapDescriptor mCurrentMarker;// Marker图标
	private MapView mapView;
	BaiduMap mBaiduMap;
	private String flag;
	private OtherMerchant otherMerchant;
	private CanteenMerchant canteenMerchant;
	private FinalDb db;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.map);
		righTextLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_map);
		db = FinalDb.create(this);
		flag = getIntent().getStringExtra("flag");
		if (flag.equals("other")) {
			otherMerchant = (OtherMerchant) getIntent().getSerializableExtra(
					"object");
			longitude = Double.parseDouble(otherMerchant.getLongitude());
			latitude = Double.parseDouble(otherMerchant.getLatitude());
			merchantCity = otherMerchant.getCity();
		} else {
			canteenMerchant = (CanteenMerchant) getIntent()
					.getSerializableExtra("object");
			longitude = Double.parseDouble(canteenMerchant.getLongitude());
			latitude = Double.parseDouble(canteenMerchant.getLatitude());
			merchantCity = canteenMerchant.getCity();
		}
		startNav = (LinearLayout) findViewById(R.id.startNav);
		startNav.setOnClickListener(this);
		// 构建mark图标
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_markg);
		// 地图初始化
		mapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 修改为自定义marker
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_markg);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, mCurrentMarker));
		MyLocationData locData = new MyLocationData.Builder()
				.latitude(latitude).longitude(longitude).build();
		mBaiduMap.setMyLocationData(locData);
		LatLng ll = new LatLng(latitude, longitude);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(u);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.startNav:
		List<LocationInfo> infos = db.findAll(LocationInfo.class);
			if (infos != null && infos.size() != 0) {
				LocationInfo info = infos.get(0);
				if (info != null) {
					userCity = info.getCity();
				}
	}
					if (merchantCity.equals(userCity)) {
				// 利用划线实现导航
				Intent intent = new Intent();
				intent.putExtra("longitude", longitude);
				intent.putExtra("latitude", latitude);
				intent.setClass(MapViewActivity.this, RoutePlanDemo.class);
				startActivity(intent);
			} else {
				TipDialog.showDialog(MapViewActivity.this, R.string.tip,
						R.string.confirm, R.string.merchant_no_city_tip);
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		mapView = null;
		super.onDestroy();
	}
}
