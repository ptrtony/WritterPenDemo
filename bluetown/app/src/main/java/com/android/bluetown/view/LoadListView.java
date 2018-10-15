package com.android.bluetown.view;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.android.bluetown.R;


/**
 * Created by jun on 2017/11/1.
 */

public class LoadListView extends PinnedSectionListView implements AbsListView.OnScrollListener {
    private View footer;// 底部布局
    int totalItemCount;// 总数量
    int lastVisibleItem;// 最后一个可见的item;
    boolean isLoading;// 判断变量
    public int simple_size = -1;
    public int total_size = 0;
    IloadListener iLoadListener;// 接口变量
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TextView textView = footer.findViewById(R.id.tv_load_text);
            switch (msg.what) {
                case 0:
                    footer.findViewById(R.id.iv_load_image).setVisibility(View.GONE);
                    textView.setText("没有更多的账单了");
                    break;
                case 1:
                    footer.findViewById(R.id.iv_load_image).setVisibility(View.VISIBLE);
                    textView.setText("正在加载");
                    break;
            }

        }
    };

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    //每一页的数据量
    public void setSimple_size(int simple_size) {
        this.simple_size = simple_size;
    }
    //当前总数据量
    public void setTotal_size(int total_size) {
        this.total_size = total_size;
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
        // TODO Auto-generated constructor stub
    }
    public int getTotalSize(){
        return total_size;
    }
    // listview加载底部布局
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.view_refresh_footer, null);
        // 设置隐藏底部布局
//        footer.findViewById(R.id.footer_layout).setVisibility(View.GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
//        if (totalItemCount >= lastVisibleItem - 6) {
//            if (total_size % simple_size == 0&&(total_size % 15 == 0)) {
//        if (total_size % 15 ==0&&total_size!=0){
                handler.sendEmptyMessage(1);
                if (!isLoading) {
                    if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                        // 加载更多（获取接口）
                        isLoading = true;
                        iLoadListener.onLoad();
                    }
                }
//            }else {
//                handler.sendEmptyMessage(0);
//            }

//        }
    }

    IFirstListener iFirstListener;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
//        if (firstVisibleItem>=1){
//            iFirstListener.onVisible(false);
//        }else{
//            iFirstListener.onVisible(true);
//        }
    }

    public void setInterface(IloadListener iLoadListener) {

        this.iLoadListener = iLoadListener;
    }

    public void setFirstListener(IFirstListener iFirstListener) {
        this.iFirstListener = iFirstListener;
    }

    // 加载更多数据的回调接口
    public interface IloadListener {
        public void onLoad();
    }

    //滑动到第一个的时候隐藏
    public interface IFirstListener {
        public void onVisible(boolean visible);
    }

    // 加载完成通知隐藏
    public void loadComplete() {
        isLoading = false;
        handler.sendEmptyMessage(0);
    }

}