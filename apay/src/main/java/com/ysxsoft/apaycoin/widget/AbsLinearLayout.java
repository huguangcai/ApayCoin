package com.ysxsoft.apaycoin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 描述： LinearLayout的基类
 * 日期： 2018/10/23 0023 10:43
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public abstract class AbsLinearLayout extends LinearLayout {

    public AbsLinearLayout(Context context) {
        super(context);
        init();
    }

    public AbsLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttributeSet(attrs);
    }

    /**
     * 初始化布局
     */
    private void init(){
        LayoutInflater.from(getContext()).inflate(getLayoutResId(), this);
        initView();
    }

    /**
     * 初始化属性
     */
    protected void initAttributeSet(AttributeSet attrs){

    }

    /**
     *
     * 获取指定资源Id的View
     */
    protected <T extends View> T getViewById(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 布局资源ID
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化view
     */
    protected abstract void initView();
}
