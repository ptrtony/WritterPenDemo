package com.android.bluetown.home.main.model.act;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tsz.afinal.FinalDb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.alibaba.fastjson.JSON;
import com.android.annotations.NonNull;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ComplainAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.circle.activity.PublishPostActivity;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.img.AlbumFilesInterface;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.mulphoto.adapter.ImagePublishAdapter;
import com.android.bluetown.mulphoto.model.ImageItem;
import com.android.bluetown.mulphoto.utils.IntentConstants;
import com.android.bluetown.multiphoto.ImageBucketChooseActivity;
import com.android.bluetown.multiphoto.MediaLoader;
import com.android.bluetown.popup.AlbumPopupWindow;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.SelfServiceTypeResult;
import com.android.bluetown.spinner.custom.AbstractSpinerAdapter;
import com.android.bluetown.spinner.custom.SpinerPopWindow;
import com.android.bluetown.utils.AlbumUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DisplayUtils;
import com.android.bluetown.utils.ImageCompress.BitmapUtil;
import com.android.bluetown.utils.ImageUtils;
import com.android.bluetown.utils.InputSoftUtil;
import com.android.bluetown.view.NoScrollGridView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import static com.yanzhenjie.album.AlbumFile.TYPE_IMAGE;

@SuppressLint("NewApi")
public class PublishProductActivity extends TitleBarActivity implements
        OnClickListener, ComplainAdapter.OnItemClickListener, AlbumPopupWindow.OnClickListener {
    private static final String TAG = "PublishProductActivity";
    private EditText productName, linkTel, productPrice, productDes,
            productNumber;
    private TextView productType;
    private EditText normsType;
    private RecyclerView productImgGrid;
    private Button publishBtn;
    private View avatarView;
    // 储存图片集的list
    public ArrayList<String> mDataList = new ArrayList<String>();
    private ArrayList<AlbumFile> albumFiles;
    // 展示图片的适配器
    private ComplainAdapter mAdapter;
    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";
    private ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
    private List<String> imgsList;
    private String productTypeId;
    private ArrayList<String> typesArrayList;
    private ArrayList<String> typeList;
    private AlbumPopupWindow mPopupwindow;
    private SpinerPopWindow productTypeWindow;
    private String userId;
    private FinalDb db;
    private List<MemberUser> users;
    private SharePrefUtils prefUtils;
    private Pattern pattern;
    private String productNameStr, linkTelStr, productPriceStr, productTypeStr,
            productDesStr, productNumberStr, normsTypeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_publish_product);
        BlueTownExitHelper.addActivity(this);
        pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
        initUIView();
        initData();
    }

    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
        setBackImageView();
        setTitleLayoutBg(R.color.title_bg_blue);
        setTitleView(R.string.public_goods);
        rightImageLayout.setVisibility(View.INVISIBLE);
    }

    private void initUIView() {
        // TODO Auto-generated method stub
        db = FinalDb.create(this);
        users = db.findAll(MemberUser.class);
        if (users != null && users.size() != 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                userId = user.getMemberId();
            }
        }
        typesArrayList = getIntent().getStringArrayListExtra("typesId");
        typeList = getIntent().getStringArrayListExtra("types");
        imgsList = new ArrayList<String>();
        productName = (EditText) findViewById(R.id.productName);
        linkTel = (EditText) findViewById(R.id.linkTel);
        productPrice = (EditText) findViewById(R.id.productPrice);
        productType = (TextView) findViewById(R.id.productType);
        normsType = (EditText) findViewById(R.id.normsType);
        productDes = (EditText) findViewById(R.id.productDes);
        productNumber = (EditText) findViewById(R.id.number);
        productImgGrid =  findViewById(R.id.productImgGrid);
        productImgGrid.setLayoutManager(new GridLayoutManager(this,4));
        if (productImgGrid.getRecycledViewPool()!=null){
            productImgGrid.getRecycledViewPool().setMaxRecycledViews(0,10);
        }
        bmp.add(ImageUtils.decodeSampledBitmapFromResource(getResources(),R.drawable.add_pic, DisplayUtils.dip2px(this,80)
                ,DisplayUtils.dip2px(this,80)));

        // 初始化临时添加图片的按钮
        mAdapter = new ComplainAdapter(this, this);
        mAdapter.setData(bmp);
        productImgGrid.setAdapter(mAdapter);
