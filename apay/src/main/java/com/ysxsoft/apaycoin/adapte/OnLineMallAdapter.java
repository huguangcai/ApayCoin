package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.OnlineMallGoodsListBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;

public class OnLineMallAdapter extends ListBaseAdapter<OnlineMallGoodsListBean.DataBean> {
    public OnLineMallAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.online_mall_fragment_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        OnlineMallGoodsListBean.DataBean dataBean = mDataList.get(position);
        ImageView img_goods = holder.getView(R.id.img_goods);
        TextView tv_desc = holder.getView(R.id.tv_desc);
        TextView tv_price = holder.getView(R.id.tv_price);
        ImageLoadUtil.NewGoodsGlideImageLoad(mContext,dataBean.getIcon(),img_goods);
        tv_desc.setText(AppUtil.stringReplace(dataBean.getTitle()));
        tv_price.setText(dataBean.getMoney());
    }
}
