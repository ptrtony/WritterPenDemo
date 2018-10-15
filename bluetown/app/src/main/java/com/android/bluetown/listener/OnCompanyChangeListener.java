package com.android.bluetown.listener;

import com.android.bluetown.bean.CompanyShowItemBean;

/**
 * 企业展示回调
 * 
 * @author shenyz
 * 
 */
public interface OnCompanyChangeListener {
	/**
	 * 企业收藏
	 * 
	 * @param company
	 */
	public void onCompanyCollect(int position, CompanyShowItemBean company);

}
