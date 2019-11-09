package com.ysxsoft.apaycoin.widget;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ysxsoft.apaycoin.R;

/**
 * 描述： TODO
 * 日期： 2018/11/21 0021 10:41
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class CrowFundingRunBuyDialog extends ABSDialog {
    public CrowFundingRunBuyDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        PayPwdEditText payPwdEditText = (PayPwdEditText) findViewById(R.id.ed_ppet);
        payPwdEditText.initStyle(R.drawable.edit_num_bg_red, 6, 0.33f, R.color.black, R.color.black, 20);
        payPwdEditText.setFocus();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.crowdfunfing_run_buy_dialog;
    }
}
