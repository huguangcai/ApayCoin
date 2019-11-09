package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.OnlineMallGoodsListBean;

public class SearchDataAdapter extends ListBaseAdapter<OnlineMallGoodsListBean.DataBean> {

    public SearchDataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.search_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        OnlineMallGoodsListBean.DataBean dataBean = mDataList.get(position);
        TextView tv_name = holder.getView(R.id.tv_name);
        tv_name.setText(dataBean.getTitle());
    }
}
