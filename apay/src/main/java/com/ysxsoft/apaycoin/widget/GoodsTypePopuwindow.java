package com.ysxsoft.apaycoin.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * 描述： 商品分类的popuwindow
 * 日期： 2018/11/24 0024 10:57
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
@Deprecated
public class GoodsTypePopuwindow extends PopupWindow {

    private Context context;

    public GoodsTypePopuwindow(Context context){
        this.context = context;
        InitView();
    }

    private void InitView() {
        View view = LayoutInflater.from(context).inflate(com.ysxsoft.apaycoin.R.layout.goods_type_popuwindow_layout, null);
        view.findViewById(com.ysxsoft.apaycoin.R.id.recyclerView);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(view);

        setFocusable(true);//设置获取焦点
        setTouchable(true);//设置可以触摸
        setOutsideTouchable(true);//设置外边可以点击
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }
}
