package com.android.bluetown.multiphoto;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

/**
 * Created by Dafen on 2018/5/25.
 */

public class MediaLoader implements AlbumLoader {
    @Override

    public void load(ImageView imageView, AlbumFile albumFile) {

        load(imageView, albumFile.getPath());

    }



    @Override

    public void load(ImageView imageView, String url) {

        Glide.with(imageView.getContext())

                .load(url)
//                .error(R.drawable.placeholder)
//
//                .placeholder(R.drawable.placeholder)

                .crossFade()

                .into(imageView);

    }
}
