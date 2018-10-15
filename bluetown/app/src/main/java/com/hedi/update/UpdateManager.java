package com.hedi.update;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.tsz.afinal.FinalDb;
import com.ab.http.AbHttpUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.MainActivity;
import com.android.bluetown.R;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.my.AuthenticationActivity;
import com.android.bluetown.my.AuthenticationIngActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

public class UpdateManager {
    private static final String TAG = "UpdateManager";
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    private static final int TO_MAIN_ACTIVITY = 3;
    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    private Thread newThread;
    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;
    private SharePrefUtils prefUtils;
    public static int Code;
    private FinalDb db;
    private String userId;
    private Handler handler;
    private AbHttpUtil httpUtil = null;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Activity activity;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                case TO_MAIN_ACTIVITY:
//                    mContext.startActivity(new Intent((WelcomeActivity)mContext,MainActivity.class));
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context) {
        this.mContext = context;
        activity = (Activity) mContext;
        prefUtils = new SharePrefUtils(mContext);
        httpUtil = AbHttpUtil.getInstance(mContext);
        db = FinalDb.create(mContext);
        httpUtil.setTimeout(10000);
        httpUtil.setEasySSLEnabled(true);
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        activity.registerReceiver(myNetReceiver, mFilter);

    }

    public void removeReceiver() {
        if (null != myNetReceiver) {
            activity.unregisterReceiver(myNetReceiver);
        }

    }

    /**
     * 检测软件更新
     */
    public void checkUpdate(ToTranslateActivityListener listener) {
        this.listener = listener;
        newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                isUpdate();
            }
        });
        newThread.start();

        handler = new Handler() {
            @Override
            // 当有消息发送出来的时候就执行Handler的这个方法
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                showNoticeDialog();
            }
        };

    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private void isUpdate() {
        // 获取当前软件版本
        int versionCode = getVersionCode(mContext);
        // 把version.xml放到网络上，然后获取文件信息
        Log.d(TAG, "加载数据前6" + getSystemCurrentTime());
        // 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
        ParseXmlService service = new ParseXmlService();
        URL url = null;
        try {
            url = new URL(Constant.HOST_URL
                    + Constant.Interface.MobiCollectAction_downloadXml);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        try {
            conn.connect();
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            //Toast.makeText(mContext, "网络连接超时，请稍后重试", Toast.LENGTH_SHORT).show();
        }
        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            mHashMap = service.parseXml(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != mHashMap) {
            prefUtils.setString(SharePrefUtils.APPOINT_CARNUM,
                    mHashMap.get("appointcarnum"));
            prefUtils.setString(SharePrefUtils.APPOINT_INFO,
                    mHashMap.get("appointinfo"));
            prefUtils.setString(SharePrefUtils.PARKING_MONTH_INFO,
                    mHashMap.get("parkingmonthinfo"));
            prefUtils.setString(SharePrefUtils.CHECK_MEMBER,
                    mHashMap.get("checkmember"));
            prefUtils
                    .setString(SharePrefUtils.HEADIMG, mHashMap.get("headImg"));
            Intent intent = new Intent();
            intent.setAction("com.android.headimg");
            mContext.sendBroadcast(intent);
            int serviceCode = Integer.valueOf(mHashMap.get("version"));
            // 版本判断
            if (serviceCode > versionCode) {
                Code = 1;
                handler.sendEmptyMessage(0);
            } else {
                if (listener != null) {
                    listener.onTranslateListener();
                }
            }
//            else {
//                List<M  emberUser> users = db.findAll(MemberUser.class);
//                if (users != null && users.size() != 0) {
//                    MemberUser user = users.get(0);
//                    if (user != null) {
//                        userId = user.getMemberId();
//                    }
//                }
//                if (!TextUtils.isEmpty(userId)) {
//                    String user_phone = users.get(0).getUsername();
//                    String user_pswd = users.get(0).getPassword();
//                    login(user_phone, user_pswd);
//                } else {
//                    Intent intent2 = new Intent(mContext,
//                            LoginActivity.class);
//                    mContext.startActivity(intent2);
//                    Activity a = (Activity) mContext;
//                    a.finish();
//                }
//            }
        } else {
            Intent intent2 = new Intent(mContext,
                    LoginActivity.class);
            mContext.startActivity(intent2);
            Activity a = (Activity) mContext;
            a.finish();
        }

    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(
                    "com.android.bluetown", 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(mHashMap.get("updateinfo"));
        builder.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    if (mHashMap.get("forceupdate").equals("0")) {
                        System.exit(0);
                    } else {
                        if (listener != null) {
                            listener.onTranslateListener();
                        }
                    }
//
//                    else {
//                        List<MemberUser> users = db.findAll(MemberUser.class);
//                        if (users != null && users.size() != 0) {
//                            MemberUser user = users.get(0);
//                            if (user != null) {
//                                userId = user.getMemberId();
//                            }
//                        }
//                        if (!TextUtils.isEmpty(userId)) {
//                            String user_phone = users.get(0).getUsername();
//                            String user_pswd = users.get(0).getPassword();
//                            login(user_phone, user_pswd);
//                        } else {
//                            Intent intent = new Intent(mContext,
//                                    LoginActivity.class);
//                            mContext.startActivity(intent);
//                            Activity a = (Activity) mContext;
//                            a.finish();
//                        }
//                    }
                }
                return false;
            }
        });
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 显示下载对话框
                        showDownloadDialog();
                    }
                });
        // 稍后更新
        if (!mHashMap.get("forceupdate").equals("0")) {
            builder.setNegativeButton(R.string.soft_update_later,
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (listener != null) {
                                listener.onTranslateListener();
                            }
//                            List<MemberUser> users = db.findAll(MemberUser.class);
//                            if (users != null && users.size() != 0) {
//                                MemberUser user = users.get(0);
//                                if (user != null) {
//                                    userId = user.getMemberId();
//                                }
//                            }
//                            if (!TextUtils.isEmpty(userId)) {
//                                String user_phone = users.get(0).getUsername();
//                                String user_pswd = users.get(0).getPassword();
//                                login(user_phone, user_pswd);
//                            } else {
//                                Intent intent = new Intent(mContext,
//                                        LoginActivity.class);
//                                mContext.startActivity(intent);
//                                Activity a = (Activity) mContext;
//                                a.finish();
//                            }
                        }
                    });
        }

        Dialog noticeDialog = builder.create();
        noticeDialog.setCancelable(true);