//        productImgGrid.setOnItemClickListener(this);
        publishBtn = (Button) findViewById(R.id.publishProduct);
        publishBtn.setOnClickListener(this);
        productType.setOnClickListener(this);
        normsType.setOnClickListener(this);
        productTypeWindow = new SpinerPopWindow(this, productType);
        mPopupwindow = new AlbumPopupWindow(this,this);
        if (typeList.contains("全部")) {
            typeList.remove("全部");
        }

        for (int i = 0; i < typesArrayList.size(); i++) {
            if (TextUtils.isEmpty(typesArrayList.get(i))) {
                typesArrayList.remove(i);
            }
        }
        productTypeWindow.refreshData(typeList, 0);

        productTypeWindow.setItemListener(new onSipnnerItemClick(typeList,
                productType));

        productPrice.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence src, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                String str1 = src.toString();
                String str2 = dest.toString();
                if (!TextUtils.isEmpty(str2 + str1)
                        && pattern.matcher(str2 + str1).matches()) {
                    if (Double.parseDouble(str2 + str1) <= 10000000.00) {
                        String[] splitArray = str2.split("\\.");
                        if (splitArray.length > 1) {
                            String dotValue = splitArray[1];
                            if (dotValue.length() >= 2) {
                                str1 = "";
                            }
                        }
                    } else {
                        str1 = "";
                    }
                } else {
                    str1 = "";
                }
                return str1;
            }
        }});

    }

    private void initData() {
        prefUtils = new SharePrefUtils(this);
//        getTempFromPref();
//        List<ImageItem> incomingDataList = (List<ImageItem>) getIntent()
//                .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
//        if (incomingDataList != null) {
//            mDataList.addAll(incomingDataList);
//        }
        try {
            // 将存储的数据重新设置
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                productNameStr = bundle.get("productName").toString();
                linkTelStr = bundle.get("linkTel").toString();
                productPriceStr = bundle.get("productPrice").toString();
                productTypeStr = bundle.get("productType").toString();
                productDesStr = bundle.get("productDes").toString();
                productNumberStr = bundle.get("productNumber").toString();
                normsTypeStr = bundle.get("normsType").toString();
                productName.setText(productNameStr);
                linkTel.setText(linkTelStr);
                productPrice.setText(productPriceStr);
                productType.setText(productTypeStr);
                productDes.setText(productDesStr);
                productNumber.setText(productNumberStr);
                normsType.setText(normsTypeStr);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("PublishProductActivity", "list startActivity");
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (!TextUtils.isEmpty(userId)) {
            publishBtn.setClickable(true);
            publishBtn.setBackground(getResources().getDrawable(
                    R.drawable.blue_darker_btn_bg));
        } else {
            publishBtn.setClickable(false);
            publishBtn.setBackground(getResources().getDrawable(
                    R.drawable.gray_btn_bg1));
            TipDialog.showDialogNoClose(PublishProductActivity.this,
                    R.string.tip, R.string.confirm, R.string.login_info_tip,
                    LoginActivity.class);
        }
        // 当在ImageZoomActivity中删除图片时，返回这里需要刷新
        notifyDataChanged();
    }

    // 将数据保存到outState对象中, 该对象会在重建activity时传递给onCreate方法
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
//        saveTempToPref();
    }

    // 恢复数据
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        saveTempToPref();
    }

    /**
     * 获取临时存储的文件
     */
//    private void getTempFromPref() {
//        String prefStr = prefUtils.getString(SharePrefUtils.PREF_TEMP_IMAGES,
//                null);
//        if (!TextUtils.isEmpty(prefStr)) {
//            List<ImageItem> tempImages = JSON.parseArray(prefStr,
//                    ImageItem.class);
//            mDataList = tempImages;
//        }
//    }

