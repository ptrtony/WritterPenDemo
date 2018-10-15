package com.android.bluetown.home.hot.model.act;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.widget.GridView;
import android.widget.TextView;

import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.alibaba.fastjson.JSON;
import com.android.annotations.NonNull;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.GridViewCheckAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.DemandTypeBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.datewheel.SevenMinDateDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.mulphoto.adapter.ImagePublishAdapter;
import com.android.bluetown.mulphoto.model.ImageItem;
import com.android.bluetown.mulphoto.utils.IntentConstants;
import com.android.bluetown.multiphoto.ImageBucketChooseActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.SelfServiceTypeResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.ImageUtils;
import com.android.bluetown.view.NoScrollGridView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

/**
 * 
 * @ClassName: PublishNewDemandActivity
 * @Description:TODO(HomeActivity-企业发布信息-发布新需求)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:34:18
 * 
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class PublishNewDemandActivity extends TitleBarActivity implements
		OnClickListener {
	private EditText demandTitle, demandDes;
	private NoScrollGridView showDemandImgGrid, showDemandTypeGrid;
	private TextView demandDeadlineDate;
	private Button commit;
	private GridViewCheckAdapter mAdapter;
	private View avatarView;
	// 储存图片集的list
	public ArrayList<String> mDataList = new ArrayList<String>();
	public ArrayList<AlbumFile> albumFiles = new ArrayList<>();
	// 展示图片的适配器
	private ImagePublishAdapter mImageAdapter;

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";
	private ArrayList<DemandTypeBean> modelsList = null;
	private List<Bitmap> bmp = new ArrayList<Bitmap>();
	private List<String> imgsList;
	private String endtime;
	private String selection = "0";
	private int ChoosePosition;
	private DateRecevier receiver;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	private SharePrefUtils prefUtils;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
//				removeTempFromPref();
				mDataList.clear();
				Intent intent = new Intent();
				intent.setClass(PublishNewDemandActivity.this,
						DemanPublishSuccessActivity.class);
				startActivity(intent);
				finish();
				break;
			case 2:
				SelfServiceTypeResult result = (SelfServiceTypeResult) msg.obj;
				TipDialog.showDialog(PublishNewDemandActivity.this,
						R.string.tip, R.string.confirm, result.getRepMsg());
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_company_info_public_new);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		initData();
		IntentFilter filter = new IntentFilter();
		filter.addAction("publish_date_choice");
		receiver = new DateRecevier();
		registerReceiver(receiver, filter);
	}

	private void initUIView() {
		db = FinalDb.create(this);
		demandTitle = (EditText) findViewById(R.id.demandTitle);
		demandDes = (EditText) findViewById(R.id.demandDes);
		showDemandImgGrid = (NoScrollGridView) findViewById(R.id.showDemandImgGrid);
		// 初始化临时添加图片的按钮
		mImageAdapter = new ImagePublishAdapter(this, mDataList);
		showDemandImgGrid.setAdapter(mImageAdapter);
		showDemandImgGrid.setOnItemClickListener(demandImgsClickListener);
		showDemandTypeGrid = (NoScrollGridView) findViewById(R.id.showDemandTypeGrid);
		demandDeadlineDate = (TextView) findViewById(R.id.demandDeadlineDate);
		commit = (Button) findViewById(R.id.demandCommit);
		commit.setOnClickListener(this);
		demandDeadlineDate.setOnClickListener(this);

	}

	private void initData() {
		prefUtils = new SharePrefUtils(this);
//		getTempFromPref();
//		List<ImageItem> incomingDataList = (List<ImageItem>) getIntent()
//				.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
//		if (incomingDataList != null) {
//			mDataList.addAll(incomingDataList);
//		}
		try {
			// 将存储的数据重新设置
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				selection = bundle.get("needType").toString();
				endtime = bundle.get("endTime").toString();
				String demandDesStr = bundle.get("demandDes").toString();
				String demandTitleStr = bundle.get("demandTitle").toString();
				demandDeadlineDate.setText(endtime);
				demandDes.setText(demandDesStr);
				demandTitle.setText(demandTitleStr);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("PublishNewDemandActivity", "list startActivity");
		}
		setData(this, showDemandTypeGrid);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 当在ImageZoomActivity中删除图片时，返回这里需要刷新
		notifyDataChanged();
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		if (TextUtils.isEmpty(userId)) {
			commit.setClickable(false);
			commit.setBackground(getResources().getDrawable(
					R.drawable.gray_btn_bg1));
			TipDialog.showDialogNoClose(PublishNewDemandActivity.this,
					R.string.tip, R.string.confirm, R.string.login_info_tip,
					LoginActivity.class);
		} else {
			commit.setClickable(true);
			commit.setBackground(getResources().getDrawable(
					R.drawable.blue_darker_btn_bg));
		}

	}

	public void setData(Context context, GridView gridView) {
		if (modelsList == null) {
			modelsList = new ArrayList<DemandTypeBean>();
		}
		modelsList.clear();
		// 设置默认选择的需求分类
		int selectIndex = Integer.parseInt(selection);
		switch (selectIndex) {
		case 0:
			modelsList.add(new DemandTypeBean(R.drawable.demand_recruit,
					R.string.demand_recruit, true));
			modelsList.add(new DemandTypeBean(R.drawable.demand_study,
					R.string.demand_study, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_sale,
					R.string.demand_sale, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_peo,
					R.string.demand_peo, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_outing,
					R.string.demand_outing, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_meeing,
					R.string.demand_meeting, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_makefriends,
					R.string.demand_makefriends, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_more,
					R.string.demand_others, false));
			break;
		case 1:
			modelsList.add(new DemandTypeBean(R.drawable.demand_recruit,
					R.string.demand_recruit, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_study,
					R.string.demand_study, true));
			modelsList.add(new DemandTypeBean(R.drawable.demand_sale,
					R.string.demand_sale, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_peo,
					R.string.demand_peo, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_outing,
					R.string.demand_outing, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_meeing,
					R.string.demand_meeting, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_makefriends,
					R.string.demand_makefriends, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_more,
					R.string.demand_others, false));
			break;
		case 2:
			modelsList.add(new DemandTypeBean(R.drawable.demand_recruit,
					R.string.demand_recruit, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_study,
					R.string.demand_study, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_sale,
					R.string.demand_sale, true));
			modelsList.add(new DemandTypeBean(R.drawable.demand_peo,
					R.string.demand_peo, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_outing,
					R.string.demand_outing, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_meeing,
					R.string.demand_meeting, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_makefriends,
					R.string.demand_makefriends, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_more,
					R.string.demand_others, false));
			break;
		case 3:
			modelsList.add(new DemandTypeBean(R.drawable.demand_recruit,
					R.string.demand_recruit, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_study,
					R.string.demand_study, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_sale,
					R.string.demand_sale, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_peo,
					R.string.demand_peo, true));
			modelsList.add(new DemandTypeBean(R.drawable.demand_outing,
					R.string.demand_outing, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_meeing,
					R.string.demand_meeting, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_makefriends,
					R.string.demand_makefriends, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_more,
					R.string.demand_others, false));
			break;
		case 4:
			modelsList.add(new DemandTypeBean(R.drawable.demand_recruit,
					R.string.demand_recruit, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_study,
					R.string.demand_study, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_sale,
					R.string.demand_sale, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_peo,
					R.string.demand_peo, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_outing,
					R.string.demand_outing, true));
			modelsList.add(new DemandTypeBean(R.drawable.demand_meeing,
					R.string.demand_meeting, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_makefriends,
					R.string.demand_makefriends, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_more,
					R.string.demand_others, false));
			break;
		case 5:
			modelsList.add(new DemandTypeBean(R.drawable.demand_recruit,
					R.string.demand_recruit, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_study,
					R.string.demand_study, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_sale,
					R.string.demand_sale, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_peo,
					R.string.demand_peo, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_outing,
					R.string.demand_outing, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_meeing,
					R.string.demand_meeting, true));
			modelsList.add(new DemandTypeBean(R.drawable.demand_makefriends,
					R.string.demand_makefriends, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_more,
					R.string.demand_others, false));
			break;
		case 6:
			modelsList.add(new DemandTypeBean(R.drawable.demand_recruit,
					R.string.demand_recruit, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_study,
					R.string.demand_study, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_sale,
					R.string.demand_sale, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_peo,
					R.string.demand_peo, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_outing,
					R.string.demand_outing, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_meeing,
					R.string.demand_meeting, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_makefriends,
					R.string.demand_makefriends, true));
			modelsList.add(new DemandTypeBean(R.drawable.demand_more,
					R.string.demand_others, false));
			break;
		case 7:
			modelsList.add(new DemandTypeBean(R.drawable.demand_recruit,
					R.string.demand_recruit, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_study,
					R.string.demand_study, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_sale,
					R.string.demand_sale, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_peo,
					R.string.demand_peo, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_outing,
					R.string.demand_outing, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_meeing,
					R.string.demand_meeting, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_makefriends,
					R.string.demand_makefriends, false));
			modelsList.add(new DemandTypeBean(R.drawable.demand_more,
					R.string.demand_others, true));
			break;
		default:
			break;
		}
		mAdapter = new GridViewCheckAdapter(context, modelsList);
		gridView.setAdapter(mAdapter);
		// 设置item点击事件
		gridView.setOnItemClickListener(demandTypeClickListener);

	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description:初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.publish_demand);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.demandCommit:

			if (TextUtils.isEmpty(userId)) {
				commit.setClickable(false);
				commit.setBackground(getResources().getDrawable(
						R.drawable.gray_btn_bg1));
				TipDialog.showDialogNoClose(PublishNewDemandActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			} else {
				String needTitle = demandTitle.getText().toString();
				String needDes = demandDes.getText().toString();
				if (TextUtils.isEmpty(needTitle)) {
					TipDialog.showDialog(PublishNewDemandActivity.this,
							R.string.tip, R.string.confirm,
							R.string.demand_title_empty);
					return;
				}
				if (needTitle.length() > 15) {
					TipDialog.showDialog(PublishNewDemandActivity.this,
							R.string.tip, R.string.confirm,
							R.string.demand_title_length_tip);
					return;
				}

				if (selection.isEmpty()) {
					TipDialog.showDialog(PublishNewDemandActivity.this,
							R.string.tip, R.string.confirm,
							R.string.demand_type_empty);
					return;
				}
				if (TextUtils.isEmpty(needDes)) {
					TipDialog.showDialog(PublishNewDemandActivity.this,
							R.string.tip, R.string.confirm,
							R.string.demand_des_empty);
					return;
				}
				if (needDes.length() > 500) {
					TipDialog.showDialog(PublishNewDemandActivity.this,
							R.string.tip, R.string.confirm,
							R.string.demand_des_length_tip);
					return;
				}
				commit.setClickable(true);
				commit.setBackground(getResources().getDrawable(
						R.drawable.blue_darker_btn_bg));
				imgList();
				publishNewDemand(userId);
			}

			break;
		case R.id.demandDeadlineDate:
			SevenMinDateDialog timeChoiceDialog = new SevenMinDateDialog(
					PublishNewDemandActivity.this);
			timeChoiceDialog.Show();
			break;

		default:
			break;
		}
	}

	private void publishNewDemand(String userId) {
		// 访问接口交互
		params.put("userId", userId);
		params.put("title", demandTitle.getText().toString());
		params.put("needType", selection);
		params.put("endTime", endtime);
		params.put("content", demandDes.getText().toString());
		if (imgsList != null && imgsList.size() > 0) {
			params.put("pictures", AbJsonUtil.toJson(imgsList));
		}
		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.COMPANY_DEMAND_PUBLISH, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						SelfServiceTypeResult result = (SelfServiceTypeResult) AbJsonUtil
								.fromJson(s, SelfServiceTypeResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							handler.sendEmptyMessage(1);
						} else {
							Message message = new Message();
							message.obj = result;
							message.what = 2;
							handler.sendMessage(message);
						}

					}

				});
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
//	private void getTempFromPref() {
//		String prefStr = prefUtils.getString(SharePrefUtils.PREF_TEMP_IMAGES,
//				null);
//		if (!TextUtils.isEmpty(prefStr)) {
//			ArrayList<AlbumFile> tempImages = JSON.parseArray(prefStr,
//					AlbumFile.class);
//			mDataList = tempImages;
//		}
//	}

//	private void saveTempToPref() {
//		String prefStr = JSON.toJSONString(mDataList);
//		prefUtils.setString(SharePrefUtils.PREF_TEMP_IMAGES, prefStr);
//	}

//	private void removeTempFromPref() {
//		prefUtils.remove(SharePrefUtils.PREF_TEMP_IMAGES);
//	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		saveTempToPref();
	}

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

	public void takePhoto() {
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
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (mDataList.size() < SharePrefUtils.MAX_IMAGE_SIZE
					&& resultCode == -1 && !TextUtils.isEmpty(path)) {
//				ImageItem item = new ImageItem();
//				item.sourcePath = path;
				mDataList.add(path);
			}
			break;
		}
	}

	private void notifyDataChanged() {
		mImageAdapter.notifyDataSetChanged();
	}

	/**
	 * show pick dialog
	 */
	private void showPickDialog() {
		avatarView = getLayoutInflater().inflate(R.layout.choose_avatar, null);
		Button albumButton = (Button) avatarView
				.findViewById(R.id.choose_album);
		Button camButton = (Button) avatarView.findViewById(R.id.choose_cam);
		Button cancelButton = (Button) avatarView
				.findViewById(R.id.choose_cancel);
		albumButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(PublishNewDemandActivity.this);
//				// 从相册中去获取
//				Intent intent = new Intent(PublishNewDemandActivity.this,
//						ImageBucketChooseActivity.class);
//				intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
//						getAvailableSize());
//				intent.putExtra("class", "publishDemand");
//				Bundle bundle = new Bundle();
//				bundle.putString("needType", selection);
//				bundle.putString("endTime", demandDeadlineDate.getText()
//						.toString());
//				bundle.putString("demandDes", demandDes.getText().toString());
//				bundle.putString("demandTitle", demandTitle.getText()
//						.toString());
//				intent.putExtras(bundle);
//				startActivity(intent);
				selectImages();
			}

		});

		camButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(PublishNewDemandActivity.this);
				takePhoto();
			}

		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(PublishNewDemandActivity.this);
			}

		});
		AbDialogUtil.showDialog(avatarView, Gravity.BOTTOM);
	}

	/**
	 * 
	 * @Title: imgToBase64
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bitmap
	 * @return
	 * @throws
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
		if (mDataList.size() != 0) {
			for (int i = 0; i < mDataList.size(); i++) {
				if (mDataList.get(i) != null) {
					Bitmap bitmap = ImageUtils
							.getimage(mDataList.get(i));
					bmp.add(bitmap);
				}
			}
			imgsList = getImageBase64(bmp);
		}
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

	DemandTypeBean itemMap, chooseItemMap;
	private OnItemClickListener demandTypeClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			// 当前点击的与上次点击的相同
			if (position == ChoosePosition) {
				// 设置当前点击的状态（如果之前为true，则设置为false，如果为false则设置为true）
				DemandTypeBean itemMap = modelsList.get(position);
				if (itemMap.isCheck()) {
					itemMap.setCheck(false);
					selection = "";
				} else {
					itemMap.setCheck(true);
					selection = ChoosePosition + "";
				}
			} else {
				// 设置当前点击的为点击状态true
				DemandTypeBean itemMap = modelsList.get(position);
				itemMap.setCheck(true);
				// 设置之前点击的为false
				chooseItemMap = modelsList.get(ChoosePosition);
				chooseItemMap.setCheck(false);
				ChoosePosition = position;
				selection = ChoosePosition + "";
			}
			mAdapter.notifyDataSetChanged();

		}
	};
	private OnItemClickListener demandImgsClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (position == getDataSize()) {
				showPickDialog();
			} else {
				// 删除图片
				mDataList.remove(position);
				notifyDataChanged();
			}

		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		removeTempFromPref();
		finish();

		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	public class DateRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("publish_date_choice")) {
				endtime = intent.getStringExtra("date_string");
				demandDeadlineDate.setText(endtime);
			}
		}
	}

	private void selectImages() {

		Album.album(this)

				.multipleChoice()

				.columnCount(4)

				.selectCount(6)

				.camera(true)

				.cameraVideoQuality(1)

				.cameraVideoLimitDuration(Integer.MAX_VALUE)

				.cameraVideoLimitBytes(Integer.MAX_VALUE)

				.checkedList(albumFiles)

				.onResult(new Action<ArrayList<AlbumFile>>() {

					@Override

					public void onAction(@NonNull ArrayList<AlbumFile> result) {
						albumFiles = result;
						if (mDataList.size()>0)mDataList.clear();
						for (int i=0;i<result.size();i++){
							mDataList.add(result.get(i).getPath());
						}
						mAdapter.notifyDataSetChanged();
					}

				})

				.onCancel(new Action<String>() {
					@Override

					public void onAction(@NonNull String result) {
					}
				})
				.start();

	}

}
