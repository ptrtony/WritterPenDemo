package com.android.bluetown.circle.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.alibaba.fastjson.JSON;
import com.android.annotations.NonNull;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ComplainAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.img.AlbumFilesInterface;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.mulphoto.adapter.ImagePublishAdapter;
import com.android.bluetown.mulphoto.model.ImageItem;
import com.android.bluetown.mulphoto.utils.IntentConstants;
import com.android.bluetown.multiphoto.ImageBucketChooseActivity;
import com.android.bluetown.popup.AlbumPopupWindow;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.AlbumUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DisplayUtils;
import com.android.bluetown.utils.ImageCompress.BitmapUtil;
import com.android.bluetown.utils.ImageUtils;
import com.android.bluetown.utils.InputSoftUtil;
import com.android.bluetown.view.NoScrollGridView;
import com.android.bluetown.view.widgets.IFooterWrapper;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

/**
 * @ClassName: PublishPostActivity
 * @Description:TODO(圈子-发帖)
 * @author: shenyz
 * @date: 2015年8月19日 下午3:40:31
 */
public class PublishPostActivity extends TitleBarActivity implements
        OnClickListener, TextWatcher, AlbumPopupWindow.OnClickListener, ComplainAdapter.OnItemClickListener {
    private EditText postTitle, postContent;
    private String postTitleStr, postContentStr;
    private RecyclerView mGridView;
    private Button publishButton;
    private View avatarView;
    // 储存图片集的list
//	public static List<ImageItem> mDataList = new ArrayList<ImageItem>();
    public static ArrayList<String> mDataList = new ArrayList<String>();
    private ArrayList<AlbumFile> albumFiles = new ArrayList<>();
    public static List<String> sendPast = new ArrayList<>();
    public static String title;
    public static String content;
    private AlbumPopupWindow mPopupwindow;
    // 展示图片的适配器
    private ComplainAdapter mAdapter;
    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";
    private ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
    //	private String groupId;
    private List<String> imgsList;
    private SharePrefUtils prefUtils;
    private static final int PUSLISH_POST = 1;
    private String userId;
    private FinalDb db;
    private List<MemberUser> users;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PUSLISH_POST:
                    postTitleStr = (String) msg.obj;
                    postContentStr = (String) msg.obj;
                    publishPost(postTitleStr, postContentStr);
                    break;

                default:
                    break;
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_circle_publish_post);
//		initData();
        initUIView(savedInstanceState);
    }

    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
//		setBackImageView();
        backImageLayout.setVisibility(View.INVISIBLE);
        setTitleLayoutBg(R.color.white);
        setTitleView(R.string.publish_post);
        titleTextView.setTextColor(getColor(R.color.text_black));
        leftTextLayout.setVisibility(View.VISIBLE);
        leftTextView.setTextColor(getColor(R.color.color_888788));
        leftTextView.setText("取消");
//		righTextLayout.setBackground(getDrawable(R.drawable.bg_post_right));
        righTextLayout.setVisibility(View.VISIBLE);
        righTextLayout.setOnClickListener(this);
        righTextView.setText("发送");
        righTextView.setBackground(getDrawable(R.drawable.bg_post_right));
        righTextView.setTextColor(getColor(R.color.item_showmore_click));
        titleLayout.setBackgroundResource(R.color.color_f9f9f9);
        rightImageLayout.setVisibility(View.INVISIBLE);
        leftTextLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataList.clear();
                finish();
                overridePendingTransition(0, R.anim.bottom_out);
            }
        });
    }

