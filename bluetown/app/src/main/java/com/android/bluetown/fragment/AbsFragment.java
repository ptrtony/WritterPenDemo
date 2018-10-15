package com.android.bluetown.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @ClassName: AbsFragment
 * @Description: TODO(实例了Http请求的类)
 * @author shenyz
 * @date 2015年4月30日 下午2:19:53
 * 
 */
public class AbsFragment extends Fragment {
	protected AbHttpUtil httpInstance;
	protected AbRequestParams params;
	protected Context context;
	protected ImageLoader loader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		initHttp();
		initImageLoader();
	}

	/**
	 * 
	 * @Title: initHttp
	 * @Description: TODO(初始化http请求)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void initHttp() {
		if (params == null) {
			params = new AbRequestParams();
		}
		if (httpInstance == null) {
			httpInstance = AbHttpUtil.getInstance(context);
			httpInstance.setEasySSLEnabled(true);
		}

	}

	private void initImageLoader() {
		// TODO Auto-generated method stub
		if (loader == null) {
			loader = ImageLoader.getInstance();
		}

	}

}
