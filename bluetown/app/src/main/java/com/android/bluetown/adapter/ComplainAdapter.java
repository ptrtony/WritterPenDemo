package com.android.bluetown.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.bluetown.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Dafen on 2018/7/5.
 */

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.ViewHolder> {

    private OnItemClickListener onItemDelClickListener;
    private LayoutInflater mInflater;
    private List<Bitmap> bitmaps;
    private Context context;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                notifyDataSetChanged();
            }
        }
    };

    public ComplainAdapter(Context context, OnItemClickListener onItemDelClickListener) {
        this.onItemDelClickListener = onItemDelClickListener;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(ArrayList<Bitmap> bitmaps) {
        if (bitmaps != null && bitmaps.size() > 0) {
            this.bitmaps = (List<Bitmap>) bitmaps.clone();
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mPicture;
        ImageView mDelPicture;

        public ViewHolder(View itemView) {
            super(itemView);
            mPicture = itemView.findViewById(R.id.modifyImg);
            mDelPicture = itemView.findViewById(R.id.delete);
        }
    }

    public void deleteItem(int position){
        notifyItemRemoved(position);
        bitmaps.remove(position);
        mHandler.sendEmptyMessageDelayed(0,500);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_modify_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (position == bitmaps.size() - 1) {
            viewHolder.mDelPicture.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.add_pic).into(viewHolder.mPicture);
            if (position == 6) {
                viewHolder.mPicture.setVisibility(View.GONE);
            } else {
                viewHolder.mPicture.setVisibility(View.VISIBLE);
            }
            viewHolder.mPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemDelClickListener != null) {
                        onItemDelClickListener.onItemClickListener(viewHolder.mPicture, position);
                    }
                }
            });
        } else {
            viewHolder.mDelPicture.setVisibility(View.VISIBLE);
            viewHolder.mDelPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemDelClickListener != null) {
                        onItemDelClickListener.onItemDelListener(position, viewHolder.mPicture);
                    }
                }
            });
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewHolder.mPicture.setImageBitmap(bitmaps.get(position));
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return bitmaps.size()==0?0:bitmaps.size();
    }

    public interface OnItemClickListener {
        void onItemDelListener(int position, ImageView picture);

        void onItemClickListener(View view, int position);
    }
}