//	private void initData() {
//		prefUtils = new SharePrefUtils(this);
//		getTempFromPref();
//		List<Album> incomingDataList = (List<Album>) getIntent()
//				.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
//		if (incomingDataList != null) {
//			mDataList.addAll(incomingDataList);
//		}
//	}

    private void initUIView(Bundle savedInstanceState) {
        try {
//			groupId = getIntent().getStringExtra("groupId");
            postTitleStr = getIntent().getStringExtra("postTitle");
            postContentStr = getIntent().getStringExtra("postContent");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        db = FinalDb.create(PublishPostActivity.this);
        users = db.findAll(MemberUser.class);
        mPopupwindow = new AlbumPopupWindow(this, this);
        if (users != null && users.size() != 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                userId = user.getMemberId();
            }
        }
        postTitle = (EditText) findViewById(R.id.postTitle);
        postContent = (EditText) findViewById(R.id.postContent);
        postContent.addTextChangedListener(this);
        if (postTitleStr != null && !TextUtils.isEmpty(postTitleStr)) {
            postTitle.setText(postTitleStr);
        }
        if (postContentStr != null && !TextUtils.isEmpty(postContentStr)) {
            postContent.setText(postContentStr);
        }

        mGridView = findViewById(R.id.imgGridView);
        mGridView.setLayoutManager(new GridLayoutManager(this, 4));
        if (mGridView.getRecycledViewPool() != null) {
            mGridView.getRecycledViewPool().setMaxRecycledViews(0, 10);
        }
        mGridView.setItemAnimator(new DefaultItemAnimator());
        // 初始化临时添加图片的按钮
        mAdapter = new ComplainAdapter(this, this);
        bmp.add(ImageUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.add_pic
                , DisplayUtils.dip2px(this, 80), DisplayUtils.dip2px(this, 80)));
        mAdapter.setData(bmp);
        mGridView.setAdapter(mAdapter);
        publishButton = (Button) findViewById(R.id.publishPostCommit);
        publishButton.setOnClickListener(this);
//        mGridView.setOnItemClickListener(this);
        publishButton.setClickable(false);
    }

    // 将数据保存到outState对象中, 该对象会在重建activity时传递给onCreate方法
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
//		saveTempToPref();
    }

    // 恢复数据
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 获取临时存储的文件
     */
    private void getTempFromPref() {
        String prefStr = prefUtils.getString(SharePrefUtils.PREF_TEMP_IMAGES,
                null);
        if (!TextUtils.isEmpty(prefStr)) {
//			List<ImageItem> tempImages = JSON.parseArray(prefStr,
//					ImageItem.class);
//			mDataList = tempImages;
        }
    }

