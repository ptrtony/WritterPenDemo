package com.android.bluetown.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.PostBean;
import com.android.bluetown.circle.activity.CircleActivity;
import com.android.bluetown.circle.activity.PostDetailsActivity;
import com.android.bluetown.img.ShowBigPicture;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.DisplayUtils;
import com.android.bluetown.view.NineGridCircleLayout;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Dafen on 2018/6/9.
 */

public class CircleDetailAdapter extends RecyclerView.Adapter<CircleDetailAdapter.CirlceDetailViewHolder> {
    private static final String CLIKE_LIKE = "like";
    private List<PostBean> list;
    private LayoutInflater mInflater;
    private Context context;
    protected ImageLoader imageLoader;
    private SharePrefUtils sharePrefUtils;
    public CircleDetailAdapter(List<PostBean> list, Context context){
        this.list = list;
        this.context = context;

        mInflater = LayoutInflater.from(context);
        initFinalBitmap();
        sharePrefUtils = new SharePrefUtils(context);
        sharePrefUtils.setBoolean(CLIKE_LIKE,false);
    }
    private void initFinalBitmap() {
        if (imageLoader==null) {
            imageLoader = ImageLoader.getInstance();
        }
    }

    @NonNull
    @Override
    public CirlceDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_circle_lists,parent,false);
        CirlceDetailViewHolder holder = new CirlceDetailViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CirlceDetailViewHolder holder, int position) {
        setData(holder,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class CirlceDetailViewHolder extends BaseViewHolder{
        TextView postTitle, postContent, postUserName,
                postDate, postCommentCount,postTextLike;
//        LinearLayout postImgs,postImgs1;
        NineGridCircleLayout nineGridCircleLayout;
        RelativeLayout postLike,postComment;
        ImageView mCricleNick;
        public CirlceDetailViewHolder(View convertView) {
            super(convertView);
            postTitle = (TextView) convertView
                    .findViewById(R.id.postTitle);
            postContent = (TextView) convertView
                    .findViewById(R.id.postContent);
            postUserName = (TextView) convertView
                    .findViewById(R.id.tv_circle_nick);
            postDate = (TextView) convertView
                    .findViewById(R.id.tv_circle_time);
            postCommentCount = (TextView) convertView
                    .findViewById(R.id.postCommentCount);
//            postImgs = (LinearLayout) convertView
//                    .findViewById(R.id.postImgs);
            nineGridCircleLayout = convertView.findViewById(R.id.nine_images);
            mCricleNick = convertView.findViewById(R.id.iv_head_nick);
//            postImgs1 = convertView.findViewById(R.id.postImgs1);
            postComment = convertView.findViewById(R.id.rl_click_comment);
            mCricleNick = convertView.findViewById(R.id.iv_head_nick);
            postLike = convertView.findViewById(R.id.rl_click_like);
            postTextLike = convertView.findViewById(R.id.tv_like);
        }
    }

    private void setData(CirlceDetailViewHolder mHolder, final int position) {
        // TODO Auto-generated method stub
        final PostBean item = list.get(position);
        mHolder.postTitle.setText(item.getManagementName());
        String content = item.getContent();
        setLikeData(mHolder,item);
        if (content != null && !content.isEmpty()) {
            mHolder.postContent.setVisibility(View.VISIBLE);
            mHolder.postContent.setText(content);
        } else {
            mHolder.postContent.setVisibility(View.GONE);
        }
        mHolder.nineGridCircleLayout.removeAllViews();
        imageLoader.displayImage(item.getHeadImg(),mHolder.mCricleNick);
        mHolder.postUserName.setText(item.getNickName());
        setCreateTime(mHolder, item);
        mHolder.postCommentCount.setText(item.getManagementNumber());
        mHolder.postLike.setTag(position);
        mHolder.postLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharePrefUtils.getBoolean(CLIKE_LIKE,false)){
                    Animation animation = AnimationUtils.loadAnimation(context,R.anim.zoom_like);
                    Drawable drawable = context.getResources()
                            .getDrawable(R.drawable.timeline_icon_like);
                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                    mHolder.postTextLike.setCompoundDrawables(drawable,null, null, null);
                    mHolder.postTextLike.setText(Integer.parseInt(item.getApplyNumber())+1+"");
                    mHolder.postTextLike.setTextColor(context.getResources().getColor(R.color.font_orange));
                    mHolder.postTextLike.startAnimation(animation);
                    sharePrefUtils.setBoolean(CLIKE_LIKE,true);
                    item.setIsPraise("1");
                }else{
                    Drawable drawable = context.getResources()
                            .getDrawable(R.drawable.timeline_icon_unlike);
                    drawable.setBounds(0, 0,
                            drawable.getMinimumWidth(),
                            drawable.getMinimumHeight()); // 设置边界
                    mHolder.postTextLike.setCompoundDrawables(drawable,
                            null, null, null);
                    if (Integer.parseInt(item.getApplyNumber())==0){
                        mHolder.postTextLike.setText("赞");
                    }
                    mHolder.postTextLike.setTextColor(context.getResources().getColor(R.color.text_gray));
                    sharePrefUtils.setBoolean(CLIKE_LIKE,false);
                    item.setIsPraise("0");
                }

            }
        });

        mHolder.postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("mid", item.getMid());
                intent.setClass((CircleActivity)context, PostDetailsActivity.class);
                ((CircleActivity) context).startActivity(intent);
                ((CircleActivity) context).overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
            }
        });

        if (item.getPicturesList().size() == 0) {
            mHolder.nineGridCircleLayout.setVisibility(View.GONE);
        } else {
            mHolder.nineGridCircleLayout.setVisibility(View.VISIBLE);

            final List<String> picturesList = item.getPicturesList();
            if (picturesList.size()>0){
                mHolder.nineGridCircleLayout.setSpacing(DisplayUtils.dip2px(context,5));
                mHolder.nineGridCircleLayout.setUrlList(picturesList);
                mHolder.nineGridCircleLayout.setIsShowAll(false);

                mHolder.nineGridCircleLayout.setOnClickItemListener(new NineGridCircleLayout.OnClickItemListener() {
                    @Override
                    public void onClickItem(int position, String url, List<String> urlList, ImageView view) {
                        Intent intent = new Intent((Activity) context, ShowBigPicture.class);
                        Bundle bundle=new Bundle();
                        bundle.putInt("position",position);
                        bundle.putSerializable("key", (Serializable) item);
                        intent.putExtras(bundle);
//                        ActivityOptionsCompat options = null;
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                            options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context,view,"circleImage");
//                            ((Activity) context).startActivity(intent,options.toBundle());
//                        }else{
                            ((Activity) context).startActivity(intent);
//                        }

                    }
                });
            }

        }

    }

    private void setLikeData(CirlceDetailViewHolder mHolder, PostBean item){
        if ("0".equals(item.getIsPraise())){
            mHolder.postTextLike.setCompoundDrawablePadding(DisplayUtils.dip2px(context,10));
            mHolder.postTextLike.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.timeline_icon_unlike),null,null,null);
            mHolder.postTextLike.setTextColor(context.getResources().getColor(R.color.text_gray));
            mHolder.postTextLike.setText("赞");
            sharePrefUtils.setBoolean(CLIKE_LIKE,false);
        }else if ("1".equals(item.getIsPraise())){
            mHolder.postTextLike.setCompoundDrawablePadding(DisplayUtils.dip2px(context,10));
            mHolder.postTextLike.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.timeline_icon_like),null,null,null);
            mHolder.postTextLike.setTextColor(context.getResources().getColor(R.color.font_red));
            mHolder.postTextLike.setText(item.getApplyNumber());
            sharePrefUtils.setBoolean(CLIKE_LIKE,true);
        }
    }

    /**
     * 设置评论时间
     *
     * @param mHolder
     * @param item
     */
    private void setCreateTime(CirlceDetailViewHolder mHolder, final PostBean item) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String todayDateTime = format.format(new Date());
        try {
            Date nowTime = format.parse(todayDateTime);
            Date createTime = format.parse(item.getCreateTime());
            // 相差的毫秒数
            long diff = nowTime.getTime() - createTime.getTime();
            // 秒
            long timeInterval = diff / 1000;
            if (timeInterval < 60) {
                mHolder.postDate.setText("刚刚");
            } else if ((timeInterval / 60) < 60) {
                mHolder.postDate.setText((timeInterval / 60) + "分钟前");
            } else if ((timeInterval / (60 * 60)) < 24) {
                mHolder.postDate.setText((timeInterval / (60 * 60)) + "小时前");
            } else {
                mHolder.postDate.setText(item.getCreateTime());
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mHolder.postDate.setText(item.getCreateTime());
        }
    }
}