//    private void saveTempToPref() {
//        String prefStr = JSON.toJSONString(mDataList);
//        prefUtils.setString(SharePrefUtils.PREF_TEMP_IMAGES, prefStr);
//    }

//    private void removeTempFromPref() {
//        prefUtils.remove(SharePrefUtils.PREF_TEMP_IMAGES);
//    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        mDataList.clear();
        super.onBackPressed();
    }

    private int getDataSize() {
        return mDataList == null ? 0 : mDataList.size();
    }

    private int getAvailableSize() {
        int availSize = SharePrefUtils.MAX_IMAGE_SIZE - mDataList.size();
        if (availSize >= 0) {
            return availSize;
        }
        return 0;
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,
//                            long id) {
//        // TODO Auto-generated method stub
//        if (position == getDataSize()) {
//            showPickDialog();
//        } else {
//            // 删除图片
//            mDataList.remove(position);
//            notifyDataChanged();
//        }
//
//    }

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
//                // 从相册中去获取
////                Intent intent = new Intent(PublishProductActivity.this,
////                        ImageBucketChooseActivity.class);
////                intent.putExtra("class", "publishProduct");
////                intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
////                        getAvailableSize());
////                Bundle bundle = new Bundle();
////                bundle.putString("productName", productName.getText()
////                        .toString());
////                bundle.putString("linkTel", linkTel.getText().toString());
////                bundle.putString("productPrice", productPrice.getText()
////                        .toString());
////                bundle.putString("productType", productType.getText()
////                        .toString());
////                bundle.putString("productDes", productDes.getText().toString());
////                bundle.putString("productNumber", productNumber.getText()
////                        .toString());
////                bundle.putString("normsType", normsType.getText().toString());
////                bundle.putStringArrayList("typesId", typesArrayList);
////                bundle.putStringArrayList("types", typeList);
////                intent.putExtras(bundle);
////                startActivity(intent);
//                AbDialogUtil.removeDialog(PublishProductActivity.this);
//                selectImages();
//            }
//
//        });
//
//        camButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                AbDialogUtil.removeDialog(PublishProductActivity.this);
//                takePhoto();
//            }
//
//        });
//
//        cancelButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                AbDialogUtil.removeDialog(PublishProductActivity.this);
//            }
//
//        });
//        AbDialogUtil.showDialog(avatarView, Gravity.BOTTOM);
//    }
//
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
//        if (mDataList.size() != 0) {
//            for (int i = 0; i < mDataList.size(); i++) {
//                if (mDataList.get(i) != null) {
//                    Bitmap bitmap = ImageUtils
//                            .getimage(mDataList.get(i));
//                    bmp.add(bitmap);
//                }
//            }
//            imgsList = getImageBase64(bmp);
//        }
        if (bmp.size()>0) imgsList = getImageBase64(bmp);
    }

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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.publishProduct:
                publishBtn.setClickable(false);
                if (!TextUtils.isEmpty(userId)) {
                    publishBtn.setBackground(getResources().getDrawable(
                            R.drawable.blue_darker_btn_bg));
                    final String productNameStr = productName.getText().toString();
                    final String linkTelStr = linkTel.getText().toString();
                    final String productPriceStr = productPrice.getText()
                            .toString();
                    final String productTypeStr = productType.getText().toString();
                    final String productDesStr = productDes.getText().toString();
                    final String productNumberStr = productNumber.getText()
                            .toString();
                    final String normsTypeStr = normsType.getText().toString();
                    if (TextUtils.isEmpty(productNameStr)
                            || TextUtils.isEmpty(linkTelStr)
                            || TextUtils.isEmpty(productPriceStr)
                            || TextUtils.isEmpty(productTypeStr)
                            || TextUtils.isEmpty(productDesStr)
                            || TextUtils.isEmpty(normsTypeStr) ) {
                        TipDialog.showDialog(PublishProductActivity.this,
                                R.string.tip, R.string.confirm,
                                R.string.publish_perfect_info);
                        publishBtn.setClickable(true);
                    } else if (mDataList.size() == 0){
                        TipDialog.showDialog(PublishProductActivity.this,
                                R.string.tip, R.string.confirm,
                                R.string.input_empty_image);
                        publishBtn.setClickable(true);
                    }else {
                        if (linkTelStr.length() >30||productPriceStr.length()>30) {
                            TipDialog.showDialog(PublishProductActivity.this,
                                    R.string.tip, R.string.confirm,
                                    R.string.number_chart_limit);
                            publishBtn.setClickable(true);
                            return;
                        }

//                        if (!isTelPhoto(linkTelStr)) {
//                            TipDialog.showDialog(PublishProductActivity.this,
//                                    R.string.tip, R.string.confirm,
//                                    R.string.error_phone);
//                            publishBtn.setClickable(true);
//                            return;
//                        }

                        if (productDesStr.length() > 500) {
                            TipDialog.showDialog(PublishProductActivity.this,
                                    R.string.tip, R.string.confirm,
                                    R.string.product_des_content_tip);
                            publishBtn.setClickable(true);
                            return;
                        }
                        imgList();
                        new Thread() {
                            public void run() {
                                Looper.prepare();
                                publishProduct(productNameStr, linkTelStr,
                                        productPriceStr, productDesStr,
                                        productNumberStr, normsTypeStr);
                                Looper.loop();
                            }

                            ;
                        }.start();

                    }
                } else {
                    publishBtn.setClickable(false);
                    publishBtn.setBackground(getResources().getDrawable(
                            R.drawable.gray_btn_bg1));
                    TipDialog.showDialogNoClose(PublishProductActivity.this,
                            R.string.tip, R.string.confirm,
                            R.string.login_info_tip, LoginActivity.class);
                }

                break;
            case R.id.productType:
                InputSoftUtil.hideSoftInput(this);
                showSpinWindow(productTypeWindow, productType);
                break;
            case R.id.normsType:

                break;
            default:
                break;
        }
    }

    private void showSpinWindow(SpinerPopWindow productTypeWindow,
                                TextView productType) {
        // TODO Auto-generated method stub
        productTypeWindow.setWidth(productTypeWindow.getWidth());
        productTypeWindow.showAsDropDown(productType);
    }

    private void publishProduct(String productNameStr, String linkTelStr,
                                String productPriceStr, String productDesStr, String productNumber,
                                String normsTypeStr) {
        // 访问接口交互
        params.put("commodityName", productNameStr);
        params.put("tell", linkTelStr);
        params.put("price", productPriceStr);
        params.put("norms", normsTypeStr);
        if (TextUtils.isEmpty(productTypeId)) {
            productTypeId = typesArrayList.get(0);
        }
        params.put("typeId", productTypeId);
        params.put("content", productDesStr);
        params.put("userId", userId);
        params.put("number", productNumber);
        params.put("gardenId",
                prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
        if (imgsList != null && imgsList.size() > 0) {
            params.put("pictures", AbJsonUtil.toJson(imgsList));
        }

        httpInstance.post(Constant.HOST_URL
                        + Constant.Interface.PUBLISH_PRODUCT, params,
                new AbsHttpStringResponseListener(PublishProductActivity.this, null) {
                    @Override
                    public void onSuccess(int i, String s) {
                        SelfServiceTypeResult result = (SelfServiceTypeResult) AbJsonUtil
                                .fromJson(s, SelfServiceTypeResult.class);
                        if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                            showDialogFinishSelf(PublishProductActivity.this,
                                    R.string.tip, R.string.confirm,
                                    R.string.publish_success,
                                    FleaMarketActivity.class);
                        } else {
                            TipDialog.showDialog(PublishProductActivity.this,
                                    R.string.tip, R.string.confirm,
                                    result.getRepMsg());
                        }
                        publishBtn.setClickable(true);
                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        super.onFailure(i, s, throwable);
                        Log.i(TAG, throwable.getMessage().toString());
                        publishBtn.setClickable(true);
                    }
                });

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
                                      int confirmTextId, int contentStr, final Class<?> cls) {
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
//                removeTempFromPref();
                mDataList.clear();
                Intent intent = new Intent();
                intent.setClass(context, cls);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                context.finish();
                sweetAlertDialog.dismiss();
            }
        });
        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (mDataList.size() < SharePrefUtils.MAX_IMAGE_SIZE
                        && resultCode == -1 && !TextUtils.isEmpty(path)) {
//                    AlbumFile item = new AlbumFile();
//                    item.setPath(path);
//                    item.setMimeType();
                    mDataList.add(path);
                }
                break;
        }
    }

    @Override
    public void onItemDelListener(int position, ImageView picture) {
        mAdapter.deleteItem(position);
        mDataList.remove(position);
        bmp.remove(position);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        if (bmp.size()-1==position)
        mPopupwindow.show(view);
        InputSoftUtil.hideSoftInput(this);
    }

    @Override
    public void onCamere() {
        AlbumUtils.cameraPicture(this, new AlbumFilesInterface() {
            @Override
            public void setAlbumFiles(ArrayList<AlbumFile> result) {

            }

            @Override
            public void setFilePath(String path) {
                bmp.add(bmp.size()-1,ImageUtils.getimage(BitmapUtil.amendRotatePhoto(path,PublishProductActivity.this)));
                mDataList.add(path);
                mAdapter.setData(bmp);
            }
        });
    }

    @Override
    public void onAlbum() {
        ArrayList<AlbumFile> albumFiles = new ArrayList<>();
        AlbumUtils.selectAlbume(this, albumFiles, new AlbumFilesInterface() {
            @Override
            public void setAlbumFiles(ArrayList<AlbumFile> result) {
                for (int i=0;i<result.size();i++){
                    if (bmp.size()<7){
                        bmp.add(bmp.size()-1,ImageUtils.getimage(result.get(i).getPath()));
                        mDataList.add(result.get(i).getPath());
                    }
                }
                mAdapter.setData(bmp);
            }

            @Override
            public void setFilePath(String path) {

            }
        });
    }

    /**
     * @ClassName: onSipnnerItemClick
     * @Description:TODO(这里用一句话描述这个类的作用)
     * @author: shenyz
     * @date: 2015年8月6日 下午2:59:33
     */
    class onSipnnerItemClick implements
            AbstractSpinerAdapter.IOnItemSelectListener {
        private List<String> data;
        private TextView textView;

        public onSipnnerItemClick() {
            // TODO Auto-generated constructor stub
        }

        public onSipnnerItemClick(List<String> data, TextView textView) {
            // TODO Auto-generated constructor stub
            this.data = data;
            this.textView = textView;
        }

        @Override
        public void onItemClick(int pos) {
            // TODO Auto-generated method stub
            setItemData(data, textView, pos);
        }

        private void setItemData(List<String> data, TextView textView, int pos) {
            if (pos >= 0 && pos <= data.size()) {
                String value = data.get(pos);
                textView.setText(value);
                productTypeId = typesArrayList.get(pos);
            }

        }
    }

    /**
     * @param mobiles 电话号码
     * @return
     * @throws
     * @Title: isTelPhoto
     * @Description: TODO(判断电话号码是否有效)
     */
    private boolean isTelPhoto(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
//        removeTempFromPref();
        finish();
    }

//    private void selectImages() {
//
//            Album.album(this).multipleChoice()
//
//                .columnCount(4)
//
//                .selectCount(6)
//
//                .camera(true)
//
//                .cameraVideoQuality(1)
//
//                .cameraVideoLimitDuration(Integer.MAX_VALUE)
//
//                .cameraVideoLimitBytes(Integer.MAX_VALUE)
//
//                .checkedList(albumFiles)
//
//                .onResult(new Action<ArrayList<AlbumFile>>() {
//
//                    @Override
//
//                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
//
//                        albumFiles = result;
//
//                        if (mDataList.size()>0)mDataList.clear();
//
//                        for (int i=0;i<result.size();i++){
//
//                            mDataList.add(result.get(i).getPath());
//                        }
//
//                        mAdapter.notifyDataSetChanged();
//                    }
//
//                })
//
//                .onCancel(new Action<String>() {
//                    @Override
//
//                    public void onAction(@NonNull String result) {
//                    }
//                })
//                .start();
//
//    }
}
