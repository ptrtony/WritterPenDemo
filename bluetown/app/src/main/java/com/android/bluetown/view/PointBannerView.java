package com.android.bluetown.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.bluetown.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class PointBannerView extends FrameLayout {

  public static final int TIME_INTERVAL = 3 * 1000;
//  public static final int CURSOR_COLOR_NORMAL = 0xffd3d3d3;
//  public static final int CUROSR_COLOR_SELECTED = 0xffffaa3d;
  public static final int CURSOR_COLOR_NORMAL = 0x00000000;
  public static final int CUROSR_COLOR_SELECTED = 0x00000000;
  public static final int CURSOR_SPACING = 5;

  private static final int WHAT_SWITCH_BANNER = 100;

  private final List<String> urls = new ArrayList<String>();

  private int colorNormal = CURSOR_COLOR_NORMAL;
  private int colorSelected = CUROSR_COLOR_SELECTED;
  private int cursorSpacing = CURSOR_SPACING;
  private int switchInterval = TIME_INTERVAL;
  
  private DisplayImageOptions options;
  private ViewPager viewPager;
  private LinearLayout cursorLayout;
  private final List<ImageView> imageViewList = new ArrayList<ImageView>();
  private final List<View> cursorViewList = new ArrayList<View>();


  public PointBannerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initialize();
  }

  public PointBannerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public PointBannerView(Context context) {
    super(context);
    initialize();
  }

  private void initialize() {
    options = new DisplayImageOptions.Builder()
    .resetViewBeforeLoading(true)
    .cacheOnDisk(true)
    .imageScaleType(ImageScaleType.EXACTLY)
    .bitmapConfig(Bitmap.Config.RGB_565)
    .considerExifParams(true)
    .displayer(new FadeInBitmapDisplayer(300))
    .build();
    RelativeLayout layout = new RelativeLayout(getContext());
    addView(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    viewPager = new ViewPager(getContext());
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    layout.addView(viewPager, params);

    cursorLayout = new LinearLayout(getContext());
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
    cursorLayout.setGravity(Gravity.CENTER_HORIZONTAL);
    cursorLayout.setPadding(0, 0, 0, 10);
    layout.addView(cursorLayout, layoutParams);
  }

  private void createContents() {
    int currentItem = viewPager.getCurrentItem();
    if (urls.size() > 0) {
      int amount = urls.size();
      int viewCount = cursorLayout.getChildCount();
      if (amount == viewCount) {
        for (int i = 0; i < amount; i++) {
         // imageViewList.get(i).setBackgroundResource(R.drawable.image_1);
          displayImg(imageViewList.get(i), urls.get(i));
          //imageLoader.display(imageViewList.get(i), urls.get(i));
        }
      } else {
        if (amount > viewCount) {
          for (int i = 0; i < amount; i++) {
            if (i < viewCount) {
              displayImg(imageViewList.get(i), urls.get(i));
              //imageLoader.display(imageViewList.get(i), urls.get(i));
              //imageViewList.get(i).setBackgroundResource(R.drawable.image_1);
              if (i == viewCount - 1) {
                View view = cursorViewList.get(i);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
                lp.rightMargin = cursorSpacing;
                view.setLayoutParams(lp);
              }
            } else {
              createImageAndCursor(urls.get(i), i == amount - 1);
            }
          }
        } else {
          for (int i = 0; i < viewCount; i++) {
            if (i < amount) {
              displayImg(imageViewList.get(i), urls.get(i));
              imageViewList.get(i).setBackgroundResource(R.drawable.ic_msg_empty);
              if (i == amount - 1) {
                View view = cursorViewList.get(i);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
                view.setLayoutParams(lp);
              }
            } else {
              imageViewList.remove(amount);
              cursorViewList.remove(amount);
              cursorLayout.removeViewAt(amount);
            }
          }
        }
      }

      viewPager.setAdapter(imagePagerAdapter);
      viewPager.setOnPageChangeListener(onPageChangeListener);
      viewPager.setCurrentItem(currentItem);
      for (int i = 0; i < cursorViewList.size(); i++) {
        if (currentItem == i) {
          cursorViewList.get(i).setBackgroundColor(colorSelected);
        } else {
          cursorViewList.get(i).setBackgroundColor(colorNormal);
        }
      }
    } else {
      imageViewList.clear();
      cursorViewList.clear();
    }
  }

  private void createImageAndCursor(String url, boolean isLast) {
    Context context = getContext();
    ImageView view = new ImageView(context);
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,180);
    view.setLayoutParams(params);
    view.setAdjustViewBounds(true);
    view.setScaleType(ScaleType.FIT_XY);
    imageViewList.add(view);
    displayImg(view, url);

    View cursor = new View(context);
    cursor.setBackgroundColor(colorNormal);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20,20);
    cursor.setPadding(0, 0, 0, 0);
    if (!isLast) {
      lp.rightMargin = cursorSpacing;
    }
    cursorViewList.add(cursor);
    cursorLayout.addView(cursor, lp);
  }

  /**
   * 设置图片链接集合
   * 
   * @param collection
   */
  public void setImageUrls(Collection<String> collection) {
    urls.clear();
    if (collection != null) {
      urls.addAll(collection);
    }
    createContents();
  }

  private void selectCursor(int position) {
    for (int i = 0; i < cursorViewList.size(); i++) {
      View cursor = cursorViewList.get(i);
      if (i == position) {
        cursor.setBackgroundColor(colorSelected);
      } else {
        cursor.setBackgroundColor(colorNormal);
      }
    }
  }



  private PagerAdapter imagePagerAdapter = new PagerAdapter() {
    @Override
    public Object instantiateItem(View container, int position) {
      ((ViewPager) container).addView(imageViewList.get(position));
      return imageViewList.get(position);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
      ((ViewPager) container).removeView(imageViewList.get(position));
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
      return arg0 == arg1;
    }

    @Override
    public int getCount() {
      synchronized (urls) {
        return imageViewList.size();
      }
    }
  };


  private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

    @Override
    public void onPageSelected(int arg0) {
      selectCursor(arg0);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }
  };

  /**
   * @return the switchInterval
   */
  public int getSwitchInterval() {
    return switchInterval;
  }

  /**
   * @param switchInterval the switchInterval to set
   */
  public void setSwitchInterval(int switchInterval) {
    this.switchInterval = switchInterval;
  }
  
  private void displayImg(ImageView view, String url){
    ImageLoader.getInstance().displayImage(url, view, options, new SimpleImageLoadingListener() {
      @Override
      public void onLoadingStarted(String imageUri, View view) {
      }

      @Override
      public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
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
     //   Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

      }

      @Override
      public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
      }
    });
  }
}
