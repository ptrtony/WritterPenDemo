package com.android.bluetown.view.NumberPayCodeView;

/**
 * Created by Dafen on 2018/7/30.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.bluetown.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by linkaipeng on 16/8/2.
 */

public abstract class BaseNumberCodeView extends RelativeLayout implements OnTouchItemListener {

    private final static int NUMBER_BUTTON_DELETE = 11;
    private final static int NUMBER_BUTTON_ZERO = 10;//0号按键
    private final static int NUMBER_BUTTON_CLEAR = 9;//清除按键
    private final static int NUMBER_COUNT = 6;

    private final static String PASSWORD_NUMBER_SYMBOL = "●";

    protected Context mContext;
    private ExpandGridView mNumbersGridView;

    private boolean mIsPassword;
    private TextView mNumber1TextView;
    private TextView mNumber2TextView;
    private TextView mNumber3TextView;
    private TextView mNumber4TextView;
    private TextView mNumber5TextView;
    private TextView mNumber6TextView;
    private Stack<Integer> mNumberStack;
    private List<TextView> mNumberViewList;

    protected OnInputNumberCodeCallback mCallback;


    public BaseNumberCodeView(Context context) {
        super(context, null);
    }

    public BaseNumberCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mNumberStack = new Stack();
        mNumberViewList = new ArrayList();
        initView();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberCodeView);
        mIsPassword = array.getBoolean(R.styleable.NumberCodeView_isPassword, false);

    }

    private void initView(){
        View view = createView();
        addView(view);
        mNumbersGridView = (ExpandGridView) view.findViewById(R.id.numbers_gridView);
        NumbersAdapter numbersAdapter = new NumbersAdapter();
        mNumbersGridView.setAdapter(numbersAdapter);
        numbersAdapter.setOnTouchItemClickListener(this);
//        mNumbersGridView.setOnItemClickListener(this);
        mNumber1TextView = (TextView) findViewById(R.id.number_1_textView);
        mNumber2TextView = (TextView) findViewById(R.id.number_2_textView);
        mNumber3TextView = (TextView) findViewById(R.id.number_3_textView);
        mNumber4TextView = (TextView) findViewById(R.id.number_4_textView);
        mNumber5TextView = (TextView) findViewById(R.id.number_5_textView);
        mNumber6TextView = (TextView) findViewById(R.id.number_6_textView);
        mNumberViewList.add(mNumber1TextView);
        mNumberViewList.add(mNumber2TextView);
        mNumberViewList.add(mNumber3TextView);
        mNumberViewList.add(mNumber4TextView);
        mNumberViewList.add(mNumber5TextView);
        mNumberViewList.add(mNumber6TextView);

    }

    public void setIsPassword(boolean isPassword){
        mIsPassword = isPassword;
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (position == NUMBER_BUTTON_CLEAR) {
//            restoreViews();
//            return;
//        }
//        if (position == NUMBER_BUTTON_DELETE) {
//            if (mNumberStack.empty() || mNumberStack.size() > NUMBER_COUNT) {
//                return;
//            }
//            mNumberStack.pop();
//        } else {
//            if (position == NUMBER_BUTTON_ZERO) {
//                mNumberStack.push(0);
//            } else {
//                mNumberStack.push(++position);
//            }
//        }
//        refreshNumberViews();
//        //input 6 numbers complete
//        if (mNumberStack.size() == NUMBER_COUNT) {
//            StringBuilder codeBuilder = new StringBuilder();
//            for (int number : mNumberStack) {
//                codeBuilder.append(number);
//            }
//            onResult(codeBuilder.toString());
//            //输入完成
//            if (onPasswordInputFinish!=null){
//                onPasswordInputFinish.inputFinish(codeBuilder.toString());
//            }
//        }
//    }

    public void setOnFinishInput(OnPasswordInputFinish onPasswordInputFinish){
        this.onPasswordInputFinish = onPasswordInputFinish;
    }
    private OnPasswordInputFinish onPasswordInputFinish;

    @Override
    public void onTouchItemClick(View view, int position) {
        if (position == NUMBER_BUTTON_CLEAR) {
            restoreViews();
            return;
        }
        if (position == NUMBER_BUTTON_DELETE) {
            if (mNumberStack.empty() || mNumberStack.size() > NUMBER_COUNT) {
                return;
            }
            mNumberStack.pop();
        } else {
            if (position == NUMBER_BUTTON_ZERO) {
                mNumberStack.push(0);
            } else {
                mNumberStack.push(++position);
            }
        }
        refreshNumberViews();
        //input 6 numbers complete
        if (mNumberStack.size() == NUMBER_COUNT) {
            StringBuilder codeBuilder = new StringBuilder();
            for (int number : mNumberStack) {
                codeBuilder.append(number);
            }
            onResult(codeBuilder.toString());
            //输入完成
            if (onPasswordInputFinish!=null){
                onPasswordInputFinish.inputFinish(codeBuilder.toString());
            }
//            if (mCallback!=null){
//                mCallback.onResult(codeBuilder.toString());
//            }
        }
    }

    public interface OnPasswordInputFinish{
        void inputFinish(String payPWd);
    }

    public void restoreViews(){
        mNumberStack.clear();
        refreshNumberViews();
    }

    private void refreshNumberViews(){
        for (int i = 0, size = mNumberViewList.size(); i < size; i++) {
            int numSize = mNumberStack.size();
            if (i < numSize) {
                if (mIsPassword) {
                    mNumberViewList.get(i).setText(PASSWORD_NUMBER_SYMBOL);
                } else {
                    mNumberViewList.get(i).setText(String.valueOf(mNumberStack.get(i)));
                }

            } else {
                mNumberViewList.get(i).setText("");
            }
        }
    }

    public void setNumberCodeCallback(OnInputNumberCodeCallback callback){
        this.mCallback = callback;
    }

    public interface OnInputNumberCodeCallback {
        void onResult(String code);
    }

    abstract protected View createView();
    abstract protected void onResult(String code);

    private class NumbersAdapter extends BaseAdapter {

        private static final String NUMBERS = "123456789C0#";
        private OnTouchItemListener onTouchItemListener;
        @Override
        public int getCount() {
            return NUMBERS.length();
        }

        @Override
        public String getItem(int position) {
            return String.valueOf(NUMBERS.charAt(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemHolder itemHolder;
            if (convertView == null) {
                itemHolder = new ItemHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_view_input_group_code, null);
                itemHolder.mRootView = (RelativeLayout) convertView.findViewById(R.id.number_root_view);
                itemHolder.mNumberTextView = (TextView) convertView.findViewById(R.id.number_textView);
                itemHolder.mDeleteImageView = (ImageView) convertView.findViewById(R.id.number_delete_imageView);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder) convertView.getTag();
            }
            String curNumber = getItem(position);
            if ("C".equals(curNumber)) {
                itemHolder.mDeleteImageView.setVisibility(GONE);
                itemHolder.mNumberTextView.setVisibility(VISIBLE);
                itemHolder.mNumberTextView.setText(curNumber);
                itemHolder.mRootView.setBackgroundColor(getResources().getColor(R.color.gray));
            } else if ("#".equals(curNumber)) {
                itemHolder.mRootView.setBackgroundColor(getResources().getColor(R.color.gray));
                itemHolder.mNumberTextView.setVisibility(GONE);
                itemHolder.mDeleteImageView.setVisibility(VISIBLE);
            } else {
                itemHolder.mRootView.setBackgroundResource(R.drawable.list_selector);
                itemHolder.mDeleteImageView.setVisibility(GONE);
                itemHolder.mNumberTextView.setVisibility(VISIBLE);
                itemHolder.mNumberTextView.setText(curNumber);
            }

            if (onTouchItemListener!=null){
                convertView.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        onTouchItemListener.onTouchItemClick(v,position);
                        return false;
                    }
                });

            }

            return convertView;
        }

        public void setOnTouchItemClickListener(OnTouchItemListener onTouchItemClickListener){
            this.onTouchItemListener = onTouchItemClickListener;
        }
    }

    private static class ItemHolder{
        RelativeLayout mRootView;
        TextView mNumberTextView;
        ImageView mDeleteImageView;
    }


}
