package com.android.bluetown.datewheel;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.android.bluetown.R;
/**
 * 免密支付Picker
 * 
 * @author hedi
 * 
 */
public class NoPSWPayPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker personPicker;
	private Context context;
	private ArrayList<String> personList = new ArrayList<String>();
	private String personStr;

	public NoPSWPayPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context);
	}

	public NoPSWPayPicker(Context context) {
		super(context);
		this.context = context;
		init(context);

	}

	/**
	 * @param context
	 */
	private void init(Context context) {
		personList.add("50.00元");
		personList.add("100.00元");
//		personList.add("200.00元");
//		personList.add("300.00元");
//		personList.add("500.00元");
//		personList.add("1000.00元");
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.no_psw_pay_picker, this);
		// 获取控件引用
		personPicker = (ScrollerNumberPicker) findViewById(R.id.personPicker);
		personPicker.setData(personList);
		personPicker.setDefault(0);
	}

	public String getPersonCount() {
		personStr = personPicker.getSelectedText();
		return personStr;
	}
}
