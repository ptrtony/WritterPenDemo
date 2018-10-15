package com.android.bluetown.img;


import com.android.bluetown.R;
import com.android.bluetown.img.ShowBigPicture.MyTouchListener;
import com.android.bluetown.view.VerticalSlideLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wingsofts.dragphotoview.DragPhotoView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


@SuppressLint("ValidFragment")
public class PictureFragment extends Fragment implements VerticalSlideLayout.PageChangeListener {

    private String url;
    private int downX, upX;
    private DragPhotoView imageView;
    private static final String LOCATION_POSITION = "position";
    private static final String URL = "url";
    private int position=0;
    public PictureFragment getNewInstance(String url,int position) {
        PictureFragment pictureFragment = new PictureFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putInt(LOCATION_POSITION, position);
        pictureFragment.setArguments(bundle);
        return pictureFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.scale_pic_item, null);
//        mSlideLayout.setPageChangeListener(this);
        initView(view, savedInstanceState);
        //�ڸ�Fragment�Ĺ��캯����ע��mTouchListener�Ļص�
        ((ShowBigPicture) this.getActivity()).registerMyTouchListener(mTouchListener);

//        mSlideLayout = view.findViewById(R.id.verticalSlideLayout);
//        mSlideLayout.setDragListener(new PhotoDragHelper().setOnDragListener(new PhotoDragHelper.OnDragListener() {
//            @Override
//            public void onAlpha(float alpha) {
//                Log.d("pictureFragment","透明度的数值"+alpha);
//                //透明度的改变
//                mSlideLayout.setAlpha(alpha);
//            }
//            @Override
//            public View getDragView() {
//                //返回需要拖拽的view
//                return imageView;
//            }
//            @Override
//            public void onAnimationEnd(boolean isRestoration) {
//                //isRestoration true 执行恢复动画  false 执行结束动画
//                if (isRestoration) {
//                    ((ShowBigPicture)getActivity()).finish();
//                    ((ShowBigPicture)getActivity()).overridePendingTransition(0, 0);
//                }
//
//            }
//        }));

        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        imageView = view.findViewById(R.id.scale_pic_item);
        //	imageView.setImageResource(resId);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            url = bundle.getString(URL);
            position = bundle.getInt(LOCATION_POSITION);
        }
        displayImage(url, imageView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName("transition"+position);
        }

    }

    public void displayImage(String imageURL, DragPhotoView imageView) {
        if (!TextUtils.isEmpty(imageURL) && imageURL != null) {
//            Glide.with(getActivity())
//                    .load(imageURL)
//                    .centerCrop()
//                    .skipMemoryCache(true)
//                    .placeholder(R.drawable.ic_msg_empty)
//                    .error(R.drawable.ic_msg_empty)
//                    .into(new SimpleTarget<GlideDrawable>() {
//                        @Override
//                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                            imageView.setImageDrawable(resource);
//                            startPostponedEnterTransition();
//                        }
//                    });

            ImageLoader.getInstance().displayImage(imageURL, imageView);// ʹ��ImageLoader��ͼƬ���м�װ��
        }
        imageView.setOnPhotoTapListener(new uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
//                getActivity().finish();
                getActivity().supportFinishAfterTransition();
            }

            @Override
            public void onOutsidePhotoTap() {
                getActivity().supportFinishAfterTransition();
//                getActivity().finish();
            }
        });

        imageView.setOnExitListener(new DragPhotoView.OnExitListener() {
            @Override
            public void onExit(DragPhotoView dragPhotoView, float v, float v1, float v2, float v3) {
                getActivity().finish();
            }
        });
//        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//            @Override
//            public void onPhotoTap(View view, float x, float y) {
//                getActivity().finish();
//            }
//        });
    }


    /**
     * Fragment�У�ע��
     * ����MainActivity��Touch�ص��Ķ���
     * ��д���е�onTouchEvent������и�Fragment���߼�����
     */
    private MyTouchListener mTouchListener = new MyTouchListener() {
        @Override
        public void onTouchEvent(MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                downX = (int) event.getX();

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                upX = (int) event.getX();
                if (upX > downX) {
                    if ((upX - downX < 10)) {
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }

                } else if (upX < downX) {
                    if ((downX - upX < 10)) {
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }

                }
            }
        }
    };

    @Override
    public void onPrevPage() {
        getActivity().finish();
    }

    @Override
    public void onNextPage() {

    }


}
