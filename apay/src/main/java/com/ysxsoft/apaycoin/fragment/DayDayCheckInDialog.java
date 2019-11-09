package com.ysxsoft.apaycoin.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.widget.ABSDialog;

/**
 * 描述： 天天签到弹窗
 * 日期： 2018/11/5 0005 10:12
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class DayDayCheckInDialog extends ABSDialog {

    public DayDayCheckInDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void initView() {
        TextView tv_money = getViewById(R.id.tv_money);
        Button btn_deposit_balance = getViewById(R.id.btn_deposit_balance);
        btn_deposit_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.check_in_layout;
    }
}
