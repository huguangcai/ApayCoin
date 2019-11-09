package com.ysxsoft.apaycoin.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.FragmentAdapter;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.fragment.CompleteFragment;
import com.ysxsoft.apaycoin.fragment.RunFragment;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.widget.PageSlidingTableView;

/**
 * 描述： 我的订单界面
 * 日期： 2018/10/26 0026 15:01
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class MyOrderActivity extends BaseActivity {

    private PageSlidingTableView slidingTableView;
    private FrameLayout fl_content;
    private Fragment currentFragment = new Fragment();//（全局）
    private RunFragment runFragment = new RunFragment();
    private CompleteFragment completeFragment = new CompleteFragment();
    private View img_back;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.setCustomDensity(this, getApplication());
        setContentView(R.layout.my_order);
        Intent intent = getIntent();
        String check = intent.getStringExtra("check");

        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("我的订单");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        slidingTableView = findViewById(R.id.pstv_indicator);
        fl_content = findViewById(R.id.fl_content);
        slidingTableView.setTabTitles(new String[]{"运行中", "已完成"});
        if ("check".equals(check)){
            switchFragment(completeFragment).commit();
        }else {
            switchFragment(runFragment).commit();
        }
//        slidingTableView.setViewPage(vp_content, new FragmentAdapter(getSupportFragmentManager(), new Fragment[]{new RunFragment(), new CompleteFragment(),}));
        slidingTableView.setOnTabClickListener(new PageSlidingTableView.onTabClickListener() {
            @Override
            public void onTabClick(String title, int position) {
                switch (position) {
                    case 0:
                        switchFragment(runFragment).commit();
                        break;
                    case 1:
                        switchFragment(completeFragment).commit();
                        break;
                }
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
