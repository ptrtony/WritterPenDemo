package com.android.bluetown.my;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.util.LogUtil;
import net.tsz.afinal.FinalDb;
import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.R.color;
import com.android.bluetown.R.drawable;
import com.android.bluetown.R.id;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.popup.AlbumPopupWindow;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.RealPathFromUriUtils;
import com.android.bluetown.view.RoundedImageView;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author hedi
 * @data: 2016年4月25日 上午10:53:05
 * @Description: 个人信息
 */
public class MyInfoActivity extends TitleBarActivity implements OnClickListener, AlbumPopupWindow.OnClickListener {
    private View avatarView;
    /* 用来标识请求照相功能的activity */
    private static final int CAMERA_WITH_DATA = 3023;
    /* 用来标识请求gallery的activity */
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    /* 用来标识请求裁剪图片后的activity */
    private static final int CAMERA_CROP_DATA = 3022;
    private File mCurrentPhotoFile;
    private String mFileName;
    private File PHOTO_DIR = null;
    private FinalDb db;
    private RoundedImageView userImg;
    private String userId, username, companyNameStr, gardenNameStr, nickname, telephone, headimg;
    private TextView tv_nickname, tv_tel, tv_authentication, title_nickname;
    private String headImg;
    private int userType;
    private boolean isModifyImg;
    private SharePrefUtils prefUtils;
    private AlbumPopupWindow popupWidnow;
    String[] mPermissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_PICK_IMAGE = 11101;
    private Uri uritempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_myinfo);
        BlueTownExitHelper.addActivity(this);
        prefUtils = new SharePrefUtils(this);
        initFolderDir();
        initView();
        initPopupWindow();
    }

    private void initPopupWindow() {
        popupWidnow = new AlbumPopupWindow(this, this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        switch (v.getId()) {
            case id.touxing:
                popupWidnow.show(findViewById(R.id.scrollview));
//			showPickDialog();
                break;
            case id.rl_nickname:
                intent.putExtra("title", "昵称");
                intent.putExtra("content", tv_nickname.getText().toString());
                intent.setClass(this, MyInfoChangeActivity.class);
                startActivityForResult(intent, 1000);
                break;
            case id.rl_my_QR:
                Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
                intent.putExtra("bitmap", bitmap);
                intent.setClass(this, MyQRActivity.class);
                startActivity(intent);
                break;
            case id.rl_authentication:
                if ("3".equals(prefUtils.getString(
                        SharePrefUtils.CHECKSTATE, ""))) {
                    intent.setClass(this, AuthenticationIngActivity.class);
                    startActivity(intent);
                } else if ("1".equals(prefUtils.getString(
                        SharePrefUtils.CHECKSTATE, ""))) {
                    intent.setClass(this, AuthenticationSuccessActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(this, AuthenticationActivity.class);
                    startActivity(intent);
                }
                break;

        }
    }

    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
        setBackImageView();
        setTitleView("");
        setTitleLayoutBg(color.title_bg_blue);
        rightImageLayout.setVisibility(View.INVISIBLE);
    }

    private void initView() {
        title_nickname = (TextView) findViewById(id.title_nickname);
        tv_nickname = (TextView) findViewById(id.tv_nickname);
        tv_tel = (TextView) findViewById(id.tv_tel);
        tv_authentication = (TextView) findViewById(id.tv_authentication);
        userImg = (RoundedImageView) findViewById(id.touxing);
        userImg.setOnClickListener(this);
        findViewById(id.rl_nickname).setOnClickListener(this);
        findViewById(id.rl_my_QR).setOnClickListener(this);
        findViewById(id.rl_tel).setOnClickListener(this);
        findViewById(id.rl_email).setOnClickListener(this);
        findViewById(id.rl_authentication).setOnClickListener(this);
        db = FinalDb.create(MyInfoActivity.this);
        List<MemberUser> users = db.findAll(MemberUser.class);
        if (users != null && users.size() > 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                userId = user.getMemberId();
                userType = user.getUserType();
                companyNameStr = user.getCompanyName();
                username = user.getUsername();
                gardenNameStr = user.getHotRegion();
                nickname = user.getNickName();
                telephone = user.getUsername();
                headimg = user.getHeadImg();
            }

        }
        finalBitmap.display(userImg, prefUtils.getString(SharePrefUtils.HEAD_IMG, ""));
        tv_tel.setText(telephone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        tv_nickname.setText(prefUtils.getString(SharePrefUtils.NICK_NAME, ""));
        title_nickname.setText(prefUtils.getString(SharePrefUtils.NICK_NAME, ""));
        if ("3".equals(prefUtils
                .getString(SharePrefUtils.CHECKSTATE, ""))) {
            tv_authentication.setText("审核中");
        } else if ("1".equals(prefUtils
                .getString(SharePrefUtils.CHECKSTATE, ""))) {
            tv_authentication.setText("已认证");
        } else {
            tv_authentication.setText("未认证");
        }
//		initData();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        com.android.bluetown.bean.UserInfo userInfo = (com.android.bluetown.bean.UserInfo) getIntent()
                .getSerializableExtra("userInfo");

        if (userInfo != null) {
            tv_tel.setText(userInfo.getTelphone());
            tv_nickname.setText(userInfo.getNickName());
            if (userInfo.getHeadImg() != null
                    && !"".equals(userInfo.getHeadImg())) {
                finalBitmap.display(userImg, userInfo.getHeadImg());
            } else {
                userImg.setImageResource(drawable.ic_msg_empty);
            }
            headImg = userInfo.getHeadImg();

        }
    }

    private void initFolderDir() {
        String photo_dir = AbFileUtil.getImageDownloadDir(this);
        if (AbStrUtil.isEmpty(photo_dir)) {
            AbToastUtil.showToast(this, "存储卡不存在");
        } else {
            PHOTO_DIR = new File(photo_dir);
        }
    }

    protected void selectImageFromGallry() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            AbToastUtil.showToast(MyInfoActivity.this, "没有找到照片");
        }
    }

    /**
     * 从照相机获取
     */
    private void doPickPhotoAction() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            doTakePhoto();
        } else {
            AbToastUtil.showToast(this, "没有可用的存储卡");
        }
    }

    /**
     * 拍照获取图片
     */

    protected void doTakePhoto() {
        try {
            mFileName = System.currentTimeMillis() + ".jpg";
            mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(mCurrentPhotoFile));
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (Exception e) {
            AbToastUtil.showToast(this, "未找到系统相机程序");
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
        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA:
                try {
                    Uri uri = mIntent.getData();
                    String currentFilePath = RealPathFromUriUtils.getRealPathFromUri(this,uri);
                    uritempFile = Uri.fromFile(new File(currentFilePath));
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(uritempFile, "image/*");
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 190);
                    intent.putExtra("outputY", 190);
//                    uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.png");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    startActivityForResult(intent, CAMERA_CROP_DATA);
                } catch (Exception e) {
                    LogUtil.e(e.toString(), e);
                }
                break;
            case CAMERA_WITH_DATA:
                Uri uris = Uri.fromFile(mCurrentPhotoFile);
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uris, "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 190);
                intent.putExtra("outputY", 190);
                uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.png");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(intent, CAMERA_CROP_DATA);
                break;
            case CAMERA_CROP_DATA:
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    userImg.setImageBitmap(bitmap);
                    headImg = bitmapToBase64(bitmap);
                    sendinfo(headImg);
                    isModifyImg = true;
                    EventBus.getDefault().post(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//                Intent intent1 = new Intent();
//                intent1.setAction("com.android.bluetown.change.userImage");
//                sendBroadcast(intent1);
                break;
            case 1000:
                if (!mIntent.getExtras().getString("nickname").isEmpty()) {
                    tv_nickname.setText(mIntent.getExtras().getString("nickname"));
                    title_nickname.setText(mIntent.getExtras().getString("nickname"));
                }
                break;
        }
    }

    /**
     * 从相册得到的url转换为SD卡中图片路径
     */
