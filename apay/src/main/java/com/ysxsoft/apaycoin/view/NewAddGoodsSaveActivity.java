package com.ysxsoft.apaycoin.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.BaseActivity;

public class NewAddGoodsSaveActivity extends BaseActivity implements View.OnClickListener{

    private View img_back;
    private TextView tv_title;
    private Button btn_look_googs_detial;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_new_add_goods_save_layout);
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("新增商品");
        btn_look_googs_detial = getViewById(R.id.btn_look_googs_detial);

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        btn_look_googs_detial.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                Intent intent1=new Intent("FINISH");
                sendBroadcast(intent1);
                finish();
                break;
            case R.id.btn_look_googs_detial:
                Intent intent=new Intent(mContext,MallGoodsDetialActivity.class);
                intent.putExtra("pid",pid);
                intent.putExtra("newGoods","newGoods");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1=new Intent("FINISH");
        sendBroadcast(intent1);
        finish();

    }
}
