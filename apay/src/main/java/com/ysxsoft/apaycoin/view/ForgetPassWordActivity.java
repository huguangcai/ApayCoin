package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpForgetLoginPayPwdService;
import com.ysxsoft.apaycoin.impservice.ImpIdentifyingCodeService;
import com.ysxsoft.apaycoin.modle.ForgetLoginPayPwdBean;
import com.ysxsoft.apaycoin.modle.IdentifyingCodeBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.CountDownTimeHelper;
import com.ysxsoft.apaycoin.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 忘记密码界面
 * 日期： 2018/11/2 0002 11:54
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ForgetPassWordActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_phone_num, ed_identifying_code, ed_setting_login_pwd, ed_second_login_pwd;
    private TextView get_identifying_code, tv_title;
    private Button btn_submit;
    private View back;
    private int type = 1;//1 是登录密码  2 是支付密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_layout);
        initView();
        initListener();
    }

    private void initView() {
        back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("忘记登录密码");
        ed_phone_num = getViewById(R.id.ed_phone_num);
        ed_identifying_code = getViewById(R.id.ed_identifying_code);
        get_identifying_code = getViewById(R.id.get_identifying_code);
        ed_setting_login_pwd = getViewById(R.id.ed_setting_login_pwd);
        ed_second_login_pwd = getViewById(R.id.ed_second_login_pwd);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        back.setOnClickListener(this);
        get_identifying_code.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.get_identifying_code:
                if (TextUtils.isEmpty(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("手机号不能为空");
                    return;
                }
                if (!AppUtil.checkPhoneNum(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("请输入正确的手机号");
                    return;
                }
                CountDownTimeHelper helper=new CountDownTimeHelper(60,get_identifying_code);
                sendMessage();
                break;
            case R.id.btn_submit:
//                checkData();
                if (TextUtils.isEmpty(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("手机号不能为空");
                    return;
                }
                if (!AppUtil.checkPhoneNum(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(ed_identifying_code.getText().toString().trim())) {
                    showToastMessage("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_setting_login_pwd.getText().toString().trim())) {
                    showToastMessage("密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_second_login_pwd.getText().toString().trim())) {
                    showToastMessage("确认密码不能为空");
                    return;
                }
                if (!ed_setting_login_pwd.getText().toString().trim().equals(ed_second_login_pwd.getText().toString().trim())) {
                    showToastMessage("两次密码输入不一致");
                }
                submitData();
                break;
        }
    }

    private void submitData() {
        NetWork.getRetrofit()
                .create(ImpForgetLoginPayPwdService.class)
                .getCall(ed_phone_num.getText().toString().trim(),
                        ed_identifying_code.getText().toString().trim(),
                        ed_second_login_pwd.getText().toString().trim(), type + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ForgetLoginPayPwdBean>() {
                    private ForgetLoginPayPwdBean forgetLoginPayPwdBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(forgetLoginPayPwdBean.getMsg());
                        if ("0".equals(forgetLoginPayPwdBean.getCode())){
                            finish();
                        }else if ("2" == forgetLoginPayPwdBean.getCode()) {
                            SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager   instance = ActivityPageManager.getInstance();
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
                    public void onNext(ForgetLoginPayPwdBean forgetLoginPayPwdBean) {
                        this.forgetLoginPayPwdBean = forgetLoginPayPwdBean;
                    }
                });
    }

    /**
     * 发送验证码
     */
    private void sendMessage() {
        NetWork.getRetrofit()
                .create(ImpIdentifyingCodeService.class)
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
                            ActivityPageManager   instance = ActivityPageManager.getInstance();
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

    /***
     * 核对数据是否符合
     */
    private void checkData() {
        if (TextUtils.isEmpty(ed_phone_num.getText().toString().trim())) {
            showToastMessage("手机号不能为空");
            return;
        }
        if (!AppUtil.checkPhoneNum(ed_phone_num.getText().toString().trim())) {
            showToastMessage("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(ed_identifying_code.getText().toString().trim())) {
            showToastMessage("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(ed_setting_login_pwd.getText().toString().trim())) {
            showToastMessage("密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(ed_second_login_pwd.getText().toString().trim())) {
            showToastMessage("确认密码不能为空");
            return;
        }
        if (!ed_setting_login_pwd.getText().toString().trim().equals(ed_second_login_pwd.getText().toString().trim())) {
            showToastMessage("两次密码输入不一致");
        }
    }
}