//	public String getPath(Uri uri) {
//		String path = null;
//		if (AbStrUtil.isEmpty(uri.getAuthority())) {
//			return null;
//		}
//		String[] projection = { MediaStore.Images.Media.DATA };
//		Cursor cursor = getContentResolver().query(uri, projection, null, null,
//				null);
//		if (cursor.moveToFirst()) {
//			int column_index = cursor
//					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//			path = cursor.getString(column_index);
//		}
//		cursor.close();
//		return path;
//	}

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

    private void sendinfo(String headimg) {
        // TODO Auto-generated method stub
        params.put("muid", userId);
        params.put("headImg", headimg);
        httpInstance.post(Constant.HOST_URL
                        + Constant.Interface.MobiMemberAction_updateSelfMessage,
                params, new AbsHttpStringResponseListener(this, null) {
                    @Override
                    public void onSuccess(int i, String s) {
                        JSONObject json;
                        try {
                            json = new JSONObject(s);
                            String repCode = json.optString("repCode");
                            if (repCode.equals(Constant.HTTP_SUCCESS)) {
                                String data = json.optString("data");
                                prefUtils.setString(SharePrefUtils.HEAD_IMG, data);

                            } else {
                                Toast.makeText(MyInfoActivity.this,
                                        json.optString("repMsg"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if ("3".equals(prefUtils
                .getString(SharePrefUtils.CHECKSTATE, ""))) {
            tv_authentication.setText("审核中");
        } else if ("1".equals(prefUtils
                .getString(SharePrefUtils.CHECKSTATE, ""))) {
            tv_authentication.setText("已认证");
        } else {
            tv_authentication.setText("未认证");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isDestroyed()) {
            popupWidnow.dismiss();
        }
    }

    @Override
    public void onCamere() {
        doPickPhotoAction();
    }

    @Override
    public void onAlbum() {
        // 从相册中去获取
        selectImageFromGallry();
    }


}