package com.ysxsoft.apaycoin.banner;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * 描述： TODO
 * 日期： 2018/10/30 0030 16:26
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImageLoaderInterface<T extends View> extends Serializable {
    void displayImage(Context context, Object path, T imageView);

    T createImageView(Context context);
}
