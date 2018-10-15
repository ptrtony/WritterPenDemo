package com.android.bluetown.circle.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.CircleDetailAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.PostBean;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.CircleDetailsResult;
import com.android.bluetown.utils.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * @ClassName: CircleActivity
 * @Description:TODO(圈子)
 * @author: shenyz
 * @date: 2015年8月18日 下午4:43:08
 */

public class CircleActivity extends TitleBarActivity implements
        OnClickListener, OnRefreshListener, OnLoadMoreListener {
    private RecyclerView postListView;
    private SmartRefreshLayout mPullToRefreshView;
    private ArrayList<PostBean> list;
    private CircleDetailAdapter mAdapter;
    /**
     * 列表初始化状态
     */
    protected int listStatus = Constant.ListStatus.INIT;
    /**
     * 默认第一页
     */
    protected int page = 1;
    private int size = 10;
    private CircleDetailsResult result;
    private String userId;
    private FinalDb db;
    private List<MemberUser> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_circle_detail);
        BlueTownExitHelper.addActivity(this);
        initUIView();
        getData();

    }

    private void initUIView() {
        list = new ArrayList<PostBean>();
        postListView = (RecyclerView) findViewById(R.id.postList);
        mAdapter = new CircleDetailAdapter(list, this);
        postListView.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setSmoothScrollbarEnabled(true);
        manager.setAutoMeasureEnabled(true);
        postListView.setLayoutManager(manager);
        postListView.setFocusable(false);
        ((DefaultItemAnimator) postListView.getItemAnimator()).setSupportsChangeAnimations(false);
        mPullToRefreshView = findViewById(R.id.mPullRefreshView);
        mPullToRefreshView.setOnRefreshListener(this);
        mPullToRefreshView.setOnLoadMoreListener(this);
        mPullToRefreshView.setRefreshHeader(new ClassicsHeader(this));
        mPullToRefreshView.setRefreshFooter(new ClassicsFooter(this));
        db = FinalDb.create(this);

        users = db.findAll(MemberUser.class);
        if (users != null && users.size() != 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                userId = user.getMemberId();
            }
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private void getData() {
        params.put("page", page + "");
        params.put("rows", size + "");
        httpInstance.post(
                Constant.HOST_URL + Constant.Interface.MobiGroupManagementAction_getAllManagement, params,
                new AbsHttpStringResponseListener(this, null) {
                    @Override
                    public void onSuccess(int i, String s) {
                        result = (CircleDetailsResult) AbJsonUtil.fromJson(s,
                                CircleDetailsResult.class);
                        if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                            dealResult(result);
                        } else if (result.getRepCode().contains(
                                Constant.HTTP_NO_DATA)) {
                            mPullToRefreshView.finishRefresh();
                            mPullToRefreshView.finishLoadMore();
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(CircleActivity.this,
                                    R.string.no_data, Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onFailure(int arg0, String arg1, Throwable arg2) {
                        // TODO Auto-generated method stub
                        mPullToRefreshView.finishRefresh();
                        mPullToRefreshView.finishLoadMore();
                    }
                });
    }

    protected void dealResult(CircleDetailsResult result) {
        switch (listStatus) {
            case Constant.ListStatus.LOAD:
                list.addAll(result.getData().getRows());
                mPullToRefreshView.finishLoadMore();
                break;
            case Constant.ListStatus.INIT:
                list.clear();
                list.addAll(result.getData().getRows());
                break;
            case Constant.ListStatus.REFRESH:
                list.clear();
                list.addAll(result.getData().getRows());
                mPullToRefreshView.finishRefresh();
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setPostContent(final ArrayList<PostBean> topList, final int i) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        layout.setLayoutParams(params);
        TextView top = new TextView(this);
        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        topParams.setMargins(10, 10, 10, 10);
        top.setLayoutParams(topParams);
        top.setBackgroundResource(R.drawable.blue_darker_btn_bg);
        top.setPadding(15, 10, 15, 10);
        top.setText(R.string.top);
        top.setGravity(Gravity.CENTER);
        top.setTextSize(14);
        top.setTextColor(getResources().getColor(R.color.white));
        TextView topPostContent = new TextView(this);
        LinearLayout.LayoutParams topPostLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        topPostLayoutParams.setMargins(15, 0, 0, 0);
        topPostContent.setLayoutParams(topPostLayoutParams);
        topPostContent.setText(topList.get(i).getManagementName());
        topPostContent.setMaxLines(1);
        topPostContent.setEllipsize(TruncateAt.END);
        topPostContent.setTextSize(14);
        topPostContent.setGravity(Gravity.CENTER_VERTICAL);
        topPostContent.setTextColor(getResources().getColor(R.color.font_gray));
        View lineView = new View(this);
        lineView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
        lineView.setBackgroundColor(getResources().getColor(
                R.color.compnay_detail_divier));
        layout.addView(top);
        layout.addView(topPostContent);
        layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("mid", topList.get(i).getMid());
                intent.setClass(CircleActivity.this, PostDetailsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
        leftTextLayout.setVisibility(View.VISIBLE);
        setTitleLayoutBg(R.color.title_bg_blue);
        setTitleView(R.string.circle_details);
        setRightImageView(R.drawable.ic_edit);
        rightImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        users = db.findAll(MemberUser.class);
        if (users != null && users.size() != 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                userId = user.getMemberId();
            }
        }
        switch (v.getId()) {
            case R.id.rightImageView:
                if (userId != null && !userId.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra("groupId", "");
                    intent.setClass(CircleActivity.this,
                            PublishPostActivity.class);
                    startActivity(intent);

                } else {
                    TipDialog.showDialogNoClose(CircleActivity.this, R.string.tip,
                            R.string.confirm, R.string.login_info_tip,
                            LoginActivity.class);
                }

                break;

            default:
                break;
        }
    }


    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        listStatus = Constant.ListStatus.REFRESH;
        page = 1;
        getData();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        listStatus = Constant.ListStatus.LOAD;
        page++;
        getData();
    }
}
