package com.ysxsoft.apaycoin.adapte;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 描述： 指示器fagment的适配器
 * 日期： 2018/10/26 0026 15:09
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments;

    public FragmentAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
