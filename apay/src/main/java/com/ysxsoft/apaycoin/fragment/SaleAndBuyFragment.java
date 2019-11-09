package com.ysxsoft.apaycoin.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ysxsoft.apaycoin.R;

/**
 * 描述： 销售和购买的fragment
 * 日期： 2018/10/30 0030 10:28
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
@Deprecated
public class SaleAndBuyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.sale_buy_fragment_layout, null);
        return inflate;
    }
}
