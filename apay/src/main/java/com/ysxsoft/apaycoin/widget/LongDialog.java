package com.ysxsoft.apaycoin.widget;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ysxsoft.apaycoin.R;

public class LongDialog extends ABSDialog {
    public LongDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.long_dialog_layout;
    }
}
