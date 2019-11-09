package com.ysxsoft.apaycoin.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.ysxsoft.apaycoin.R;

/**
 * 描述： TODO
 * 日期： 2018/11/13 0013 17:31
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ModifyNikeNameDialog extends ABSDialog {

    public ModifyNikeNameDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        Button btn_cancel = getViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.modify_nike_name_dialog_layout;
    }
}
