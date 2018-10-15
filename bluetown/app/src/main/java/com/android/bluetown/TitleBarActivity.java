package com.android.bluetown;

import java.lang.reflect.Field;
import java.util.List;
import net.tsz.afinal.FinalBitmap;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ab.http.AbRequestParams;
import com.ab.view.ioc.AbIocEventListener;
import com.ab.view.ioc.AbIocSelect;
import com.ab.view.ioc.AbIocView;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.mywallet.activity.GesturePSWCheckActivity;
import com.android.bluetown.network.AbHttpUtil;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.sortlistview.ClearEditText;
import com.android.bluetown.utils.StatusBarUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * @ClassName: TitleBarActivity
 * @Description:TODO(应用的标题栏)
 * @author: shenyz
 * @date: 2015年7月20日 下午3:12:53
 */
public abstract class TitleBarActivity extends AppCompatActivity {
    /**
     * 标题栏 标题TextView 左边TextView 右边TextView
     */
    protected TextView titleTextView, leftTextView, righTextView;
    /**
     * 标题栏中的搜索的AutoCompleteTextView
     */
    protected AutoCompleteTextView searchView;
    /**
     * 标题中自定义搜索框
     */
    protected ClearEditText mClearEditText;
    /**
     * 标题栏 左边ImageView 右边ImageView
     */
    protected ImageView leftImageView, rightImageView;
    /**
     * 标题栏 左边的TextView LinearLayout 左边的ImageView LinearLayout 右边的TextView
     * LinearLayout 右边的ImageView LinearLayout 标题返回键LinearLayout
     */
    protected LinearLayout leftTextLayout, leftImageLayout, righTextLayout,
            rightImageLayout, backImageLayout;
    /**
     * activity的body LinearLayout
     */
    protected LinearLayout mainContainerLy;
    /**
     * title LinearLayout
     */
    protected LinearLayout titleLayout;
    /**
     * title bottom line
     */
    protected TextView bottomLine;
    protected LinearLayout searchBtnLy;
    /**
     * 加载图片的FinalBitmap类
     */
    protected FinalBitmap finalBitmap;
    protected ImageLoader imageLoader;
    // Http请求
    protected AbHttpUtil httpInstance;
    // Http请求参数
    protected AbRequestParams params;
    //http请求 验证认证
    protected AbHttpUtil httpUtil;
    //http 请求参数
    protected AbRequestParams checkParams;
    public static boolean isActive;

    private SharePrefUtils prefUtils;

    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;
    protected boolean isTrans=true;