//        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        builder.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    if (mHashMap.get("forceupdate").equals("0")) {
                        System.exit(0);
                    } else {
                        dialog.dismiss();
                    }
                }
                return false;
            }
        });
        // 取消更新
        builder.setNegativeButton(R.string.soft_update_cancel,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 设置取消状态
                        if (mHashMap.get("forceupdate").equals("0")) {
                            cancelUpdate = true;
                            System.exit(0);
                        } else {
                            dialog.dismiss();
                            cancelUpdate = true;
                        }

                    }
                });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        mDownloadDialog.setCancelable(true);
//        mDownloadDialog.setCanceledOnTouchOutside(false);
        // 现在文件

        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory()
                            + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(Constant.HOST_URL + mHashMap.get("url"));
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestProperty("Accept-Encoding", "identity");
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, mHashMap.get("appName"));
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    ;

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, mHashMap.get("appName"));
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {

                } else {
                    // 网络断开
                    List<MemberUser> users = db.findAll(MemberUser.class);
                    if (users != null && users.size() != 0) {
                        MemberUser user = users.get(0);
                        if (user != null) {
                            userId = user.getMemberId();
                        }
                    }
                    if ("1".equals(prefUtils.getString(
                            SharePrefUtils.CHECKSTATE, ""))) {
                        TipDialog.showDialogStartNewActivity(
                                activity, R.string.tip,
                                R.string.confirm, R.string.net_no,
                                MainActivity.class);
                    } else {
                        if ("3".equals(prefUtils.getString(
                                SharePrefUtils.CHECKSTATE, ""))) {
                            TipDialog.showDialogStartNewActivity(
                                    activity, R.string.tip,
                                    R.string.confirm, R.string.net_no,
                                    AuthenticationIngActivity.class);
                        } else if ("1".equals(prefUtils.getString(
                                SharePrefUtils.CHECKSTATE, ""))) {
                        } else if ("2".equals(prefUtils.getString(
                                SharePrefUtils.CHECKSTATE, ""))) {
                            TipDialog.showDialogStartNewActivity(
                                    activity, R.string.tip,
                                    R.string.confirm, R.string.net_no,
                                    AuthenticationActivity.class);
                        } else {
                            TipDialog.showDialogStartNewActivity(
                                    activity, R.string.tip,
                                    R.string.confirm, R.string.net_no,
                                    LoginActivity.class);
                        }
                    }

                }
            }

        }
    };

    public String getSystemCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    private ToTranslateActivityListener listener;

    public interface ToTranslateActivityListener {
        void onTranslateListener();
    }
}
