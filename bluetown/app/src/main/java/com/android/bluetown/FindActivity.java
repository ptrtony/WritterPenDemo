package com.android.bluetown;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.home.hot.model.act.CompanyInfoPublicActivity;
import com.android.bluetown.home.hot.model.act.ComplaintActivity;
import com.android.bluetown.home.main.model.act.MakeFriendsActivity;
import com.android.bluetown.home.main.model.act.MapActualParkActivity;
import com.android.bluetown.home.main.model.act.ParkingMonthlyDetailActivity;
import com.android.bluetown.home.main.model.act.SelfHelpServiceActivity;
import com.android.bluetown.home.main.model.act.SmartCateenActivity;
import com.android.bluetown.my.AuthenticationActivity;
import com.android.bluetown.my.AuthenticationIngActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.HomeUtils;
import com.android.bluetown.view.NoScrollGridView;

import net.tsz.afinal.FinalDb;

import java.util.List;

public class FindActivity extends TitleBarActivity {
    private NoScrollGridView moreModelGridView;
    private SharePrefUtils prefUtils;
    private FinalDb db;

    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
        setBackImageView();
        setTitleView("更多");
        setTitleLayoutBg(R.color.title_bg_blue);
        rightImageLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_find);
        prefUtils = new SharePrefUtils(this);
        db = FinalDb.create(this);
        initViews();
        initHotModel();
    }

    /**
     * 初始化界面
     */
    private void initViews() {

        moreModelGridView = (NoScrollGridView) findViewById(R.id.moreModelGridView);
        // 去掉GridView点击时默认的黄色

        moreModelGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initHotModel() {


        HomeUtils.setModelsLAdapter(this, moreModelGridView, false, false, 7);
        moreModelGridView
                .setOnItemClickListener(moreModelCompanyUserOnItemClick);

    }


    /**
     * 热门版块的OnItemClickListener(企业用户)
     */
    private OnItemClickListener moreModelCompanyUserOnItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            String userId = "";
            List<MemberUser> users = db.findAll(MemberUser.class);
            if (users != null && users.size() != 0) {
                MemberUser user = users.get(0);
                if (user != null) {
                    userId = user.getMemberId();
                }
            }
            // TODO Auto-generated method stub
            switch (arg2) {

//			case 0:
//				// 日常查询
//				startActivity(new Intent(FindActivity.this,
//						DiarySearchActivity.class));
//				break;
//			case 1:
//				// 缴费管理
//				startActivity(new Intent(FindActivity.this,
//						PayManagerActivity.class));
//				break;
//			case 2:
//				// 手续报批
//
//				startActivity(new Intent(FindActivity.this,
//						ProcedureApprovalActivity.class));
//				break;
                case 0:
                    // 园区介绍
                    Intent intent = (new Intent(FindActivity.this,
                            WebViewActivity.class));
                    intent.putExtra("URL", "http://wp.era3.net/about.html");
                    intent.putExtra("title", "园区介绍");
                    startActivity(intent);
                    break;
                case 1:
                    // 车位包月
                    if (!TextUtils.isEmpty(userId)) {
                        if (prefUtils.getString(SharePrefUtils.CHECKSTATE, "")
                                .equals("1")) {
						startActivity(new Intent(FindActivity.this,
								ParkingMonthlyDetailActivity.class));
//                            TipDialog.showDialog(FindActivity.this,
//                                    R.string.tip, R.string.confirm,
//                                    R.string.noOpenModule);
                        } else if (prefUtils.getString(SharePrefUtils.CHECKSTATE,
                                "").equals("3")) {
                            TipDialog.showDialogNoClose(FindActivity.this,
                                    R.string.tip, R.string.AuthenticationIng,
                                    R.string.Authenticationinfo,
                                    AuthenticationIngActivity.class);
                        } else {
                            TipDialog.showDialogNoClose(FindActivity.this,
                                    R.string.tip, R.string.gotoAuthentication,
                                    R.string.Authenticationinfo,
                                    AuthenticationActivity.class);
                        }

                    } else {
                        TipDialog.showDialogNoClose(FindActivity.this,
                                R.string.tip, R.string.confirm,
                                R.string.login_info_tip, LoginActivity.class);
                    }
                    break;
                case 2:
                    // 企业需求发布
                    startActivity(new Intent(FindActivity.this,
                            CompanyInfoPublicActivity.class));
                    break;
//			case 4:
//				// 企业成长帮助
//				startActivity(new Intent(FindActivity.this,
//						CompanyGrowHelpActivity.class));
//				break;
                case 3:
                    // 投诉
                    startActivity(new Intent(FindActivity.this,
                            ComplaintActivity.class));
                    break;
                case 4:
                    // 自助报修
                    if (prefUtils.getString(SharePrefUtils.CHECKSTATE, "")
                            .equals("1")) {
                        startActivity(new Intent(FindActivity.this,
                                SelfHelpServiceActivity.class));
                    } else if (prefUtils.getString(SharePrefUtils.CHECKSTATE, "").equals("3")) {
                        TipDialog.showDialogNoClose(FindActivity.this,
                                R.string.tip, R.string.AuthenticationIng,
                                R.string.Authenticationinfo,
                                AuthenticationIngActivity.class);
                    } else {
                        TipDialog.showDialogNoClose(FindActivity.this,
                                R.string.tip, R.string.gotoAuthentication,
                                R.string.Authenticationinfo,
                                AuthenticationActivity.class);
                    }

                    break;
                case 5:
                    if (!TextUtils.isEmpty(userId)) {
                        // 交友
                        startActivity(new Intent(FindActivity.this,
                                MakeFriendsActivity.class));
                    } else {
                        TipDialog.showDialogNoClose(FindActivity.this,
                                R.string.tip, R.string.confirm,
                                R.string.login_info_tip, LoginActivity.class);
                    }
                    break;
                case 6:
                    startActivity(new Intent(FindActivity.this,
                            SmartCateenActivity.class));
                    break;
				case 7:
					// 园区企业
					startActivity(new Intent(FindActivity.this,
							MapActualParkActivity.class));
					break;
                case 8:
                    //办事指南
                    Intent intent1 = new Intent(FindActivity.this,MyGuideActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.LOAD_URL,Constant.WEB_BASE_URL+Constant.Interface.POLICY_NEW_NOTIFY);
                    intent1.putExtras(bundle);
                    startActivity(intent1,bundle);
                    break;
//				case 4:
//					// 实时园区
//					if (!TextUtils.isEmpty(userId)) {
//						startActivity(new Intent(FindActivity.this,
//								RealTimeZoneActivity.class));
//					} else {
//						TipDialog.showDialogNoClose(FindActivity.this,
//								R.string.tip, R.string.confirm,
//								R.string.login_info_tip, LoginActivity.class);
//					}
//					break;
//				case 5:
//					// 跳蚤市场
//					startActivity(new Intent(FindActivity.this,
//							FleaMarketActivity.class));
//					// 点击查看清空跳蚤市场的推送的消息数
//					if (BlueTownApp.fleaMarketMsgCount != 0) {
//						BlueTownApp.fleaMarketMsgCount = 0;
//						Intent refreshintent = new Intent(
//								"com.android.bm.refresh.new.msg.action");
//						sendBroadcast(refreshintent);
//					}
//					break;
//				case 6:
//					// 园区介绍
//					Intent intent = (new Intent(FindActivity.this,
//							WebViewActivity.class));
//					intent.putExtra("URL", "http://www.smartparks.cn/about.html");
//					intent.putExtra("title", "园区介绍");
//					startActivity(intent);
//					break;
                default:
                    break;
            }
        }
    };

}
