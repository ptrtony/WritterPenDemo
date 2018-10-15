package com.android.bluetown.monitor.live;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.bluetown.R;
import com.android.bluetown.monitor.data.Config;
import com.android.bluetown.monitor.data.TempData;
import com.android.bluetown.monitor.util.DebugLog;
import com.hik.mcrsdk.util.CLog;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.DeviceInfo;
import com.hikvision.vmsnetsdk.RealPlayURL;
import com.hikvision.vmsnetsdk.VMSNetSDK;

/**
 * 预览
 * 
 * @author huangweifeng
 * @Data 2013-10-21
 */
public class LiveActivity extends Activity implements Callback, LiveCallBack {
	private static final String TAG = "LiveActivity";
	/*** 码流类型 */
	private int mStreamType = -1;
	/*** 通过VMSNetSDK返回的预览地址对象 */
	private RealPlayURL mRealPlayURL;
	/*** 登录设备的用户名 */
	private String mName;
	/*** 登录设备的密码 */
	private String mPassword;
	/*** 控制层对象 */
	private LiveControl mLiveControl;
	/*** 播放视频的控件对象 */
	private SurfaceView mSurfaceView;
	/*** 创建取流等待bar */
	private ProgressBar mProgressBar;
	/*** 创建消息对象 */
	private Handler mMessageHandler = new MyHandler();
	/*** 播放流量 */
	private long mStreamRate = 0;
	/*** 监控点信息对象 */
	private CameraInfo cameraInfo;
	private String mDeviceID = "";
	private VMSNetSDK mVmsNetSDK = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live);
		initData();
		initUI();

	}

	/**
	 * 初始化网络库和控制层对象
	 * 
	 * @since V1.0
	 */
	private void initData() {
		mRealPlayURL = new RealPlayURL();
		mLiveControl = new LiveControl();
		mLiveControl.setLiveCallBack(this);
		cameraInfo = TempData.getIns().getCameraInfo();
		mDeviceID = cameraInfo.deviceID;
		mVmsNetSDK = VMSNetSDK.getInstance();
		DeviceInfo deviceInfo = new DeviceInfo();
		if (mVmsNetSDK == null) {
			CLog.e(TAG, "mVmsNetSDK is null");
			return;
		}
		boolean ret = mVmsNetSDK.getDeviceInfo(Config.getIns().getServerAddr(), TempData.getIns().getLoginData().sessionID, mDeviceID, deviceInfo);
		if (ret && deviceInfo != null) {
			mName = deviceInfo.userName;
			mPassword = deviceInfo.password;
		} else {

		}

		DebugLog.info(TAG, "mName is " + mName + "---" + mPassword + "-----" + cameraInfo.deviceID);

		// ------------------------------------------------------

	}

	/**
	 * 初始化控件
	 * 
	 * @since V1.0
	 */
	private void initUI() {
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		mSurfaceView.getHolder().addCallback(this);
		mProgressBar = (ProgressBar) findViewById(R.id.liveProgressBar);
		mStreamType = ConstantLive.MAIN_STREAM;
		mProgressBar.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				super.run();
				mLiveControl.setLiveParams(getPlayUrl(mStreamType), mName, mPassword);
				if (mLiveControl.LIVE_PLAY == mLiveControl.getLiveState()) {
					mLiveControl.stop();
				}

				if (mLiveControl.LIVE_INIT == mLiveControl.getLiveState()) {
					mLiveControl.startLive(mSurfaceView);
				}
			}
		}.start();
	}

	/**
	 * 该方法是获取播放地址的，当mStreamType=2时，获取的是MAG，当mStreamType =1时获取的子码流，当mStreamType =
	 * 0时获取的是主码流 由于该方法中部分参数是监控点的属性，所以需要先获取监控点信息，具体获取监控点信息的方法见resourceActivity。
	 * 
	 * @param streamType
	 *            2、表示MAG取流方式；1、表示子码流取流方式；0、表示主码流取流方式；
	 * @return String 播放地址 ：2、表示返回的是MAG的播放地址;1、表示返回的是子码流的播放地址；0、表示返回的是主码流的播放地址。
	 * @since V1.0
	 */
	private String getPlayUrl(int streamType) {
		String url = "";
		// 登录平台地址
		String mAddress = Config.getIns().getServerAddr();
		// 登录返回的sessiond
		String mSessionID = TempData.getIns().getLoginData().sessionID;
		if (cameraInfo == null) {
			DebugLog.error(TAG, "getPlayUrl():: cameraInfo is null");
			return url;
		}
		if (streamType == 2) {
			// TODO 原有代码streamType传0
			VMSNetSDK.getInstance().getRealPlayURL(mAddress, mSessionID, cameraInfo.cameraID, streamType, mRealPlayURL);
			if (null == mRealPlayURL) {
				DebugLog.info(TAG, "getPlayUrl():: mRealPlayURL is null");
				return "";
			}
			// MAG地址
			url = mRealPlayURL.url2;
			DebugLog.info(TAG, "getPlayUrl():: url is " + url);
		} else {
			VMSNetSDK.getInstance().getRealPlayURL(mAddress, mSessionID, cameraInfo.cameraID, streamType, mRealPlayURL);
			if (null == mRealPlayURL) {
				DebugLog.info(TAG, "getPlayUrl():: mRealPlayURL is null");
				return "";
			}
			// mRealPlayURL.url1 是主码流还是子码流取决于 streamType，见上面注释
			url = mRealPlayURL.url1;
			DebugLog.info(TAG, "getPlayUrl():: url is " + url);
		}
		DeviceInfo deviceInfo = new DeviceInfo();
		boolean ret = VMSNetSDK.getInstance().getDeviceInfo(mAddress, mSessionID, cameraInfo.deviceID, deviceInfo);
		if (ret && deviceInfo != null) {
			mName = deviceInfo.userName;
			mPassword = deviceInfo.password;
		} else {
			DebugLog.error(TAG, "getPlayUrl():: deviceInfo is error");
		}
		return url;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (null != mLiveControl) {
			mLiveControl.stop();
		}
	}

	@Override
	public void onMessageCallback(int messageID) {
		sendMessageCase(messageID);
	}

	/**
	 * 返回已经播放的流量 void
	 * 
	 * @return long
	 * @since V1.0
	 */
	public long getStreamRate() {
		return mStreamRate;
	}

	/**
	 * 发送消息
	 * 
	 * @param i
	 *            void
	 * @since V1.0
	 */
	private void sendMessageCase(int i) {
		if (null != mMessageHandler) {
			Message msg = Message.obtain();
			msg.arg1 = i;
			mMessageHandler.sendMessage(msg);
		}
	}

	/**
	 * 消息类
	 * 
	 * @author huangweifeng
	 * @Data 2013-10-23
	 */
	@SuppressLint("HandlerLeak")
	private final class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case ConstantLive.RTSP_SUCCESS:
//				UIUtil.showToast(LiveActivity.this, "启动取流成功");
				break;

			case ConstantLive.STOP_SUCCESS:
//				UIUtil.showToast(LiveActivity.this, "停止成功");
				break;

			case ConstantLive.START_OPEN_FAILED:
//				UIUtil.showToast(LiveActivity.this, "开启播放库失败");
				if (null != mProgressBar) {
					mProgressBar.setVisibility(View.GONE);
				}
				break;

			case ConstantLive.PLAY_DISPLAY_SUCCESS:
//				UIUtil.showToast(LiveActivity.this, "播放成功");
				if (null != mProgressBar) {
					mProgressBar.setVisibility(View.GONE);
				}
				break;

			case ConstantLive.RTSP_FAIL:
//				UIUtil.showToast(LiveActivity.this, "RTSP链接失败");
				if (null != mProgressBar) {
					mProgressBar.setVisibility(View.GONE);
				}
				if (null != mLiveControl) {
					mLiveControl.stop();
				}
				break;
			}
		}
	}
}
