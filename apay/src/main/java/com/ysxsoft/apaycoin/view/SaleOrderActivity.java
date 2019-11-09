package com.ysxsoft.apaycoin.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.fragment.SaleOrderCompleteFragment;
import com.ysxsoft.apaycoin.fragment.SaleOrderRunFragment;
import com.ysxsoft.apaycoin.widget.PageSlidingTableView;

/**
 * 描述： 出售订单
 * 日期： 2018/11/15 0015 17:42
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class SaleOrderActivity extends BaseActivity {
    private PageSlidingTableView slidingTableView;
    private ViewPager vp_content;
    private TextView tv_title;
    private SaleOrderRunFragment saleOrderRunFragment = new SaleOrderRunFragment();
    private SaleOrderCompleteFragment saleOrderCompleteFragment = new SaleOrderCompleteFragment();
    private FrameLayout fl_content;
    private Fragment currentFragment = new Fragment();//（全局）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_order_layout);
        View img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("出售订单");
        slidingTableView = findViewById(R.id.pstv_indicator);
        vp_content = findViewById(R.id.vp_content);
        slidingTableView.setTabTitles(new String[]{"运行中", "已完成"});
//        slidingTableView.setViewPage(vp_content, new FragmentAdapter(getSupportFragmentManager(),
//                new Fragment[]{new SaleOrderRunFragment(), new SaleOrderCompleteFragment(),}));
        fl_content = getViewById(R.id.fl_content);
        switchFragment(saleOrderRunFragment).commit();
        slidingTableView.setOnTabClickListener(new PageSlidingTableView.onTabClickListener() {
            @Override
            public void onTabClick(String title, int position) {
                switch (position) {
                    case 0:
                        switchFragment(saleOrderRunFragment).commit();
                        break;
                    case 1:
                        switchFragment(saleOrderCompleteFragment).commit();
                        break;
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
}
