package com.android.bluetown.app;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import java.io.File;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.litepal.LitePalApplication;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.text.Spanned;
import android.util.Log;

import com.android.bluetown.BuildConfig;
import com.android.bluetown.R;
import com.android.bluetown.bean.MerchantDiscountBean;
import com.android.bluetown.bean.RecommendDish;
import com.android.bluetown.bean.SeleteDish;
import com.android.bluetown.listener.ScreenObserver;
import com.android.bluetown.listener.ScreenObserver.ScreenStateListener;
import com.android.bluetown.multiphoto.MediaLoader;
import com.android.bluetown.mywallet.activity.GesturePSWCheckActivity;
import com.android.bluetown.mywallet.activity.RechargeActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.receiver.RongCloudEvent;
import com.android.bluetown.utils.CrashHandler;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class BlueTownApp extends LitePalApplication {
	private static final String TAG = "BlueTownApp";
	/**
	 * 用于存放倒计时时间
	 */
	public static Map<String, Long> map;
	public static BlueTownApp ins;
	public static boolean TURN_ON = false;
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/BlueCity/";
	public static String IMAGE_PATH = "/BlueCity/";
	public static String CANAPPOINT = "canapponitdate";
	/**
	 * 好友验证消息未读消息的状态
	 */
	public static boolean isScanUnReadMsg;
	/**
	 * 好友验证消息未读消息数(交友)
	 */
	public static int unReadMsgCount;
	/**
	 * 活动中心消息未读消息数
	 */
	public static int actionMsgCount;
	/**
	 * 跳蚤市场消息未读消息数
	 */
	public static int fleaMarketMsgCount;
	public static List<RecommendDish> dishList;
	public static List<SeleteDish> orderDishList;
	public static  List<MerchantDiscountBean> discountBeenList;
	public static int dishesCount = 0;
	public static String dishesPrice = "0.00";
	public static String originalPrice = "0.00";
	public static Spanned discountRemind;
	/**
	 * 订菜类型（orderType订单类型（0只订座，1订座订菜）默认订座订菜）
	 */
	public static String orderType = "1";
	/**
	 * 点菜类型（其他地方点菜还是继续点菜）
	 */
	public static String ORDER_TYPE = "";
	/**
	 * 从美食中点菜还是食堂点菜
	 */
	public static String DISH_TYPE = "food";
	/**
	 * 点菜的顺序（先订座在点菜-即详情预定-提前点菜，为“0”，到了等吃-点菜；先点菜后订座-即详情点菜为“1”）
	 */
	public static String ORDER_BY = "1";
	/**
	 * 商家详情-（预定（先订座不一定点菜--TableBookActivity--下一步）（点菜或食堂、美食推荐点菜（先点菜后订座--
	 * TableBookActivity--完成）,默认点菜为1，预订为0）
	 */
	public static String BOOK_OR_ORDER = "1";
	public static String SHARE_CONTENT = "";
	public static String SHARE_IMAGE = "";
	public static String SHARE_TITLE = "";
	/**
	 * 支付成功跳转相应--默认“”跳转商家详情，1跳转订单列表
	 */
	public static String ACTIVITY_ACTION = "";
	private static Handler hanler = null;
	
	private ImageLoader imageLoader = null;
	// 是否出于前台
	private boolean isForeground = true;
	// 是否出于后台
	private boolean isBackground = false;

	private ScreenObserver mScreenObserver;
	
	private SharePrefUtils prefUtils;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
		Log.d(TAG,"attachBaseContext first run");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG,"onCreate second run");
		prefUtils = new SharePrefUtils(this);
		ins = this;
		dishList = new ArrayList<RecommendDish>();
		discountBeenList = new ArrayList<>();
		new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
				if (!"generic".equalsIgnoreCase(Build.BRAND)) {
					SDKInitializer.initialize(BlueTownApp.this);
				}
				// 初始化海康视频SDK
				// 初始化极光推送
				// 设置调试模式
				JPushInterface.setDebugMode(true);
				JPushInterface.init(BlueTownApp.this);
				//初始化多选图片
				Album.initialize(AlbumConfig.newBuilder(BlueTownApp.this).setAlbumLoader(new MediaLoader()).build());

				//如果是调试的apk，就初始化
				if(BuildConfig.DEBUG){
					CrashHandler.getInstance().init(BlueTownApp.this);
				}
				handleSSLHandshake();
