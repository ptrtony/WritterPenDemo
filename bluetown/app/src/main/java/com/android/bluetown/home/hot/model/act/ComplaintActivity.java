package com.android.bluetown.home.hot.model.act;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import com.ab.util.AbFileUtil;
import com.ab.util.AbJsonUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ComplainAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.img.AlbumFilesInterface;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.popup.AlbumPopupWindow;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.AlbumUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DisplayUtils;
import com.android.bluetown.utils.ImageCompress.BitmapUtil;
import com.android.bluetown.utils.ImageUtils;
import com.android.bluetown.utils.InputSoftUtil;
import com.android.bluetown.utils.PictureItemDecoration;
import com.yanzhenjie.album.AlbumFile;

/**
 * @ClassName: ComplaintActivity
 * @Description:TODO(HomeActivity--投诉建议)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:34:59
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ComplaintActivity extends TitleBarActivity implements
        OnClickListener, ComplainAdapter.OnItemClickListener, AlbumPopupWindow.OnClickListener {
    private RadioButton complaintBtn, suggestBtn;
    private EditText complaintSuggestReasons, complaintAddress;
    private RecyclerView mGridView;
    private Button complaintCommitBtn;
    //	private ModifyPicGridViewAdapter adapter;
    private ComplainAdapter mAdapter;
    private View avatarView;
    /* 用来标识请求照相功能的activity */
    private static final int CAMERA_WITH_DATA = 3023;
    /* 用来标识请求gallery的activity */
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    /* 用来标识请求裁剪图片后的activity */
    private static final int CAMERA_CROP_DATA = 3022;
    /* 拍照的照片存储位置 */
    private File PHOTO_DIR = null;
    // 照相机拍照得到的图片
    private File mCurrentPhotoFile;
    private String mFileName;
    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";
    private String types = "0";
    private List<String> imgsList;
    private String userId;
    private FinalDb db;
    private List<MemberUser> users;
    private SharePrefUtils prefUtils;
    private ArrayList<AlbumFile> albumFiles = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private PictureItemDecoration itemDecoration;
    private AlbumPopupWindow mPopupwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_complaint);
        BlueTownExitHelper.addActivity(this);
        initUIView();
        initData();

    }

    private void initUIView() {
        prefUtils = new SharePrefUtils(this);
        complaintBtn = (RadioButton) findViewById(R.id.complaint);
        suggestBtn = (RadioButton) findViewById(R.id.suggest);
        complaintSuggestReasons = (EditText) findViewById(R.id.complaintSuggestContent);
        complaintAddress = (EditText) findViewById(R.id.complaintAddress);
        mGridView = findViewById(R.id.complaintImgGridView);
        complaintCommitBtn = (Button) findViewById(R.id.complaintCommit);
        complaintCommitBtn.setOnClickListener(this);
        int padding = DisplayUtils.dip2px(this, 20);
        complaintBtn.setPadding(padding, 0, 0, 0);
        suggestBtn.setPadding(padding, 0, 0, 0);
        complaintBtn.setOnClickListener(this);
        suggestBtn.setOnClickListener(this);
        db = FinalDb.create(this);
    }

    /**
     * @Title: initTitle
     * @Description:初始化标题栏
     * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
     */
    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
        setBackImageView();
        setTitleView(R.string.complaint_offer);
        setRightImageView(R.drawable.ic_history);
        setTitleLayoutBg(R.color.title_bg_blue);
        rightImageLayout.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        users = db.findAll(MemberUser.class);
        if (users != null && users.size() != 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                userId = user.getMemberId();
            }
        }
        if (!TextUtils.isEmpty(userId)) {
            complaintCommitBtn.setClickable(true);
            complaintCommitBtn.setBackground(getResources().getDrawable(
                    R.drawable.blue_darker_btn_bg));
        } else {
            complaintCommitBtn.setClickable(false);
            complaintCommitBtn.setBackground(getResources().getDrawable(
                    R.drawable.gray_btn_bg1));
            TipDialog.showDialogNoClose(ComplaintActivity.this, R.string.tip,
                    R.string.confirm, R.string.login_info_tip,
                    LoginActivity.class);
        }
    }

    private void initData() {
        mPopupwindow = new AlbumPopupWindow(this, this);
        // 初始化图片保存路径
        String photo_dir = AbFileUtil.getImageDownloadDir(this);
        if (AbStrUtil.isEmpty(photo_dir)) {
            AbToastUtil.showToast(this, "存储卡不存在");
        } else {
            PHOTO_DIR = new File(photo_dir);
        }
        mAdapter = new ComplainAdapter(this, this);
        bitmaps.add(ImageUtils.decodeSampledBitmapFromResource(getResources()
                , R.drawable.add_pic, DisplayUtils.dip2px(this, 80)
                , DisplayUtils.dip2px(this, 80)));
        mAdapter.setData(bitmaps);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mGridView.setLayoutManager(manager);
        if (mGridView.getRecycledViewPool() != null) {
            mGridView.getRecycledViewPool().setMaxRecycledViews(0, 10);
        }
        mGridView.setItemAnimator(new DefaultItemAnimator());
        itemDecoration = new PictureItemDecoration(DisplayUtils.dip2px(this, 15)
                , DisplayUtils.dip2px(this, 5));
        itemDecoration.getPictureLists(bitmaps);
        mGridView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.complaint:
                types = "0";
                complaintBtn.setChecked(true);
                suggestBtn.setChecked(false);
                break;
            case R.id.suggest:
                types = "1";
                complaintBtn.setChecked(false);
                suggestBtn.setChecked(true);
                break;
            case R.id.complaintCommit:
                InputSoftUtil.hideSoftInput(this);
                if (TextUtils.isEmpty(userId)) {
                    complaintCommitBtn.setClickable(false);
                    complaintCommitBtn.setBackground(getResources().getDrawable(
                            R.drawable.gray_btn_bg1));
                    TipDialog.showDialogNoClose(ComplaintActivity.this,
                            R.string.tip, R.string.confirm,
                            R.string.login_info_tip, LoginActivity.class);
                } else {
                    complaintCommitBtn.setClickable(true);
                    complaintCommitBtn.setBackground(getResources().getDrawable(
                            R.drawable.blue_darker_btn_bg));
                    // 反馈类型：types，（0：投诉，1建议）
                    // 反馈内容：content，
                    // 反馈人：userId，
                    // 图片：pictures,
                    // 投诉地址:address
                    if (!complaintBtn.isChecked() && !suggestBtn.isChecked()) {
                        TipDialog.showDialog(ComplaintActivity.this, R.string.tip,
                                R.string.confirm, R.string.type_empty);
                        return;
                    }
                    String reasons = complaintSuggestReasons.getText().toString();
                    if (TextUtils.isEmpty(reasons)) {
                        TipDialog.showDialog(ComplaintActivity.this, R.string.tip,
                                R.string.confirm,
                                R.string.complaint_suggest_reason_empty);
                        return;
                    }
                    String address = complaintAddress.getText().toString();
                    if (TextUtils.isEmpty(address)) {
                        TipDialog.showDialog(ComplaintActivity.this, R.string.tip,
                                R.string.confirm, R.string.address_empty);
                        return;
                    }
                    bitmaps.remove(bitmaps.size() - 1);
                    imgsList = getImageBase64(bitmaps);
                    commitComplaint(reasons, address);
                }
                break;
            case R.id.rightImageLayout:
                startActivity(new Intent(ComplaintActivity.this,
                        HistoryComlaintSuggestActivity.class));
                break;
            default:
                break;
        }

    }

    private void commitComplaint(String reasons, String address) {
        params.put("types", types);
        params.put("content", reasons);
        params.put("userId", userId);
        params.put("gardenId",
                prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
        params.put("address", address);
        if (imgsList != null && imgsList.size() > 0) {
            params.put("pictures", AbJsonUtil.toJson(imgsList));
        }
        httpInstance.post(Constant.HOST_URL + Constant.Interface.ADD_COMPLAINT,
                params, new AbsHttpStringResponseListener(this, null) {
                    @Override
                    public void onSuccess(int i, String s) {
                        Result result = (Result) AbJsonUtil.fromJson(s,
                                Result.class);
                        if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                            TipDialog.showDialogStartNewActivity(
                                    ComplaintActivity.this, R.string.tip,
                                    R.string.confirm, R.string.commit_success,
                                    HistoryComlaintSuggestActivity.class);
                        } else {
                            TipDialog.showDialog(ComplaintActivity.this,
                                    R.string.tip, R.string.confirm,
                                    result.getRepMsg());
                        }

                    }

                });
    }

    private void openAddImg() {
        // TODO Auto-generated method stub
//        showPickDialog();
        InputSoftUtil.hideSoftInput(this);
        mPopupwindow.show(findViewById(R.id.ac_complaint));
    }

    /**
     * show pick dialog
     */


    /**
     * get image by base64
     *
     * @return
     */
    protected List<String> getImageBase64(List<Bitmap> imgs) {
        List<String> t = new ArrayList<String>();
        for (Bitmap b : imgs) {
            String t1 = bitmapToBase64(b);
            if (t1 != null) {
                t.add(t1);
            }
        }
        return t;
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
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (!isDestroyed()){
            mPopupwindow.dismiss();
        }
    }

    @Override
    public void onItemDelListener(int position, ImageView picture) {
        mAdapter.deleteItem(position);
        bitmaps.remove(position);
        itemDecoration.getPictureLists(bitmaps);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        if (bitmaps.size() - 1 == position) {
            openAddImg();
        }
    }

    @Override
    public void onCamere() {
        AlbumUtils.cameraPicture(ComplaintActivity.this, new AlbumFilesInterface() {
            @Override
            public void setAlbumFiles(ArrayList<AlbumFile> result) {

            }

            @Override
            public void setFilePath(String path) {
                bitmaps.add(bitmaps.size() - 1, ImageUtils.decodeSampledBitmapFromFilePath(BitmapUtil.amendRotatePhoto(path,ComplaintActivity.this), DisplayUtils.dip2px(ComplaintActivity.this, 80),
                        DisplayUtils.dip2px(ComplaintActivity.this, 80)));
                itemDecoration.getPictureLists(bitmaps);
                mAdapter.setData(bitmaps);
            }
        });
    }

    @Override
    public void onAlbum() {
        AlbumUtils.selectAlbume(ComplaintActivity.this, albumFiles, new AlbumFilesInterface() {
            @Override
            public void setAlbumFiles(ArrayList<AlbumFile> result) {

                for (AlbumFile albumFile : result) {
                    if (bitmaps.size() < 7) {
                        bitmaps.add(bitmaps.size() - 1, ImageUtils.decodeSampledBitmapFromFilePath(albumFile.getPath(), DisplayUtils.dip2px(ComplaintActivity.this, 80),
                                DisplayUtils.dip2px(ComplaintActivity.this, 80)));
                    }
                }
                itemDecoration.getPictureLists(bitmaps);

                mAdapter.setData(bitmaps);

            }

            @Override
            public void setFilePath(String path) {

            }
        });
    }


}
