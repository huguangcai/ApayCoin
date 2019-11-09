package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpBusinessInstructionsService;
import com.ysxsoft.apaycoin.modle.BusinessInstructionsBean;
import com.ysxsoft.apaycoin.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * 商家须知界面
 */
public class BusinessInstructionsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private View img_back;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_instructions_layout);
        intView();
        requestData();
        initListener();
    }

    private void requestData() {
        NetWork.getRetrofit()
                .create(ImpBusinessInstructionsService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BusinessInstructionsBean>() {
                    private BusinessInstructionsBean businessInstructionsBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(businessInstructionsBean.getCode())) {
                            String data = businessInstructionsBean.getData();
                            webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
                        }else if ("2".equals(businessInstructionsBean.getCode())) {
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
                    public void onNext(BusinessInstructionsBean businessInstructionsBean) {
                        this.businessInstructionsBean = businessInstructionsBean;
                    }
                });

    }

    private void intView() {
        img_back = findViewById(R.id.img_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("商家须知");
        webView = findViewById(R.id.wb_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new MyWebViewClient());

    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        // 在WebView中而不在默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

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