//		BlockCanary.install(this, new AppBlockCanaryContext()).start();

				/**
				 * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
				 * io.rong.push 为融云 push 进程名称，不可修改。
				 */
				if (getApplicationInfo().packageName
						.equals(getCurProcessName(getApplicationContext()))) {
					/**
					 * IMKit SDK调用第一步 初始化
					 */
					RongIM.init(BlueTownApp.this);
					/**
					 * 融云SDK事件监听处理
					 */
					RongCloudEvent.init(BlueTownApp.this);
				}

				// 初始化加载图片的ImageLoader
				initImageLoader(BlueTownApp.this);

				mScreenObserver = new ScreenObserver(BlueTownApp.this);
				mScreenObserver.requestScreenStateUpdate(new ScreenStateListener() {
					@Override
					public void onScreenOn() {
						// 屏幕的解锁操作
					}

					@Override
					public void onScreenOff() {
						// 屏幕上锁操作
						doSomethingOnScreenOff();
					}
				});
				Looper.loop();
			}
		}.start();
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				while (true) {
//					isBackground = !isAppOnForeground();
//					if (isForeground) {
//						doSomethingOnScreenOff();
//						isForeground = false;
//					}
//				}
//			}
//		}).start();

	}


	public static DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			// 设置下载的图片是否缓存在内存中
			.cacheInMemory(true)
			// 设置下载的图片是否缓存在SD卡中
			.cacheOnDisc(true)
			// 设置图片的解码类型
			.bitmapConfig(Bitmap.Config.RGB_565)
			// 设置图片的质量(图片以如何的编码方式显示 ),其中，imageScaleType的选择值:
			// EXACTLY :图像将完全按比例缩小的目标大小
			// EXACTLY_STRETCHED:图片会缩放到目标大小完全
			// IN_SAMPLE_INT:图像将被二次采样的整数倍
			// IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
			// NONE:图片不会调整
			.imageScaleType(ImageScaleType.EXACTLY)
			.showStubImage(R.drawable.ic_msg_empty)
			.showImageForEmptyUri(R.drawable.ic_msg_empty)
			.showImageOnFail(R.drawable.ic_msg_empty)
			// 载入图片前稍做延时可以提高整体滑动的流畅度
			.delayBeforeLoading(100).build();

	public void initImageLoader(Context context) {
		// 设置缓存的目录
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, SDPATH);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(480, 800)
				// max width, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5
				// 加密
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You
				// can
				// pass
				// your
				// own
				// memory
				// cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				// 内存缓存的最大值
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb sd卡(本地)缓存的最大值
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 将内存缓存到内存中
				.memoryCache(new WeakMemoryCache())
				// 线程池内加载的数量
				.defaultDisplayImageOptions(defaultOptions)
				// 自定义缓存目录
				.discCache(new UnlimitedDiskCache(cacheDir))
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileCount(100)
				// 解决加载图片时java.io.FileNotFoundException:
				// eg:http://112.64.173.178/LETU/SJYS_IMG/异常
				.imageDownloader(new BaseImageDownloader(context, 10000, 10000))
				.writeDebugLogs().build();
		// 全局初始化
		ImageLoader.getInstance().init(config);

	}

	public static BlueTownApp getInstance() {
		if (ins == null) {
			ins = new BlueTownApp();
		}

		return ins;
	}
	private void doSomethingOnScreenOff() {

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String className = GesturePSWCheckActivity.class.getName();		
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);	
		if (tasksInfo.size() > 0) {
			// 应用程序位于堆栈的顶层
			if (!className.equals(tasksInfo.get(0).topActivity.getClassName())&&tasksInfo.get(0).topActivity.getClassName().indexOf("mywallet")!=-1&&
					!RechargeActivity.class.getName().equals(tasksInfo.get(0).topActivity.getClassName())) {
				if (!(prefUtils.getString(SharePrefUtils.HANDPASSWORD, "").equals("")
						|| prefUtils.getString(SharePrefUtils.HANDPASSWORD, "") == null
						|| prefUtils.getString(SharePrefUtils.HANDPASSWORD, "").equals(
								"null"))) {
					Intent intent = new Intent(this, GesturePSWCheckActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("type", 2);
					startActivity(intent);
				}

			}
		}

	}

	/**
	 * 忽略SSL证书
	 */
	@SuppressLint("TrulyRandom")
	public static void handleSSLHandshake() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}};

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
		} catch (Exception ignored) {
		}
	}


	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			// 应用程序位于堆栈的顶层
			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				if (isBackground) {
					isForeground = true;
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 获得当前进程的名字
	 * 
	 * @param context
	 * @return 进程号
	 */
	public static String getCurProcessName(Context context) {

		int pid = android.os.Process.myPid();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {

			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	public static List<RecommendDish> getDishList() {
		return dishList;
	}

	public static void setDishList(List<RecommendDish> dishList) {
		BlueTownApp.dishList = dishList;
	}

	public static List<SeleteDish> getOrderDishList() {
		return orderDishList;
	}

	public static void setOrderDishList(List<SeleteDish> orderDishList) {
		BlueTownApp.orderDishList = orderDishList;
	}

	public static Handler getHanler() {
		return hanler;
	}

	public static void setHanler(Handler hanler) {
		BlueTownApp.hanler = hanler;
	}

	public static int getDishesCount() {
		return dishesCount;
	}

	public static void setDishesCount(int dishesCount) {
		BlueTownApp.dishesCount = dishesCount;
	}

	public static String getDishesPrice() {
		return dishesPrice;
	}

	public static void setDishesPrice(String dishesPrice) {
		BlueTownApp.dishesPrice = dishesPrice;
	}

	public static String getOriginalPrice() {
		return originalPrice;
	}

	public static void setOriginalPrice(String originalPrice) {
		BlueTownApp.originalPrice = originalPrice;
	}

	public static void setOrderDiscountList(List<MerchantDiscountBean> discountBeenList){
		BlueTownApp.discountBeenList = discountBeenList;
	}

	public static List<MerchantDiscountBean> getOrderDiscountList(){
		return discountBeenList;
	}

	public static void setDiscountRemind(Spanned spanned){
		BlueTownApp.discountRemind = spanned;
	}
	public static Spanned getDiscountRemind(){
		return discountRemind;
	}
}
