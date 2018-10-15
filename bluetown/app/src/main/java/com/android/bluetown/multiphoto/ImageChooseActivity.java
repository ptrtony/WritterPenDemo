package com.android.bluetown.multiphoto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.circle.activity.PublishPostActivity;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.home.hot.model.act.PublishNewDemandActivity;
import com.android.bluetown.home.main.model.act.PublishProductActivity;
import com.android.bluetown.mulphoto.adapter.ImageGridAdapter;
import com.android.bluetown.mulphoto.model.ImageItem;
import com.android.bluetown.mulphoto.utils.IntentConstants;
import com.android.bluetown.pref.SharePrefUtils;

/**
 * 图片选择
 * 
 */
@SuppressWarnings("unchecked")
public class ImageChooseActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener {
	private List<ImageItem> mDataList = new ArrayList<ImageItem>();
	private String className, mBucketName, postTitle, postContent;
	private int availableSize;
	private GridView mGridView;
	private ImageGridAdapter mAdapter;
	private Button mFinishBtn;
	private HashMap<String, ImageItem> selectedImgs = new HashMap<String, ImageItem>();
	private Bundle publishProductBundle, publishDemandBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentView(R.layout.act_image_choose);
		BlueTownExitHelper.addActivity(this);
		initGalleryImgData();
		initView();
	}

	private void initGalleryImgData() {
		mDataList = (List<ImageItem>) getIntent().getSerializableExtra(
				IntentConstants.EXTRA_IMAGE_LIST);
		className = getIntent().getStringExtra("class");
		if (className.equals("publishProduct")) {
			publishProductBundle = getIntent().getExtras();
		} else if (className.equals("productPost")) {
//			groupId = getIntent().getStringExtra("groupId");
			postTitle = getIntent().getStringExtra("postTitle");
			postContent = getIntent().getStringExtra("postContent");
		} else if (className.equals("publishDemand")) {
			publishDemandBundle = getIntent().getExtras();
		}
		if (mDataList == null)
			mDataList = new ArrayList<ImageItem>();
		mBucketName = getIntent().getStringExtra(
				IntentConstants.EXTRA_BUCKET_NAME);
		if (TextUtils.isEmpty(mBucketName)) {
			mBucketName = "请选择";
		}
		availableSize = getIntent().getIntExtra(
				IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
				SharePrefUtils.MAX_IMAGE_SIZE);
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void initView() {
		setTitleView(mBucketName);
		setTitleLayoutBgRes(R.color.title_bg_blue);
		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mFinishBtn = (Button) findViewById(R.id.finish_btn);
		mFinishBtn.setOnClickListener(this);
		mGridView.setOnItemClickListener(this);
		mAdapter = new ImageGridAdapter(ImageChooseActivity.this, mDataList);
		mGridView.setAdapter(mAdapter);
		mFinishBtn.setText("完成" + "(" + selectedImgs.size() + "/"
				+ availableSize + ")");
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.finish_btn:
			//如果没有选择照片  提示请选择图片
			if ( selectedImgs.size()==0){
				TipDialog.showDialog(ImageChooseActivity.this,
						R.string.tip, R.string.confirm,
						R.string.image_choose_info);
				return;
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			if (className.equals("publishProduct")) {
				intent.putExtras(publishProductBundle);
				intent.setClass(ImageChooseActivity.this,
						PublishProductActivity.class);
			} else if (className.equals("productPost")) {
//				intent.putExtra("groupId", groupId);
				intent.putExtra("postTitle", postTitle);
				intent.putExtra("postContent", postContent);
				intent.setClass(ImageChooseActivity.this,
						PublishPostActivity.class);
			} else if (className.equals("publishDemand")) {
				intent.putExtras(publishDemandBundle);
				intent.setClass(ImageChooseActivity.this,
						PublishNewDemandActivity.class);
			} else if(className.equals("productPost")){
//				intent.putExtra("groupId", groupId);
				intent.putExtra("postTitle", postTitle);
				intent.putExtra("postContent", postContent);
				intent.setClass(ImageChooseActivity.this,
						PublishPostActivity.class);
			}
			intent.putExtra(
					IntentConstants.EXTRA_IMAGE_LIST,
					(Serializable) new ArrayList<ImageItem>(selectedImgs
							.values()));
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ImageItem item = mDataList.get(arg2);
		if (item.isSelected) {
			item.isSelected = false;
			selectedImgs.remove(item.imageId);
		} else {
			if (selectedImgs.size() >= availableSize) {
				Toast.makeText(ImageChooseActivity.this,
						"最多选择" + availableSize + "张图片", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			item.isSelected = true;
			selectedImgs.put(item.imageId, item);
		}

		mFinishBtn.setText("完成" + "(" + selectedImgs.size() + "/"
				+ availableSize + ")");
		mAdapter.notifyDataSetChanged();
	}
}