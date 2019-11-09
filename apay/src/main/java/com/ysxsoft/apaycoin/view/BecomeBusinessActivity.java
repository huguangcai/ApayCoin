package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.impservice.ImpBecomeBusinessService;
import com.ysxsoft.apaycoin.impservice.ImpGoodsTypeService;
import com.ysxsoft.apaycoin.impservice.ImpNumAssetApayService;
import com.ysxsoft.apaycoin.modle.BecomeBusinessBean;
import com.ysxsoft.apaycoin.modle.GoodsTypeBean;
import com.ysxsoft.apaycoin.modle.NumAssetApayBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.BecomeBusinessDialog;
import com.ysxsoft.apaycoin.widget.paypwdpopuwindow.popwindow.SelectPopupWindow;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 成为商家界面
 * 日期： 2018/11/6 0006 13:33
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BecomeBusinessActivity extends BaseActivity implements View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {

    private View img_back;
    private TextView tv_title, tv_title_right, tv_pay_money, tv_mall_type;
    private RadioGroup rg_online_underline;
    private RadioButton rb_online, rb_under_line;
    private EditText ed_mall_name, ed_contact_way;
    private ImageView img_down_arrow;
    private Button btn_immediately_buy;
    private ArrayList<GoodsTypeBean.DataBean> data;
    ArrayList<String> options1Items = new ArrayList<>();
    private String id;
    private int line = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.become_business_layout);
//        if (isShowDialog()) {
//            showDialogTip();
//        }
        initView();
        getData();
        getGoodsTypeData();
        initListener();
    }

    private void getData() {
        NetWork.getRetrofit().create(ImpNumAssetApayService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NumAssetApayBean>() {
                    private NumAssetApayBean numAssetApayBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(numAssetApayBean.getCode())) {
                            tv_pay_money.setText(numAssetApayBean.getData().getShops());
                        }else if ("2" .equals( numAssetApayBean.getCode())) {
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
                    public void onNext(NumAssetApayBean numAssetApayBean) {

                        this.numAssetApayBean = numAssetApayBean;
                    }
                });
    }

    /**
     * 获取商品分类
     */
    private void getGoodsTypeData() {
        NetWork.getRetrofit()
                .create(ImpGoodsTypeService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodsTypeBean>() {
                    private GoodsTypeBean goodsTypeBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(goodsTypeBean.getMsg());
                        if ("0".equals(goodsTypeBean.getCode())) {
                            data = goodsTypeBean.getData();
                            for (int i = 0; i < data.size(); i++) {
                                options1Items.add(data.get(i).getTitle());
                            }
                        }else if ("2" == goodsTypeBean.getCode()) {
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
                    public void onNext(GoodsTypeBean goodsTypeBean) {
                        this.goodsTypeBean = goodsTypeBean;
                    }
                });

    }

    /**
     * 弹出提示框
     */
    private void showDialogTip() {
        BecomeBusinessDialog dialog = new BecomeBusinessDialog(mContext);
        dialog.show();
    }

    /**
     * 是否弹出提示框
     */
    private boolean isShowDialog() {
        if (Math.floor(Double.valueOf(BaseApplication.loginBean.getUserinfo().getQuans())) <= 200000) {
            return true;
        }
        return false;
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("申请成为商家");
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("商家须知");
        tv_pay_money = getViewById(R.id.tv_pay_money);
        rg_online_underline = getViewById(R.id.rg_online_underline);
        rb_online = getViewById(R.id.rb_online);
        rb_under_line = getViewById(R.id.rb_under_line);
        tv_mall_type = getViewById(R.id.tv_mall_type);
        img_down_arrow = getViewById(R.id.img_down_arrow);
        ed_mall_name = getViewById(R.id.ed_mall_name);
        ed_contact_way = getViewById(R.id.ed_contact_way);
        btn_immediately_buy = getViewById(R.id.btn_immediately_buy);

        rg_online_underline.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_online:
                        line = 1;
                        break;
                    case R.id.rb_under_line:
                        line = 2;
                        break;
                }
            }
        });
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        img_down_arrow.setOnClickListener(this);
        btn_immediately_buy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_title_right:
                startActivity(BusinessInstructionsActivity.class);
                break;
            case R.id.img_down_arrow:
                AppUtil.colsePhoneKeyboard(this);
                showGoodsType();
                break;
            case R.id.btn_immediately_buy:
//                if (isShowDialog()) {
//                    showDialogTip();
//                    return;
//                }
                if (TextUtils.isEmpty(ed_mall_name.getText().toString().trim())) {
                    showToastMessage("店铺名称不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_contact_way.getText().toString().trim())) {
                    showToastMessage("联系方式不能为空");
                    return;
                }
                if (!AppUtil.checkPhoneNum(ed_contact_way.getText().toString().trim())) {
                    showToastMessage("输入正确的联系方式");
                    return;
                }
                if (TextUtils.isEmpty(tv_mall_type.getText().toString())) {
                    showToastMessage("店铺分类不能为空");
                    return;
                }
                showPayPopupwindow();
                break;
        }
    }

    /**
     * 展示商品分类的弹窗
     */
    private void showGoodsType() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = data.get(options1).getTitle();
                id = data.get(options1).getId();
                tv_mall_type.setText(tx);
            }
        })
                .setTitleText("选择商品")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items);//三级选择器
        pvOptions.show();
    }

    /**
     * 展示输入密码框
     */
    private void showPayPopupwindow() {
        SelectPopupWindow menuWindow = new SelectPopupWindow(this, this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            NetWork.getRetrofit()
                    .create(ImpBecomeBusinessService.class)
                    .getCall(NetWork.getToken(), ed_mall_name.getText().toString().trim(),
                            line + "",
                            ed_contact_way.getText().toString().trim(),
                            id, psw)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BecomeBusinessBean>() {
                        private BecomeBusinessBean becomeBusinessBean;

                        @Override
                        public void onCompleted() {
                            showToastMessage(becomeBusinessBean.getMsg());
                            if ("1".equals(becomeBusinessBean.getCode())) {
                                finish();
                            }else if ("2".equals(becomeBusinessBean.getCode())) {
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
                        public void onNext(BecomeBusinessBean becomeBusinessBean) {
                            this.becomeBusinessBean = becomeBusinessBean;
                        }
                    });
        }
    }

    @Override
    public void onForgetPayPwd() {
        startActivity(ForgetPayPwdActivity.class);
    }
}
