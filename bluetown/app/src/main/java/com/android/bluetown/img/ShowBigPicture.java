package com.android.bluetown.img;
import java.util.ArrayList;
import java.util.List;
import com.android.bluetown.R;
import com.android.bluetown.bean.PostBean;
import com.android.bluetown.utils.StatusBarUtils;
import com.lesogo.cu.custom.ScaleView.HackyViewPager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowBigPicture extends AppCompatActivity implements OnItemClickListener{
	public final static String TOP = "top";
	public final static String LEFT = "left";
	public final static String WIDTH = "width";
	public final static String HEIGHT = "height";
	private HackyViewPager viewPager;
	private PostBean item ;
	List<String> pic;
	private int position=0;


	@Override
	public void onStart() {
		super.onStart();
		Window window = getWindow();
		WindowManager.LayoutParams windowParams = window.getAttributes();
		windowParams.alpha = 1f;//1.０全透明．０不透明．
		window.setAttributes(windowParams);
	}


	@SuppressWarnings({ "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_big_pictrue_a);
		postponeEnterTransition();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			StatusBarUtils.getInstance().initStatusBar(true, this);
		}
		Bundle bundle=getIntent().getExtras();
		position=bundle.getInt("position", 0);
		try{
			Bundle bundleObject = getIntent().getExtras();
			item = (PostBean) bundleObject.getSerializable("key");
			pic =  (item).getPicturesList();
			initViewPager();

		} catch(Exception e){
			e.printStackTrace();
		}

	}

	private void initViewPager(){
		viewPager = (HackyViewPager) findViewById(R.id.viewPager_show_bigPic);
//		viewPager.setBackgroundColor(Color.TRANSPARENT);
//		viewPager.setPageMarginDrawable(R.color.transparent);
		ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position);
		viewPager.setOffscreenPageLimit(pic.size());
	}

	private class ViewPagerAdapter extends FragmentStatePagerAdapter{

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			String url= pic.get(position);
			return new PictureFragment().getNewInstance(url,position);
		}

		@Override
		public int getCount() {
			return pic.size();
		}


	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ShowBigPicture.this.finish();
	}
	/**
	 * �ص��ӿ�
	 *
	 *
	 */
	public interface MyTouchListener
	{
		public void onTouchEvent(MotionEvent event);
	}

	/*
	 * ����MyTouchListener�ӿڵ��б�
	 */
	private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MyTouchListener>();

	/**
	 * �ṩ��Fragmentͨ��getActivity()������ע���Լ��Ĵ����¼��ķ���
	 * @param listener
	 */
	public void registerMyTouchListener(MyTouchListener listener)
	{
		myTouchListeners.add( listener );
	}

	/**
	 * �ṩ��Fragmentͨ��getActivity()������ȡ��ע���Լ��Ĵ����¼��ķ���
	 * @param listener
	 */
	public void unRegisterMyTouchListener(MyTouchListener listener)
	{
		myTouchListeners.remove( listener );
	}

	/**
	 * �ַ������¼�������ע����MyTouchListener�Ľӿ�
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		for (MyTouchListener listener : myTouchListeners) {
			listener.onTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void finish() {
		ShowBigPicture.super.finish();
	}


}
