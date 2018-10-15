package com.android.bluetown.my;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.FinalDb;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.ab.util.AbDialogUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.android.bluetown.MainActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.popup.AlbumPopupWindow;
import com.android.bluetown.popup.LoadingAlertDialog;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.CheckUtil;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.InputSoftUtil;
import com.android.bluetown.utils.PictureUtil;
import com.android.bluetown.wrapper.LoginWrapper;
import com.android.bluetown.wrapper.UploadPictureWrapper;
import com.bumptech.glide.Glide;

import static com.android.bluetown.pref.SharePrefUtils.SELECT_USER_TYPE;

/**
 * @author hedi
 * @data: 2016年4月26日 上午8:57:09
 * @Description: 认证
 */
public class AuthenticationActivity extends TitleBarActivity implements
        OnClickListener, AbPullToRefreshView.OnHeaderRefreshListener, AbPullToRefreshView.OnFooterLoadListener {
    private static final String TAG = "AuthenticationActivity";
    private AbPullToRefreshView mAbPullToRefreshView;
    private ListView mListView;
    private View avatarView;
    private GardenPickerRecevier recevier;
    private TextView  choose_company, tv_submit_get;
    private EditText real_name, id_card;
    private String path = "";
    private Uri originalUri = null;
    private TextView mReAuthentication;
    private ImageView iv_1;
    private ImageView iv_2;
    private ImageView iv_3;
    private String iv1_img, iv2_img, iv3_img;
    private FinalDb db;
    private String userId, gardenId = "113110000020", cid;
    private SharePrefUtils prefUtils;
    private boolean isCompanyUser = false;
    private LoadingAlertDialog loadingDialog;
    private String username;
    private String password;
    UploadPictureWrapper uploadPictureWrapper;
    private AlbumPopupWindow mAlbumPopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_authentication_center);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mReAuthentication = findViewById(R.id.tv_reAuthentication);
        mReAuthentication.setOnClickListener(this);
        BlueTownExitHelper.addActivity(this);
        prefUtils = new SharePrefUtils(this);
        loadingDialog = new LoadingAlertDialog(this);
        uploadPictureWrapper = new UploadPictureWrapper();
        initView();
        registerGardenRecevier();
        isCompanyUser = prefUtils.getBoolean(SELECT_USER_TYPE,false);
        if (isCompanyUser){
            choose_company.setVisibility(View.VISIBLE);
        }else{
            choose_company.setVisibility(View.GONE);
        }

    }

    /**
     * 注册设置园区的广播
     */
    private void registerGardenRecevier() {
        recevier = new GardenPickerRecevier();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.garden.choice.action");
        registerReceiver(recevier, filter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.submit_get:
                InputSoftUtil.hideSoftInput(this);
                if (real_name.getText().toString().isEmpty()
                        || id_card.getText().toString().isEmpty()
                        || iv1_img == null
                        || iv2_img == null
                        || iv3_img == null) {
                    Toast.makeText(AuthenticationActivity.this, "请输入完整信息",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isCompanyUser&&choose_company.getText().toString().isEmpty()){
                    Toast.makeText(AuthenticationActivity.this, "请输入完整信息",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!CheckUtil.IDCardValidate(id_card.getText().toString())) {
                    Toast.makeText(AuthenticationActivity.this, "请输入正确格式的身份证",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                send();
                break;
            case R.id.imageView1:
                InputSoftUtil.hideSoftInput(this);
                showPickDialog(1);
                break;
            case R.id.imageView2:
                InputSoftUtil.hideSoftInput(this);
                showPickDialog(2);
                break;
            case R.id.imageView3:
                InputSoftUtil.hideSoftInput(this);
                showPickDialog(3);
                break;
//		case R.id.choose_garden:
//			GardenPickerDialog gardenDialog = new GardenPickerDialog(
//					AuthenticationActivity.this);
//			gardenDialog.show();
//			break;
            case R.id.choose_company:
//			if(tv_garden.getText().toString().isEmpty()){
//				Toast.makeText(AuthenticationActivity.this, "请先选择园区",
//						Toast.LENGTH_SHORT).show();
//				return;
//			`}
                InputSoftUtil.hideSoftInput(this);
                Intent intent = (new Intent(AuthenticationActivity.this,
                        ChooseCompanyActivity.class));
                intent.putExtra("gardenId", gardenId);
                startActivityForResult(intent, 1000);
                break;
            case R.id.tv_reAuthentication:
                InputSoftUtil.hideSoftInput(this);
                clear();
                break;
            case R.id.iv_back:
                Log.d(TAG,"点击回退键");
                InputSoftUtil.hideSoftInput(this);
                finish();
                break;
        }

    }

    @Override
    public void initTitle() {
        setTitleState();
    }

    private void initView() {
        mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
        mAbPullToRefreshView.setOnHeaderRefreshListener(AuthenticationActivity.this);
        mAbPullToRefreshView.setOnFooterLoadListener(AuthenticationActivity.this);
        mAbPullToRefreshView.setPullRefreshEnable(false);
        mAbPullToRefreshView.setLoadMoreEnable(false);
        mAbPullToRefreshView.setFocusable(false);
        mListView = (ListView) findViewById(R.id.companyInfoList);
        View view = LayoutInflater.from(this).inflate(R.layout.ac_authentication, null);
        tv_submit_get = (TextView) view.findViewById(R.id.submit_get);
        tv_submit_get.setOnClickListener(this);
        real_name = (EditText) view.findViewById(R.id.real_name);
        id_card = (EditText) view.findViewById(R.id.id_card);
        choose_company = (TextView) view.findViewById(R.id.choose_company);
        iv_1 = (ImageView) view.findViewById(R.id.imageView1);
        iv_2 = (ImageView) view.findViewById(R.id.imageView2);
        iv_3 = (ImageView) view.findViewById(R.id.imageView3);
        iv_1.setOnClickListener(this);
        iv_2.setOnClickListener(this);
        iv_3.setOnClickListener(this);
        choose_company.setOnClickListener(this);
        mListView.addHeaderView(view);
        mListView.setAdapter(null);
        mListView.setHeaderDividersEnabled(false);
        mListView.setFooterDividersEnabled(false);
        db = FinalDb.create(AuthenticationActivity.this);
        List<MemberUser> users = db.findAll(MemberUser.class);
        if (users != null && users.size() > 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                userId = user.getMemberId();
                username = user.getUsername();
                password = user.getPassword();
            }

        }

    }

    private void send() {
//        String idType = "0";
        params.put("user_id", userId);
        params.put("name", real_name.getText().toString());
        params.put("idcard", id_card.getText().toString());
        params.put("garden", gardenId);
//        params.put("cid", cid);
        if (isCompanyUser){
            params.put("member_type","1");
            params.put("companyid",choose_company.getText().toString());
        }else{
            params.put("member_type","0");
            params.put("companyid","");
        }
        params.put("face_idcard_img", iv1_img);
        params.put("back_idcard_img", iv2_img);
//        params.put("idType", idType);
        params.put("stamp_img", iv3_img);
        httpInstance.post(Constant.HOST_URL1
                        + Constant.Interface.IDENTITY_AUTHENTICATION, params,
                new AbsHttpStringResponseListener(this, null) {
                    @Override
                    public void onSuccess(int i, String s) {
                        JSONObject json;
                        try {
                            json = new JSONObject(s);
                            String repCode = json.optString("repCode");
                            if (repCode.equals(Constant.HTTP_SUCCESS)) {
                                JSONObject data = json.optJSONObject("data");
                                JSONObject mem = data.optJSONObject("mem");
                                prefUtils.setString(SharePrefUtils.CHECKSTATE, "3");
                                prefUtils.setString(SharePrefUtils.REALNAME, mem.optString("name"));
                                prefUtils.setString(SharePrefUtils.IDCARD, mem.optString("idCard"));
                                prefUtils.setString(SharePrefUtils.GARDEN, data.optString("gardenName"));
                                prefUtils.setString(SharePrefUtils.COMPANYNAME, data.optString("companyName"));
                                prefUtils.setString(SharePrefUtils.OOID, mem.optString("ooId"));
                                prefUtils.setString(SharePrefUtils.PPID, mem.optString("ppId"));
                                prefUtils.setString(SharePrefUtils.STAMPIMG, mem.optString("stampImg"));
                                mAbPullToRefreshView.setLoadMoreEnable(false);
                                mAbPullToRefreshView.setPullRefreshEnable(true);
                                noEditAuthentication(false);
                                TipDialog.showDialog(AuthenticationActivity.this, SweetAlertDialog.SUCCESS_TYPE,"已提交审核");
                                LoginWrapper loginWrapper = new LoginWrapper(AuthenticationActivity.this);
                                loginWrapper.login(username,password);
                            } else {
                                Toast.makeText(AuthenticationActivity.this,
                                        json.optString("repMsg"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        mAbPullToRefreshView.onHeaderRefreshFinish();
                        mAbPullToRefreshView.onFooterLoadFinish();

                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        super.onFailure(i, s, throwable);
                        mAbPullToRefreshView.onHeaderRefreshFinish();
                        mAbPullToRefreshView.onFooterLoadFinish();
                    }
                });
    }

    private void setSubmitButtonStatus(boolean isClick,String authen) {
        tv_submit_get.setText(authen);
        if (isClick){
            tv_submit_get.setClickable(true);
            setEditTextFocus(real_name,true);
            setEditTextFocus(id_card,true);
            mReAuthentication.setVisibility(View.INVISIBLE);
            noEditAuthentication(true);
        }else{
            tv_submit_get.setClickable(false);
            setEditTextFocus(real_name,false);
            setEditTextFocus(id_card,false);
            noEditAuthentication(false);
            mReAuthentication.setVisibility(View.VISIBLE);
        }
    }

    private void showPickDialog(final int i) {
        mAlbumPopupWindow = new AlbumPopupWindow(this, new AlbumPopupWindow.OnClickListener() {
            @Override
            public void onCamere() {
                takePhoto(i);
            }

            @Override
            public void onAlbum() {
                // 从相册中去获取
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");

                } else {
                    intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                }

                if (i == 1) {
                    startActivityForResult(intent, 1);
                } else if (i == 2) {
                    startActivityForResult(intent, 2);
                } else if (i == 3) {
                    startActivityForResult(intent, 3);
                }
            }
        });
        mAlbumPopupWindow.show(findViewById(R.id.companyInfoList));
    }

    /**
     * 从照相机获取
     */
    public void takePhoto(int i) {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File vFile = new File(BlueTownApp.SDPATH, String.valueOf(System
                .currentTimeMillis()) + ".jpg");
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        path = vFile.getPath();
        Uri cameraUri = Uri.fromFile(vFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        if (i == 1) {
            startActivityForResult(openCameraIntent, 4);
        } else if (i == 2) {
            startActivityForResult(openCameraIntent, 5);
        } else if (i == 3) {
            startActivityForResult(openCameraIntent, 6);
        }
    }

    /**
     * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
     */
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent mIntent) {
        if (resultCode != RESULT_OK) {
            return;
        }

        loadingDialog.show();
        switch (requestCode) {

            case 1:
                getPicture(1,mIntent);
                break;
            case 2:
                getPicture(2,mIntent);
                break;
            case 3:
                getPicture(3,mIntent);
                break;
            case 4:
                if (path != null || !path.equals("")) {
//                    Bitmap bitmapOrg = PictureUtil.getimage(path);
//                    iv_1.setImageBitmap(bitmapOrg);
//                    iv1_img = bitmapToBase64(bitmapOrg);
                    getCamera(4,path);
                }
                break;
            case 5:
                if (path != null || !path.equals("")) {
//                    Bitmap bitmapOrg = PictureUtil.getimage(path);
//                    iv_2.setImageBitmap(bitmapOrg);
//                    iv2_img = bitmapToBase64(bitmapOrg);
                    getCamera(5,path);
                }
                break;
            case 6:
                if (path != null || !path.equals("")) {
//                    Bitmap bitmapOrg = PictureUtil.getimage(path);
//                    iv_3.setImageBitmap(bitmapOrg);
//                    iv3_img = bitmapToBase64(bitmapOrg);
                    getCamera(6,path);
                }
                break;
            case 1000:
                if (!mIntent.getExtras().getString("companyId").isEmpty()) {
                    cid = mIntent.getExtras().getString("companyId");
                    choose_company.setText(mIntent.getExtras().getString("company"));
                }
                break;

        }
    }
    private void getPicture(int type, Intent mIntent){
        if (mIntent!=null){
            originalUri = mIntent.getData(); // 获得图片的uri
            String[] proj = {MediaStore.Images.Media.DATA};
            // 好像是android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = getContentResolver().query(originalUri, proj,
                    null, null, null);
            // 按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            // 最后根据索引值获取图片路径
            String path = cursor.getString(column_index);

            Bitmap bitmapOrg = PictureUtil.getimage(path);
            uploadPictureWrapper.uploadFile(path, new UploadPictureWrapper.OnResponseResult() {
                @Override
                public void success(String pathUrl) {
                    loadingDialog.dismiss();
                    switch (type){
                        case 1:
                            iv1_img = pathUrl;
//                            Glide.with(AuthenticationActivity.this).load("http://"+pathUrl)
//                                    .centerCrop().into(iv_1);
                            iv_1.setImageBitmap(bitmapOrg);
                            break;
                        case 2:
                            iv2_img = pathUrl;
//                            Glide.with(AuthenticationActivity.this).load("http://"+pathUrl)
//                                    .centerCrop().into(iv_2);
                            iv_2.setImageBitmap(bitmapOrg);
                            break;
                        case 3:
                            iv3_img = pathUrl;
//                            Glide.with(AuthenticationActivity.this).load("http://"+pathUrl)
//                                    .centerCrop().into(iv_3);
                            iv_3.setImageBitmap(bitmapOrg);
                            break;
                    }

                }
                @Override
                public void failed(String e) {
                    loadingDialog.dismiss();
                    TipDialog.showDialog(AuthenticationActivity.this,R.string.tip,"上传失败"+e);
                }
            });
        }
    }

    public void getCamera(int type,String path){
        Bitmap bitmapOrg = PictureUtil.getimage(path);
        uploadPictureWrapper.uploadFile(path, new UploadPictureWrapper.OnResponseResult() {
            @Override
            public void success(String pathUrl) {
                loadingDialog.dismiss();
                switch (type){
                    case 4:
                        iv1_img = pathUrl;
//
//                        iv_1.setImageBitmap(bitmapOrg);
//                        Glide.with(AuthenticationActivity.this).load("http://"+pathUrl)
//                                .centerCrop().into(iv_1);
                        iv_1.setImageBitmap(bitmapOrg);
                        break;
                    case 5:
                        iv2_img = pathUrl;
//                        Bitmap bitmapOrg1 = PictureUtil.getimage(path);
//                        iv_2.setImageBitmap(bitmapOrg1);
//                        Glide.with(AuthenticationActivity.this).load("http://"+pathUrl)
//                                .centerCrop().into(iv_2);
                        iv_2.setImageBitmap(bitmapOrg);
                        break;
                    case 6:
                        iv3_img = pathUrl;
                        iv_3.setImageBitmap(bitmapOrg);
//                        Glide.with(AuthenticationActivity.this).load("http://"+pathUrl)
//                                .centerCrop().into(iv_3);
                        break;
                }

            }
            @Override
            public void failed(String e) {
                loadingDialog.dismiss();
                TipDialog.showDialog(AuthenticationActivity.this,R.string.tip,"上传失败"+e);
            }
        });
    }

    /**
     * @param bitmap
     * @return
     * @throws
     * @Title: imgToBase64
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {

    }

    @Override
    public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
        checkAuthentication();
    }

    private class GardenPickerRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals("android.garden.choice.action")) {
                String garden = intent.getStringExtra("garden");
                gardenId = intent.getStringExtra("gardenId");
//				tv_garden.setText(garden);
            }
        }
    }

    private void checkAuthentication() {
        checkParams.put("userId", userId);
        httpUtil.post(Constant.HOST_URL + Constant.Interface.USER_INFO, checkParams, new AbsHttpStringResponseListener(this, null) {
            @Override
            public void onSuccess(int i, String s) {
                JSONObject json;
                try {
                    json = new JSONObject(s);
                    String reqCode = json.optString("reqCode");
                    if (reqCode.equals(Constant.HTTP_SUCCESS)) {
                        JSONObject data = json.optJSONObject("data");
                        String checkState = data.getString("checkState");
                        if (checkState != null) {
                            if ("3".equals(prefUtils
                                    .getString(SharePrefUtils.CHECKSTATE, ""))) {
                                TipDialog.showDialog(AuthenticationActivity.this, R.string.tip, R.string.confirm, "审核中");
                                setSubmitButtonStatus(false,"审核中");
                            } else if ("1".equals(prefUtils
                                    .getString(SharePrefUtils.CHECKSTATE, ""))) {
                                Intent intent = new Intent();
                                intent.setClass(AuthenticationActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                TipDialog.showDialog(AuthenticationActivity.this, R.string.tip, R.string.confirm, "未认证");
                                setSubmitButtonStatus(true,"提交");
                            }

                        }
                    } else {
                        Toast.makeText(AuthenticationActivity.this,
                                json.optString("repMsg"),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAbPullToRefreshView.onHeaderRefreshFinish();
                mAbPullToRefreshView.onFooterLoadFinish();
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                super.onFailure(i, s, throwable);
                mAbPullToRefreshView.onHeaderRefreshFinish();
                mAbPullToRefreshView.onFooterLoadFinish();
            }
        });
    }

    /**
     * 点击重新认证按钮 清空字段数据
     */
    private void clear() {
        mAbPullToRefreshView.setPullRefreshEnable(false);
        mAbPullToRefreshView.setLoadMoreEnable(false);
        real_name.setText("");
        id_card.setText("");
//		tv_garden.setText("");
        choose_company.setText("");
        iv1_img = null;
        iv2_img = null;
        iv3_img = null;
        iv_1.setImageDrawable(getResources().getDrawable(R.drawable.identity_card));
        iv_2.setImageDrawable(getResources().getDrawable(R.drawable.identity_rear));
        iv_3.setImageDrawable(getResources().getDrawable(R.drawable.company_chaper));
        tv_submit_get.setText("提交");
        tv_submit_get.setClickable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv_submit_get.setBackground(getDrawable(R.drawable.bg_identity_submit_cornel));
            }
        }
    }

    /**
     * 编辑框是否可点击的状态
     */
    public void setEditTextFocus(EditText editText,boolean isFocus){
        if (isFocus){
            editText.setFocusable(true);
            editText.setCursorVisible(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        }else{
            editText.setCursorVisible(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }
    }

    public void noEditAuthentication(boolean isEdit){
        setEditTextFocus(real_name,isEdit);
        setEditTextFocus(id_card,isEdit);
        iv_1.setClickable(isEdit);
        iv_2.setClickable(isEdit);
        iv_3.setClickable(isEdit);
        choose_company.setClickable(isEdit);
        tv_submit_get.setClickable(isEdit);
        if (isEdit){
            mReAuthentication.setVisibility(View.INVISIBLE);
            mReAuthentication.setClickable(false);
            tv_submit_get.setBackground(getDrawable(R.drawable.bg_identity_submit_cornel));
            tv_submit_get.setText("提交");
        }else{
            tv_submit_get.setBackground(getDrawable(R.drawable.bg_un_identity_submit_cornal));
            tv_submit_get.setText("审核中");
            mReAuthentication.setVisibility(View.VISIBLE);
            mReAuthentication.setClickable(true);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recevier);
    }
}
