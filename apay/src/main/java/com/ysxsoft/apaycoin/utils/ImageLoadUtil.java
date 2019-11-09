package com.ysxsoft.apaycoin.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ysxsoft.apaycoin.R;

/**
 * 描述： 图片加载
 * 日期： 2018/11/8 0008 13:56
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ImageLoadUtil {
    public static void GlideImageLoad(Context context,String url,ImageView view){
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_normal_head).error(R.mipmap.img_normal_head);
        Glide.with(context).load(url).apply(options).into(view);
    }
    public static void NewGoodsGlideImageLoad(Context context,String url,ImageView view){
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_normal_img).error(R.mipmap.img_normal_img);
        Glide.with(context).load(url).apply(options).into(view);
    }
}
