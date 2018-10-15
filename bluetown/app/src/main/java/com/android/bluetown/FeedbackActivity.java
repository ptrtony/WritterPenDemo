package com.android.bluetown;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.custom.dialog.TipDialog;

public class FeedbackActivity extends Activity {
	private static final int NUMBER = 72;
	private String feedbackComment;
	private EditText mFeedback;
	private TextView mTextView;
	private Button mSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		BlueTownExitHelper.addActivity(this);
		initTitle();
		initView();
		mFeedback.addTextChangedListener(mTextWatcher);
		initListener();
	}

	TextWatcher mTextWatcher = new TextWatcher() {

		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

			editStart = mFeedback.getSelectionStart();
			editEnd = mFeedback.getSelectionEnd();
			int len = NUMBER - temp.length();
			mTextView.setText("还剩" + len + "个字");
			if (temp.length() > NUMBER) {
				Toast.makeText(FeedbackActivity.this, "你输入的字数已经超过了限制！",
						Toast.LENGTH_SHORT).show();
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				mFeedback.setText(s);
				mFeedback.setSelection(tempSelection);
			}
		}
	};

	private void initListener() {
		mSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				feedbackComment = mFeedback.getText().toString();
				if (TextUtils.isEmpty(feedbackComment)) {
					TipDialog.showDialog(FeedbackActivity.this, R.string.tip,
							R.string.confirm, "意见不能为空");
					return;
				}
			}
		});

	}

	private void initView() {
		mFeedback = (EditText) findViewById(R.id.et_setting_feedback);
		mTextView = (TextView) findViewById(R.id.tv_findpwd);
		mSend = (Button) findViewById(R.id.btnPwd);
	}

	private void initTitle() {
		TextView textView = (TextView) findViewById(R.id.titleMiddle);
		textView.setText("意见反馈");
		TextView textBack = (TextView) findViewById(R.id.titleLeft);
		textBack.setVisibility(View.VISIBLE);
	}

	public void onBack(View v) {
		finish();
	}
}
