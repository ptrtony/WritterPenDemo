package com.android.bluetown.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

@SuppressWarnings("deprecation")
public class LocationTools {
	private static Location location;

	public static LocationManager getLocationManager(Context context) {
		return (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	// 获取位置信息
	public static String[] getLocation(Context context) {
		LocationManager locationManager = getLocationManager(context);
		if (!locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// 打开GPS 需Android2.2以上系统支持
			android.provider.Settings.Secure.setLocationProviderEnabled(
					context.getContentResolver(), LocationManager.GPS_PROVIDER,
					false);
		}
		return doWork(context);
	}

	private static String[] doWork(Context context) {
		LocationManager locationManager = getLocationManager(context);
		Criteria criteria = new Criteria();
		// 获得最好的定位效果
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		// 使用省电模式
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(criteria, true);
		Log.i("provider>>>>>>", provider);
		// 获得当前位置 location为空是一直取 从onLocationChanged里面取
		while (location == null) {
			location = locationManager.getLastKnownLocation(provider);
		}
		// locationListener
		LocationListener locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				LocationTools.location = location;
			}

			@Override
			public void onProviderDisabled(String provider) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

		};
		locationManager.requestLocationUpdates(provider, 1000, 10,
				locationListener);
		String[] locations = new String[] { location.getLatitude() + "",
				location.getLongitude() + "" };
		return locations;
	}
}
