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
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.impservice.ImpConvertibilityService;
import com.ysxsoft.apaycoin.impservice.ImpNumAssetApayService;
import com.ysxsoft.apaycoin.modle.ConvertibilityBean;
import com.ysxsoft.apaycoin.modle.NumAssetApayBean;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.paypwdpopuwindow.popwindow.SelectPopupWindow;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 兑换券界面
 * 日期： 2018/11/5 0005 14:38
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ConvertibilityActivity extends BaseActivity implements View.OnClickListener,SelectPopupWindow.OnPopWindowClickListener {
    private View img_back;
    private TextView tv_title, tv_title_right, tv_balance_money, tv_ticket_num;
    private EditText ed_banlance_change_money;
    private Button btn_check_change;
    private String phone_uid;
    private String name;
    private String headPath, uid, money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_ticket_layout);
        Intent intent = getIntent();
        phone_uid = intent.getStringExtra("对方账户");
        name = intent.getStringExtra("name");
        headPath = intent.getStringExtra("headPath");
        uid = intent.getStringExtra("uid");
        money = intent.getStringExtra("money");
        initView();
        initdata();
        initLisetener();

    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("兑换券");
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("兑换记录");
        tv_balance_money = getViewById(R.id.tv_balance_money);
        tv_ticket_num = getViewById(R.id.tv_ticket_num);
        ed_banlance_change_money = getViewById(R.id.ed_banlance_change_money);
        btn_check_change = getViewById(R.id.btn_check_change);
    }

    private void initdata() {
        NetWork.getRetrofit()
                .create(ImpNumAssetApayService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NumAssetApayBean>() {
                    private NumAssetApayBean numAssetApayBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(numAssetApayBean.getCode())) {
                            tv_balance_money.setText(numAssetApayBean.getData().getMoney());
                            tv_ticket_num.setText(numAssetApayBean.getData().getQuans());
                        }else if ("2" .equals( numAssetApayBean.getCode())) {
                            SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
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
                    public void onNext(NumAssetApayBean numAssetApayBean) {
                        this.numAssetApayBean = numAssetApayBean;
                    }
                });
    }

    private void initLisetener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        btn_check_change.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_title_right:
//                startActivity(ExchangeRecordActivity.class);
                startActivity(ChangeTicketRecordActivity.class);
                break;
            case R.id.btn_check_change:
                if (TextUtils.isEmpty(ed_banlance_change_money.getText().toString().trim())) {
                    showToastMessage("兑换金额不能为空");
                    return;
                }
                if (Math.floor(Double.valueOf(tv_balance_money.getText().toString()))<Double.valueOf(ed_banlance_change_money.getText().toString().trim())){
                    showToastMessage("余额不足，请先充值");
                    return;
                }
                if (Integer.valueOf(ed_banlance_change_money.getText().toString().trim())<=0){
                    showToastMessage("输入金额不能为0");
                    return;
                }
                if ((Integer.valueOf(ed_banlance_change_money.getText().toString().trim())) % 100 != 0) {
                    showToastMessage("请输入100的整数倍");
                    return;
                }
                showpayPopuwindow();
//                startActivity(MyActivity.class);
//                finish();
                break;

        }
    }
    private void showpayPopuwindow() {
        SelectPopupWindow menuWindow = new SelectPopupWindow(this, this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
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

    private void submitData(String psw) {
         NetWork.getRetrofit()
                 .create(ImpConvertibilityService.class)
                .getCall(NetWork.getToken(),ed_banlance_change_money.getText().toString().trim(),psw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ConvertibilityBean>() {
                    private ConvertibilityBean convertibilityBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(convertibilityBean.getMsg());
                        if ("0".equals(convertibilityBean.getCode())){
                            finish();
                        }else if ("2".equals(convertibilityBean.getCode())) {
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
                    public void onNext(ConvertibilityBean convertibilityBean) {
                        this.convertibilityBean = convertibilityBean;
                    }
                });
    }
}
