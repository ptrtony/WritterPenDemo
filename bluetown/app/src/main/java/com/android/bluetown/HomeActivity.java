package com.android.bluetown;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import java.util.ArrayList;
import java.util.List;
import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.adapter.HomeAdapter;
import com.android.bluetown.adapter.HomeBusinessAdapter;
import com.android.bluetown.adapter.HomeEventAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.BusinessAndEventBean;
import com.android.bluetown.bean.GardenBean;
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.home.hot.model.act.SelectAreaActivity;
import com.android.bluetown.home.main.model.act.ActionCenterActivity;
import com.android.bluetown.home.main.model.act.CompanyShowActivity;
import com.android.bluetown.home.main.model.act.FleaMarketActivity;
import com.android.bluetown.home.main.model.act.RealTimeZoneActivity;
import com.android.bluetown.img.GlideRoundTransform;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.my.AuthenticationActivity;
import com.android.bluetown.my.AuthenticationIngActivity;
import com.android.bluetown.my.MyInfoActivity;
import com.android.bluetown.my.SettingMessageActivity;
import com.android.bluetown.mywallet.activity.BliiListActivity;
import com.android.bluetown.mywallet.activity.GesturePSWCheckActivity;
import com.android.bluetown.mywallet.activity.MyWalletActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.receiver.RongCloudEvent;
import com.android.bluetown.result.GardenListResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DisplayUtils;
import com.android.bluetown.utils.HomeUtils;
import com.android.bluetown.utils.ImageSizeUtil;
import com.android.bluetown.utils.LocationUtil;
import com.android.bluetown.utils.StatusBarUtils;
import com.android.bluetown.view.DecoratorViewPager;
import com.android.bluetown.view.NoScrollGridView;
import com.android.bluetown.view.PopWind;
import com.android.bluetown.view.RoundedImageView;
import com.android.bluetown.view.widgets.IHeaderWrapper;
import com.android.bluetown.view.widgets.SimpleHomeLoadView;
import com.android.bluetown.view.widgets.SimpleHomeRefreshView;
import com.android.bluetown.view.widgets.SimpleRefreshLayout;
import com.bumptech.glide.Glide;
import static com.android.bluetown.utils.Constant.HOME_REFRESH;
import static com.android.bluetown.utils.Constant.Interface.URL_HOME;

/**
 * @author hedi
 * @data: 2016年4月22日 下午3:01:14
 * @Description: 主页
 */
