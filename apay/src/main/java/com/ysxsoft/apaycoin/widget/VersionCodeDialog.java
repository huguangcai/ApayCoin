package com.ysxsoft.apaycoin.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.utils.AppUtil;

/**
 * 描述： 版本提示
 * 日期： 2018/11/6 0006 14:02
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class VersionCodeDialog extends ABSDialog {

    private TextView tv_version_code, tv_version_code_tip;
    private TextView tv_i_kown;

    public VersionCodeDialog(@NonNull Context context) {
        super(context);
        initData(context);
    }

    private void initData(Context context) {
//        tv_version_code.setText(String.valueOf(AppUtil.getVersionCode(context)));
        tv_version_code.setText(AppUtil.getVersionName(context));
        tv_i_kown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void initView() {
        tv_version_code = getViewById(R.id.tv_version_code);
        tv_version_code_tip = getViewById(R.id.tv_version_code_tip);
        tv_i_kown = getViewById(R.id.tv_i_kown);


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.version_code_layout;
    }
}
