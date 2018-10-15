package com.android.bluetown.home.main.model.act;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.base.BaseToolbarMapActivity;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.OnMarkClickListener;
import com.android.bluetown.model.TileParam;
import com.android.bluetown.monitor.callbacks.MsgCallback;
import com.android.bluetown.monitor.callbacks.MsgIds;
import com.android.bluetown.monitor.data.Config;
import com.android.bluetown.monitor.data.TempData;
import com.android.bluetown.monitor.resource.ResourceControl;
import com.android.bluetown.monitor.util.MonitorConstant;
import com.android.bluetown.monitor.util.UIUtil;
import com.android.bluetown.network.AbHttpUtil;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.VideoAccountResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DisplayUtils;
import com.android.bluetown.utils.MapManageUtil;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TileOverlay;
import com.baidu.mapapi.map.TileOverlayOptions;
import com.baidu.mapapi.map.TileProvider;
import com.baidu.mapapi.map.UrlTileProvider;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.LineInfo;
import com.hikvision.vmsnetsdk.RegionInfo;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import net.tsz.afinal.FinalDb;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MapActualParkActivity extends BaseToolbarMapActivity implements BaiduMap.OnMapLoadedCallback, MsgCallback {
    private static final String TAG = "MyLocationListener";
    private static final String POP_TITLE = "MAPACUALPARK";
    private static final int LIVE_LIST_MESSAGE = 0x111111;
    private String onlineUrl = "http://smartparkapi.dolphintek.cn:8901/api/File/ReadMapImage?zoom={z}&x={x}&y={y}";
    // 瓦片图对象
    TileOverlay tileOverlay;
    MapStatusUpdate mMapStatusUpdate;
    private LocationClient mLocationClient = null;
    private static final int MIN_LEVEL = 16;
    private static final int MAX_LEVEL = 18;
    private static final int TILE_TMP = 20 * 1024 * 1024;
    private boolean mapLoaded = false;
    // Http请求
    protected AbHttpUtil httpInstance;
    // Http请求参数
    protected AbRequestParams params;
    /*************** 监控的相关变量 **********************/
    /**
     * 登录平台地址
     */
    private String servAddr, username, password;
    /**
     * 用户选中的线路
     */
    private LineInfo lineInfo;
    /**
     * 登录返回的数据
     */
    private ServInfo servInfo;
    /**
     * 是否是第一次执行onResume方法
     */
    private boolean isFirstResume = true;
    /**
     * 发送消息的对象
     */
    private MsgHandler msgHandler = new MsgHandler();
    /************** 页面相关组件和变量 ****************/

    /***
     * 父节点资源类型，TYPE_UNKNOWN表示首次获取资源列表
     */
    private int pResType = MonitorConstant.Resource.TYPE_UNKNOWN;
    /***
     * 父控制中心的id，只有当parentResType为TYPE_CTRL_UNIT才有用
     */
    private int pCtrlUnitId;
    /***
     * 父区域的id，只有当parentResType为TYPE_REGION才有用
     */
    private int pRegionId;
    /*** 资源列表适配器 */
//    private RealTimeZoneListAdapter adapter;
    /*** 资源列表适配器(父区) */
//    private RealtimeZoneTopAdapter adapter2;
    /***
     * 获取资源逻辑控制类
     */
    private ResourceControl rc;
    //    private AbLoadDialogFragment mDialogFragment;
    private String userId;
    private FinalDb db;
    private List<MemberUser> users;
    private SharePrefUtils prefUtils;
//    private HorizontalListView hListView;
    /***
     * 判断加载位置（0：区域，1：监控点）
     */
    private int type = 0;

    private int position = 1;//标记每次获取到的监控点的个数
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    Map<String,Object> liveMap = new HashMap<>();
    private MapManageUtil mapUtil;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MapActualPark Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
//    private Object oneByOneItem;//A区一号楼
//    private Button button;


    private final class MsgHandler extends Handler {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MonitorConstant.Login.GET_LINE_SUCCESS:
                    onGetLineSuccess((List<Object>) msg.obj);
                    break;
                case MonitorConstant.Login.GET_LINE_FAILED:
                    onGetLineFailed();
                    break;
                case MonitorConstant.Login.LOGIN_SUCCESS:
                    // 登录成功
                    onLoginSuccess();
                    break;
                case MonitorConstant.Login.LOGIN_FAILED:
                    // 登录失败
                    onLoginFailed();
                    break;
                // 获取控制中心列表成功
                case MsgIds.GET_C_F_NONE_SUC:

                    // 从控制中心获取下级资源列表成功
                case MsgIds.GET_SUB_F_C_SUC:
                    // 从区域获取下级列表成功
                case MsgIds.GET_SUB_F_R_SUC:
                    refreshResList((List<Object>) msg.obj);
                    break;
                // 获取控制中心列表失败
                case MsgIds.GET_C_F_NONE_FAIL:
                    // 调用getControlUnitList失败
                case MsgIds.GET_CU_F_CU_FAIL:
                    // 调用getRegionListFromCtrlUnit失败
                case MsgIds.GET_R_F_C_FAIL:
                    // 调用getCameraListFromCtrlUnit失败
                case MsgIds.GET_C_F_C_FAIL:
                    // 从控制中心获取下级资源列表成失败
                case MsgIds.GET_SUB_F_C_FAIL:
                    // 调用getRegionListFromRegion失败
                case MsgIds.GET_R_F_R_FAIL:
                    // 调用getCameraListFromRegion失败
                case MsgIds.GET_C_F_R_FAIL:
                    // 从区域获取下级列表失败
                case MsgIds.GET_SUB_F_R_FAILED:
                    onGetResListFailed();
                case LIVE_LIST_MESSAGE:
                    break;
                default:
                    break;
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_acture_park);
        setTitle("实时园区");
        if (httpInstance == null) {
            httpInstance = AbHttpUtil.getInstance(this);
            httpInstance.setEasySSLEnabled(true);
        }
        if (params == null) {
            params = new AbRequestParams();
        }
        db = FinalDb.create(this);
        prefUtils = new SharePrefUtils(this);

        LatLng centerLatLng = new LatLng(29.9047946929932, 121.632743835449);
        TileParam tile = new TileParam();
        tile.maxLevel = MAX_LEVEL;
        tile.minLevel = MIN_LEVEL;
        tile.northeast = new LatLng(29.939057, 121.656158);
        tile.southwest = new LatLng(29.864668, 121.621663);
        tile.tileTmp = TILE_TMP;
        tile.onlineUrl = onlineUrl;
        mapUtil = new MapManageUtil(this,mMapView,mBaiduMap);
        mapUtil.initMap(16.0f,centerLatLng);
        mapUtil.setOnMarkerClickListener(onMarkerClickListener);
        mapUtil.onlineTile(tile);
        getVideoAccount();
        getGestureLat();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void getGestureLat() {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                Log.d(TAG, "获取点击时的经纬度" + latLng.latitude + "," + latLng.longitude);
//                Toast.makeText(MapActualParkActivity.this, latLng.latitude + "," + latLng.longitude, Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
//                Log.d(TAG, "获取点击时的经纬度" + mapPoi.getPosition().latitude + "," + mapPoi.getPosition().longitude);
                return true;
            }
        });
    }


    @Override
    public void onMapLoaded() {
        mapLoaded = true;
    }

    private void getVideoAccount() {
        params.put("gardenId", prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
        httpInstance.post(Constant.HOST_URL
                        + Constant.Interface.GARDEN_VIDEO_LIST, params,
                new AbsHttpResponseListener() {
                    @Override
                    public void onSuccess(int i, String s) {
                        // TODO
                        VideoAccountResult result = (VideoAccountResult) AbJsonUtil
                                .fromJson(s, VideoAccountResult.class);
                        if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                            servAddr = result.getData().getUrl();
                            username = result.getData().getAccount();
                            password = result.getData().getPassword();
                            Config.getIns().setServerAddr(servAddr);
                            initData();
                            fetchLine();
                        } else if (result.getRepCode().contains("777777")) {
                            new PromptDialog.Builder(
                                    MapActualParkActivity.this)
                                    .setViewStyle(
                                            PromptDialog.VIEW_STYLE_NORMAL)
                                    .setMessage(
                                            result.getRepMsg())
                                    .setCancelable(false)
                                    .setButton1(
                                            "确定",
                                            new PromptDialog.OnClickListener() {

                                                @Override
                                                public void onClick(
                                                        Dialog dialog,
                                                        int which) {
                                                    // TODO
                                                    // Auto-generated
                                                    // method stub
                                                    MapActualParkActivity.this
                                                            .finish();
                                                    dialog.cancel();
                                                }
                                            }).show();

                        } else {
                            Toast.makeText(MapActualParkActivity.this,
                                    result.getRepMsg(), Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        super.onFailure(i, s, throwable);
                    }
                });


    }

    /**
     * @param lineList
     * @throws
     * @Title: onGetLineSuccess
     * @Description: TODO(获取线路成功，刷新线路列表)
     */
    private void onGetLineSuccess(List<Object> lineList) {
        // UIUtil.showToast(RealTimeZoneActivity.this,
        // R.string.getline_suc_tip);
        if (lineList == null || lineList.isEmpty()) {
            return;
        }
        // 获取默认线路
        lineInfo = (LineInfo) lineList.get(0);
        login();
    }

    /**
     * @throws
     * @Title: login
     * @Description: TODO(登录监控平台)
     */
    protected void login() {
        if (servAddr.length() <= 0) {
            UIUtil.showToast(this, R.string.serveraddr_empty_tip);
            return;
        }
        Config.getIns().setServerAddr(servAddr);
        if (lineInfo == null) {
            UIUtil.showToast(this, R.string.line_unavailable);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String macAddress = getMac();
                // 登录请求
                boolean ret = VMSNetSDK.getInstance().login(servAddr, username,
                        password, lineInfo.lineID, macAddress, servInfo);
                if (servInfo != null) {
                    // 打印出登录时返回的信息
                    Log.i(MonitorConstant.LOG_TAG, "login ret : " + ret);
                    Log.i(MonitorConstant.LOG_TAG, "login response info["
                            + "sessionID:" + servInfo.sessionID + ",userID:"
                            + servInfo.userID + ",magInfo:" + servInfo.magInfo
                            + ",picServerInfo:" + servInfo.picServerInfo
                            + ",ptzProxyInfo:" + servInfo.ptzProxyInfo
                            + ",userCapability:" + servInfo.userCapability
                            + ",vmsList:" + servInfo.vmsList + ",vtduInfo:"
                            + servInfo.vtduInfo + ",webAppList:"
                            + servInfo.webAppList + "]");
                }

                if (ret) {
                    TempData.getIns().setLoginData(servInfo);
                    msgHandler
                            .sendEmptyMessage(MonitorConstant.Login.LOGIN_SUCCESS);
                } else {
                    msgHandler
                            .sendEmptyMessage(MonitorConstant.Login.LOGIN_FAILED);
                }

            }
        }).start();
    }

    /**
     * 获取登录设备mac地址
     *
     * @return
     */
    protected String getMac() {
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        String mac = wm.getConnectionInfo().getMacAddress();
        return mac == null ? "" : mac;
    }

    /**
     * @throws
     * @Title: onGetLineFailed
     * @Description: TODO(获取线路失败)
     */
    public void onGetLineFailed() {
        UIUtil.showToast(MapActualParkActivity.this,
                getString(R.string.getline_fail_tip, UIUtil.getErrorDesc()));
    }

    /**
     * @throws
     * @Title: onLoginSuccess
     * @Description: TODO(登录成功)
     */
    public void onLoginSuccess() {
        // UIUtil.showToast(this, R.string.login_suc_tip);
        getParentResTypeAndId();
        reqResList();
    }

    /**
     * @throws
     * @Title: getParentResTypeAndId
     * @Description: TODO(初始化父资源类型和父控制中心的Id)
     */
    private void getParentResTypeAndId() {
        Intent it = getIntent();
        if (it.hasExtra(MonitorConstant.IntentKey.CONTROL_UNIT_ID)) {
            pResType = MonitorConstant.Resource.TYPE_CTRL_UNIT;
            pCtrlUnitId = it.getIntExtra(
                    MonitorConstant.IntentKey.CONTROL_UNIT_ID, 0);
            Log.i(MonitorConstant.LOG_TAG,
                    "Getting resource from ctrlunit.parent id is "
                            + pCtrlUnitId);
        } else if (it.hasExtra(MonitorConstant.IntentKey.REGION_ID)) {
            pResType = MonitorConstant.Resource.TYPE_REGION;
            pRegionId = it.getIntExtra(MonitorConstant.IntentKey.REGION_ID, 0);
            Log.i(MonitorConstant.LOG_TAG,
                    "Getting resource from region. parent id is " + pRegionId);
        } else {
            pResType = MonitorConstant.Resource.TYPE_UNKNOWN;
            Log.i(MonitorConstant.LOG_TAG,
                    "Getting resource for the first time.");
        }
    }

    /**
     * @throws
     * @Title: reqResList
     * @Description: TODO(请求资源列表)
     */
    private void reqResList() {
        rc = new ResourceControl();
        rc.setCallback(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pId = 0;
                if (MonitorConstant.Resource.TYPE_CTRL_UNIT == pResType) {
                    pId = pCtrlUnitId;
                } else if (MonitorConstant.Resource.TYPE_REGION == pResType) {
                    pId = pRegionId;
                }
                rc.reqResList(pResType, pId);
            }
        }).start();
    }

    @Override
    public void onMsg(int msgId, Object data) {
        // TODO Auto-generated method stub
        Message msg = new Message();
        msg.what = msgId;
        msg.obj = data;
        msgHandler.sendMessage(msg);
    }

    /**
     * @throws
     * @Title: onLoginFailed
     * @Description: TODO(登录失败)
     */
    public void onLoginFailed() {
        UIUtil.showToast(this,
                getString(R.string.login_failed, UIUtil.getErrorDesc()));
    }

    /**
     * @param data
     * @throws
     * @Title: refreshResList
     * @Description: TODO(获取数据成功后刷新列表)
     */
    private void refreshResList(List<Object> data) {
        if (data == null || data.isEmpty()) {
            UIUtil.showToast(this, R.string.no_data_tip);
            return;
        }
        // UIUtil.showToast(this, R.string.fetch_resource_suc);
        if (type == 0) {
            showFatherRegionList(data);
        } else {
            showRegionList(data);
            position++;
        }

    }

    private void showFatherRegionList(final List<Object> data) {
        if (pResType != MonitorConstant.Resource.TYPE_CTRL_UNIT
                && pResType != MonitorConstant.Resource.TYPE_UNKNOWN) {
//            if (mDialogFragment != null) {
//                mDialogFragment.dismiss();
//            }

            for (int i = 0; i < data.size(); i++) {
                String desc = getItemDesc(data.get(i));
                if (desc.indexOf("_H") != -1) {
                    data.remove(i);
                    i--;
                }
            }
            type = 1;
            Object item = data.get(2);
            if (!(item instanceof LineInfo)){
                gotoNextLevelList(item);
            }
        } else {
            gotoNextLevelList(data.get(0));
        }
    }

    private String getItemDesc(Object itemData) {
        if (itemData instanceof ControlUnitInfo) {
            ControlUnitInfo info = (ControlUnitInfo) itemData;
            return info.name;
        }

        if (itemData instanceof RegionInfo) {
            RegionInfo info = (RegionInfo) itemData;
            return info.name;
        }

        if (itemData instanceof CameraInfo) {
            CameraInfo info = (CameraInfo) itemData;
            return info.name;
        }

        if (itemData instanceof LineInfo) {
            LineInfo info = (LineInfo) itemData;
            return info.lineName;
        }
        return null;
    }

    private void showRegionList(final List<Object> data) {
        if (pResType != MonitorConstant.Resource.TYPE_CTRL_UNIT
                && pResType != MonitorConstant.Resource.TYPE_UNKNOWN) {
            List<OverlayOptions> options = new ArrayList<>();
            CameraInfo cameraInfo = new CameraInfo();
            cameraInfo.acsIP = "192.168.12.250";
            cameraInfo.acsPort = 7302;
            cameraInfo.cameraID = "5446567845ef4e498b8f9f3ae1c9daff";
            cameraInfo.cameraType = 0;
            cameraInfo.cascadeFlag = 0;
            cameraInfo.channelNo = 1;
            cameraInfo.collectedFlag = 0;
            cameraInfo.deviceID = "86";
            cameraInfo.groupID = 0;
            cameraInfo.isOnline = true;
            cameraInfo.isPTZControl = false;
            cameraInfo.name = "1号楼1大堂";
            cameraInfo.recordPos = Arrays.asList(1);
            cameraInfo.userCapability = Arrays.asList(7);
            Object item = cameraInfo;

            CameraInfo cameraInfo1 = new CameraInfo();
            cameraInfo1.acsIP = "192.168.12.250";
            cameraInfo1.acsPort = 7302;
            cameraInfo1.cameraID = "6fa42771448c412d82d7f57e94aa4a9a";
            cameraInfo1.cameraType = 0;
            cameraInfo1.cascadeFlag = 0;
            cameraInfo1.channelNo = 1;
            cameraInfo1.collectedFlag = 0;
            cameraInfo1.deviceID = "87";
            cameraInfo1.groupID = 0;
            cameraInfo1.isOnline = true;
            cameraInfo1.isPTZControl = false;
            cameraInfo1.name = "1号搂1电梯厅";
            cameraInfo1.recordPos = Arrays.asList(1);
            cameraInfo1.userCapability = Arrays.asList(7);
            Object item1 = cameraInfo1;

            CameraInfo cameraInfo2 = new CameraInfo();
            cameraInfo2.acsIP = "192.168.12.250";
            cameraInfo2.acsPort = 7302;
            cameraInfo2.cameraID = "7b54a88ec9764ead9ddf73c027d1bf23";
            cameraInfo2.cameraType = 0;
            cameraInfo2.cascadeFlag = 0;
            cameraInfo2.channelNo = 1;
            cameraInfo2.collectedFlag = 0;
            cameraInfo2.deviceID = "93";
            cameraInfo2.groupID = 0;
            cameraInfo2.isOnline = true;
            cameraInfo2.isPTZControl = false;
            cameraInfo2.name = "1号搂地下一层门厅";
            cameraInfo2.recordPos = Arrays.asList(1);
            cameraInfo2.userCapability = Arrays.asList(7);
            Object item2 = cameraInfo2;

            CameraInfo cameraInfo3= new CameraInfo();
            cameraInfo3.acsIP = "192.168.12.250";
            cameraInfo3.acsPort = 7302;
            cameraInfo3.cameraID = "ad454345ee2d4209bdc03b36f5979e30";
            cameraInfo3.cameraType = 0;
            cameraInfo3.cascadeFlag = 0;
            cameraInfo3.channelNo = 1;
            cameraInfo3.collectedFlag = 0;
            cameraInfo3.deviceID = "130";
            cameraInfo3.groupID = 0;
            cameraInfo3.isOnline = true;
            cameraInfo3.isPTZControl = false;
            cameraInfo3.name = "3号搂1大堂";
            cameraInfo3.recordPos = Arrays.asList(1);
            cameraInfo3.userCapability = Arrays.asList(7);
            Object item3 = cameraInfo3;

            CameraInfo cameraInfo4= new CameraInfo();
            cameraInfo4.acsIP = "192.168.12.250";
            cameraInfo4.acsPort = 7302;
            cameraInfo4.cameraID = "4199b19770f9459c81d37d6aefcbc212";
            cameraInfo4.cameraType = 0;
            cameraInfo4.cascadeFlag = 0;
            cameraInfo4.channelNo = 1;
            cameraInfo4.collectedFlag = 0;
            cameraInfo4.deviceID = "131";
            cameraInfo4.groupID = 0;
            cameraInfo4.isOnline = true;
            cameraInfo4.isPTZControl = false;
            cameraInfo4.name = "3号搂1电梯厅";
            cameraInfo4.recordPos = Arrays.asList(1);
            cameraInfo4.userCapability = Arrays.asList(7);
            Object item4 = cameraInfo4;

            CameraInfo cameraInfo5= new CameraInfo();
            cameraInfo5.acsIP = "192.168.12.250";
            cameraInfo5.acsPort = 7302;
            cameraInfo5.cameraID = "6c1f632f0926461fa7ba5f44c4f7a037";
            cameraInfo5.cameraType = 0;
            cameraInfo5.cascadeFlag = 0;
            cameraInfo5.channelNo = 1;
            cameraInfo5.collectedFlag = 0;
            cameraInfo5.deviceID = "258";
            cameraInfo5.groupID = 0;
            cameraInfo5.isOnline = true;
            cameraInfo5.isPTZControl = false;
            cameraInfo5.name = "3号楼b1门厅";
            cameraInfo5.recordPos = Arrays.asList(1);
            cameraInfo5.userCapability = Arrays.asList(7);
            Object item5 = cameraInfo5;

            CameraInfo cameraInfo6= new CameraInfo();
            cameraInfo6.acsIP = "192.168.12.250";
            cameraInfo6.acsPort = 7302;
            cameraInfo6.cameraID = "b1e999f6a102452baa7e03ebf08948ca";
            cameraInfo6.cameraType = 0;
            cameraInfo6.cascadeFlag = 0;
            cameraInfo6.channelNo = 1;
            cameraInfo6.collectedFlag = 0;
            cameraInfo6.deviceID = "133";
            cameraInfo6.groupID = 0;
            cameraInfo6.isOnline = true;
            cameraInfo6.isPTZControl = false;
            cameraInfo6.name = "7号楼东北角";
            cameraInfo6.recordPos = Arrays.asList(1);
            cameraInfo6.userCapability = Arrays.asList(7);
            Object item6 = cameraInfo6;

            CameraInfo cameraInfo7= new CameraInfo();
            cameraInfo7.acsIP = "192.168.12.250";
            cameraInfo7.acsPort = 7302;
            cameraInfo7.cameraID = "20700f1176a9439baa0777ae3c5f2782";
            cameraInfo7.cameraType = 0;
            cameraInfo7.cascadeFlag = 0;
            cameraInfo7.channelNo = 1;
            cameraInfo7.collectedFlag = 0;
            cameraInfo7.deviceID = "136";
            cameraInfo7.groupID = 0;
            cameraInfo7.isOnline = true;
            cameraInfo7.isPTZControl = false;
            cameraInfo7.name = "8号搂东北角西";
            cameraInfo7.recordPos = Arrays.asList(1);
            cameraInfo7.userCapability = Arrays.asList(7);
            Object item7 = cameraInfo7;

            CameraInfo cameraInfo8= new CameraInfo();
            cameraInfo8.acsIP = "192.168.12.250";
            cameraInfo8.acsPort = 7302;
            cameraInfo8.cameraID = "a864ec80c736419ea4610ee54279b4f2";
            cameraInfo8.cameraType = 0;
            cameraInfo8.cascadeFlag = 0;
            cameraInfo8.channelNo = 1;
            cameraInfo8.collectedFlag = 0;
            cameraInfo8.deviceID = "138";
            cameraInfo8.groupID = 0;
            cameraInfo8.isOnline = true;
            cameraInfo8.isPTZControl = false;
            cameraInfo8.name = "6号搂西南角";
            cameraInfo8.recordPos = Arrays.asList(1);
            cameraInfo8.userCapability = Arrays.asList(7);
            Object item8 = cameraInfo8;

            CameraInfo cameraInfo9= new CameraInfo();
            cameraInfo9.acsIP = "192.168.12.250";
            cameraInfo9.acsPort = 7302;
            cameraInfo9.cameraID = "c8fe829293744b18a024e0723ef442a1";
            cameraInfo9.cameraType = 0;
            cameraInfo9.cascadeFlag = 0;
            cameraInfo9.channelNo = 1;
            cameraInfo9.collectedFlag = 0;
            cameraInfo9.deviceID = "164";
            cameraInfo9.groupID = 0;
            cameraInfo9.isOnline = true;
            cameraInfo9.isPTZControl = false;
            cameraInfo9.name = "东汽车坡道口4";
            cameraInfo9.recordPos = Arrays.asList(1);
            cameraInfo9.userCapability = Arrays.asList(7);
            Object item9 = cameraInfo9;

            CameraInfo cameraInfo10= new CameraInfo();
            cameraInfo10.acsIP = "192.168.12.250";
            cameraInfo10.acsPort = 7302;
            cameraInfo10.cameraID = "6c60c960627a413db20e88376220c987";
            cameraInfo10.cameraType = 0;
            cameraInfo10.cascadeFlag = 0;
            cameraInfo10.channelNo = 1;
            cameraInfo10.collectedFlag = 0;
            cameraInfo10.deviceID = "169";
            cameraInfo10.groupID = 0;
            cameraInfo10.isOnline = true;
            cameraInfo10.isPTZControl = false;
            cameraInfo10.name = "2号搂前";
            cameraInfo10.recordPos = Arrays.asList(1);
            cameraInfo10.userCapability = Arrays.asList(7);
            Object item10 = cameraInfo10;

            CameraInfo cameraInfo11= new CameraInfo();
            cameraInfo11.acsIP = "192.168.12.250";
            cameraInfo11.acsPort = 7302;
            cameraInfo11.cameraID = "fb120e2c73894dc68127db853cb2f2ae";
            cameraInfo11.cameraType = 0;
            cameraInfo11.cascadeFlag = 0;
            cameraInfo11.channelNo = 1;
            cameraInfo11.collectedFlag = 0;
            cameraInfo11.deviceID = "172";
            cameraInfo11.groupID = 0;
            cameraInfo11.isOnline = true;
            cameraInfo11.isPTZControl = false;
            cameraInfo11.name = "1号搂东";
            cameraInfo11.recordPos = Arrays.asList(1);
            cameraInfo11.userCapability = Arrays.asList(7);
            Object item11 = cameraInfo11;

            CameraInfo cameraInfo12= new CameraInfo();
            cameraInfo12.acsIP = "192.168.12.250";
            cameraInfo12.acsPort = 7302;
            cameraInfo12.cameraID = "3006fd3c2eb54e62a470a885b47afa2c";
            cameraInfo12.cameraType = 0;
            cameraInfo12.cascadeFlag = 0;
            cameraInfo12.channelNo = 1;
            cameraInfo12.collectedFlag = 0;
            cameraInfo12.deviceID = "291";
            cameraInfo12.groupID = 0;
            cameraInfo12.isOnline = true;
            cameraInfo12.isPTZControl = false;
            cameraInfo12.name = "自行车库东入口";
            cameraInfo12.recordPos = Arrays.asList(1);
            cameraInfo12.userCapability = Arrays.asList(7);
            Object item12 = cameraInfo12;

            CameraInfo cameraInfo13= new CameraInfo();
            cameraInfo13.acsIP = "192.168.12.250";
            cameraInfo13.acsPort = 7302;
            cameraInfo13.cameraID = "d46cd2dbd7d845b1a77fd0af28d78c36";
            cameraInfo13.cameraType = 0;
            cameraInfo13.cascadeFlag = 0;
            cameraInfo13.channelNo = 1;
            cameraInfo13.collectedFlag = 0;
            cameraInfo13.deviceID = "291";
            cameraInfo13.groupID = 0;
            cameraInfo13.isOnline = true;
            cameraInfo13.isPTZControl = false;
            cameraInfo13.name = "5号搂西1";
            cameraInfo13.recordPos = Arrays.asList(1);
            cameraInfo13.userCapability = Arrays.asList(7);
            Object item13 = cameraInfo13;

            OverlayOptions option = setMarkPop(item,"1号楼1大堂", new LatLng(29.907987533774435, 121.63044923421224));
            options.add(option);

            OverlayOptions option1 = setMarkPop(item1,"1号搂1电梯厅", new LatLng(29.906398715182124, 121.62879635207442));
            options.add(option1);

            OverlayOptions option2 = setMarkPop(item2,"1号搂地下一层门厅", new LatLng(29.90836321127564, 121.62944313204139));
            options.add(option2);

            OverlayOptions option3 = setMarkPop(item3,"3号搂1大堂",new LatLng(29.900865043701888,121.62673923245725));
            options.add(option3);

            OverlayOptions option4 = setMarkPop(item4,"3号搂1电梯厅",new LatLng(29.904003698876494,121.62469109589517));
            options.add(option4);

            OverlayOptions option5 = setMarkPop(item5,"3号楼b1门厅",new LatLng(29.902313064111098,121.62800584322589));
            options.add(option5);

            OverlayOptions option6 = setMarkPop(item6,"7号楼东北角",new LatLng(29.89421170373836,121.6234963495673));
            options.add(option6);

            OverlayOptions option7 = setMarkPop(item7,"8号搂东北角西",new LatLng(29.89170680066164,121.628850250405));
            options.add(option7);

            OverlayOptions option8 = setMarkPop(item8,"6号搂西南角",new LatLng(29.887205643147468,121.62391855315686));
            options.add(option8);

            OverlayOptions option9 = setMarkPop(item9,"东汽车坡道口4",new LatLng(29.888544269702408,121.62949703037197));
            options.add(option9);

            OverlayOptions option10 = setMarkPop(item10,"2号搂前",new LatLng(29.906680477982157,121.63764466134478));
            options.add(option10);

            OverlayOptions option11 = setMarkPop(item11,"1号搂东",new LatLng(29.901765166996967,121.64073483229808));
            options.add(option11);

            OverlayOptions option12 = setMarkPop(item12,"自行车库东入口", new LatLng(29.900411065358494,121.64307940967835));
            options.add(option12);

            OverlayOptions option13 = setMarkPop(item13,"5号搂西1",new LatLng(29.898383791740244,121.6465917842212));
            options.add(option13);
            mBaiduMap.addOverlays(options);
            Log.d(TAG,"显示的摄像头的个数"+data.size());
        } else {
            gotoNextLevelList(data.get(0));
        }
    }

    /**
     * @param info
     */
    protected void gotoNextLevelList(final Object info) {
        // 当前是监控点（摄像头）
        if (info instanceof CameraInfo) {
            gotoLiveOrPlayBack((CameraInfo) info);
            return;
        }

        // 当前是控制中心
        if (info instanceof ControlUnitInfo) {
            gotoNextLevelListFromCtrlUnit((ControlUnitInfo) info);
            return;
        }

        // 当前是区域
        if (info instanceof RegionInfo) {
            gotoNextLevelListFromRegion((RegionInfo) info);
            return;
        }
    }

    private void gotoLiveOrPlayBack(final CameraInfo info) {
        gotoLive(info);
    }

    /**
     * 从控制中心获取下级列表
     *
     * @param info
     */
    private void gotoNextLevelListFromCtrlUnit(ControlUnitInfo info) {
        pResType = MonitorConstant.Resource.TYPE_CTRL_UNIT;
        pCtrlUnitId = info.controlUnitID;
        reqResList();
    }

    /**
     * 从区域获取下级列表
     *
     * @param info
     */
    private void gotoNextLevelListFromRegion(RegionInfo info) {
        pResType = MonitorConstant.Resource.TYPE_REGION;
        pRegionId = info.regionID;
        reqResList();
    }

    /**
     * 进入实时预览
     *
     * @param info
     * @since V1.0
     */
    protected void gotoLive(CameraInfo info) {
        if (info == null) {
            Log.e(MonitorConstant.LOG_TAG, "gotoLive():: fail");
            return;
        }
        Intent it = new Intent(MapActualParkActivity.this,
                RealTimeZoneDetailsActivity.class);
        it.putExtra(MonitorConstant.IntentKey.CAMERA_ID, info.cameraID);
        TempData.getIns().setCameraInfo(info);
        startActivity(it);
    }

    /**
     * @throws
     * @Title: initData
     * @Description: TODO(初始化视频的相关参数)
     */
    private void initData() {
        servInfo = new ServInfo();
        // 登录平台地址
        servAddr = Config.getIns().getServerAddr();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        servAddr = Config.getIns().getServerAddr();
        super.onResume();
        users = db.findAll(MemberUser.class);
        if (users != null && users.size() != 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                userId = user.getMemberId();
            }
        }
        autoLogin();
    }

    /**
     * @throws
     * @Title: autoLogin
     * @Description: TODO(自动登录)
     */
    private void autoLogin() {
        if (!isFirstResume) {
            return;
        }
        isFirstResume = false;
//		if (Config.getIns().isAutoLogin()) {
//			login();
//		}
    }

    /**
     * @throws
     * @Title: fetchLine
     * @Description: TODO(获取视频线路)
     */
    protected void fetchLine() {
        if (servAddr.length() <= 0) {
            UIUtil.showToast(this, R.string.serveraddr_empty_tip);
            return;
        }
        new Thread() {
            public void run() {
                List<LineInfo> lineInfoList = new ArrayList<LineInfo>();
                Log.i(MonitorConstant.LOG_TAG, "servAddr:" + servAddr);
                boolean ret = VMSNetSDK.getInstance().getLineList(servAddr,
                        lineInfoList);
                if (ret) {
                    Message msg = new Message();
                    msg.what = MonitorConstant.Login.GET_LINE_SUCCESS;
                    msg.obj = lineInfoList;
                    msgHandler.sendMessage(msg);
                } else {
                    msgHandler
                            .sendEmptyMessage(MonitorConstant.Login.GET_LINE_FAILED);
                }
            }

            ;
        }.start();
    }

    /**
     * @throws
     * @Title: onGetResListFailed
     * @Description: TODO(调用接口失败时，界面弹出提示)
     */
    private void onGetResListFailed() {
        UIUtil.showToast(this,
                getString(R.string.fetch_reslist_failed, UIUtil.getErrorDesc()));
    }

    public OverlayOptions setMarkPop(Object item,String tagStr, LatLng point) {
        View view = LayoutInflater.from(this).inflate(R.layout.baidu_camera_marker,null);
        TextView mTvTitle = view.findViewById(R.id.tv_camera_msg);
        mTvTitle.setText(tagStr);
        BitmapDescriptor bitmap =BitmapDescriptorFactory.fromBitmap(mapUtil.getViewBitmap(view));
        Bundle bundle = new Bundle();
        bundle.putString(POP_TITLE,tagStr);
        liveMap.put(tagStr,item);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .title(tagStr)
                .extraInfo(bundle)
                .icon(bitmap);
        return option;
    }

    OnMarkClickListener onMarkerClickListener = new OnMarkClickListener() {
        @Override
        public void onMarkClick(Marker marker) {
            Bundle bundle = marker.getExtraInfo();
            String popTitle = bundle.getString(POP_TITLE);
            Toast.makeText(MapActualParkActivity.this, popTitle, Toast.LENGTH_SHORT).show();
            for (String key:liveMap.keySet()){
                Object value = liveMap.get(key);
                if (!TextUtils.isEmpty(userId)) {
                    if (key.equals(popTitle)&&!(value instanceof LineInfo)){
                        gotoNextLevelList(value);
                    }
                } else {
                    TipDialog.showDialogNoClose(MapActualParkActivity.this,
                            R.string.tip, R.string.confirm,
                            R.string.login_info_tip, LoginActivity.class);
                }
            }
        }
    };

}
