package com.ysxsoft.apaycoin.com;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.ysxsoft.apaycoin.impservice.ImpLoginService;
import com.ysxsoft.apaycoin.modle.LoginBean;
import com.ysxsoft.apaycoin.utils.NetWork;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 描述： application的基类
 * 日期： 2018/10/23 0023 11:17
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BaseApplication extends Application {

    public static LoginBean loginBean=new LoginBean();
    private static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        getLoginInfo();
    }
    public static Context getInstance() {
        return sInstance;
    }
    private void getLoginInfo() {
        SharedPreferences sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE);
        String username = sp.getString("username", "");
        String pwd = sp.getString("pwd", "");
        if (!"".equals(username) && !"".equals(pwd)) {
            Retrofit retrofit = NetWork.getRetrofit();
            ImpLoginService service = retrofit.create(ImpLoginService.class);
            Observable<LoginBean> observable = service.getLogin(username, pwd);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LoginBean>() {
                        private LoginBean loginBean;

                        @Override
                        public void onCompleted() {
                            BaseApplication.loginBean = loginBean;

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(LoginBean loginBean) {

                            this.loginBean = loginBean;
                        }
                    });
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
