package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpIdentifyingCodeService;
import com.ysxsoft.apaycoin.impservice.ImpRegisterService;
import com.ysxsoft.apaycoin.modle.IdentifyingCodeBean;
import com.ysxsoft.apaycoin.modle.RegisterBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.CountDownTimeHelper;
import com.ysxsoft.apaycoin.utils.CustomDialog;
import com.ysxsoft.apaycoin.utils.NetWork;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 注册界面
 * 日期： 2018/11/2 0002 11:53
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_invite_code, ed_nike_name, ed_phone_num, ed_identifying_code, ed_login_pwd;
    private EditText ed_second_login_pwd, ed_pay_pwd, ed_second_pay_pwd;
    private Button btn_register;
    private TextView tv_immediately_login, tv_existing_account, get_identifying_code,tv_register;
    private CustomDialog customDialog;
    private int stateBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customDialog = new CustomDialog(mContext,"正在提交数据...");
        setContentView(R.layout.register_layout);
        setHalfTransparent();
        setFitSystemWindow(false);
        stateBar = getStateBar();
        initView();
        initListener();
    }

    private void initListener() {
        btn_register.setOnClickListener(this);
        tv_immediately_login.setOnClickListener(this);
        tv_existing_account.setOnClickListener(this);
        get_identifying_code.setOnClickListener(this);
    }

    private void initView() {
        tv_register = getViewById(R.id.tv_register);
        tv_register.setPadding(0,stateBar,0,0);
        ed_invite_code = getViewById(R.id.ed_Invite_code);
        ed_nike_name = getViewById(R.id.ed_nike_name);
        ed_phone_num = getViewById(R.id.ed_phone_num);
        ed_identifying_code = getViewById(R.id.ed_identifying_code);
        ed_login_pwd = getViewById(R.id.ed_login_pwd);
        ed_second_login_pwd = getViewById(R.id.ed_second_login_pwd);
        ed_pay_pwd = getViewById(R.id.ed_pay_pwd);
        ed_second_pay_pwd = getViewById(R.id.ed_second_pay_pwd);
        btn_register = getViewById(R.id.btn_register);
        tv_immediately_login = getViewById(R.id.tv_immediately_login);
        tv_existing_account = getViewById(R.id.tv_existing_account);
        get_identifying_code = getViewById(R.id.get_identifying_code);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_identifying_code:
                if (checkData()) return;
                CountDownTimeHelper helper=new CountDownTimeHelper(60,get_identifying_code);
                sendMessage();
                break;
            case R.id.btn_register:
                if (checkData()) return;
                if (checkPwdData()) return;
                customDialog.show();
                register();
                break;
            case R.id.tv_immediately_login:
                finish();
                break;
            case R.id.tv_existing_account:
                finish();
                break;
        }
    }

    /**
     * 注册
     */
    private void register() {
        NetWork.getRetrofit().
                create(ImpRegisterService.class)
                .getRegister(ed_phone_num.getText().toString().trim(),
                        ed_login_pwd.getText().toString().trim(),
                        ed_identifying_code.getText().toString().trim(),
                        ed_invite_code.getText().toString().trim(),
                        ed_pay_pwd.getText().toString().trim(),
                        ed_nike_name.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterBean>() {
                    private RegisterBean registerBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(registerBean.getMsg());
                        if ("0".equals(registerBean.getCode())) {
                            customDialog.dismiss();
//                            SharedPreferences.Editor sp = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
//                            sp.putBoolean("is_first", true);
//                            sp.commit();
//                            startActivity(LoginActivity.class);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(RegisterBean registerBean) {
                        this.registerBean = registerBean;
                    }
                });

    }

    /**
     * 发送短信
     */
    private void sendMessage() {
        NetWork.getRetrofit().
                create(ImpIdentifyingCodeService.class)
                .getIdentifyingCode(ed_phone_num.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IdentifyingCodeBean>() {
                    private IdentifyingCodeBean identifyingCodeBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(identifyingCodeBean.getMsg());
                        if ("0".equals(identifyingCodeBean.getCode())) {
//                            get_identifying_code.setEnabled(true);
                        }else if ("2" == identifyingCodeBean.getCode()) {
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
                        log(e.getMessage());
                    }

                    @Override
                    public void onNext(IdentifyingCodeBean identifyingCodeBean) {
                        this.identifyingCodeBean = identifyingCodeBean;
                    }
                });
    }

    /**
     * 核对密码是否符合
     *
     * @return
     */
    private boolean checkPwdData() {
        if (TextUtils.isEmpty(ed_identifying_code.getText().toString().trim())) {
            showToastMessage("验证码不能为空");
            return true;
        }
        if (TextUtils.isEmpty(ed_login_pwd.getText().toString().trim())) {
            showToastMessage("密码不能为空");
            return true;
        }
        if (TextUtils.isEmpty(ed_second_login_pwd.getText().toString().trim())) {
            showToastMessage("确认密码不能为空");
            return true;
        }
        if (!ed_login_pwd.getText().toString().trim().equals(ed_second_login_pwd.getText().toString().trim())) {
            showToastMessage("两次输入密码不一致");
            return true;
        }

        if (TextUtils.isEmpty(ed_pay_pwd.getText().toString().trim())) {
            showToastMessage("支付密码不能为空");
            return true;
        }
        if (ed_pay_pwd.getText().toString().trim().length()!=6){
            showToastMessage("支付密码为六位数");
            return true;
        }

        if (TextUtils.isEmpty(ed_second_pay_pwd.getText().toString().trim())) {
            showToastMessage("确认支付密码不能为空");
            return true;
        }
        if (!ed_pay_pwd.getText().toString().trim().equals(ed_second_pay_pwd.getText().toString().trim())) {
            showToastMessage("两次输入支付密码不一致");
            return true;
        }
        return false;
    }

    /***
     * 核对数据是否很符合
     * @return
     */
    private boolean checkData() {
        if (TextUtils.isEmpty(ed_nike_name.getText().toString().trim())) {
            showToastMessage("昵称不能为空");
            return true;
        }
        if (TextUtils.isEmpty(ed_phone_num.getText().toString().trim())) {
            showToastMessage("手机号不能为空");
            return true;
        }
        if (!AppUtil.checkPhoneNum(ed_phone_num.getText().toString().trim())) {
            showToastMessage("请输入正确的手机号");
            return true;
        }
        return false;
    }

}
