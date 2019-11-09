package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.impservice.ImpLoginService;
import com.ysxsoft.apaycoin.modle.LoginBean;
import com.ysxsoft.apaycoin.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 启动界面
 * 日期： 2018/10/24 0024 16:34
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class LauncherActivity extends BaseActivity {

    private CountDownTimer countDownTimer;
    private String flag;
    private TextView tv_time;
    private LinearLayout ll_jump;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.launcher_layout);
        tv_time = getViewById(R.id.tv_time);
        ll_jump = getViewById(R.id.ll_jump);

        final SharedPreferences sp = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE);
        final boolean is_first = sp.getBoolean("is_first", false);
        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_time.setText("("+String.valueOf(millisUntilFinished/1000)+"s)");
            }

            @Override
            public void onFinish() {
                if (is_first) {
                    getLoginInfo();
                } else {
                    startActivity(LoginActivity.class);
                    finish();
                }
            }
        }.start();
        ll_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_first) {
                    startActivity(MyActivity.class);
                    finish();
                } else {
                    startActivity(LoginActivity.class);
                    finish();
                }
                countDownTimer.cancel();
            }
        });
    }

    /**
     * 获取登录信息
     */
    private void getLoginInfo() {
        SharedPreferences sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE);
        String username = sp.getString("username", "");
        String pwd = sp.getString("pwd", "");
        if (!"".equals(username) && !"".equals(pwd)) {
            NetWork.getRetrofit().
                    create(ImpLoginService.class).getLogin(username, pwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LoginBean>() {
                        private LoginBean loginBean;

                        @Override
                        public void onCompleted() {
                            if ("0".equals(loginBean.getCode())) {
                                SharedPreferences.Editor spToken = getSharedPreferences("save_token", Context.MODE_PRIVATE).edit();
                                spToken.putString("token",loginBean.getUserinfo().getSign());
                                spToken.commit();

                                flag = loginBean.getUserinfo().getFlag();
                                BaseApplication.loginBean = loginBean;
                                Intent intent = new Intent(mContext, MyActivity.class);
                                intent.putExtra("flag", flag);
                                intent.putExtra("head", loginBean.getUserinfo().getAvatar());
                                intent.putExtra("credit", loginBean.getUserinfo().getStar());
                                intent.putExtra("uid", loginBean.getUserinfo().getUid());
                                startActivity(intent);
                                finish();
                            }else {
                                startActivity(LoginActivity.class);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToastMessage(e.getMessage());
                        }

                        @Override
                        public void onNext(LoginBean loginBean) {
                            this.loginBean = loginBean;
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}
