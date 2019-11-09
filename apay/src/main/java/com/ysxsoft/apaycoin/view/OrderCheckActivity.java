package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.banner.GlideImageLoader;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpGetNormalAddressService;
import com.ysxsoft.apaycoin.impservice.ImpMallGoodsDetialHeaderService;
import com.ysxsoft.apaycoin.impservice.ImpSubmitOrderService;
import com.ysxsoft.apaycoin.modle.MallGoodsDetialHeaderBean;
import com.ysxsoft.apaycoin.modle.SubmitOrderBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.CustomDialog;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.paypwdpopuwindow.popwindow.SelectPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderCheckActivity extends BaseActivity implements View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {

    private String pid, uid, money, stock;
    private View img_back;
    private TextView tv_title, tv_name, tv_phone_num, tv_address, tv_desc, tv_money, tv_sum,
            tv_radius, tv_sum_money, tv_add, tv_sum_goods, tv_sum_money_goods, tv_num;
    private ImageView img_tupian;
    private EditText ed_beizhu;
    private Button btn_submit_order;
    private Integer aid;
    private String linkname;
    private CustomDialog customDialog;
    private LinearLayout img_address_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_check_layout);
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        uid = intent.getStringExtra("uid");
        money = intent.getStringExtra("money");
        stock = intent.getStringExtra("stock");
        customDialog = new CustomDialog(mContext,"正在提交订单...");
        initView();
        initListener();
        ggetAddress();
        getGoodsData();
    }

    /**
     * 获取商品信息
     */
    private void getGoodsData() {
        NetWork.getRetrofit()
                .create(ImpMallGoodsDetialHeaderService.class)
                .getCall(NetWork.getToken(), pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallGoodsDetialHeaderBean>() {
                    private MallGoodsDetialHeaderBean mallGoodsDetialHeaderBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(mallGoodsDetialHeaderBean.getMsg());
                        if ("0".equals(mallGoodsDetialHeaderBean.getCode())) {
                            ImageLoadUtil.GlideImageLoad(mContext,mallGoodsDetialHeaderBean.getData().getPro_icon(),img_tupian);
                            tv_desc.setText(AppUtil.stringReplace(mallGoodsDetialHeaderBean.getData().getTitle()));
                            tv_money.setText(mallGoodsDetialHeaderBean.getData().getMoney());
                            tv_sum_money.setText(mallGoodsDetialHeaderBean.getData().getMoney());
                            tv_sum_money_goods.setText(mallGoodsDetialHeaderBean.getData().getMoney());
                        }else if ("2" == mallGoodsDetialHeaderBean.getCode()) {
                            SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager   instance = ActivityPageManager.getInstance();
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
                    public void onNext(MallGoodsDetialHeaderBean mallGoodsDetialHeaderBean) {
                        this.mallGoodsDetialHeaderBean = mallGoodsDetialHeaderBean;
                    }
                });

    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("订单确认");
        tv_name = getViewById(R.id.tv_name);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        tv_address = getViewById(R.id.tv_address);
        img_tupian = getViewById(R.id.img_tupian);
        img_address_manager = getViewById(R.id.img_address_manager);
        tv_desc = getViewById(R.id.tv_desc);
        tv_money = getViewById(R.id.tv_money);
        tv_sum = getViewById(R.id.tv_sum);
        tv_num = getViewById(R.id.tv_num);
        tv_sum_money = getViewById(R.id.tv_sum_money);
        tv_add = getViewById(R.id.tv_add);
        tv_radius = getViewById(R.id.tv_radius);
        ed_beizhu = getViewById(R.id.ed_beizhu);
        btn_submit_order = getViewById(R.id.btn_submit_order);
        tv_sum_goods = getViewById(R.id.tv_sum_goods);
        tv_sum_money_goods = getViewById(R.id.tv_sum_money_goods);
        tv_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_sum.setText(tv_num.getText().toString());
                tv_sum_goods.setText(tv_num.getText().toString());
                tv_sum_money_goods.setText(String.valueOf(Float.valueOf(tv_money.getText().toString()) * Float.valueOf(tv_num.getText().toString())));
                tv_sum_money.setText(tv_sum_money_goods.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_radius.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        btn_submit_order.setOnClickListener(this);
        img_address_manager.setOnClickListener(this);
    }

    /**
     * 获取默认地址
     */
    private void ggetAddress() {
        NetWork.getRetrofit1()
                .create(ImpGetNormalAddressService.class)
                .getCall(NetWork.getToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String jsonStr;//把原始数据转为字符串
                        try {
                            jsonStr = new String(response.body().bytes());
                            JSONObject jsonObject = new JSONObject(jsonStr);
                            String code = (String) jsonObject.get("code");
                            if ("0".equals(code)) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                String provice = (String) data.get("provice");
                                String city = (String) data.get("city");
                                String area = (String) data.get("area");
                                String address = (String) data.get("address");
                                linkname = (String) data.get("linkname");
                                Long phone = (Long) data.get("phone");
                                aid = (Integer) data.get("aid");
                                tv_phone_num.setText(String.valueOf(phone));
                                tv_name.setText(linkname);
                                tv_address.setText(address);
                            }else if ("2".equals(code)) {
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_radius:
                //做判断的目的是不让值小于1
                if (tv_num.getText().toString().equals("1")) {
                    tv_num.setText(1 + "");
                } else {
                    tv_num.setText(String.valueOf(Integer.valueOf(tv_num.getText().toString()) - 1));
                }
                break;
            case R.id.tv_add:
                tv_num.setText(String.valueOf(Integer.valueOf(tv_num.getText().toString()) + 1));
                break;
            case R.id.img_address_manager:
                startActivity(AddressManagerActivity.class);
                break;
            case R.id.btn_submit_order:
                if (TextUtils.isEmpty(linkname)||linkname==null){
                    showToastMessage("请填写地址");
                    return;
                }
                showPayDialog();
                break;
        }
    }

    /**
     * 弹出支付密码框
     */
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
            customDialog.show();
            submitData(psw);
        }
    }

    private void submitData(String psw) {
        NetWork.getRetrofit()
                .create(ImpSubmitOrderService.class)
                .getCall(NetWork.getToken(), pid, psw,
                        tv_num.getText().toString(),
                        String.valueOf(aid),
                        ed_beizhu.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SubmitOrderBean>() {
                    private SubmitOrderBean submitOrderBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(submitOrderBean.getMsg());
                        if ("0".equals(submitOrderBean.getCode())){
                            customDialog.dismiss();
                            startActivity(MyOrderActivity.class);
                        }else if ("2" .equals( submitOrderBean.getCode())) {
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
                    public void onNext(SubmitOrderBean submitOrderBean) {
                        this.submitOrderBean = submitOrderBean;
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
        ggetAddress();
    }
}
