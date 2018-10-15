package com.android.bluetown.surround;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.utils.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageScanActivity extends TitleBarActivity implements
		OnClickListener {
	private static final String tag = "ImagePhotoActivity";
	private List<String> dishList;
	private ViewPager pager;
	private PagerAdapter adapter;
	private ImageView iv_arrow_left;
	private ImageView iv_arrow_right;
	private int positions;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setTitleState();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_image_scan);
		BlueTownExitHelper.addActivity(this);
		Intent intent = getIntent();
		int position = intent.getIntExtra("position", 0);
		dishList = intent.getStringArrayListExtra("imagelist");
		pager = (ViewPager) this.findViewById(R.id.gl_images_view);
		adapter = new ImageAdapter();
		pager.setAdapter(adapter);
		pager.setCurrentItem(position);
		positions = pager.getCurrentItem();
		iv_arrow_left = (ImageView) this.findViewById(R.id.iv_arrow_left);
		iv_arrow_right = (ImageView) this.findViewById(R.id.iv_arrow_right);
		iv_arrow_left.setOnClickListener(this);
		iv_arrow_right.setOnClickListener(this);
		iv_arrow_left.setVisibility(View.VISIBLE);
		iv_arrow_right.setVisibility(View.VISIBLE);
		if ((dishList != null && dishList.size() == 1)
				|| position == dishList.size() - 1) {
			iv_arrow_right.setVisibility(View.GONE);
			iv_arrow_left.setVisibility(View.VISIBLE);
		}
		if (position == 0) {
			iv_arrow_left.setVisibility(View.GONE);
			iv_arrow_right.setVisibility(View.VISIBLE);
		}
		pager.setOnPageChangeListener(pageChangeListener);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_arrow_right:
			if (positions == dishList.size() - 1) {
				return;
			}
			positions = positions + 1;
			pager.setCurrentItem(positions);
			adapter.notifyDataSetChanged();
			Log.i(tag, "positionsleft:" + positions);
			break;
		case R.id.iv_arrow_left:
			if (positions == 0) {
				return;
			}
			positions = positions - 1;
			pager.setCurrentItem(positions);
			adapter.notifyDataSetChanged();
			Log.i(tag, "positionsright:" + positions);
			break;

		default:
			break;
		}
	}

	private void doloadImg() {
		int current = pager.getCurrentItem();
		String urlString = dishList.get(current);
		String name = urlString.substring(urlString.lastIndexOf("/")+1);
		if (FileUtils.getFilePath(this, name).exists()) {
			showDialog(this, R.string.tip, R.string.confirm, R.string.is_file,
					R.string.cancel, urlString);
		} else {
			FileUtils.loadImg(this, name, urlString);
		}
	}

	private class ImageAdapter extends PagerAdapter {

		private LayoutInflater inflater;

		ImageAdapter() {
			inflater = LayoutInflater.from(ImageScanActivity.this);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return dishList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, final int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_scan_image,
					view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);
			String imgUrlString = dishList.get(position);
			imageView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					doloadImg();
					return false;
				}
			});
			ImageLoader.getInstance().displayImage(imgUrlString, imageView,
					BlueTownApp.defaultOptions,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
							case DECODING_ERROR:
							case NETWORK_DENIED:
							case OUT_OF_MEMORY:
							case UNKNOWN:
								message = "图片加载失败";
								break;
							}
							Toast.makeText(ImageScanActivity.this, message,
									Toast.LENGTH_SHORT).show();
							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
						}
					});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			// 当滑动时，顶部的imageView是通过animation缓慢的滑动
			if (arg0 == 0) {
				iv_arrow_left.setVisibility(View.GONE);
			} else {
				iv_arrow_left.setVisibility(View.VISIBLE);
			}
			if (arg0 == dishList.size() - 1) {
				iv_arrow_right.setVisibility(View.GONE);
			} else {
				iv_arrow_right.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	private void showDialog(final Activity context, int titleId,
			int confirmTextId, int contentStr, int cancelStr,
			final String nameString) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.setCancelText(context.getString(cancelStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				FileUtils.loadImg(context,
						nameString.substring(nameString.lastIndexOf("/")+1),
						nameString);
				sweetAlertDialog.dismiss();
			}
		});
		dialog.setCancelClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
	}

}