//	private void saveTempToPref() {
//		String prefStr = JSON.toJSONString(mDataList);
//		prefUtils.setString(SharePrefUtils.PREF_TEMP_IMAGES, prefStr);
//	}

    private void removeTempFromPref() {
//		prefUtils.remove(SharePrefUtils.PREF_TEMP_IMAGES);
        prefUtils.remove("postTitle");
        prefUtils.remove("postContent");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // 当在ImageZoomActivity中删除图片时，返回这里需要刷新
        notifyDataChanged();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//		saveTempToPref();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightTextLayout:
                if (userId == null || userId.isEmpty()) {
                    TipDialog.showDialogNoClose(PublishPostActivity.this,
                            R.string.tip, R.string.confirm,
                            R.string.login_info_tip, LoginActivity.class);
                } else {
//				postTitleStr = postTitle.getText().toString();
                    postContentStr = postContent.getText().toString();
                    if (TextUtils.isEmpty(postContentStr)) {
                        TipDialog.showDialog(PublishPostActivity.this,
                                R.string.tip, R.string.confirm,
                                R.string.publish_post_content_commit_error);
                        return;
                    }
                    imgList();
                    publishPost(postTitleStr, postContentStr);

                }

                break;

            default:
                break;
        }

    }

    ;

    /**
     * 发帖
     *
     * @param postTitleStr
     * @param postContentStr
     */
    private void publishPost(String postTitleStr, String postContentStr) {
        /**
         * userId：用户id managementName：标题 content：内容 groupId：圈子id
         */
        params.put("userId", userId);
        // 表示圈子
        params.put("managementName", postContentStr);
        params.put("content", postContentStr);
//		params.put("groupId", groupId);
        if (imgsList != null && imgsList.size() > 0) {
            params.put("pictures", AbJsonUtil.toJson(imgsList));
        }
        httpInstance.post(Constant.HOST_URL + Constant.Interface.PUBLISH_POST,
                params, new AbsHttpStringResponseListener(this, null) {
                    @Override
                    public void onSuccess(int i, String s) {
                        Result result = (Result) AbJsonUtil.fromJson(s,
                                Result.class);
                        if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                            showDialogFinishSelf(PublishPostActivity.this,
                                    R.string.tip, R.string.confirm,
                                    R.string.publish_success);
                        } else {
                            Toast.makeText(PublishPostActivity.this,
                                    result.getRepMsg(), Toast.LENGTH_LONG)
                                    .show();

                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        mDataList.clear();
        super.onBackPressed();
    }

    private int getDataSize() {
        return mDataList == null ? 0 : mDataList.size();
    }

//    private int getAvailableSize() {
//        int availSize = SharePrefUtils.MAX_IMAGE_SIZE - mDataList.size();
//        if (availSize >= 0) {
//            return availSize;
//        }
//        return 0;
//    }
//
//    public String getString(String s) {
//        String path = null;
//        if (s == null)
//            return "";
//        for (int i = s.length() - 1; i > 0; i++) {
//            s.charAt(i);
//        }
//        return path;
//    }

//    public void takePhoto() {
//        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File vFile = new File(BlueTownApp.SDPATH, String.valueOf(System
//                .currentTimeMillis()) + ".jpg");
//        if (!vFile.exists()) {
//            File vDirPath = vFile.getParentFile();
//            vDirPath.mkdirs();
//        } else {
//            if (vFile.exists()) {
//                vFile.delete();
//            }
//        }
//        path = vFile.getPath();
//        Uri cameraUri = Uri.fromFile(vFile);
//        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
//        startActivityForResult(openCameraIntent, TAKE_PICTURE);
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (mDataList.size() < SharePrefUtils.MAX_IMAGE_SIZE
                        && resultCode == -1 && !TextUtils.isEmpty(path)) {
                    mDataList.add(path);
                }
                break;
        }
    }

    /**
     * @param context
     * @param titleId
     * @param confirmTextId
     * @param contentStr
     * @throws
     * @Title: showDialogFinishSelf
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private void showDialogFinishSelf(final Activity context, int titleId,
                                      int confirmTextId, int contentStr) {
        SweetAlertDialog dialog = new SweetAlertDialog(context)
                .setContentText(context.getString(contentStr));
        dialog.setTitleText(context.getString(titleId));
        dialog.setConfirmText(context.getString(confirmTextId));
        dialog.isShowCancelButton();
        dialog.setContentText(context.getString(contentStr));
        dialog.setConfirmClickListener(new OnSweetClickListener() {

            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                // TODO Auto-generated method stub
//				removeTempFromPref();
                mDataList.clear();
                sweetAlertDialog.dismiss();
                context.finish();
                overridePendingTransition(0, R.anim.bottom_out);
            }
        });
        dialog.show();
    }

    private void notifyDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * show pick dialog
     */
//    private void showPickDialog() {
//        avatarView = getLayoutInflater().inflate(R.layout.choose_avatar, null);
//        Button albumButton = (Button) avatarView
//                .findViewById(R.id.choose_album);
//        Button camButton = (Button) avatarView.findViewById(R.id.choose_cam);
//        Button cancelButton = (Button) avatarView
//                .findViewById(R.id.choose_cancel);
//        albumButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                AbDialogUtil.removeDialog(PublishPostActivity.this);
//                AlbumUtils.selectAlbume(PublishPostActivity.this, albumFiles, new AlbumFilesInterface() {
//                    @Override
//                    public void setAlbumFiles(ArrayList<AlbumFile> result) {
//                        albumFiles = result;
//                        if (mDataList.size() > 0) mDataList.clear();
//                        for (int i = 0; i < result.size(); i++) {
//                            mDataList.add(result.get(i).getPath());
//                        }
//                        mAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void setFilePath(String path) {
//
//                    }
//                });
//            }
//
//        });
//
//        camButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                AbDialogUtil.removeDialog(PublishPostActivity.this);
//                takePhoto();
//            }
//
//        });
//
//        cancelButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                AbDialogUtil.removeDialog(PublishPostActivity.this);
//            }
//
//        });
//        AbDialogUtil.showDialog(avatarView, Gravity.BOTTOM);
//    }

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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

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

    /**
     * 将图片转化为base64
     */
    private void imgList() {
        if (imgsList != null) {
            imgsList.clear();
        }

        imgsList = getImageBase64(bmp);
    }

    /**
     * get image by base64
     *
     * @return
     */
    protected List<String> getImageBase64(List<Bitmap> imgs) {
        imgs.remove(imgs.size()-1);
        List<String> t = new ArrayList<String>();
        for (Bitmap b : imgs) {
            String t1 = bitmapToBase64(b);
            if (t1 != null) {
                t.add(t1);
            }
        }
        return t;
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mDataList.size() > 0) mDataList.clear();
//		removeTempFromPref();

        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString().intern()) || s == null) {
            righTextView.setBackground(getDrawable(R.drawable.bg_post_right));
            righTextView.setTextColor(getColor(R.color.item_showmore_click));
            publishButton.setClickable(false);
        } else {
            righTextView.setBackground(getDrawable(R.drawable.bg_post_right_orange));
            righTextView.setTextColor(getColor(R.color.white));
            publishButton.setClickable(true);
        }
    }

    @Override
    public void onCamere() {
        AlbumUtils.cameraPicture(this, new AlbumFilesInterface() {
            @Override
            public void setAlbumFiles(ArrayList<AlbumFile> result) {

            }

            @Override
            public void setFilePath(String path) {
                bmp.add(bmp.size() - 1, ImageUtils.getimage(BitmapUtil.amendRotatePhoto(path,PublishPostActivity.this)));
                mAdapter.setData(bmp);
                mDataList.add(path);
            }
        });
    }

    @Override
    public void onAlbum() {
        AlbumUtils.selectAlbume(PublishPostActivity.this, albumFiles, new AlbumFilesInterface() {
            @Override
            public void setAlbumFiles(ArrayList<AlbumFile> result) {
                if (result != null && result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        if (bmp.size() < 7) {
                            bmp.add(bmp.size() - 1, ImageUtils.getimage(result.get(i).getPath()));
                            mDataList.add(result.get(i).getPath());
                        }
                    }
                }
                mAdapter.setData(bmp);
            }

            @Override
            public void setFilePath(String path) {

            }
        });
    }

    @Override
    public void onItemDelListener(int position, ImageView picture) {
        mAdapter.deleteItem(position);
        bmp.remove(position);
        mDataList.remove(position);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        if (bmp.size()-1==position)
        mPopupwindow.show(findViewById(R.id.scrollView));
        InputSoftUtil.hideSoftInput(this);
    }

