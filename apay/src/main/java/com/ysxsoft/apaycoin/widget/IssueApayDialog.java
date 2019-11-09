package com.ysxsoft.apaycoin.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.view.AddBankCardActivity;

public class IssueApayDialog extends ABSDialog {

    private Context context;

    public IssueApayDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void initView() {
        TextView tv_go_check = getViewById(R.id.tv_go_check);
        tv_go_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,AddBankCardActivity.class));
                dismiss();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.issue_apay_dialog_layout;
    }
}