public class HomeActivity extends AppCompatActivity implements OnClickListener
        , AppBarLayout.OnOffsetChangedListener, SimpleRefreshLayout.OnSimpleRefreshListener{

    private static final String TAG = "HomeActivity";
    /**
     * 头像
     */
    private RoundedImageView userImg;
    /**
     * 园区类型
     */
    private TextView gardentype;

    /**
     * 主要模块和热门版块的GridView
     */
    private NoScrollGridView mainModelGridView;
    /**
     * 存放园区的ArrayList
     */
    private ArrayList<String> gardenList;
    private List<GardenBean> gardens;
    private SharePrefUtils prefUtils;
    private FinalDb db;
    // 注册成功和修改密码的标志
    private String garden, gardenId, province, city;
//    private double latitude = 0.0;
//    private double longitude = 0.0;

    /**
     * 全局的LayoutInflater对象，已经完成初始化.
     */
    public LayoutInflater mInflater;

    private AppBarLayout abl_bar;
    private View v_title_big_mask;
    private View v_toolbar_small_mask;

    private View include_toolbar_small;
    private int mMaskColor;

    /**
     * 加载图片的FinalBitmap类
     */

    /**
     * 活动列表视图
     */
    private DecoratorViewPager mRecyclerHotEventList;

    /**
     * 商家列表
     */
    private DecoratorViewPager mRecyclerBusinessList;


    /**
     * 内容显示的列表
     */
    private RecyclerView mRecyclerView;
    private SimpleRefreshLayout mRefreshView;
    private HomeAdapter mHomeAdapter;
    private ArrayList<BusinessAndEventBean.AppointmentsBean> appointmentsLists;
    private ArrayList<BusinessAndEventBean.HotMerchantsBean> hotMerchantss;
    private ArrayList<BusinessAndEventBean.HotActivitiesBean> hotActivities;
    private HomeBusinessAdapter mHomeBusinessAdapter;
    private HomeEventAdapter mHomeEventAdapter;
    // Http请求
    protected AbHttpUtil httpInstance;
    // Http请求参数
    protected AbRequestParams params;
    private Toolbar mLlToolbar;
    private TextView mTvPayContent;
    private int oldOffset = 0;
    private boolean isAdd = true;
    private TextView tvNotice;
    private BusinessAndEventBean.NoticeBean noticeBean;
    private String userId = "";
    private int absVerticalOffset;
    private int totalScrollRange;
    private int argb;
    private int title_small_offset;
    private int title_small_argb;
    private LinearLayout mLlHotEventList;
    private LinearLayout mLlBusinessList;
    private SharePrefUtils sharePrefUtils;
    private boolean isRongConnect = true;
    private String rongToken ="";
//    private FreshAreaBroadcastReceiver receiver;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_home_al);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtils.getInstance().initStatusBar(true, this);
        }
        if (params == null) {
            params = new AbRequestParams();
        }


        if (httpInstance == null) {
            httpInstance = AbHttpUtil.getInstance(this);
            httpInstance.setEasySSLEnabled(true);
        }
        if (db == null) {
            db = FinalDb.create(this);
        }
        List<MemberUser> users = db.findAll(MemberUser.class);
        if (users != null && users.size() > 0) {
            userId = users.get(0).getMemberId();
        }

        BlueTownExitHelper.addActivity(this);
        initViews();
        initActionBar();
        initLocationInfo();
        initMainModel();
        initRongUI();
