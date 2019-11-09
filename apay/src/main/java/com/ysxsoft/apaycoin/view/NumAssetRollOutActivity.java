package com.ysxsoft.apaycoin.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpChangeAdvertisementMoneyService;
import com.ysxsoft.apaycoin.modle.ChangeAdvertisementMoneyBean;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.paypwdpopuwindow.popwindow.SelectPopupWindow;
import com.ysxsoft.zxing.CaptureActivity;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 描述： 数字资产界面的转出
 * 日期： 2018/11/15 0015 17:04
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NumAssetRollOutActivity extends BaseActivity implements View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {

    public static final int ROLL_OUT_SCAN_CODE = 1;
    private View img_back;
    private TextView tv_title;
    private EditText ed_roll_out_num, ed_wallet_address;
    private ImageView img_scan;
    private Button btn_roll_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_asset_roll_out_layout);
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("转出");
        ed_roll_out_num = getViewById(R.id.ed_roll_out_num);
        ed_wallet_address = getViewById(R.id.ed_wallet_address);
        img_scan = getViewById(R.id.img_scan);
        btn_roll_out = getViewById(R.id.btn_roll_out);
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        img_scan.setOnClickListener(this);
        btn_roll_out.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_scan:
                RxPermissions.getInstance(mContext)
                        .request(Manifest.permission.CAMERA)//多个权限用","隔开
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    //当所有权限都允许之后，返回true
                                    Intent intent = new Intent(mContext, CaptureActivity.class);
                                    startActivityForResult(intent, ROLL_OUT_SCAN_CODE);
                                } else {
                                    //只要有一个权限禁止，返回false，
                                    //下一次申请只申请没通过申请的权限
                                  showToastMessage("请打开权限");
                                }
                            }
                        });
//                Intent intent = new Intent(mContext, CaptureActivity.class);
//                startActivityForResult(intent, ROLL_OUT_SCAN_CODE);
//                startActivity(CaptureActivity.class);
                break;
            case R.id.btn_roll_out:
                if (TextUtils.isEmpty(ed_roll_out_num.getText().toString().trim())) {
                    showToastMessage("转出数额不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_wallet_address.getText().toString().trim())) {
                    showToastMessage("钱包地址不能为空");
                    return;
                }
                showPayPopupwindow();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case ROLL_OUT_SCAN_CODE:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("scan_result");
                    ed_wallet_address.setText(result);
                } else if (resultCode == RESULT_CANCELED) {
//                    showToastMessage("扫描出错");
                }
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

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            submitData(psw);
        }
    }

    /**
     * 扫码转广告币
     */
    private void submitData(String psw) {
        NetWork.getRetrofit()
                .create(ImpChangeAdvertisementMoneyService.class)
                .getCall(NetWork.getToken(), ed_roll_out_num.getText().toString().trim(), psw, ed_wallet_address.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChangeAdvertisementMoneyBean>() {
                    private ChangeAdvertisementMoneyBean changeAdvertisementMoneyBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(changeAdvertisementMoneyBean.getMsg());
                        if ("0".equals(changeAdvertisementMoneyBean.getCode())){
                            finish();
                        } else if ("2".equals(changeAdvertisementMoneyBean.getCode())) {
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
                    public void onNext(ChangeAdvertisementMoneyBean changeAdvertisementMoneyBean) {
                        this.changeAdvertisementMoneyBean = changeAdvertisementMoneyBean;
                    }
                });
    }

    @Override
    public void onForgetPayPwd() {
        startActivity(ForgetPayPwdActivity.class);
    }
}
