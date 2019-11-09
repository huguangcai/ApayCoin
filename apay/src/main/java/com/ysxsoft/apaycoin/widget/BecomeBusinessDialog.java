package com.ysxsoft.apaycoin.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;

/**
 * 描述： TODO
 * 日期： 2018/11/12 0012 17:12
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BecomeBusinessDialog extends ABSDialog {
    public BecomeBusinessDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        TextView tv_i_kown = getViewById(R.id.tv_i_kown);
        tv_i_kown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.become_business_dialog_layout;
    }
}
