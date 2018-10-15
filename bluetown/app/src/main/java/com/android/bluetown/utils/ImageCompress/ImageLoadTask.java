package com.android.bluetown.utils.ImageCompress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.tsz.afinal.core.AsyncTask;


/**
 * Created by Dafen on 2018/6/22.
 */

public class ImageLoadTask extends AsyncTask<String,Void,Void> {

    @Override
    protected Void doInBackground(String... strings) {
        String url = strings[0];
        Bitmap bitmap = BitmapFactory.decodeStream(Request.handlerData(url));

        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        if (isCancelled())return;

    }


}
