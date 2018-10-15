package com.android.bluetown.datewheel;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.bluetown.R;

/**
 * 
 * @author shenyz
 * 
 */
@SuppressLint("InflateParams")
public class DateSelectDialog extends Dialog implements OnClickListener {
	private View rootView;
	WheelOptions wheelOptions;
	private View btnSubmit, btnCancel;
	private OnOptionsSelectListener optionsSelectListener;
	private static final String TAG_SUBMIT = "submit";
	private static final String TAG_CANCEL = "cancel";

	 public DateSelectDialog(Context context) {
		super(context, R.style.WinDialog);
		LayoutInflater mLayoutInflater = LayoutInflater.from(context);
		rootView = mLayoutInflater.inflate(R.layout.pw_options, null);
		btnSubmit = rootView.findViewById(R.id.btnSubmit);
		btnSubmit.setTag(TAG_SUBMIT);
		btnCancel = rootView.findViewById(R.id.btnCancel);
		btnCancel.setTag(TAG_CANCEL);
		btnSubmit.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		final View optionspicker = rootView.findViewById(R.id.optionspicker);
		ScreenInfo screenInfo = new ScreenInfo((Activity) context);
		wheelOptions = new WheelOptions(optionspicker);
		wheelOptions.screenheight = screenInfo.getHeight();
		setContentView(rootView);
	}

	public void setPicker(ArrayList<String> optionsItems) {
		wheelOptions.setPicker(optionsItems, null, null, false);
	}

	public void setPicker(ArrayList<String> options1Items,
			ArrayList<ArrayList<String>> options2Items, boolean linkage) {
		wheelOptions.setPicker(options1Items, options2Items, null, linkage);
	}

	public void setPicker(ArrayList<String> options1Items,
			ArrayList<ArrayList<String>> options2Items,
			ArrayList<ArrayList<ArrayList<String>>> options3Items,
			boolean linkage) {
		wheelOptions.setPicker(options1Items, options2Items, options3Items,
				linkage);
	}

	/**
	 * 
	 * @param option1
	 */
	public void setSelectOptions(int option1) {
		wheelOptions.setCurrentItems(option1, 0, 0);
	}

	/**
	 * 
	 * @param option1
	 * @param option2
	 */
	public void setSelectOptions(int option1, int option2) {
		wheelOptions.setCurrentItems(option1, option2, 0);
	}

	/**
	 * 
	 * @param option1
	 * @param option2
	 * @param option3
	 */
	public void setSelectOptions(int option1, int option2, int option3) {
		wheelOptions.setCurrentItems(option1, option2, option3);
	}

	/**
	 * 
	 * @param label1
	 */
	public void setLabels(String label1) {
		wheelOptions.setLabels(label1, null, null);
	}

	/**
	 * 
	 * @param label1
	 * @param label2
	 */
	public void setLabels(String label1, String label2) {
		wheelOptions.setLabels(label1, label2, null);
	}

	/**
	 * 
	 * @param label1
	 * @param label2
	 * @param label3
	 */
	public void setLabels(String label1, String label2, String label3) {
		wheelOptions.setLabels(label1, label2, label3);
	}

	/**
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wheelOptions.setCyclic(cyclic);
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		if (tag.equals(TAG_CANCEL)) {
			dismiss();
			return;
		} else {
			if (optionsSelectListener != null) {
				int[] optionsCurrentItems = wheelOptions.getCurrentItems();
				optionsSelectListener.onOptionsSelect(optionsCurrentItems[0],
						optionsCurrentItems[1], optionsCurrentItems[2]);
			}
			dismiss();
			return;
		}
	}

	public interface OnOptionsSelectListener {
		public void onOptionsSelect(int options1, int option2, int options3);
	}

	public void setOnoptionsSelectListener(
			OnOptionsSelectListener optionsSelectListener) {
		this.optionsSelectListener = optionsSelectListener;
	}
}