    /**
     * register network state
     */
    private void registerNetWorkRecevicer() {
        // TODO Auto-generated method stub
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_title_ly);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtils.getInstance().initStatusBar(true, this);
        }
        //初始化ButterKnife
        //ButterKnife.bind(this);
        prefUtils = new SharePrefUtils(this);
        initViews();
        initTitle();
        initFinalBitmapImageLoader();
        initImageLoader();
        initHttpRequest();

    }
    public void setTranslationActivity(boolean isTranslation){
        this.isTrans = isTranslation;
    }
    /**
     * @throws
     * @Title: initImageLoader
     * @Description: TODO(初始化加载图片的类)
     */
    private void initFinalBitmapImageLoader() {
        if (finalBitmap == null) {
            finalBitmap = FinalBitmap.create(this);
        }
        finalBitmap.configBitmapLoadThreadSize(3);// 定义线程数量
        finalBitmap.configDiskCachePath(this.getApplicationContext()
                .getFilesDir().toString());// 设置缓存目录；
        finalBitmap.configDiskCacheSize(1024 * 1024 * 50);// 设置缓存大小
        finalBitmap.configLoadingImage(R.drawable.ic_msg_empty);
        finalBitmap.configLoadfailImage(R.drawable.ic_msg_empty);
    }

    public DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
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
            .displayer(new RoundedBitmapDisplayer(3))
            // 设置成圆角图片
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .showStubImage(R.drawable.ic_msg_empty)
            .showImageForEmptyUri(R.drawable.ic_msg_empty)
            .showImageOnFail(R.drawable.ic_msg_empty)
            // 载入图片前稍做延时可以提高整体滑动的流畅度
            .delayBeforeLoading(100).build();

    /**
     * @throws
     * @Title: initImageLoader
     * @Description: TODO(初始化加载图片的类)
     */
    private void initImageLoader() {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
    }


    private void initHttpRequest() {
        // TODO Auto-generated method stub
        if (httpInstance == null) {
            httpInstance = AbHttpUtil.getInstance(this);
            httpInstance.setEasySSLEnabled(true);
        }

        if (params == null) {
            params = new AbRequestParams();
        }

        if (httpUtil == null) {
            httpUtil = AbHttpUtil.getInstance(this);
            httpUtil.setEasySSLEnabled(true);
        }
        if (checkParams == null) {
            checkParams = new AbRequestParams();
        }
    }

    /**
     * @throws
     * @Title: initViews
     * @Description: TODO(初始化界面组件)
     */
    private void initViews() {
        mainContainerLy = (LinearLayout) findViewById(R.id.mainContainerLy);
        bottomLine = (TextView) findViewById(R.id.bottomLine);
        leftTextView = (TextView) findViewById(R.id.leftTextView);
        righTextView = (TextView) findViewById(R.id.rightTextView);
        titleTextView = (TextView) findViewById(R.id.title);
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);
        titleLayout = (LinearLayout) findViewById(R.id.titleLy1);
        leftImageLayout = (LinearLayout) findViewById(R.id.leftImageLayout);
        leftImageLayout = (LinearLayout) findViewById(R.id.leftImageLayout);
        rightImageLayout = (LinearLayout) findViewById(R.id.rightImageLayout);
        backImageLayout = (LinearLayout) findViewById(R.id.backLayout);
        leftTextLayout = (LinearLayout) findViewById(R.id.leftTextLayout);
        righTextLayout = (LinearLayout) findViewById(R.id.rightTextLayout);
        searchView = (AutoCompleteTextView) findViewById(R.id.searchView);
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        searchBtnLy = (LinearLayout) findViewById(R.id.searchBtnLy);

        backImageLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (null != prefUtils.getString("postTitle", "") || null != prefUtils.getString("postContent", "")) {
                    prefUtils.remove("postTitle");
                    prefUtils.remove("postContent");
                }
                if (!isTrans){
                    overridePendingTransition(0,0);
                    supportFinishAfterTransition();
                }else{
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        if (!BlueTownApp.ins.isAppOnForeground()) {
            // app 进入后台
            isActive = false;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (!isActive) {
            // app 从后台唤醒，进入前台
            doSomethingOnScreenOff();
            isActive = true;
        }
    }

    private void doSomethingOnScreenOff() {

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String className = GesturePSWCheckActivity.class.getName();
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (!className.equals(tasksInfo.get(0).topActivity.getClassName()) && tasksInfo.get(0).topActivity.getClassName().indexOf("mywallet") != -1) {
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
     * @param leftContent
     * @throws
     * @Title: setLeftTextView
     * @Description: TODO(设置左边TextView的内容)
     */
    public void setLeftTextView(CharSequence leftContent) {
        leftTextLayout.setVisibility(View.VISIBLE);
        leftTextView.setText(leftContent);
    }

    /**
     * @param leftContentId
     * @throws
     * @Title: setLeftTextView
     * @Description: TODO(设置左边TextView的内容)
     */
    public void setLeftTextView(int leftContentId) {
        leftTextLayout.setVisibility(View.VISIBLE);
        leftTextView.setText(leftContentId);
    }

    /**
     * @throws
     * @Title: setLeftImageView
     * @Description: TODO(设置左边的组件)
     */
    public void setLeftImageView() {
        leftImageLayout.setVisibility(View.VISIBLE);
    }

    /**
     * @param titleId
     * @throws
     * @Title: setTitleView
     * @Description: TODO(设置中间显示Title)
     */
    public void setTitleView(int titleId) {
        // TODO Auto-generated method stub
        if (titleId == R.string.login) {
            backImageLayout.setVisibility(View.INVISIBLE);
        }
        titleTextView.setVisibility(View.VISIBLE);
        titleTextView.setText(titleId);
    }

    /**
     * @param title
     * @throws
     * @Title: setTitleView
     * @Description: TODO(设置中间显示Title)
     */
    public void setTitleView(CharSequence title) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(title)) {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        } else {
            titleTextView.setVisibility(View.INVISIBLE);
        }

    }

    public void setTitleState() {
        // TODO Auto-generated method stub
        titleLayout.setVisibility(View.GONE);
    }

    /**
     * @param searchRes
     * @throws
     * @Title: setSearchView
     * @Description: TODO(设置中间显示的AutoTextView)
     */
    public void setSearchView(CharSequence searchRes) {
        searchView.setVisibility(View.VISIBLE);
        searchView.setHint(searchRes);
    }

    /**
     * @param searchResId
     * @throws
     * @Title: setSearchView
     * @Description: TODO(设置中间显示的AutoTextView)
     */
    public void setSearchView(int searchResId) {
        searchView.setVisibility(View.VISIBLE);
        searchView.setHint(searchResId);
    }

    /**
     * @param searchResId
     * @throws
     * @Title: setCustomSearchView
     * @Description: TODO(设置中间显示的自定义搜索框)
     */
    public void setCustomSearchView(int searchResId) {
        mClearEditText.setVisibility(View.VISIBLE);
        mClearEditText.setHint(searchResId);
    }

    /**
     * @param searchResId
     * @throws
     * @Title: setCustomSearchView
     * @Description: TODO(设置中间显示的自定义搜索框)
     */
    public void setCustomSearchView(String searchContent) {
        mClearEditText.setVisibility(View.VISIBLE);
        mClearEditText.setHint(searchContent);
    }

    /**
     * @param title
     * @throws
     * @Title: setRighTextView
     * @Description: TODO(设置右边显示的TextView)
     */
    public void setRighTextView(CharSequence title) {
        righTextLayout.setVisibility(View.VISIBLE);
        righTextView.setText(title);
    }

    /**
     * @param titleId
     * @throws
     * @Title: setRighTextView
     * @Description: TODO(设置右边显示的TextView)
     */
    public void setRighTextView(int titleId) {
        righTextLayout.setVisibility(View.VISIBLE);
        righTextView.setText(titleId);
    }

    /**
     * @param resid
     * @throws
     * @Title: setRighTextView
     * @Description: TODO(设置右边显示的TextView)
     */
    public void setRighTextViewBg(int resid) {
        righTextLayout.setVisibility(View.VISIBLE);
        righTextView.setBackgroundResource(resid);
    }

    /**
     * @param resId
     * @throws
     * @Title: setRightImageView
     * @Description: TODO(设置右边显示的Icon)
     */
    public void setRightImageView(int resId) {
        rightImageLayout.setVisibility(View.VISIBLE);
        rightImageView.setImageResource(resId);
    }

    public void setRightTextClick(OnClickListener onClickListener) {
        righTextLayout.setVisibility(View.VISIBLE);
        righTextLayout.setOnClickListener(onClickListener);
    }

    /**
     * @throws
     * @Title: setBackImageView
     * @Description: TODO(显示back键)
     */
    public void setBackImageView() {
        backImageLayout.setVisibility(View.VISIBLE);
    }

    /**
     * @Title: setTitleLayout <BR>
     * @Description: 设置Title的背景 <BR>
     */

    public void setTitleLayoutBg(int color) {
        titleLayout.setBackgroundResource(color);
    }

    /**
     * @Title: setTitleLayoutBgRes <BR>
     * @Description: 设置Title的背景 <BR>
     */

    public void setTitleLayoutBgRes(int resId) {
        titleLayout.setBackgroundResource(resId);
    }

    /**
     * @Title: setMainLayoutBg <BR>
     * @Description: 设置页面的布局<BR>
     */
    public void setMainLayoutBg(int resId) {
        mainContainerLy.setBackgroundResource(resId);
    }

    /**
     * @Title: setBottomLine <BR>
     * @Description: please write your description <BR>
     * @Param:bottomLine
     */

    public void setBottomLine() {
        bottomLine.setVisibility(View.VISIBLE);
    }

    /**
     * @throws
     * @Title: initTitle
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public abstract void initTitle();

    /**
     * @param resId
     * @throws
     * @Title: addContentView
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public void addContentView(int resId) {
        // TODO Auto-generated method stub
        View v = getLayoutInflater().inflate(resId, null);
        addContentView(v);
    }

    /**
     * @param v
     * @throws
     * @Title: addContentView
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public void addContentView(View v) {
        mainContainerLy.addView(v, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        initIocView();
    }

    // -----------------------------------Next code copies from andbase
    private void initIocView() {
        Field[] fields = getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);

                    if (field.get(this) != null)
                        continue;

                    AbIocView viewInject = field.getAnnotation(AbIocView.class);
                    if (viewInject != null) {

                        int viewId = viewInject.id();
                        field.set(this, findViewById(viewId));

                        setListener(field, viewInject.click(), Method.Click);
                        setListener(field, viewInject.longClick(),
                                Method.LongClick);
                        setListener(field, viewInject.itemClick(),
                                Method.ItemClick);
                        setListener(field, viewInject.itemLongClick(),
                                Method.itemLongClick);

                        AbIocSelect select = viewInject.select();
                        if (!TextUtils.isEmpty(select.selected())) {
                            setViewSelectListener(field, select.selected(),
                                    select.noSelected());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param field    the field
     * @param select   the select
     * @param noSelect the no select
     * @throws Exception the exception
     */
    private void setViewSelectListener(Field field, String select,
                                       String noSelect) throws Exception {
        Object obj = field.get(this);
        if (obj instanceof View) {
            ((AbsListView) obj)
                    .setOnItemSelectedListener(new AbIocEventListener(this)
                            .select(select).noSelect(noSelect));
        }
    }

    /**
     * @param field      the field
     * @param methodName the method name
     * @param method     the method
     * @throws Exception the exception
     */
    private void setListener(Field field, String methodName, Method method)
            throws Exception {
        if (methodName == null || methodName.trim().length() == 0)
            return;

        Object obj = field.get(this);

        switch (method) {
            case Click:
                if (obj instanceof View) {
                    ((View) obj).setOnClickListener(new AbIocEventListener(this)
                            .click(methodName));
                }
                break;
            case ItemClick:
                if (obj instanceof AbsListView) {
                    ((AbsListView) obj)
                            .setOnItemClickListener(new AbIocEventListener(this)
                                    .itemClick(methodName));
                }
                break;
            case LongClick:
                if (obj instanceof View) {
                    ((View) obj)
                            .setOnLongClickListener(new AbIocEventListener(this)
                                    .longClick(methodName));
                }
                break;
            case itemLongClick:
                if (obj instanceof AbsListView) {
                    ((AbsListView) obj)
                            .setOnItemLongClickListener(new AbIocEventListener(this)
                                    .itemLongClick(methodName));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ButterKnife.unbind(this);
    }

    /**
     * The Enum Method.
     */
    public enum Method {

        /**
         * The Click.
         */
        Click,
        /**
         * The Long click.
         */
        LongClick,
        /**
         * The Item click.
         */
        ItemClick,
        /**
         * The item long click.
         */
        itemLongClick
    }

    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {

                } else {
                    // 网络断开
                    TipDialog.showDialog(TitleBarActivity.this,
                            R.string.tip, R.string.confirm,
                            "网络连接失败，请检查网络");

                }
            }

        }
    };

}
