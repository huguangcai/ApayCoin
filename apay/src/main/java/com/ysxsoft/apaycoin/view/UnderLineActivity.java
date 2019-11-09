package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpMyMallHeaderInfoService;
import com.ysxsoft.apaycoin.modle.MyMallHeaderInfoBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UnderLineActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_title, tv_phone_num, tv_shop_name;
    private ImageView img_head;
    private LinearLayout ll_credit_star,ll_credit,ll_no_credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_my_shop_layout);
        initView();
        requestData();
    }
    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("线下商家");
        img_head = getViewById(R.id.img_head);
        tv_shop_name = getViewById(R.id.tv_shop_name);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        ll_credit_star = getViewById(R.id.ll_credit_star);
        ll_credit = getViewById(R.id.ll_credit);
        ll_no_credit = getViewById(R.id.ll_no_credit);
        img_back.setOnClickListener(this);
    }
    private void requestData() {
        NetWork.getRetrofit()
                .create(ImpMyMallHeaderInfoService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyMallHeaderInfoBean>() {
                    private MyMallHeaderInfoBean myMallHeaderInfoBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(myMallHeaderInfoBean.getMsg());
                        if ("0".equals(myMallHeaderInfoBean.getCode())){
                            ImageLoadUtil.GlideImageLoad(mContext,myMallHeaderInfoBean.getData().getIcon(),img_head);
                            tv_shop_name.setText(myMallHeaderInfoBean.getData().getName());
                            tv_phone_num.setText(myMallHeaderInfoBean.getData().getPhone());
                            for (int i = 0; i < Integer.valueOf(myMallHeaderInfoBean.getData().getStar()); i++) {
                                ImageView imageView=new ImageView(mContext);
                                imageView.setImageResource(R.mipmap.img_credit_star);
                                ll_credit.addView(imageView);
                            }
                            for (int i = 0; i < 5 - Integer.valueOf(myMallHeaderInfoBean.getData().getStar()); i++) {
                                ImageView imageView = new ImageView(mContext);
                                imageView.setBackgroundResource(R.mipmap.img_gray_star);
                                ll_no_credit.addView(imageView);
                            }
                        }else if ("2" .equals( myMallHeaderInfoBean.getCode())) {
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
                    public void onNext(MyMallHeaderInfoBean myMallHeaderInfoBean) {
                        this.myMallHeaderInfoBean = myMallHeaderInfoBean;
                    }
                });
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
