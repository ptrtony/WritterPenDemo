package com.android.bluetown.adapter;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Administrator on 2018/5/20.
 */

public interface BaseMomentVH <T>{
    void onFindView(@NonNull View rootView);

    void onBindDataToView(@NonNull final T data,int position,int viewType);
}
