package com.ysxsoft.apaycoin.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpCheckShouHuoService;
import com.ysxsoft.apaycoin.impservice.ImpMyOrderDetialService;
import com.ysxsoft.apaycoin.impservice.MerchantFaHuoService;
import com.ysxsoft.apaycoin.modle.CheckShouHuoBean;
import com.ysxsoft.apaycoin.modle.MerchantFaHuoBean;
import com.ysxsoft.apaycoin.modle.MyOrderDetialBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.paypwdpopuwindow.popwindow.SelectPopupWindow;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderDetialActivity extends BaseActivity implements View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {

    private View img_back;
    private TextView tv_title, tv_time, tv_is_fahuo, tv_name, tv_phone_num, tv_address, tv_order_number, tv_shop_name, tv_contact_bussiness, tv_desc, tv_money, tv_num, tv_apay_num, tv_sum_money, tv_num_goods, tv_sum_money_goods, tv_check_order;
    private ImageView img_copy, img_goods;
    private String oid, flag;
    private String isuser;
    private LinearLayout ll_bussiness_gone, ll_is_fahuo;
    private TextView tv_is_user_xiadan, tv_beizhu, tv_wuliu_time, tv_is_user_fahuo, tv_wuliu_name, tv_wuliu_address, tv_wuliu_shuohuo_time, tv_wuliu_complete;
    private ImageView img_wuliu_tupian;
    private String shop_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        Intent intent = getIntent();
        oid = intent.getStringExtra("oid");
        flag = intent.getStringExtra("flag");
        isuser = intent.getStringExtra("user");
        initView();
        requestData();
        initLIstener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        ll_bussiness_gone = getViewById(R.id.ll_bussiness_gone);
        ll_is_fahuo = getViewById(R.id.ll_is_fahuo);
        tv_is_user_xiadan = getViewById(R.id.tv_is_user_xiadan);
        tv_beizhu = getViewById(R.id.tv_beizhu);
        tv_wuliu_time = getViewById(R.id.tv_wuliu_time);
        tv_is_user_fahuo = getViewById(R.id.tv_is_user_fahuo);
        tv_wuliu_name = getViewById(R.id.tv_wuliu_name);
        tv_wuliu_address = getViewById(R.id.tv_wuliu_address);
        tv_wuliu_shuohuo_time = getViewById(R.id.tv_wuliu_shuohuo_time);
        tv_wuliu_complete = getViewById(R.id.tv_wuliu_complete);
        img_wuliu_tupian = getViewById(R.id.img_wuliu_tupian);
        tv_check_order = getViewById(R.id.tv_check_order);
        tv_contact_bussiness = getViewById(R.id.tv_contact_bussiness);
        if ("user".equals(isuser)) {
            tv_title.setText("订单详情");
            tv_check_order.setText("确认收货");
            tv_wuliu_complete.setText("等待卖家确认发货");
            tv_contact_bussiness.setText("联系卖家");
        } else {
            tv_title.setText("商家订单");
            tv_check_order.setText("确认发货");
            tv_wuliu_complete.setText("等待买家确认收货");
            tv_contact_bussiness.setText("联系买家");
        }
        tv_time = getViewById(R.id.tv_time);
        tv_is_fahuo = getViewById(R.id.tv_is_fahuo);
        tv_name = getViewById(R.id.tv_name);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        tv_address = getViewById(R.id.tv_address);
        tv_order_number = getViewById(R.id.tv_order_number);
        img_copy = getViewById(R.id.img_copy);
        tv_shop_name = getViewById(R.id.tv_shop_name);
        img_goods = getViewById(R.id.img_goods);
        tv_desc = getViewById(R.id.tv_desc);
        tv_money = getViewById(R.id.tv_money);
        tv_num = getViewById(R.id.tv_num);
        tv_apay_num = getViewById(R.id.tv_apay_num);
        tv_sum_money = getViewById(R.id.tv_sum_money);
        tv_num_goods = getViewById(R.id.tv_num_goods);
        tv_sum_money_goods = getViewById(R.id.tv_sum_money_goods);

    }

    private void requestData() {
        NetWork.getRetrofit()
                .create(ImpMyOrderDetialService.class)
                .getCall(NetWork.getToken(), oid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyOrderDetialBean>() {
                    private MyOrderDetialBean myOrderDetialBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(myOrderDetialBean.getMsg());
                        if ("0".equals(myOrderDetialBean.getCode())) {
                            shop_phone = myOrderDetialBean.getData().getShop_phone();
                            String flag = myOrderDetialBean.getData().getFlag();
                            if ("0".equals(flag)) {
                                tv_is_fahuo.setText("待发货");
                                tv_time.setText(myOrderDetialBean.getData().getAdd_times());//购买时间
                                ll_is_fahuo.setVisibility(View.GONE);
                                tv_wuliu_time.setText(myOrderDetialBean.getData().getAdd_times());
                                if ("user".equals(isuser)) {
                                    ll_bussiness_gone.setVisibility(View.GONE);
                                } else {
                                    ll_bussiness_gone.setVisibility(View.VISIBLE);
                                }
                            } else if ("1".equals(flag)) {
                                tv_is_fahuo.setText("已发货");
                                tv_time.setText(myOrderDetialBean.getData().getFh_time());
                                ll_is_fahuo.setVisibility(View.VISIBLE);
                                tv_is_user_fahuo.setText("已发货");
                                tv_wuliu_name.setText(myOrderDetialBean.getData().getLinkname());
                                tv_wuliu_address.setText(myOrderDetialBean.getData().getAddress());
                                tv_wuliu_shuohuo_time.setText(myOrderDetialBean.getData().getFh_time());
                                if ("user".equals(isuser)) {
                                    ll_bussiness_gone.setVisibility(View.VISIBLE);
                                } else {
                                    ll_bussiness_gone.setVisibility(View.GONE);
                                }
                            } else if ("2".equals(flag)) {
                                tv_is_fahuo.setText("已完成");
                                tv_time.setText(myOrderDetialBean.getData().getQr_time());
                                tv_wuliu_complete.setText("已完成");
                                tv_wuliu_complete.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                                img_wuliu_tupian.setImageResource(R.mipmap.img_order_follow_select_point);
                                ll_bussiness_gone.setVisibility(View.GONE);
                            }
                            tv_name.setText(myOrderDetialBean.getData().getLinkname());
                            tv_phone_num.setText(myOrderDetialBean.getData().getPhone());
                            tv_address.setText(myOrderDetialBean.getData().getAddress());
                            tv_order_number.setText(myOrderDetialBean.getData().getOrder_sn());
                            tv_shop_name.setText(myOrderDetialBean.getData().getShopname());
                            ImageLoadUtil.GlideImageLoad(mContext, myOrderDetialBean.getData().getIcon(), img_goods);
                            tv_desc.setText(myOrderDetialBean.getData().getTitle());
                            tv_money.setText(myOrderDetialBean.getData().getPrice());
                            tv_apay_num.setText(myOrderDetialBean.getData().getNum());
                            tv_num.setText(myOrderDetialBean.getData().getNum());
                            tv_num_goods.setText(myOrderDetialBean.getData().getNum());
                            tv_sum_money.setText(myOrderDetialBean.getData().getMoney());
                            tv_sum_money_goods.setText(myOrderDetialBean.getData().getMoney());
                        }else if ("2" .equals( myOrderDetialBean.getCode())) {
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
                        if ("user".equals(isuser)) {
                            tv_beizhu.setText(myOrderDetialBean.getData().getC2());
                        } else {
                            tv_beizhu.setText(myOrderDetialBean.getData().getC1());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(MyOrderDetialBean myOrderDetialBean) {
                        this.myOrderDetialBean = myOrderDetialBean;
                    }
                });
    }

    private void initLIstener() {
        img_back.setOnClickListener(this);
        img_copy.setOnClickListener(this);
        tv_contact_bussiness.setOnClickListener(this);
        tv_check_order.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_copy:
                ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tv_order_number.getText().toString());//TODO 将要复制的下载地址放入settext函数中
                showToastMessage("复制完成");
                break;
            case R.id.tv_contact_bussiness:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data ;
                if ("user".equals(isuser)) {
                    data = Uri.parse("tel:" + shop_phone);
                }else {
                    data = Uri.parse("tel:" + tv_phone_num.getText().toString());
                }
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.tv_check_order:
                if ("user".equals(isuser)) {
                    showPayDialog();
                } else {
                    checkFahuoData();
                }
                break;
        }
    }

    private void showPayDialog() {
        SelectPopupWindow menuWindow = new SelectPopupWindow(this, this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            submitData(psw);
        }
    }

    /**
     * 确认发货
     */
    private void checkFahuoData() {
        NetWork.getRetrofit()
                .create(MerchantFaHuoService.class)
                .getCall(NetWork.getToken(), oid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantFaHuoBean>() {
                    private MerchantFaHuoBean merchantFaHuoBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(merchantFaHuoBean.getMsg());
                        if ("0".equals(merchantFaHuoBean.getCode())) {
                            Intent intent = new Intent(mContext, MerchantOrderActivity.class);
                            intent.putExtra("checkfahuo", "checkfahuo");
                            startActivity(intent);
                        }else if ("2" .equals( merchantFaHuoBean.getCode())) {
                            SharedPreferences.Editor sp =getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first =getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
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
                    public void onNext(MerchantFaHuoBean merchantFaHuoBean) {

                        this.merchantFaHuoBean = merchantFaHuoBean;
                    }
                });
    }

    /**
     * 确认收货
     *
     * @param psw
     */
    private void submitData(String psw) {
        NetWork.getRetrofit()
                .create(ImpCheckShouHuoService.class)
                .getCall(NetWork.getToken(), oid, psw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckShouHuoBean>() {
                    private CheckShouHuoBean checkShouHuoBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(checkShouHuoBean.getMsg());
                        if ("0".equals(checkShouHuoBean.getCode())) {
                            Intent intent = new Intent(mContext, MyOrderActivity.class);
                            intent.putExtra("check", "check");
                            startActivity(intent);
                        }else if ("2".equals(checkShouHuoBean.getCode())) {
                            SharedPreferences.Editor sp =getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first =getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
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
                    public void onNext(CheckShouHuoBean checkShouHuoBean) {
                        this.checkShouHuoBean = checkShouHuoBean;
                    }
                });
    }

    @Override
    public void onForgetPayPwd() {
        startActivity(ForgetPayPwdActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }
}
