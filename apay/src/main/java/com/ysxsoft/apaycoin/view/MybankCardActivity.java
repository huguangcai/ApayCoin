package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpGetMyBankecardInfoService;
import com.ysxsoft.apaycoin.modle.GetMyBankecardInfoBean;
import com.ysxsoft.apaycoin.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 我的银行卡界面
 * 日期： 2018/11/6 0006 13:42
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class MybankCardActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_add_bank_card, add_band_card_sucess;
    private TextView tv_title, tv_edit, tv_zhifubao;
    private View img_back;
    private TextView tv_khh;
    private TextView tv_khr;
    private TextView tv_bank_card_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bank_card_layout);
        initView();
        requestData();
        initListener();
    }

    private void requestData() {
        NetWork.getRetrofit()
                .create(ImpGetMyBankecardInfoService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetMyBankecardInfoBean>() {
                    private GetMyBankecardInfoBean getMyBankecardInfoBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(getMyBankecardInfoBean.getMsg());
                        if ("0".equals(getMyBankecardInfoBean.getCode())) {
                            if (null==getMyBankecardInfoBean.getData().getKhr()||"".equals(getMyBankecardInfoBean.getData().getKhr())){
                                rl_add_bank_card.setVisibility(View.VISIBLE);
                                add_band_card_sucess.setVisibility(View.GONE);
                            }else {
                                rl_add_bank_card.setVisibility(View.GONE);
                                add_band_card_sucess.setVisibility(View.VISIBLE);
                                String alipay = getMyBankecardInfoBean.getData().getAlipay();
                                String khh = getMyBankecardInfoBean.getData().getKhh();
                                tv_zhifubao.setText(getMyBankecardInfoBean.getData().getAlipay());
                                tv_khh.setText(getMyBankecardInfoBean.getData().getKhh());
                                tv_khr.setText(getMyBankecardInfoBean.getData().getKhr());
                                tv_bank_card_num.setText(getMyBankecardInfoBean.getData().getCarnum());
//                            tv_zhifubao.setText(getMyBankecardInfoBean.getData().getAlipay());
                            }
                        }else if ("2" == getMyBankecardInfoBean.getCode()) {
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
                    public void onNext(GetMyBankecardInfoBean getMyBankecardInfoBean) {
                        this.getMyBankecardInfoBean = getMyBankecardInfoBean;
                    }
                });
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        rl_add_bank_card.setOnClickListener(this);
    }

    private void initView() {
        rl_add_bank_card = getViewById(R.id.rl_add_bank_card);
        add_band_card_sucess = getViewById(R.id.add_band_card_sucess);
        tv_edit = getViewById(R.id.tv_edit);
        tv_zhifubao = getViewById(R.id.tv_zhifubao);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("我的银行卡");
        img_back = getViewById(R.id.img_back);
        tv_khh = getViewById(R.id.tv_khh);
        tv_khr = getViewById(R.id.tv_khr);
        tv_bank_card_num = getViewById(R.id.tv_bank_card_num);
//        View img_head = getViewById(R.id.img_head);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_edit:
                Intent intent=new Intent(mContext,AddBankCardActivity.class);
                intent.putExtra("khh",tv_khh.getText().toString());
                intent.putExtra("khr",tv_khr.getText().toString());
                intent.putExtra("cardnum",tv_bank_card_num.getText().toString());
                intent.putExtra("zhifubao",tv_zhifubao.getText().toString());
                startActivity(intent);
                break;
            case R.id.rl_add_bank_card:
                startActivity(AddBankCardActivity.class);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }
}
