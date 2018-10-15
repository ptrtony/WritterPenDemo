package com.android.bluetown.utils;

import android.app.Activity;

import com.android.annotations.NonNull;
import com.android.bluetown.img.AlbumFilesInterface;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

/**
 * Created by Dafen on 2018/7/5.
 */

public class AlbumUtils {

    public static void selectAlbume(Activity activity, ArrayList<AlbumFile> albumFiles,AlbumFilesInterface interfaces){
        Album.album(activity)

                .multipleChoice()

                .columnCount(4)

                .selectCount(6)

                .camera(true)

                .cameraVideoQuality(1)

                .cameraVideoLimitDuration(Integer.MAX_VALUE)

                .cameraVideoLimitBytes(Integer.MAX_VALUE)

                .checkedList(albumFiles)

                .onResult(new Action<ArrayList<AlbumFile>>() {

                    @Override

                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        if (interfaces!=null){
                            interfaces.setAlbumFiles(result);
                        }
                    }

                })

                .onCancel(new Action<String>() {
                    @Override

                    public void onAction(@NonNull String result) {

                    }
                })
                .start();
    }

    public static void cameraPicture(Activity activity,AlbumFilesInterface interfaces){
        Album.camera(activity) // Camera function.
                .image() // Take Picture.
//                .filePath("filePath") // File save path, not required.
                .onResult(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        if (interfaces!=null){
                            interfaces.setFilePath(result);
                        }
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {

                    }
                })
                .start();
    }


}
