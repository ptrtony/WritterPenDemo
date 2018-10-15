package com.android.bluetown.baidu.overlay;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.android.bluetown.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.search.poi.PoiResult;

/**
 * 用于显示poi的overly
 */
public class PoiOverlay extends OverlayManager {

	private static final int MAX_POI_SIZE = 10;

	private PoiResult mPoiResult = null;

	/**
	 * 构造函数
	 * 
	 * @param baiduMap
	 *            该 PoiOverlay 引用的 BaiduMap 对象
	 */
	public PoiOverlay(BaiduMap baiduMap) {
		super(baiduMap);
	}

	/**
	 * 设置POI数据
	 * 
	 * @param poiResult
	 *            设置POI数据
	 */
	public void setData(PoiResult poiResult) {
		this.mPoiResult = poiResult;
	}

	@Override
	public final List<OverlayOptions> getOverlayOptions() {
		if (mPoiResult == null || mPoiResult.getAllPoi() == null) {
			return null;
		}
		List<OverlayOptions> markerList = new ArrayList<OverlayOptions>();
		int markerSize = 0;
		for (int i = 0; i < mPoiResult.getAllPoi().size()
				&& markerSize < MAX_POI_SIZE; i++) {
			if (mPoiResult.getAllPoi().get(i).location == null) {
				continue;
			}
			markerSize++;
			Bundle bundle = new Bundle();
			bundle.putInt("index", i);
			setMarkBg(markerList, i, bundle);
		}
		return markerList;
	}

	/**
	 * @param markerList
	 * @param i
	 * @param bundle
	 */
	private void setMarkBg(List<OverlayOptions> markerList, int i, Bundle bundle) {
		switch (i % 7) {
		case 0:
			markerList.add(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_map_poi3))
					.extraInfo(bundle)
					.position(mPoiResult.getAllPoi().get(i).location));
			break;
		case 1:
			markerList.add(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_map_poi1))
					.extraInfo(bundle)
					.position(mPoiResult.getAllPoi().get(i).location));
			break;
		case 2:
			markerList.add(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_map_poi2))
					.extraInfo(bundle)
					.position(mPoiResult.getAllPoi().get(i).location));
			break;
		case 3:
			markerList.add(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_map_poi3))
					.extraInfo(bundle)
					.position(mPoiResult.getAllPoi().get(i).location));
			break;
		case 4:
			markerList.add(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_map_poi4))
					.extraInfo(bundle)
					.position(mPoiResult.getAllPoi().get(i).location));
			break;
		case 5:
			markerList.add(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_map_poi5))
					.extraInfo(bundle)
					.position(mPoiResult.getAllPoi().get(i).location));
			break;
		case 6:
			markerList.add(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_map_poi6))
					.extraInfo(bundle)
					.position(mPoiResult.getAllPoi().get(i).location));
			break;
		case 7:
			markerList.add(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_map_poi7))
					.extraInfo(bundle)
					.position(mPoiResult.getAllPoi().get(i).location));
			break;
		default:
			break;
		}
	}

	/**
	 * 获取该 PoiOverlay 的 poi数据
	 * 
	 * @return
	 */
	public PoiResult getPoiResult() {
		return mPoiResult;
	}

	/**
	 * 覆写此方法以改变默认点击行为
	 * 
	 * @param i
	 *            被点击的poi在
	 *            {@link PoiResult#getAllPoi()} 中的索引
	 * @return
	 */
	public boolean onPoiClick(int i) {
		// if (mPoiResult.getAllPoi() != null
		// && mPoiResult.getAllPoi().get(i) != null) {
		// Toast.makeText(BMapManager.getInstance().getContext(),
		// mPoiResult.getAllPoi().get(i).name, Toast.LENGTH_LONG)
		// .show();
		// }
		return false;
	}

	@Override
	public final boolean onMarkerClick(Marker marker) {
		if (!mOverlayList.contains(marker)) {
			return false;
		}
		if (marker.getExtraInfo() != null) {
			return onPoiClick(marker.getExtraInfo().getInt("index"));
		}
		return false;
	}

	@Override
	public boolean onPolylineClick(Polyline polyline) {
		// TODO Auto-generated method stub
		return false;
	}
}
