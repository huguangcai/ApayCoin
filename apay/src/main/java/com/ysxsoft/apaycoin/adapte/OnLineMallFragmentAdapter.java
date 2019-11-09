package com.ysxsoft.apaycoin.adapte;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ysxsoft.apaycoin.modle.GoodsTypeBean;
import com.ysxsoft.apaycoin.modle.OnlineMallGoodsListBean;

import java.util.ArrayList;

public class OnLineMallFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<GoodsTypeBean.DataBean> data;
    private ArrayList<Fragment> fragments;

    public OnLineMallFragmentAdapter(FragmentManager fm, ArrayList<GoodsTypeBean.DataBean> data, ArrayList<Fragment> fragments) {
        super(fm);
        this.data = data;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getTitle();
    }
    @Override
    public int getCount() {
        return fragments.size();
    }
}
