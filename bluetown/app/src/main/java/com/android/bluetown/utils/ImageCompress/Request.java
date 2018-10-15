package com.android.bluetown.utils.ImageCompress;
import android.graphics.drawable.Drawable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Dafen on 2018/6/22.
 */

public class Request {
    public static BufferedInputStream handlerData(String url){
        URLConnection connection;
        InputStream is;
        BufferedInputStream bis = null;
        try {
            connection = new URL(url).openConnection();
            is = connection.getInputStream();
            bis = new BufferedInputStream(is);
            bis.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bis;
    }

    public static Drawable loadImageFromNetwork(String imageUrl){
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(),"image.jpg");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }
}
