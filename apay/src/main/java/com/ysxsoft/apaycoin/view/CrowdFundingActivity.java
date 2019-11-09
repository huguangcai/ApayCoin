package com.ysxsoft.apaycoin.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.fragment.CrowdfundingRunFragment;
import com.ysxsoft.apaycoin.fragment.CrowfundingCompleteFragment;
import com.ysxsoft.apaycoin.fragment.TostartFragment;
import com.ysxsoft.apaycoin.widget.OnTabSelectListener;
import com.ysxsoft.apaycoin.widget.SegmentTabLayout;
import com.ysxsoft.apaycoin.widget.ViewFindUtils;

/**
 * 描述： 众筹界面
 * 日期： 2018/11/15 0015 17:10
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class CrowdFundingActivity extends BaseActivity {
    private String[] mTitles_2 = {"待开始", "运行中", "已完成"};
    private View mDecorView;
    private SegmentTabLayout stl_tab1;
    private View img_back;
    private FrameLayout fl_content;
    private Fragment currentFragment = new Fragment();//（全局）
    private TostartFragment tostartFragment = new TostartFragment();
    private CrowdfundingRunFragment runFragment = new CrowdfundingRunFragment();
    private CrowfundingCompleteFragment crowfundingCompleteFragment = new CrowfundingCompleteFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecorView = getWindow().getDecorView();
        setContentView(R.layout.crowd_funding_layout);
        initView();
        initListener();
    }

    private void initListener() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("众筹");
        stl_tab1 = ViewFindUtils.find(mDecorView, R.id.stl_tab);
        fl_content = getViewById(R.id.fl_content);
        stl_tab1.setTabData(mTitles_2);
        switchFragment(tostartFragment).commit();
        stl_tab1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0:
                        switchFragment(tostartFragment).commit();
                        break;

                    case 1:
                        switchFragment(runFragment).commit();
                        break;

                    case 2:
                        switchFragment(crowfundingCompleteFragment).commit();
                        break;
                }

            }

            @Override
            public void onTabReselect(int position) {

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
