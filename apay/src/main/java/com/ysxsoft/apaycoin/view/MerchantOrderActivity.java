package com.ysxsoft.apaycoin.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.fragment.MerchantOrderCompleteFragment;
import com.ysxsoft.apaycoin.fragment.MerchantOrderRunFragment;
import com.ysxsoft.apaycoin.widget.PageSlidingTableView;

public class MerchantOrderActivity extends BaseActivity {

    private PageSlidingTableView slidingTableView;
    private FrameLayout fl_content;
    private Fragment currentFragment = new Fragment();//（全局）
    private MerchantOrderRunFragment runFragment = new MerchantOrderRunFragment();
    private MerchantOrderCompleteFragment completeFragment = new MerchantOrderCompleteFragment();
    private String checkfahuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order);
        Intent intent = getIntent();
        checkfahuo = intent.getStringExtra("checkfahuo");
        View img_back = getViewById(R.id.img_back);
        slidingTableView = getViewById(R.id.pstv_indicator);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("商家订单");
        fl_content = findViewById(R.id.fl_content);
        slidingTableView.setTabTitles(new String[]{"运行中", "已完成"});
//        if ("checkfahuo".equals(checkfahuo)) {
//            switchFragment(completeFragment).commit();
//        } else {
        switchFragment(runFragment).commit();
//        }
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

    @Override
    protected void onResume() {
        super.onResume();

    }
}
