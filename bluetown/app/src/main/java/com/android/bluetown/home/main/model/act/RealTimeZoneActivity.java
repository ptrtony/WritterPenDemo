package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;
import java.util.List;
import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.ab.fragment.AbLoadDialogFragment;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.RealTimeZoneListAdapter;
import com.android.bluetown.adapter.RealtimeZoneTopAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listview.XListView;
import com.android.bluetown.listview.XListView.IXListViewListener;
import com.android.bluetown.monitor.callbacks.MsgCallback;
import com.android.bluetown.monitor.callbacks.MsgIds;
import com.android.bluetown.monitor.data.Config;
import com.android.bluetown.monitor.data.TempData;
import com.android.bluetown.monitor.resource.ResourceControl;
import com.android.bluetown.monitor.util.MonitorConstant;
import com.android.bluetown.monitor.util.UIUtil;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.VideoAccountResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.HorizontalListView;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.LineInfo;
import com.hikvision.vmsnetsdk.RegionInfo;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;

/**
 * 
 * @ClassName: RealTimeZoneActivity
 * @Description:TODO(HomeActivity-实时园区)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:30:40 ac_realtime_zone
 */
@SuppressLint("ResourceAsColor")
public class RealTimeZoneActivity extends TitleBarActivity implements
		IXListViewListener, MsgCallback {
	/*************** 监控的相关变量 **********************/
	/** 登录平台地址 */
	private String servAddr, username, password;
	/** 用户选中的线路 */
	private LineInfo lineInfo;
	/** 登录返回的数据 */
	private ServInfo servInfo;
	/** 是否是第一次执行onResume方法 */
	private boolean isFirstResume = true;
	/** 发送消息的对象 */
	private MsgHandler msgHandler = new MsgHandler();
	/************** 页面相关组件和变量 ****************/
	/*** 资源列表 */
	private XListView mListView;
	/*** 父节点资源类型，TYPE_UNKNOWN表示首次获取资源列表 */
	private int pResType = MonitorConstant.Resource.TYPE_UNKNOWN;
	/*** 父控制中心的id，只有当parentResType为TYPE_CTRL_UNIT才有用 */
	private int pCtrlUnitId;
	/*** 父区域的id，只有当parentResType为TYPE_REGION才有用 */
	private int pRegionId;
	/*** 资源列表适配器 */
	private RealTimeZoneListAdapter adapter;
	/*** 资源列表适配器(父区) */
	private RealtimeZoneTopAdapter adapter2;
	/*** 获取资源逻辑控制类 */
	private ResourceControl rc;
	private AbLoadDialogFragment mDialogFragment;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	private SharePrefUtils prefUtils;
	private HorizontalListView hListView;
	/*** 判断加载位置（0：区域，1：监控点） */
	private int type=0;

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
			default:
				break;
			}

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_realtime_zone);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		// 初始化视频监控的平台地址和用户名和密码
		getVideoAccount();


	}

	private void getVideoAccount() {
		params.put("gardenId",prefUtils.getString(SharePrefUtils.GARDEN_ID,""));
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
									RealTimeZoneActivity.this)
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
													RealTimeZoneActivity.this
															.finish();
													dialog.cancel();
												}
											}).show();

						} else {
							Toast.makeText(RealTimeZoneActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
						}

					}
				});
		

	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description:初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.real_time_zone);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(this);
		mDialogFragment = AbLoadDialogFragment.newInstance(1, 16973939);
		mDialogFragment.setIndeterminateDrawable(R.drawable.progress_circular);
		mDialogFragment.setMessage(getString(R.string.load_waitting));
		mDialogFragment.setTextColor(R.color.font_black);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setTransition(4097);
		mDialogFragment.show(ft, "");
		mListView = (XListView) findViewById(R.id.companyInfoList);
		hListView=(HorizontalListView)findViewById(R.id.listview);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mListView.setXListViewListener(this);

	}

	/**
	 * 
	 * @Title: onRefresh 上拉刷新
	 * @Description:
	 * @see com.android.bluetown.listview.XListView.IXListViewListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mListView.stopRefresh();

	}

	/**
	 * 
	 * @Title: onLoadMore 下拉加载
	 * @Description:
	 * @see com.android.bluetown.listview.XListView.IXListViewListener#onLoadMore()
	 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mListView.stopLoadMore();

	}

	/**
	 * 
	 * @Title: initData
	 * @Description: TODO(初始化视频的相关参数)
	 * @throws
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
	 * 
	 * @Title: autoLogin
	 * @Description: TODO(自动登录)
	 * @throws
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
	 * 
	 * @Title: fetchLine
	 * @Description: TODO(获取视频线路)
	 * @throws
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
			};
		}.start();
	}

	/**
	 * 
	 * @Title: login
	 * @Description: TODO(登录监控平台)
	 * @throws
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
	 * 
	 * @Title: onGetLineFailed
	 * @Description: TODO(获取线路失败)
	 * @throws
	 */
	public void onGetLineFailed() {
		UIUtil.showToast(RealTimeZoneActivity.this,
				getString(R.string.getline_fail_tip, UIUtil.getErrorDesc()));
	}

	/**
	 * 
	 * @Title: onGetLineSuccess
	 * @Description: TODO(获取线路成功，刷新线路列表)
	 * @param lineList
	 * @throws
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
	 * 
	 * @Title: onLoginFailed
	 * @Description: TODO(登录失败)
	 * @throws
	 */
	public void onLoginFailed() {
		UIUtil.showToast(this,
				getString(R.string.login_failed, UIUtil.getErrorDesc()));
	}

	/**
	 * 
	 * @Title: onLoginSuccess
	 * @Description: TODO(登录成功)
	 * @throws
	 */
	public void onLoginSuccess() {
		// UIUtil.showToast(this, R.string.login_suc_tip);
		getParentResTypeAndId();
		reqResList();
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
	 * 
	 * @Title: getParentResTypeAndId
	 * @Description: TODO(初始化父资源类型和父控制中心的Id)
	 * @throws
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
	 * 
	 * @Title: onGetResListFailed
	 * @Description: TODO(调用接口失败时，界面弹出提示)
	 * @throws
	 */
	private void onGetResListFailed() {
		UIUtil.showToast(this,
				getString(R.string.fetch_reslist_failed, UIUtil.getErrorDesc()));
	}

	/**
	 * 
	 * @Title: refreshResList
	 * @Description: TODO(获取数据成功后刷新列表)
	 * @param data
	 * @throws
	 */
	private void refreshResList(List<Object> data) {
		if (data == null || data.isEmpty()) {
			UIUtil.showToast(this, R.string.no_data_tip);
			return;
		}
		// UIUtil.showToast(this, R.string.fetch_resource_suc);	
		if(type==0){
			showFatherRegionList(data);
		}else{
			showRegionList(data);
		}
		
	}
	private void showFatherRegionList(final List<Object> data) {
		if (pResType != MonitorConstant.Resource.TYPE_CTRL_UNIT
				&& pResType != MonitorConstant.Resource.TYPE_UNKNOWN) {
			if (mDialogFragment != null) {
				mDialogFragment.dismiss();
			}	

			for(int i = 0; i < data.size(); i++){
				String desc = getItemDesc(data.get(i));
				if(desc.indexOf("_H")!=-1){
					data.remove(i);
					i--;
				}
			}

			adapter2 = new RealtimeZoneTopAdapter(this, data, false);
			hListView.setAdapter(adapter2);
			adapter2.notifyDataSetChanged();
			adapter2.setSeclection(0);
			gotoNextLevelList(data.get(0));
			type=1;
			hListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					// 实时园区
					adapter2.setSeclection(position);
					adapter2.notifyDataSetChanged();
					if (!TextUtils.isEmpty(userId)) {
						Object item = data.get(position);
						if (!(item instanceof LineInfo)) {
							gotoNextLevelList(item);
						}
					} else {
						TipDialog.showDialogNoClose(RealTimeZoneActivity.this,
								R.string.tip, R.string.confirm,
								R.string.login_info_tip, LoginActivity.class);
					}

				}
			});
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
			if (mDialogFragment != null) {
				mDialogFragment.dismiss();
			}
			adapter = new RealTimeZoneListAdapter(this, data, false);
			mListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					// 实时园区
					if (!TextUtils.isEmpty(userId)) {
						Object item = data.get(position - 1);
						if (!(item instanceof LineInfo)) {
							gotoNextLevelList(item);
						}
					} else {
						TipDialog.showDialogNoClose(RealTimeZoneActivity.this,
								R.string.tip, R.string.confirm,
								R.string.login_info_tip, LoginActivity.class);
					}

				}
			});
		} else {
			gotoNextLevelList(data.get(0));
		}
	}

	/**
	 * 
	 * @Title: reqResList
	 * @Description: TODO(请求资源列表)
	 * @throws
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
	 * 
	 * 
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
		Intent it = new Intent(RealTimeZoneActivity.this,
				RealTimeZoneDetailsActivity.class);
		it.putExtra(MonitorConstant.IntentKey.CAMERA_ID, info.cameraID);
		TempData.getIns().setCameraInfo(info);
		startActivity(it);
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
}
