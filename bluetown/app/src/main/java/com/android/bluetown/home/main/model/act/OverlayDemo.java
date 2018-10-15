//package com.android.bluetown.home.main.model.act;
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.map.GroundOverlay;
//
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.model.inner.GeoPoint;
//
///**
// * 演示覆盖物的用法
// */
//public class OverlayDemo extends Activity {
//
//    /**
//     *  MapView 是地图主控件
//     */
//    private MapView mMapView = null;
//    /**
//     *  用MapController完成地图控制
//     */
//    private MapController mMapController = null;
//    private MyOverlay mOverlay = null;
//    private PopupOverlay   pop  = null;
//    private ArrayList<OverlayItem>  mItems = null;
//    private Button button = null;
//    private MapView.LayoutParams layoutParam = null;
//    private OverlayItem mCurItem = null;
//    /**
//     * overlay 位置坐标
//     */
//    double mLon1 = 116.400244 ;
//    double mLat1 = 39.963175 ;
//    double mLon2 = 116.369199;
//    double mLat2 = 39.942821;
//    double mLon3 = 116.425541;
//    double mLat3 = 39.939723;
//    double mLon4 = 116.401394;
//    double mLat4 = 39.906965;
//
//    // ground overlay
//    private GroundOverlay mGroundOverlay;
//    private Ground mGround;
//    private double mLon5 = 116.380338;
//    private double mLat5 = 39.92235;
//    private double mLon6 = 116.414977;
//    private double mLat6 = 39.947246;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        /**
//         * 使用地图sdk前需先初始化BMapManager.
//         * BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
//         * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
//         */
//        CrashApplication app = (CrashApplication)this.getApplication();
//        if (app.mBMapManager == null) {
//            app.mBMapManager = new BMapManager(this);
//            /**
//             * 如果BMapManager没有初始化则初始化BMapManager
//             */
//            app.mBMapManager.init(CrashApplication.strKey,new CrashApplication.MyGeneralListener());
//        }
//        /**
//         * 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
//         */
//        setContentView(R.layout.activity_overlay);
//        mMapView = (MapView)findViewById(R.id.bmapView);
//        /**
//         * 获取地图控制器
//         */
//        mMapController = mMapView.getController();
//        /**
//         *  设置地图是否响应点击事件  .
//         */
//        mMapController.enableClick(true);
//        /**
//         * 设置地图缩放级别
//         */
//        mMapController.setZoom(12);
//        /**
//         * 显示内置缩放控件
//         */
//        mMapView.setBuiltInZoomControls(true);
//
//
//        /**
//         * 设定地图中心点241B7D0822F1068B3E0C472A4F825FFEFF8C6982
//         */
//        GeoPoint p = new GeoPoint((int)(mLat1 * 1E6), (int)(mLon1* 1E6));
//        mMapController.setCenter(p);initOverlay();
//
//    }
//
//    public void initOverlay(){
//        mOverlay = new MyOverlay(getResources().getDrawable(
//                R.drawable.icon_marka), mMapView);
//        //一个覆盖物
//        OverlayItem item1 = new OverlayItem(transformToGeoP(mLat1,mLon1), "覆盖物", "bb");
//        item1.setMarker(getResources().getDrawable(R.drawable.icon_marka));
//
//        mMapView.getOverlays().clear();
//        mMapView.getOverlays().add(mOverlay);
//        mOverlay.addItem(item1);
//        // 初始化 ground 图层
//        mGroundOverlay = new GroundOverlay(mMapView);
//        GeoPoint leftBottom = new GeoPoint((int) (mLat5 * 1E6),
//                (int) (mLon5 * 1E6));
//        GeoPoint rightTop = new GeoPoint((int) (mLat6 * 1E6),
//                (int) (mLon6 * 1E6));
//        Drawable d = getResources().getDrawable(R.drawable.ground_overlay);
//        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
//        mGround = new Ground(bitmap, leftBottom, rightTop);
//
//        /**
//         * 将overlay 添加至MapView中
//         */
//        mMapView.getOverlays().add(mGroundOverlay);
//        mGroundOverlay.addGround(mGround);
//
//        //  //默认弹出
//        showPopupOverlay(transformToGeoP(mLat1,mLon1 ));
//        mMapView.refresh();     mMapView.refresh();Log.d("map","refresh end"+ mMapView.getOverlays().get(0).mLayerID);
//    }
//
//
//    // 自定义覆盖物
//    public class MyOverlay extends ItemizedOverlay  <OverlayItem>
//    {
//        public MyOverlay(Drawable arg0, MapView arg1)
//        {
//            super(arg0, arg1);
//        }
//
//        @Override
//        protected boolean onTap(int arg0)
//        {
//            //弹出覆盖物
//            showPopupOverlay(getItem(arg0).getPoint());
//            return true;
//        }
//
//        @Override
//        public boolean onTap(GeoPoint arg0, MapView arg1)
//        {
//            if (pop != null)
//            {
//                //隐藏覆盖物
//                pop.hidePop();
//            }
//            return false;
//        }
//    }
//
//    // 弹出覆盖物
//    public void showPopupOverlay(GeoPoint p1)
//    {
//        pop = new PopupOverlay(mMapView, null);
//        View view = getLayoutInflater().inflate(R.layout.custom_text_view, null);
//        View pop_layout = view.findViewById(R.id.popleft);
//        //将View转化成用于显示的bitmap
//        View viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
//        View popupLeft = (View) viewCache.findViewById(R.id.popleft);
//        Bitmap[] bitMaps = {BMapUtil.getBitmapFromView(popupLeft) };
//
//        pop.showPopup(bitMaps, p1, 32);
//    }
//
//    // 将经纬度转换成GeoPoint
//    private GeoPoint transformToGeoP(double lat, double lng)
//    {
//        return new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
//    }
//
//    @Override
//    protected void onPause() {
//        /**
//         *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
//         */
//        //  mMapView.onPause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        /**
//         *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
//         */
//        //  mMapView.onResume();
//        super.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//        /**
//         *  MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
//         */
//        mMapView.onDestroy();
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mMapView.onSaveInstanceState(outState);
//
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mMapView.onRestoreInstanceState(savedInstanceState);
//    }
//
//
//}