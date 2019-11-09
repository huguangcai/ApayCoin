package com.ysxsoft.apaycoin.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.impservice.ImpNumAssetApayService;
import com.ysxsoft.apaycoin.modle.NumAssetApayBean;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 余额购买和出售的dialog
 * 日期： 2018/11/16 0016 17:11
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BalanceMoneyBuyOrSaleDialog extends ABSDialog {

    private TextView tv_current_price;

    public BalanceMoneyBuyOrSaleDialog(@NonNull Context context) {
        super(context);
        initdata();
    }

    private void initdata() {

        Retrofit retrofit = NetWork.getRetrofit();
        retrofit.create(ImpNumAssetApayService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NumAssetApayBean>() {
                    private NumAssetApayBean numAssetApayBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(numAssetApayBean.getCode())) {
                            tv_current_price.setText(numAssetApayBean.getData().getJiaoyi());
                        }else if ("2" .equals( numAssetApayBean.getCode())) {
                            SharedPreferences.Editor sp =getContext().getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = getContext().getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            getContext().startActivity(new Intent(getContext(),LoginActivity.class));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(NumAssetApayBean numAssetApayBean) {

                        this.numAssetApayBean = numAssetApayBean;
                    }
                });
    }

    @Override
    protected void initView() {
        tv_current_price= findViewById(R.id.tv_current_price);
        PayPwdEditText payPwdEditText = (PayPwdEditText) findViewById(R.id.ed_ppet);
        payPwdEditText.initStyle(R.drawable.edit_num_bg_red, 6, 0.33f, R.color.black, R.color.black, 20);
        payPwdEditText.setFocus();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.balance_moneybuy_sale_dialog_layout;
    }
}
