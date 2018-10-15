package com.android.bluetown.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dafen on 2018/6/21.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {

        if (crashHandler == null) {
            synchronized (CrashHandler.class) {
                crashHandler = new CrashHandler();
            }
        }
        return crashHandler;

    }
    // 系统默认的UncaughtException处理类实例
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler静态实例
    private static CrashHandler crashHandler;
    // 程序的Context对象
    private Context mContext;
    //时间输出格式化
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {

    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 调用Thread.setDefaultUncaughtExceptionHandler(this);后，this实现了
     * UncaughtExceptionHandler，所以crash会进入到这里的回调。
     * 当UncaughtException发生时会转入该重写的方法来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (thread == null || ex == null || mDefaultHandler == null) {
            System.exit(0);
            return;
        }

        File savePathFile = getLogFilePath(mContext);

        if (savePathFile == null) {
            //抛出异常
            mDefaultHandler.uncaughtException(thread, ex);
            return;
        }

        String logMessage = String
                .format("CustomUncaughtExceptionHandler.uncaughtException: Thread %d Message %s",
                        thread.getId(), ex.getMessage());
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(new FileWriter(savePathFile, true));
            logMessage = String.format("%s\r\n\r\n%s\r\n\r\nThread: %d\r\n\r\nMessage:\r\n\r\n%s\r\n\r\nStack Trace:\r\n\r\n%s",
            geDeviceInfo(mContext), new Date(),
                    thread.getId(), ex.getMessage(),
                    android.util.Log.getStackTraceString(ex));
            printWriter.print(logMessage);
            printWriter.print("\n\n-------------------------------------------------\n\n");
        } catch (Throwable tr2) {
            //最后回抛出异常，所以这里就可以不用处理了。
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
        mDefaultHandler.uncaughtException(thread, ex);
    }

    /**
     *
     * 获取设备信息
     *
     * @param context
     * @return String  设备信息
     */
    public String geDeviceInfo(Context ctx)
    {
        StringBuffer deviceInfo = new StringBuffer();

        deviceInfo.append(android.os.Build.MODEL).append("/");

        deviceInfo.append(android.os.Build.VERSION.SDK).append("/");

        deviceInfo.append(getVersionName(ctx));

        return deviceInfo.toString();
    }

    /**
     *
     * 获取应用版本号
     *
     * @param context
     * @return String  应用版本号
     */
    public String getVersionName(Context context) {
        String version = "";
        if(context == null) {
            return version;
        }
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            if(packageManager != null) {
                PackageInfo packInfo;
                packInfo = packageManager.getPackageInfo(context.getPackageName(),
                        0);
                if(packInfo != null) {
                    version = packInfo.versionName;
                }
            }
        } catch (Exception e) {
        }
        return version;
    }

    /**
     *
     * 获取日志保存路径
     *
     * @param context
     * @return String  日志保存路径
     */
    public File getLogFilePath(Context ctx) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        String pathName = Environment.getExternalStorageDirectory().getPath()
                + "/Android/data/" + ctx.getPackageName()
                + "/files/error/"
                + android.os.Build.MODEL + "_"
                + getVersionName(ctx) + simpleDateFormat.format(new Date()) +".log";

        File path = new File(pathName);
        if (!path.getParentFile().exists()) {
            path.getParentFile().mkdirs();
        }

        return path;
    }

}