//        initRongConnect();
        getGardenList();
        if (users != null && users.size() > 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                // 设置用户登录园区
                gardentype.setText(user.getHotRegion());
                gardenId = user.getGardenId();
                garden = user.getHotRegion();
            }
        }
        RongCloudEvent.getInstance().setOtherListener();
        if (users != null && users.size() != 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                String userId = user.getMemberId();
                String nickname = user.getNickName();
                RongIM.getInstance().setCurrentUserInfo(
                        new io.rong.imlib.model.UserInfo(userId, nickname, Uri
                                .parse(prefUtils.getString(
                                        SharePrefUtils.HEAD_IMG,
                                        ""))));
                RongIM.getInstance().setMessageAttachedUserInfo(true);
            }

        }
        businessAndEvent();
    }


    //初始化actionbar相关
    private void initActionBar() {
        //AppBarLayout
        v_title_big_mask = findViewById(R.id.v_title_big_mask);
        v_toolbar_small_mask = findViewById(R.id.v_toolbar_small_mask);
        include_toolbar_small = findViewById(R.id.include_toolbar_small);

        ImageView ivScan = include_toolbar_small.findViewById(R.id.saoma);
        ImageView ivPayCode = include_toolbar_small.findViewById(R.id.paycode);
        ImageView ivPassCode = include_toolbar_small.findViewById(R.id.passcode);
        ImageView ivPuls = include_toolbar_small.findViewById(R.id.puls);
        //扫码
        ivScan.setOnClickListener(this);
        //支付码
        ivPayCode.setOnClickListener(this);
        //通行码
        ivPassCode.setOnClickListener(this);
        //添加好友
        ivPuls.setOnClickListener(this);

    }


    /**
     * province:商家省（ 传实值）(必填) city：市 （传实值）(必填)
     */
    private void getGardenList() {
        params.put("province", province);
        params.put("city", city);
        httpInstance.post(Constant.HOST_URL + Constant.Interface.GARDEN_LIST,
                params, new AbsHttpResponseListener() {
                    @Override
                    public void onSuccess(int i, String s) {
                        GardenListResult result = (GardenListResult) AbJsonUtil
                                .fromJson(s, GardenListResult.class);
                        if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                            gardens = (ArrayList<GardenBean>) result.getData();
                            for (int j = 0; j < gardens.size(); j++) {
                                gardenList.add(gardens.get(j).getHotRegion());
                            }
                            if (gardenList != null && gardenList.size() > 0) {
                                garden = prefUtils.getString(
                                        SharePrefUtils.GARDEN, "");
                                gardenId = prefUtils.getString(
                                        SharePrefUtils.GARDEN_ID, "");
                                // 设置默认园区,没有选择的过园区
                                if (TextUtils.isEmpty(garden)) {
                                    garden = gardenList.get(0);
                                    gardenId = gardens.get(0).getHid();
                                    prefUtils.setString(
                                            SharePrefUtils.GARDEN_ID, gardenId);
                                    prefUtils.setString(SharePrefUtils.GARDEN,
                                            garden);

                                    gardentype.setText(garden);
                                } else {
                                    gardentype.setText(garden);
                                }

                                // 定位失败
                                if (TextUtils.isEmpty(city)) {
                                    city = prefUtils.getString(
                                            SharePrefUtils.CITY, "");
                                    province = prefUtils.getString(
                                            SharePrefUtils.PROVINCE, "");
                                    // 是否设置过默认城市信息
                                    if (TextUtils.isEmpty(city)) {
                                        city = gardens.get(0).getCity();
                                        province = gardens.get(0).getProvince();
                                        prefUtils.setString(
                                                SharePrefUtils.CITY, city);
                                        prefUtils.setString(
                                                SharePrefUtils.PROVINCE,
                                                province);
                                    }
                                }


                            }
                        }

                    }
                });
    }

    /**
     * 初始化定位信息
     */
    private void initLocationInfo() {
        List<LocationInfo> infos = db.findAll(LocationInfo.class);
        LocationInfo info = null;
        if (infos != null && infos.size() != 0) {
            info = infos.get(0);
            if (info != null) {
                city = info.getCity();
                province = info.getProvince();
            }
        }
        // 程序异常，定位信息被清除
        if (TextUtils.isEmpty(city)) {
            // 如果未定位成功，则继续定位
            LocationUtil.getInstance(getApplicationContext()).startLoc();
        }
    }

    /**
     * @throws
     * @Title: initViews
     * @Description: TODO(初始化界面组件)
     */
    private void initViews() {
        prefUtils = new SharePrefUtils(this);
        mInflater = LayoutInflater.from(this);
        abl_bar = findViewById(R.id.abl_bar);
        gardentype = findViewById(R.id.tv_home_city_name);
        userImg = findViewById(R.id.riv_head_picture);
        abl_bar.addOnOffsetChangedListener(this);
        garden = prefUtils.getString(SharePrefUtils.GARDEN, "");
        gardenId = prefUtils.getString(SharePrefUtils.GARDEN_ID, "");
        gardentype.setText(garden);
        gardenList = new ArrayList<String>();
        //背景颜色
        mMaskColor = getResources().getColor(R.color.color_4faaf7);
        mRefreshView = findViewById(R.id.simlerefreshlayout);
        mLlToolbar = findViewById(R.id.tb_home);
        mRecyclerView = findViewById(R.id.recyclerView);
        mTvPayContent = findViewById(R.id.tv_home_pay_remind);
        View view = LayoutInflater.from(this).inflate(R.layout.include_home, null);
        mainModelGridView = view.findViewById(R.id.mainModelGridView);
        mRecyclerHotEventList = view.findViewById(R.id.recycler_hot_event_list);
        tvNotice = view.findViewById(R.id.tv_notice);
        mRecyclerBusinessList = view.findViewById(R.id.recycler_business_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerBusinessList.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        IHeaderWrapper headerWrapper = null;
        if (headerWrapper == null) {
            headerWrapper = new SimpleHomeRefreshView(this);
        }
        mRefreshView.setHeaderView(headerWrapper);
        mRefreshView.setFooterView(new SimpleHomeLoadView(this));
        mRefreshView.setPullUpEnable(true);
        mRefreshView.setPullDownEnable(true);
        mRefreshView.setBackgroundColor(Color.WHITE);
        mRefreshView.setOnSimpleRefreshListener(this);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setFocusableInTouchMode(false);
        userImg.setOnClickListener(this);
        gardentype.setOnClickListener(this);
        mTvPayContent.setOnClickListener(this);
//        mFlowImageRemind.setOnClickListener(this);
        view.findViewById(R.id.iv_notify_notice).setOnClickListener(this);
        findViewById(R.id.rl_home_click_scan).setOnClickListener(this);
        findViewById(R.id.rl_home_click_pay_code).setOnClickListener(this);
        findViewById(R.id.rl_home_click_pass_code).setOnClickListener(this);
        findViewById(R.id.iv_home_puls).setOnClickListener(this);
        mLlHotEventList = view.findViewById(R.id.ll_hot_event_list);
        mLlHotEventList.setOnClickListener(this);
        tvNotice.setOnClickListener(this);
        mLlBusinessList = view.findViewById(R.id.ll_business_list);
        mLlBusinessList.setOnClickListener(this);
        ImageView ivGroupChat = view.findViewById(R.id.iv_group_chat);
        Glide.with(this).load(R.drawable.crop_chat)
                .placeholder(R.drawable.crop_chat)
                .transform(new GlideRoundTransform(this, DisplayUtils.dip2px(this, 5)))
                .override(ImageSizeUtil.getImageViewSize(ivGroupChat).width / 2
                        , ImageSizeUtil.getImageViewSize(ivGroupChat).height / 2)
                .into(ivGroupChat);
        ivGroupChat.setOnClickListener(this);
        if (hotMerchantss == null) {
            hotMerchantss = new ArrayList<>();
        }

        if (hotActivities == null) {
            hotActivities = new ArrayList<>();
        }


        if (appointmentsLists == null) {
            appointmentsLists = new ArrayList<>();
        }

        if (mHomeAdapter == null) {
            mHomeAdapter = new HomeAdapter();
        }
        mHomeAdapter.addHeaderView(view);
//        mHomeAdapter.addFooterView(bottomView);
        mRecyclerView.setAdapter(mHomeAdapter);
    }



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Glide.with(this)
                .load(prefUtils.getString(SharePrefUtils.HEAD_IMG, ""))
                .placeholder(R.drawable.ic_msg_empty)
                .error(R.drawable.ic_msg_empty)
                .into(userImg);
        if (HOME_REFRESH==1){
            businessAndEvent();
            getGardenList();
        }
    }

    /**
     * 初始化主要模块
     */
    private void initMainModel() {
        // 去掉GridView点击时默认的黄色
        mainModelGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 设置适配器
        HomeUtils.setModelsLAdapter(this, mainModelGridView, true, false, 8);
        // 设置item点击事件
        mainModelGridView.setOnItemClickListener(mainModeOnItemClick);
    }

    /**
     * @param v
     * @Title: onClick
     * @Description: 组件的点击事件
     * @see OnClickListener#onClick(View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rightTextLayout:
                if (!TextUtils.isEmpty(userId)) {
                    Intent intent = new Intent(HomeActivity.this,
                            SettingMessageActivity.class);
                    startActivity(intent);
                } else {
                    TipDialog.showDialogNoClose(HomeActivity.this, R.string.tip,
                            R.string.confirm, R.string.login_info_tip,
                            LoginActivity.class);
                }
                break;
            case R.id.iv_home_puls:
                setHomePlus(userId, findViewById(R.id.iv_home_puls));

                break;
            case R.id.riv_head_picture:
                setHomeHeadPicture(userId);
                break;
            case R.id.rl_home_click_scan:
                setHomeScan(userId);
                break;
            case R.id.rl_home_click_pay_code:
                setHomeAuthentication(userId, PayCodeActivity.class);
                break;
            case R.id.rl_home_click_pass_code:
                setHomePassCode(userId);
                break;

            case R.id.saoma:
                setHomeScan(userId);
                break;
            case R.id.paycode:
                setHomeAuthentication(userId, PayCodeActivity.class);
                break;
            case R.id.passcode:
                setHomePassCode(userId);
                break;
            case R.id.puls:
                setHomePlus(userId, include_toolbar_small.findViewById(R.id.puls));
                break;
//            case R.id.tv_home_flow_pay_remind:
//                setToBillListActivity(userId, new Intent(HomeActivity.this,
//                        BliiListActivity.class), 3);
//                break;
            case R.id.ll_hot_event_list:
                // 活动中心
                startActivity(new Intent(HomeActivity.this,
                        ActionCenterActivity.class));
                // 点击查看清空活动中心的推送的消息数
                if (BlueTownApp.actionMsgCount != 0) {
                    BlueTownApp.actionMsgCount = 0;
                    Intent refreshintent = new Intent(
                            "com.android.bm.refresh.new.msg.action");
                    sendBroadcast(refreshintent);
                }
                break;
            case R.id.tv_home_pay_remind:
                setToBillListActivity(userId, new Intent(HomeActivity.this,
                        BliiListActivity.class), 3);
                break;

            case R.id.ll_business_list:
                Intent intent = new Intent();
                intent.putExtra("type", "1");
                intent.setClass(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_group_chat:
                if (!TextUtils.isEmpty(userId)) {
                    RongIM.getInstance().startConversation(this, Conversation.ConversationType.CHATROOM,"DefaultChatRoom", "社区交友");
                } else {
                    TipDialog.showDialogNoClose(HomeActivity.this,
                            R.string.tip, R.string.confirm,
                            R.string.login_info_tip, LoginActivity.class);
                }
                break;
            case R.id.tv_notice:
                Intent intent1 = new Intent();
                intent1.setClass(HomeActivity.this, MessageDetailsActivity.class);
                intent1.putExtra("hid", noticeBean.Id);
                startActivity(intent1);
                break;
            case R.id.iv_notify_notice:
                startActivity(new Intent(HomeActivity.this, MessageListActivity.class));
                break;
            default:
                break;
        }
    }

    private void setHomePlus(String userId, View view) {
        if (!TextUtils.isEmpty(userId)) {
            PopWind morePopWindow = new PopWind(HomeActivity.this);
            morePopWindow.showPopupWindow(view);
        } else {
            TipDialog.showDialogNoClose(HomeActivity.this, R.string.tip,
                    R.string.confirm, R.string.login_info_tip,
                    LoginActivity.class);
        }
    }

    private void setHomePassCode(String userId) {
        if (!TextUtils.isEmpty(userId)) {
            Intent intent = new Intent(HomeActivity.this,
                    PassCodeActivity.class);
            startActivity(intent);
        } else {
            TipDialog.showDialogNoClose(HomeActivity.this, R.string.tip,
                    R.string.confirm, R.string.login_info_tip,
                    LoginActivity.class);
        }
    }

    private void setHomeAuthentication(String userId, Class<?> clazz) {
        if (!TextUtils.isEmpty(userId)) {
            if (prefUtils.getString(SharePrefUtils.CHECKSTATE, "").equals(
                    "1")) {
                startActivity(new Intent(HomeActivity.this, clazz));
            } else if (prefUtils.getString(SharePrefUtils.CHECKSTATE, "")
                    .equals("3")) {
                TipDialog.showDialogNoClose(HomeActivity.this,
                        R.string.tip, R.string.AuthenticationIng,
                        R.string.Authenticationinfo,
                        AuthenticationIngActivity.class);
            } else {
                TipDialog.showDialogNoClose(HomeActivity.this,
                        R.string.tip, R.string.gotoAuthentication,
                        R.string.Authenticationinfo,
                        AuthenticationActivity.class);
            }
        } else {
            TipDialog.showDialogNoClose(HomeActivity.this, R.string.tip,
                    R.string.confirm, R.string.login_info_tip,
                    LoginActivity.class);
        }
    }

    private void setHomeScan(String userId) {
        if (!TextUtils.isEmpty(userId)) {
            Intent intent = new Intent(HomeActivity.this,
                    CoreActivity.class);
            startActivity(intent);
        } else {
            TipDialog.showDialogNoClose(HomeActivity.this, R.string.tip,
                    R.string.confirm, R.string.login_info_tip,
                    LoginActivity.class);
        }
    }

    private void setHomeHeadPicture(String userId) {
        if (!TextUtils.isEmpty(userId)) {
            Intent intent = new Intent(HomeActivity.this,
                    MyInfoActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(HomeActivity.this,
                    LoginActivity.class);
            startActivity(intent);
        }
    }


    /**
     * 商家和活动
     */
    public void businessAndEvent() {
        params.put("userId", userId);
        params.put("token", prefUtils.getString(SharePrefUtils.TOKEN, ""));
        httpInstance.post(Constant.HOST_URL1 + URL_HOME, params, new AbsHttpStringResponseListener(this) {
            @Override
            public void onSuccess(int i, String s) {
                if (i == 401) {
                    TipDialog.showDialogStartNewActivity(HomeActivity.this, R.string.tip,
                            R.string.confirm, R.string.tokenPastDue, LoginActivity.class);
                } else {
                    BusinessAndEventBean businessAndEventBean = (BusinessAndEventBean) AbJsonUtil.fromJson(s, BusinessAndEventBean.class);
                    if (businessAndEventBean!=null&&null==businessAndEventBean.rep_code){
                        hotMerchantss.clear();
                        hotActivities.clear();
                        hotActivities.addAll(businessAndEventBean.HotActivities);
                        hotMerchantss.addAll(businessAndEventBean.HotMerchants);
                        noticeBean = businessAndEventBean.Notice;
                        tvNotice.setText(businessAndEventBean.Notice.Title);
                        setLastOrder(businessAndEventBean.LatestMessage);
                        if (businessAndEventBean.HotActivities.size() > 0) {
                            mHomeEventAdapter = new HomeEventAdapter(HomeActivity.this);
                            mHomeEventAdapter.setData(hotActivities);
                            mRecyclerHotEventList.setAdapter(mHomeEventAdapter);
                            mRecyclerHotEventList.setPageMargin(-8);
                            mRecyclerHotEventList.setOffscreenPageLimit(hotActivities.size());
                            mRecyclerHotEventList.setPageMargin(DisplayUtils.dip2px(HomeActivity.this, -8));
                            mRecyclerHotEventList.setVisibility(View.VISIBLE);
                            mLlHotEventList.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerHotEventList.setVisibility(View.GONE);
                            mLlHotEventList.setVisibility(View.GONE);
                        }
                        if (businessAndEventBean.HotMerchants.size() > 0) {
                            mHomeBusinessAdapter = new HomeBusinessAdapter(HomeActivity.this);
                            mHomeBusinessAdapter.setData(hotMerchantss);
                            mRecyclerBusinessList.setAdapter(mHomeBusinessAdapter);
                            mRecyclerBusinessList.setOffscreenPageLimit(hotMerchantss.size());
                            mRecyclerBusinessList.setPageMargin(DisplayUtils.dip2px(HomeActivity.this, 8));
                            mRecyclerBusinessList.setVisibility(View.VISIBLE);
                            mLlBusinessList.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerBusinessList.setVisibility(View.GONE);
                            mLlBusinessList.setVisibility(View.GONE);
                        }

                        setAppointments(businessAndEventBean.Appointments);
                    }else{
                        TipDialog.showDialog(HomeActivity.this,R.string.tip,businessAndEventBean.rep_msg);
                    }

                }
                mRefreshView.onRefreshComplete();
                HOME_REFRESH = -1;
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                super.onFailure(i, s, throwable);
                mRefreshView.onRefreshComplete();
                if (i == 401) {
                    TipDialog.showDialogStartNewActivity(HomeActivity.this, R.string.tip,
                            R.string.confirm, R.string.tokenPastDue, LoginActivity.class);
                }
                HOME_REFRESH = -1;
            }
        });

    }

    /**
     * 车辆管理
     *
     * @param appointments
     */
    private void setAppointments(List<BusinessAndEventBean.AppointmentsBean> appointments) {
        if (appointments.size() > 0) {
            appointmentsLists.clear();
            appointmentsLists.addAll(appointments);
            mHomeAdapter.setNewData(appointmentsLists);
        }
    }

    /**
     * 推送最新订单
     *
     * @param latestMessage
     */
    private void setLastOrder(BusinessAndEventBean.LatestMessageBean latestMessage) {
        if (latestMessage == null) {
            mTvPayContent.setVisibility(View.GONE);
//            mFlowImageRemind.setVisibility(View.GONE);
        } else {
            mTvPayContent.setVisibility(View.VISIBLE);
//            mFlowImageRemind.setVisibility(View.VISIBLE);
            mTvPayContent.setText(latestMessage.Content + "  " + latestMessage.GenerateTime);
//            mFlowImageRemind.setText(latestMessage.Content + "  " + latestMessage.GenerateTime);
        }

    }

    /**
     * 主要版块的OnItemClickListener
     */
    private OnItemClickListener mainModeOnItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            switch (arg2) {
                case 0:
                    setHomeAuthentication(userId, SelectAreaActivity.class);
                    // 访客预约
//                    if (!TextUtils.isEmpty(userId)) {
////                        startActivity(new Intent(HomeActivity.this,
////                                SmartParkingActivity.class));
//                        startActivity(new Intent(HomeActivity.this, SelectAreaActivity.class));
//                    } else {
//                        TipDialog.showDialogNoClose(HomeActivity.this,
//                                R.string.tip, R.string.confirm,
//                                R.string.login_info_tip, LoginActivity.class);
//                    }
                    break;
                case 2:
                    // 园区企业
                    startActivity(new Intent(HomeActivity.this,
                            CompanyShowActivity.class));
                    break;
                case 4:
                    // 钱包
//                    setToBillListActivity(userId, new Intent(HomeActivity.this,
//                            MyWalletActivity.class), 1);
                    setHomeAuthentication(userId, MyWalletActivity.class);
                    break;
                case 5:
                    setToBillListActivity(userId, new Intent(HomeActivity.this,
                            BliiListActivity.class), 3);
                    break;
                case 6:
                    // 实时园区
                    if (!TextUtils.isEmpty(userId)) {
                        startActivity(new Intent(HomeActivity.this,
                                RealTimeZoneActivity.class));
                    } else {
                        TipDialog.showDialogNoClose(HomeActivity.this,
                                R.string.tip, R.string.confirm,
                                R.string.login_info_tip, LoginActivity.class);
                    }
                    break;
                case 3:
                    // 活动中心
                    startActivity(new Intent(HomeActivity.this,
                            ActionCenterActivity.class));
                    // 点击查看清空活动中心的推送的消息数
                    if (BlueTownApp.actionMsgCount != 0) {
                        BlueTownApp.actionMsgCount = 0;
                        Intent refreshintent = new Intent(
                                "com.android.bm.refresh.new.msg.action");
                        sendBroadcast(refreshintent);
                    }
                    break;
                case 1:
                    // 跳蚤市场
                    startActivity(new Intent(HomeActivity.this,
                            FleaMarketActivity.class));
                    // 点击查看清空跳蚤市场的推送的消息数
                    if (BlueTownApp.fleaMarketMsgCount != 0) {
                        BlueTownApp.fleaMarketMsgCount = 0;
                        Intent refreshintent = new Intent(
                                "com.android.bm.refresh.new.msg.action");
                        sendBroadcast(refreshintent);
                    }

                    break;
                case 7:
                    // 更多
                    startActivity(new Intent(HomeActivity.this, FindActivity.class));
                    break;
                default:
                    break;
            }

        }
    };

    private void setToBillListActivity(String userId, Intent intent2, int value) {
        // 账单
        if (!TextUtils.isEmpty(userId)) {
            if (prefUtils.getString(SharePrefUtils.CHECKSTATE, "")
                    .equals("1")) {
                if (prefUtils
                        .getString(SharePrefUtils.HANDPASSWORD, "")
                        .equals("")
                        || prefUtils.getString(
                        SharePrefUtils.HANDPASSWORD, "") == null
                        || prefUtils.getString(
                        SharePrefUtils.HANDPASSWORD, "")
                        .equals("null")) {
                    startActivity(intent2);
                } else {
                    Intent intent = (new Intent(HomeActivity.this,
                            GesturePSWCheckActivity.class));
                    intent.putExtra("type", value);
                    startActivity(intent);
                }
            } else if (prefUtils.getString(SharePrefUtils.CHECKSTATE,
                    "").equals("3")) {
                TipDialog.showDialogNoClose(HomeActivity.this,
                        R.string.tip, R.string.AuthenticationIng,
                        R.string.Authenticationinfo,
                        AuthenticationIngActivity.class);
            } else {
                TipDialog.showDialogNoClose(HomeActivity.this,
                        R.string.tip, R.string.gotoAuthentication,
                        R.string.Authenticationinfo,
                        AuthenticationActivity.class);
            }
        } else {
            TipDialog.showDialogNoClose(HomeActivity.this,
                    R.string.tip, R.string.confirm,
                    R.string.login_info_tip, LoginActivity.class);
        }
    }

    @Override
    public void onBackPressed() {
        // 退出应用的时候，必须调用我们的disconnect()方法，而不是logout()。这样退出后我们这边才会启动push进程。
        RongIM.getInstance().disconnect();
        LocationUtil.getInstance(getApplicationContext()).stopLoc();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }


    protected void onDestroy() {
        super.onDestroy();
        abl_bar.removeOnOffsetChangedListener(this);

//        unregisterReceiver(receiver);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        absVerticalOffset = Math.abs(verticalOffset);
        totalScrollRange = appBarLayout.getTotalScrollRange();
        argb = Color.argb(absVerticalOffset * 2 > 255 ? 255 : absVerticalOffset * 2, Color.red(mMaskColor), Color.green(mMaskColor), Color.blue(mMaskColor));
        title_small_offset = (255 - absVerticalOffset * 2) < 0 ? 0 : (255 - absVerticalOffset * 2);
        title_small_argb = Color.argb(title_small_offset, Color.red(mMaskColor),
                Color.green(mMaskColor), Color.blue(mMaskColor));
        Log.d(TAG,"滑动的偏移量与总共滑动的距离"+totalScrollRange/2+"偏移量"+absVerticalOffset);
        if (absVerticalOffset <= totalScrollRange / 2) {
            include_toolbar_small.setVisibility(View.GONE);
            mLlToolbar.setVisibility(View.GONE);
        } else {
            include_toolbar_small.setVisibility(View.VISIBLE);
            mLlToolbar.setVisibility(View.VISIBLE);
            v_toolbar_small_mask.setBackgroundColor(title_small_argb);
        }
        Log.d(TAG,"向上偏移的距离"+absVerticalOffset);
        v_title_big_mask.setBackgroundColor(argb);
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                businessAndEvent();
                getGardenList();
//                initRongConnect();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
    }


//    private class FreshAreaBroadcastReceiver extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals("android.parking.reservation")){
//                businessAndEvent();
//                getGardenList();
//            }
//
//        }
//    }

    private void initRongUI(){
        //回调时的线程并不是UI线程，不能在回调中直接操作UI
        RongIMClient.getInstance().setChatRoomActionListener(new RongIMClient.ChatRoomActionListener() {
            @Override
            public void onJoining(String chatRoomId) {

            }

            @Override
            public void onJoined(String chatRoomId) {

            }

            @Override
            public void onQuited(String chatRoomId) {

            }

            @Override
            public void onError(String chatRoomId,final RongIMClient.ErrorCode code) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (code == RongIMClient.ErrorCode.RC_NET_UNAVAILABLE || code == RongIMClient.ErrorCode.RC_NET_CHANNEL_INVALID) {
                            Toast.makeText(HomeActivity.this,  getString(R.string.network_not_available), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, getString(R.string.fr_chat_room_join_failure), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
