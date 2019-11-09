package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.fragment.BalanceMoneyBuyFragment;
import com.ysxsoft.apaycoin.fragment.BalanceMoneySaleFragment;
import com.ysxsoft.apaycoin.fragment.DayLineFragment;
import com.ysxsoft.apaycoin.fragment.FiveHourFragment;
import com.ysxsoft.apaycoin.fragment.FiveMinuteFragment;
import com.ysxsoft.apaycoin.impservice.ImpBrokenLineSumService;
import com.ysxsoft.apaycoin.impservice.ImpNumAssetApayService;
import com.ysxsoft.apaycoin.modle.NumAssetApayBean;
import com.ysxsoft.apaycoin.utils.DateUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.OnTabSelectListener;
import com.ysxsoft.apaycoin.widget.PageSlidingTableView;
import com.ysxsoft.apaycoin.widget.SegmentTabLayout;
import com.ysxsoft.apaycoin.widget.ViewFindUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： Apay币界面
 * 日期： 2018/11/15 0015 17:02
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NumAssetApayActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_title, tv_num_asset, tv_balance_money, tv_current_price, tv_hight, tv_low;
    private LinearLayout ll_issue, ll_sale_order, ll_buy_order, ll_trade_record;
    private SegmentTabLayout stl_tab;
    private FrameLayout fl_change_content;
    private View mDecorView;
    private String[] mTitles_2 = {"5分钟", "5小时", "日线"};
    private SegmentTabLayout stl_tab1;
    private PageSlidingTableView slidingTableView;
    private LineChart linechart;
    private String max, min;
    private BalanceMoneyBuyFragment balanceMoneyBuyFragment = new BalanceMoneyBuyFragment();
    private BalanceMoneySaleFragment balanceMoneySaleFragment = new BalanceMoneySaleFragment();
    private FrameLayout fl_content;
    private Fragment currentFragment = new Fragment();//（全局）
    private Fragment fivecurrentFragment = new Fragment();//（全局）
    private FiveMinuteFragment fiveMinuteFragment = new FiveMinuteFragment();
    private FiveHourFragment fiveHourFragment = new FiveHourFragment();
    private DayLineFragment dayLineFragment = new DayLineFragment();
    private int stateBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecorView = getWindow().getDecorView();
        setContentView(R.layout.num_asset_apay_layout);
        setHalfTransparent();
        setFitSystemWindow(false);
        stateBar = getStateBar();
        Intent intent = getIntent();
        max = intent.getStringExtra("max");
        min = intent.getStringExtra("min");
        initView();
        requestData();
        initListener();
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
                       if ("0".equals(numAssetApayBean.getCode())){
                           tv_hight.setText(max);
                           tv_low.setText(min);
                           tv_num_asset.setText(numAssetApayBean.getData().getGold());
                           tv_balance_money.setText(numAssetApayBean.getData().getMoney());
                           tv_current_price.setText(numAssetApayBean.getData().getJiaoyi());
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

    private void initView() {
        RelativeLayout ll_title = getViewById(R.id.rv_title);
        ll_title.setPadding(0,stateBar,0,0);
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("Apay币");
        tv_num_asset = getViewById(R.id.tv_num_asset);
        tv_balance_money = getViewById(R.id.tv_balance_money);
        tv_current_price = getViewById(R.id.tv_current_price);
        tv_hight = getViewById(R.id.tv_hight);
        tv_low = getViewById(R.id.tv_low);
        ll_issue = getViewById(R.id.ll_issue);
        ll_sale_order = getViewById(R.id.ll_sale_order);
        ll_buy_order = getViewById(R.id.ll_buy_order);
        ll_trade_record = getViewById(R.id.ll_trade_record);
        linechart = getViewById(R.id.line_chart);
        stl_tab = getViewById(R.id.stl_tab);
        fl_change_content = getViewById(R.id.fl_change_content);
        stl_tab1 = ViewFindUtils.find(mDecorView, R.id.stl_tab);
        slidingTableView = getViewById(R.id.pstv_indicator);
        slidingTableView.setTabTitles(new String[]{"余额购买", "余额出售"});
        stl_tab1.setTabData(mTitles_2);

        switchFiveFragment(fiveMinuteFragment).commit();
        stl_tab1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0:
                        switchFiveFragment(fiveMinuteFragment).commit();
                        break;
                    case 1:
                        switchFiveFragment(fiveHourFragment).commit();
                        break;
                    case 2:
                        switchFiveFragment(dayLineFragment).commit();
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        fl_content = getViewById(R.id.fl_content);
        switchFragment(balanceMoneyBuyFragment).commit();
        slidingTableView.setOnTabClickListener(new PageSlidingTableView.onTabClickListener() {
            @Override
            public void onTabClick(String title, int position) {
                switch (position) {
                    case 0:
                        switchFragment(balanceMoneyBuyFragment).commit();
                        break;
                    case 1:
                        switchFragment(balanceMoneySaleFragment).commit();
                        break;
                }
            }
        });
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        ll_issue.setOnClickListener(this);
        ll_sale_order.setOnClickListener(this);
        ll_buy_order.setOnClickListener(this);
        ll_trade_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_issue:
                startActivity(IssueApayActivity.class);
                break;
            case R.id.ll_sale_order:
                startActivity(SaleOrderActivity.class);
                break;
            case R.id.ll_buy_order:
                startActivity(BuyOrderActivity.class);
                break;
            case R.id.ll_trade_record:
                startActivity(TradeRecordActivity.class);
                break;

        }
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
//第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fl_content, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

    private FragmentTransaction switchFiveFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
//第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (fivecurrentFragment != null) {
                transaction.hide(fivecurrentFragment);
            }
            transaction.add(R.id.fl_change_content, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(fivecurrentFragment).show(targetFragment);
        }
        fivecurrentFragment = targetFragment;
        return transaction;
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }
}
