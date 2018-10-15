package com.android.bluetown.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalDb;

import java.util.List;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.MainActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.UserInfoResult;
import com.android.bluetown.utils.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author hedi
 * @data: 2016年5月4日 下午1:48:57
 * @Description: 认证中
 */
public class AuthenticationIngActivity extends TitleBarActivity implements
        OnClickListener {
    private TextView real_name, id_card, choose_company;
    private ImageView iv_1, iv_2, iv_3;
    private SharePrefUtils prefUtils;
    private String userId;
    private FinalDb db;
    private static final String TAG = "AuthenticationIngActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_authentication);
        BlueTownExitHelper.addActivity(this);
        prefUtils = new SharePrefUtils(this);
        db = FinalDb.create(AuthenticationIngActivity.this);
        initView();
        List<MemberUser> users = db.findAll(MemberUser.class);
        if (users != null && users.size() != 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                userId = user.getMemberId();
            }
        }
        getUserInfo(userId);
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo(String userId) {
        // TODO Auto-generated method stub
        params.put("userId", userId);
        httpInstance.post(Constant.HOST_URL + Constant.Interface.USER_INFO,
                params, new AbsHttpStringResponseListener(this, null) {
                    @Override
                    public void onSuccess(int i, String s) {
                        UserInfoResult result = (UserInfoResult) AbJsonUtil
                                .fromJson(s, UserInfoResult.class);

                        if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                            try {
                                JSONObject json = new JSONObject(s);
                                JSONObject data = json.optJSONObject("data");
                                String checkState = data.optString("checkState");
                                prefUtils.setString(SharePrefUtils.CHECKSTATE, data.optString("checkState"));
                                if ("1".equals(checkState)) {
                                    TipDialog.showDialog(AuthenticationIngActivity.this, SweetAlertDialog.SUCCESS_TYPE, "审核通过");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(AuthenticationIngActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }, 1000);
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AuthenticationIngActivity.this, result.getRepMsg(),
                                    Toast.LENGTH_LONG).show();

                        }

                    }
                });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rightTextLayout:
                startActivity(new Intent(AuthenticationIngActivity.this,
                        AuthenticationActivity.class));
                AuthenticationIngActivity.this.finish();
                break;

            default:
                break;
        }

    }

    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
        setBackImageView();
        setTitleView("审核中");
        setTitleLayoutBg(R.color.title_bg_blue);
        setRighTextView("重新认证");
        righTextLayout.setOnClickListener(this);
    }

    private void initView() {
        real_name = (TextView) findViewById(R.id.real_name);
        id_card = (TextView) findViewById(R.id.id_card);
//		choose_garden=(TextView)findViewById(R.id.choose_garden);
        choose_company = (TextView) findViewById(R.id.choose_company);
        iv_1 = (ImageView) findViewById(R.id.imageView1);
        iv_2 = (ImageView) findViewById(R.id.imageView2);
        iv_3 = (ImageView) findViewById(R.id.imageView3);
        TextView mTvSubmit = findViewById(R.id.submit_get);
        mTvSubmit.setText("审核中");
        mTvSubmit.setBackground(getDrawable(R.drawable.bg_un_identity_submit_cornal));
        real_name.setText(prefUtils.getString(SharePrefUtils.REALNAME, ""));
        id_card.setText(prefUtils.getString(SharePrefUtils.IDCARD, ""));
//		choose_garden.setText(prefUtils.getString(SharePrefUtils.GARDEN, ""));
        choose_company.setText(prefUtils.getString(SharePrefUtils.COMPANYNAME, ""));
        String pathUrl1 = prefUtils.getString(SharePrefUtils.PPID, "");

        Glide.with(AuthenticationIngActivity.this).load(pathUrl1)
                .placeholder(R.drawable.ic_msg_empty)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.ic_msg_empty)
                .crossFade()
//                .listener(mRequestListener)
                .into(iv_1);

        String pathUrl2 = prefUtils.getString(SharePrefUtils.OOID, "");
        Glide.with(this).load(pathUrl2)
                .placeholder(R.drawable.ic_msg_empty)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.ic_msg_empty)
                .crossFade()
                .into(iv_2);

        String pathUrl3 = prefUtils.getString(SharePrefUtils.STAMPIMG, "");
        Glide.with(this).load(pathUrl3)
                .placeholder(R.drawable.ic_msg_empty)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.ic_msg_empty)
                .crossFade()
                .into(iv_3);

        db = FinalDb.create(AuthenticationIngActivity.this);
        List<MemberUser> users = db.findAll(MemberUser.class);
        if (users != null && users.size() != 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                userId = user.getMemberId();
            }
        }
        real_name.setFocusable(false);
        id_card.setFocusable(false);
        backImageLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });


    }

}
