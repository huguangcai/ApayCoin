package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpIssueApayService;
import com.ysxsoft.apaycoin.impservice.ImpNumAssetApayService;
import com.ysxsoft.apaycoin.modle.IssueApayBean;
import com.ysxsoft.apaycoin.modle.NumAssetApayBean;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.IssueApayDialog;
import com.ysxsoft.apaycoin.widget.OnTabSelectListener;
import com.ysxsoft.apaycoin.widget.SegmentTabLayout;
import com.ysxsoft.apaycoin.widget.ViewFindUtils;
import com.ysxsoft.apaycoin.widget.paypwdpopuwindow.popwindow.SelectPopupWindow;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 发布Apay币界面
 * 日期： 2018/11/15 0015 17:39
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class IssueApayActivity extends BaseActivity implements View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {

    private View img_back;
    private TextView tv_title, tv_num_asset, tv_balance_money, tv_sale_buy_price, tv_sale_buy_num, tv_sale_buy_sum_money, tv_way, tv_current_price, tv_sale_sum_money;
    private SegmentTabLayout stl_tab;
    private FrameLayout fl_change_content;
    private String[] mTitles_2 = {"出售", "购买"};
    private View mDecorView;
    private SegmentTabLayout stl_tab1;
    private RadioGroup rg_home;
    private RadioButton rb_one, rb_two, rb_three;
    private EditText ed_sale_num, ed_sale_price;
    private Button btn_issuance;
    private int type = 3;
    private int zf_type = 0;
    private int issale_buy_order = 0;
    ArrayList<BigDecimal> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecorView = getWindow().getDecorView();
        setContentView(R.layout.issuance_layout);
        initView();
        requestData();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("Apay币");
        tv_num_asset = getViewById(R.id.tv_num_asset);
        tv_balance_money = getViewById(R.id.tv_balance_money);
        tv_sale_buy_price = getViewById(R.id.tv_sale_buy_price);
        tv_current_price = getViewById(R.id.tv_current_price);
        tv_sale_buy_num = getViewById(R.id.tv_sale_buy_num);
        tv_sale_buy_sum_money = getViewById(R.id.tv_sale_buy_sum_money);
        ed_sale_num = getViewById(R.id.ed_sale_num);
        ed_sale_price = getViewById(R.id.ed_sale_price);
        tv_way = getViewById(R.id.tv_way);
        tv_sale_sum_money = getViewById(R.id.tv_sale_sum_money);
        rg_home = getViewById(R.id.rg_home);
        rb_one = getViewById(R.id.rb_one);
        rb_two = getViewById(R.id.rb_two);
        rb_three = getViewById(R.id.rb_three);
        stl_tab = getViewById(R.id.stl_tab);
        btn_issuance = getViewById(R.id.btn_issuance);
        fl_change_content = getViewById(R.id.fl_change_content);
        stl_tab1 = ViewFindUtils.find(mDecorView, R.id.stl_tab);
        ed_sale_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(ed_sale_price.getText().toString().trim())){
                    showToastMessage("价格不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_sale_num.getText().toString().trim())) {
                    tv_sale_sum_money.setText("");
                    return;
                }
                BigDecimal bigprice = new BigDecimal(ed_sale_price.getText().toString().trim());
                BigDecimal bignum = new BigDecimal(ed_sale_num.getText().toString().trim());
                BigDecimal multiply = bigprice.multiply(bignum);
//                double money = Double.valueOf(ed_sale_price.getText().toString().trim()) * Double.valueOf(ed_sale_num.getText().toString().trim());
                tv_sale_sum_money.setText(String.valueOf(multiply));
            }
        });
        ed_sale_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (TextUtils.isEmpty(ed_sale_price.getText().toString().trim())){
//                    return;
//                }
                if (TextUtils.isEmpty(ed_sale_num.getText().toString().trim())) {
                    return;
                }
                double money = Double.valueOf(ed_sale_price.getText().toString().trim()) * Double.valueOf(ed_sale_num.getText().toString().trim());
                tv_sale_sum_money.setText(String.valueOf(money));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        stl_tab1.setTabData(mTitles_2);
        stl_tab1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 1) {
                    type = 2;
                    issale_buy_order = 1;
                    rb_one.setVisibility(View.GONE);
                    rb_two.setChecked(true);
                    tv_sale_buy_price.setText("购买价格");
                    tv_sale_buy_num.setText("购买数量");
                    ed_sale_num.setHint("请输入购买数量");
                    tv_sale_buy_sum_money.setText("支付金额");
                    tv_way.setText("支付方式");
                } else {
                    type = 3;
                    issale_buy_order = 0;
                    rb_one.setChecked(true);
                    rb_one.setVisibility(View.VISIBLE);
                    rb_two.setChecked(false);
                    tv_sale_buy_price.setText("出售价格");
                    tv_sale_buy_num.setText("出售数量");
                    ed_sale_num.setHint("请输入出售数量");
                    tv_sale_buy_sum_money.setText("出售总额");
                    tv_way.setText("收款方式");
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        rg_home.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_one:
                        zf_type = 0;
                        break;
                    case R.id.rb_two:
                        zf_type = 1;
                        break;
                    case R.id.rb_three:
                        zf_type = 2;
                        break;
                }
            }
        });
    }

    private void requestData() {
        NetWork.getRetrofit().create(ImpNumAssetApayService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NumAssetApayBean>() {
                    private NumAssetApayBean numAssetApayBean;

                    @Override
                    public void onCompleted() {
                        tv_num_asset.setText(numAssetApayBean.getData().getGold());
                        tv_balance_money.setText(numAssetApayBean.getData().getMoney());
                        tv_current_price.setText(numAssetApayBean.getData().getJiaoyi());
                        String jiaoyi = numAssetApayBean.getData().getJiaoyi();
                        BigDecimal price = new BigDecimal(jiaoyi);
                        BigDecimal bigDecimal = new BigDecimal(jiaoyi);
                        BigDecimal bigDecimalsub = new BigDecimal(jiaoyi);
                        for (int i = 0; i < 9; i++) {
                            BigDecimal add = bigDecimal.add(new BigDecimal("0.01"));
                            bigDecimal = add;
                            int adddata = bigDecimal.intValue();
                            list.add(bigDecimal);
                        }
                        for (int i = 0; i < 9; i++) {
                            BigDecimal add = bigDecimalsub.subtract(new BigDecimal("0.01"));
                            bigDecimalsub = add;
                            int subdata = bigDecimalsub.intValue();
                            list.add(bigDecimalsub);
                        }
                        list.add(price);
                        int size = list.size();
                        Collections.sort(list);//对集合进行排序  从小到大
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println("价格===》》" + list.get(i));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(NumAssetApayBean numAssetApayBean) {

                        this.numAssetApayBean = numAssetApayBean;
                    }
                });
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        btn_issuance.setOnClickListener(this);
        ed_sale_price.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ed_sale_price:
                showPriceDialogData();
                break;
            case R.id.btn_issuance:
                if (TextUtils.isEmpty(ed_sale_price.getText().toString())) {
                    showToastMessage("请选择价格");
                    return;
                }
                if (TextUtils.isEmpty(ed_sale_num.getText().toString().trim())) {
                    showToastMessage("数量不能为空");
                    return;
                }
                if ("0".equals(ed_sale_num.getText().toString().trim())){
                    showToastMessage("数量不能为0");
                    return;
                }
                showPayPopupwindow();
                break;
        }
    }

    private void showPriceDialogData() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = list.get(options1).toString();
                ed_sale_price.setText(tx);
            }
        })
                .setTitleText("")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(list);//三级选择器
        pvOptions.show();
    }

    private void showPayPopupwindow() {
        SelectPopupWindow menuWindow = new SelectPopupWindow(this, this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    private void sugbmitData(String psw) {
        Retrofit retrofit = NetWork.getRetrofit();
        retrofit.create(ImpIssueApayService.class)
                .getCall(NetWork.getToken(), type + "", zf_type + "", ed_sale_num.getText().toString().trim(), ed_sale_price.getText().toString().trim(), psw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IssueApayBean>() {
                    private IssueApayBean issueApayBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(issueApayBean.getMsg());
                        String code = issueApayBean.getCode();
                        if ("0".equals(issueApayBean.getCode())) {
                            if (issale_buy_order == 0) {
                                startActivity(SaleOrderActivity.class);
                            } else {
                                startActivity(BuyOrderActivity.class);
                            }
                        }else if ("3".equals(issueApayBean.getCode())){
                            IssueApayDialog dialog=new IssueApayDialog(mContext);
                            dialog.show();
                        }else if ("2" == issueApayBean.getCode()) {
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
                    public void onNext(IssueApayBean issueApayBean) {
                        this.issueApayBean = issueApayBean;
                    }
                });
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            sugbmitData(psw);
        }
    }

    @Override
    public void onForgetPayPwd() {
        startActivity(ForgetPayPwdActivity.class);
    }
}
