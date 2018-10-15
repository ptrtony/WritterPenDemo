package com.android.bluetown.multiphoto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.mulphoto.adapter.ImageBucketAdapter;
import com.android.bluetown.mulphoto.model.ImageBucket;
import com.android.bluetown.mulphoto.utils.ImageFetcher;
import com.android.bluetown.mulphoto.utils.IntentConstants;
import com.android.bluetown.pref.SharePrefUtils;

/**
 * 选择相册
 * 
 */

public class ImageBucketChooseActivity extends TitleBarActivity implements
		OnItemClickListener {
	private ImageFetcher mHelper;
	private List<ImageBucket> mDataList = new ArrayList<ImageBucket>();
	private ListView mListView;
	private ImageBucketAdapter mAdapter;
	private int availableSize;
	private String className, groupId, postTitle, postContent;
	private Bundle publishProductBundle, publishDemandBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentView(R.layout.act_image_bucket_choose);
		BlueTownExitHelper.addActivity(this);
		initData();
		initView();
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.gallery);
		setTitleLayoutBgRes(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void initData() {
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
		mHelper = ImageFetcher.getInstance(this);
		mDataList = mHelper.getImagesBucketList(false);
		availableSize = getIntent().getIntExtra(
				IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
				SharePrefUtils.MAX_IMAGE_SIZE);
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mAdapter = new ImageBucketAdapter(this, mDataList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	private void selectOne(int position) {
		int size = mDataList.size();
		for (int i = 0; i != size; i++) {
			if (i == position)
				mDataList.get(i).selected = true;
			else {
				mDataList.get(i).selected = false;
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		selectOne(arg2);
		Intent intent = new Intent(ImageBucketChooseActivity.this,
				ImageChooseActivity.class);
		intent.putExtra("class", className);
		if (className.equals("publishProduct")) {
			intent.putExtras(publishProductBundle);

		} else if (className.equals("productPost")) {
//			intent.putExtra("groupId", groupId);
			intent.putExtra("postTitle", postTitle);
			intent.putExtra("postContent", postContent);
		} else if (className.equals("publishDemand")) {
			intent.putExtras(publishDemandBundle);
		}
		intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
				(Serializable) mDataList.get(arg2).imageList);
		intent.putExtra(IntentConstants.EXTRA_BUCKET_NAME,
				mDataList.get(arg2).bucketName);
		intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, availableSize);
		startActivity(intent);
	}
}
