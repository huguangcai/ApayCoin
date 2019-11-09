package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpScanPayService;
import com.ysxsoft.apaycoin.modle.ScanPayBean;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.paypwdpopuwindow.popwindow.SelectPopupWindow;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 扫码支付界面
 * 日期： 2018/11/23 0023 17:20
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ScanPayActivity extends BaseActivity implements View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {

    private View img_back;
    private TextView tv_title;
    private Button btn_check_pay;
    private String qrcode;
    private EditText ed_pay_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_change_balance_money_layout);
        Intent intent = getIntent();
        qrcode = intent.getStringExtra("qrcode");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("扫码支付");
        ed_pay_money = getViewById(R.id.ed_pay_money);
        btn_check_pay = getViewById(R.id.btn_check_pay);
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        btn_check_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_check_pay:
                if (TextUtils.isEmpty(ed_pay_money.getText().toString().trim())) {
                    showToastMessage("支付金额不能为空");
                    return;
                }
                showPayPopupwindow();
                break;

        }
    }
    private void showPayPopupwindow() {
        SelectPopupWindow menuWindow = new SelectPopupWindow(this, this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);

    }

    private void submitData(String psw) {
        NetWork.getRetrofit()
                .create(ImpScanPayService.class)
                .getCall(NetWork.getToken(), ed_pay_money.getText().toString().trim(), qrcode,psw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ScanPayBean>() {
                    private ScanPayBean scanPayBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(scanPayBean.getMsg());
                        if ("0".equals(scanPayBean.getCode())){
                            finish();
                        }else if ("2" .equals( scanPayBean.getCode())) {
                            SharedPreferences.Editor sp =getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first =getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            startActivity(LoginActivity.class);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(ScanPayBean scanPayBean) {
                        this.scanPayBean = scanPayBean;
                    }
                });

    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete){
            submitData(psw);
        }
    }

    @Override
    public void onForgetPayPwd() {
        startActivity(ForgetPayPwdActivity.class);
    }
}
