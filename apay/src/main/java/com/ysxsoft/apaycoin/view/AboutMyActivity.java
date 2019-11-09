package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpAboutMyService;
import com.ysxsoft.apaycoin.modle.AboutMyBean;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.zxing.Contents;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 关于我们界面
 * 日期： 2018/11/6 0006 14:00
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class AboutMyActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_title;
    private WebView web_content;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_myself_layout);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("关于我们");
        web_content = getViewById(R.id.web_content);
        WebSettings webSettings = web_content.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        web_content.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        // 在WebView中而不在默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

    private void initData() {
        Retrofit retrofit = NetWork.getRetrofit();
        ImpAboutMyService service = retrofit.create(ImpAboutMyService.class);
        Observable<AboutMyBean> observable = service.getAboutMy();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AboutMyBean>() {
                    private AboutMyBean aboutMyBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(aboutMyBean.getCode())) {
                            url = aboutMyBean.getData().getContent();
                            web_content.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
                        }else if ("2".equals(aboutMyBean.getCode())){
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
                    public void onNext(AboutMyBean aboutMyBean) {
                        this.aboutMyBean = aboutMyBean;
                    }
                });
    }

    private void initListener() {
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