//	private void selectImages(){
//		Album.image(this) // Image selection.
//				.multipleChoice()
//				.camera(false)
//				.columnCount(4)
//				.selectCount(6)
//				.checkedList(albumFiles)
//				.filterSize() // Filter the file size.
//				.filterMimeType() // Filter file format.
//				.afterFilterVisibility() // Show the filtered files, but they are not available.
//				.onResult(new Action<ArrayList<AlbumFile>>() {
//					@Override
//					public void onAction(@NonNull ArrayList<AlbumFile> result) {
//						if (mDataList.size()>0)mDataList.clear();
//						for (int i=0;i<result.size();i++){
//							mDataList.add(result.get(i).getPath());
//						}
//
//					}
//				})
//				.onCancel(new Action<String>() {
//					@Override
//					public void onAction(@NonNull String result) {
//
//					}
//				})
//				.start();
//	}

//	private void selectImages() {
//
//		Album.album(this)
//
//				.multipleChoice()
//
//				.columnCount(4)
//
//				.selectCount(6)
//
//				.camera(true)
//
//				.cameraVideoQuality(1)
//
//				.cameraVideoLimitDuration(Integer.MAX_VALUE)
//
//				.cameraVideoLimitBytes(Integer.MAX_VALUE)
//
//				.checkedList(albumFiles)
//
//				.onResult(new Action<ArrayList<AlbumFile>>() {
//
//					@Override
//
//					public void onAction(@NonNull ArrayList<AlbumFile> result) {
//						albumFiles = result;
//						if (mDataList.size()>0)mDataList.clear();
//							for (int i=0;i<result.size();i++){
//							mDataList.add(result.get(i).getPath());
//						}
//						mAdapter.notifyDataSetChanged();
//					}
//
//				})
//
//				.onCancel(new Action<String>() {
//					@Override
//
//					public void onAction(@NonNull String result) {
//					}
//				})
//				.start();
//
//	}
}
