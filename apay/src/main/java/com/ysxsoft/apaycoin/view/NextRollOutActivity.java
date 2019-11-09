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
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.impservice.ImpNextRollOutService;
import com.ysxsoft.apaycoin.modle.NextRollOutBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.paypwdpopuwindow.popwindow.SelectPopupWindow;

import java.text.DecimalFormat;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 点击下一步 跳转到转出界面
 * 日期： 2018/11/5 0005 14:31
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NextRollOutActivity extends BaseActivity implements View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {

    private View img_back;
    private TextView tv_title, tv_balance_money, tv_user_name;
    private EditText ed_phone_num, tv_roll_balance;
    private Button btn_check_roll_out;
    private TextView tv_title_right;
    private String phone_uid;
    private ImageView img_head;
    private DecimalFormat decimalFormat;
    private String name;
    private String headPath, uid, money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_roll_out_layout);
        Intent intent = getIntent();
        phone_uid = intent.getStringExtra("对方账户");
        decimalFormat = new DecimalFormat("0.00");
        name = intent.getStringExtra("name");
        headPath = intent.getStringExtra("headPath");
        uid = intent.getStringExtra("uid");
        money = intent.getStringExtra("money");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("转出");
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("转出记录");
        img_head = getViewById(R.id.img_head);
        tv_user_name = getViewById(R.id.tv_user_name);
        ed_phone_num = getViewById(R.id.ed_phone_num);
        tv_roll_balance = getViewById(R.id.tv_roll_balance);
        tv_balance_money = getViewById(R.id.tv_balance_money);
        btn_check_roll_out = getViewById(R.id.btn_check_roll_out);
    }

    private void initData() {
        ImageLoadUtil.GlideImageLoad(mContext, headPath, img_head);
        tv_user_name.setText(name + "(" + uid + ")");
        tv_balance_money.setText(money);
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        btn_check_roll_out.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_title_right:
//                showToastMessage("转出记录");
                startActivity(RollOutRecordActivity.class);
                break;
            case R.id.btn_check_roll_out:
                if (TextUtils.isEmpty(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("手机号后4位不能为空");
                    return;
                }
                if (TextUtils.isEmpty(tv_roll_balance.getText().toString().trim())) {
                    showToastMessage("金额不能为空");
                    return;
                }
//                if (Integer.valueOf(tv_roll_balance.getText().toString().trim())<=0){
//                    showToastMessage("请输入正确金额");
//                    return;
//                }
//                if (Math.floor(Double.valueOf(tv_balance_money.getText().toString()))<Integer.valueOf(tv_roll_balance.getText().toString().trim())){
//                    showToastMessage("余额不足，请先充值");
//                    return;
//                }
                showpayPopuwindow();
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
        if (complete) {
            submitData(psw);
        }
    }

    @Override
    public void onForgetPayPwd() {
        startActivity(ForgetPayPwdActivity.class);
    }

    /**
     * 提交数据
     */
    private void submitData(String psw) {
        NetWork.getRetrofit()
                .create(ImpNextRollOutService.class)
                .getCall(NetWork.getToken(),
                        uid,
                        tv_roll_balance.getText().toString().trim(),
                        ed_phone_num.getText().toString().trim(),
                        psw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NextRollOutBean>() {
                    private NextRollOutBean nextRollOutBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(nextRollOutBean.getMsg());
                        if ("0".equals(nextRollOutBean.getCode())) {
                            finish();
                        }else if ("2" .equals( nextRollOutBean.getCode())) {
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
                    public void onNext(NextRollOutBean nextRollOutBean) {
                        this.nextRollOutBean = nextRollOutBean;
                    }
                });
    }
}
