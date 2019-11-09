package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.impservice.ImpLoginService;
import com.ysxsoft.apaycoin.modle.LoginBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.CustomDialog;
import com.ysxsoft.apaycoin.utils.NetWork;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 登录界面
 * 日期： 2018/11/1 0001 11:53
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

    private EditText ed_phone_num, ed_pwd;
    private TextView tv_forget_pwd, tv_register,tv_login;
    private Button btn_login;
    private CustomDialog customDialog;
    private int stateBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        customDialog = new CustomDialog(mContext,"正在登陆...");

        setHalfTransparent();
        setFitSystemWindow(false);
        stateBar = getStateBar();
        initView();
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        tv_forget_pwd.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    /**
     * 初始化view
     */
    private void initView() {
        tv_login = getViewById(R.id.tv_login);
        tv_login.setPadding(0,stateBar,0,0);
        ed_phone_num = getViewById(R.id.ed_phone_num);
        ed_pwd = getViewById(R.id.ed_pwd);
        tv_forget_pwd = getViewById(R.id.tv_forget_pwd);
        btn_login = getViewById(R.id.btn_login);
        tv_register = getViewById(R.id.tv_register);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_pwd:
                startActivity(ForgetPassWordActivity.class);
                break;
            case R.id.btn_login:
                if (TextUtils.isEmpty(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("手机号不能为空");
                    return;
                }
                if (!AppUtil.checkPhoneNum(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(ed_pwd.getText().toString().trim())) {
                    showToastMessage("密码不能为空");
                    return;
                }
                if (ed_pwd.getText().toString().trim().length()<6){
                    showToastMessage("登录密码不能少于六位");
                    return;
                }
                customDialog.show();
                login();
                break;
            case R.id.tv_register:
                startActivity(RegisterActivity.class);
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        NetWork.getRetrofit()
                .create(ImpLoginService.class)
                .getLogin(ed_phone_num.getText().toString(), ed_pwd.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    private LoginBean loginBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(loginBean.getMsg());
                        if ("0".equals(loginBean.getCode())) {
                            customDialog.dismiss();
                            SharedPreferences.Editor spToken = getSharedPreferences("save_token", Context.MODE_PRIVATE).edit();
                            spToken.putString("token",loginBean.getUserinfo().getSign());
                            spToken.commit();

                            SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.putString("username", ed_phone_num.getText().toString().trim());
                            sp.putString("pwd", ed_pwd.getText().toString().trim());
                            sp.commit();
                            BaseApplication.loginBean = loginBean;
                            SharedPreferences.Editor edit = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            edit.putBoolean("is_first", true);
                            edit.commit();
                            Intent intent=new Intent(mContext,MyActivity.class);
                            intent.putExtra("flag",loginBean.getUserinfo().getFlag());
                            intent.putExtra("head",loginBean.getUserinfo().getAvatar());
                            intent.putExtra("credit",loginBean.getUserinfo().getStar());
                            intent.putExtra("uid",loginBean.getUserinfo().getUid());
                            intent.putExtra("ff",loginBean.getUserinfo().getFf());
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        log(e.getMessage());
                        showToastMessage(e.getMessage());
                    }
                    @Override
                    public void onNext(LoginBean loginBean) {
                        this.loginBean = loginBean;

                    }
                });
    }

}
