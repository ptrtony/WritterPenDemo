package com.android.bluetown.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import com.android.bluetown.listener.OnMarkClickListener;
import com.android.bluetown.model.TileParam;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.TileOverlay;
import com.baidu.mapapi.map.TileOverlayOptions;
import com.baidu.mapapi.map.TileProvider;
import com.baidu.mapapi.map.UrlTileProvider;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

/**
 * Created by Dafen on 2018/8/30.
 */

public class MapManageUtil {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Context mContext;
    TileOverlay tileOverlay;
    private MapStatusUpdate mMapStatusUpdate;
    private OnMarkClickListener onMarkerClickListener1;
    public MapManageUtil(Context context,MapView mMapView, BaiduMap baiduMap){
        this.mContext = context;
        this.mMapView = mMapView;
        this.mBaiduMap = baiduMap;
    }

    public void initMap(float zoom,LatLng centerLatLng){
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        mMapView.removeViewAt(1);
        mMapView.removeViewAt(2);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(zoom);
        builder.target(centerLatLng);
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);
    }

    BaiduMap.OnMarkerClickListener onMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (onMarkerClickListener1!=null){
                onMarkerClickListener1.onMarkClick(marker);
            }
            return false;
        }
    };

    public void setOnMarkerClickListener(OnMarkClickListener onMarkerClickListener){
        this.onMarkerClickListener1 = onMarkerClickListener;
    }


    /**
     * 使用瓦片图的在线方式
     */
    public void onlineTile(TileParam tile) {
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        if (tileOverlay != null && mBaiduMap != null) {
            tileOverlay.removeTileOverlay();
        }
        /**
         * 定义瓦片图的在线Provider，并实现相关接口
         * MAX_LEVEL、MIN_LEVEL 表示地图显示瓦片图的最大、最小级别
         * urlString 表示在线瓦片图的URL地址
         */
        TileProvider tileProvider = new UrlTileProvider() {
            @Override
            public int getMaxDisLevel() {
                return tile.maxLevel;
            }

            @Override
            public int getMinDisLevel() {
                return tile.minLevel;
            }

            @Override
            public String getTileUrl() {
                return tile.onlineUrl;
            }

        };

        TileOverlayOptions options = new TileOverlayOptions();
        // 构造显示瓦片图范围，当前为世界范围
        // 通过option指定相关属性，向地图添加在线瓦片图对象
        tileOverlay = mBaiduMap.addTileLayer(options.tileProvider(tileProvider)
                .setMaxTileTmp(tile.tileTmp)
                .setPositionFromBounds(new LatLngBounds.Builder().include(tile.northeast).include(tile.southwest).build()));
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
        mBaiduMap.setMaxAndMinZoomLevel(tile.maxLevel, tile.minLevel);
        mBaiduMap.setMapStatusLimits(new LatLngBounds.Builder().include(tile.northeast).include(tile.southwest).build());
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    public Bitmap getViewBitmap(View addViewContent) {
        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight()+ DisplayUtils.dip2px(mContext,15));
        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        return bitmap;
    }

}
