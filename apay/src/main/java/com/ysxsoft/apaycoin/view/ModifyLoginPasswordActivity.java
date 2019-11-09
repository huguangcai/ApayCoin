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
import com.ysxsoft.apaycoin.impservice.ImpModifyLoginPasswordService;
import com.ysxsoft.apaycoin.modle.ModifyLoginPwdBean;
import com.ysxsoft.apaycoin.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModifyLoginPasswordActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private Button btn_submit;
    private TextView tv_forget_login_pwd;
    private EditText ed_old_pwd, ed_new_pwd, ed_check_new_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_login_pwd_layout);
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("修改登录密码");
        tv_forget_login_pwd = getViewById(R.id.tv_forget_login_pwd);
        ed_old_pwd = getViewById(R.id.ed_old_pwd);
        ed_new_pwd = getViewById(R.id.ed_new_pwd);
        ed_check_new_pwd = getViewById(R.id.ed_check_new_pwd);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_forget_login_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit:
                checkData();
                submitData();
                break;
            case R.id.tv_forget_login_pwd:
                startActivity(ForgetPassWordActivity.class);
                break;
        }
    }

    private void submitData() {
        NetWork.getRetrofit()
//                .create(ImpModifyLoginPwdService::class.java)
                .create(ImpModifyLoginPasswordService.class)
                .getCall(NetWork.getToken(),
                        ed_old_pwd.getText().toString().trim(), ed_new_pwd.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModifyLoginPwdBean>() {
                    private ModifyLoginPwdBean modifyLoginPwdBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(modifyLoginPwdBean.getMsg());
                        if ("0".equals(modifyLoginPwdBean.getCode())) {
                            startActivity(MyActivity.class);
                            finish();
                        }else if ("2" .equals( modifyLoginPwdBean.getCode())) {
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
                    public void onNext(ModifyLoginPwdBean modifyLoginPwdBean) {

                        this.modifyLoginPwdBean = modifyLoginPwdBean;
                    }
                });


    }

    private void checkData() {
        if (TextUtils.isEmpty(ed_old_pwd.getText().toString().trim())) {
            showToastMessage("旧密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(ed_new_pwd.getText().toString().trim())) {
            showToastMessage("新密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(ed_check_new_pwd.getText().toString().trim())) {
            showToastMessage("确认密码不能为空");
            return;
        }
        if (!ed_new_pwd.getText().toString().trim().equals(ed_check_new_pwd.getText().toString().trim())) {
            showToastMessage("两次输入密码不一致");
            return;
        }
    }
}
