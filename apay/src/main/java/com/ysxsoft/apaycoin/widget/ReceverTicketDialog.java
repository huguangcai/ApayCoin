package com.ysxsoft.apaycoin.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ysxsoft.apaycoin.R;

public class ReceverTicketDialog extends ABSDialog {
    private Context context;

    public ReceverTicketDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void initView() {
        ImageView viewById = getViewById(R.id.img_recever_ticket);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Toast.makeText(context,"领取成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.receive_ticket_dialog_layout;
    }
}
