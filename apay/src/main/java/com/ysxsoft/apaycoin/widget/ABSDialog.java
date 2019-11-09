package com.ysxsoft.apaycoin.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.ysxsoft.apaycoin.R;

/**
 * 描述： dialog 的基类
 * 日期： 2018/11/5 0005 11:38
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public abstract class ABSDialog extends Dialog {
    public ABSDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        setContentView(getLayoutResId());
        initView();
    }

    /**
     * 根据Id获取View
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    protected abstract void initView();

    protected abstract int getLayoutResId();
}
