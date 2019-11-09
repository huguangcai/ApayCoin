package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.MyOrderRunCompleteBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;

public class MyOrderRunAdapter extends ListBaseAdapter<MyOrderRunCompleteBean.DataBean> {

    public MyOrderRunAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_order_running_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MyOrderRunCompleteBean.DataBean dataBean = mDataList.get(position);
        TextView tv_mall_name = holder.getView(R.id.tv_mall_name);
        TextView tv_time = holder.getView(R.id.tv_time);
        ImageView img_icon = holder.getView(R.id.img_icon);
        TextView tv_desc = holder.getView(R.id.tv_desc);
        TextView tv_money = holder.getView(R.id.tv_money);
        TextView tv_is_fahuo = holder.getView(R.id.tv_is_fahuo);
        TextView tv_num = holder.getView(R.id.tv_num);
        TextView tv_check = holder.getView(R.id.tv_check);
        String shopname = dataBean.getShopname();
        tv_mall_name.setText(dataBean.getShopname());
        tv_time.setText(dataBean.getAdd_time());
        ImageLoadUtil.GlideImageLoad(mContext, dataBean.getIcon(), img_icon);
        tv_desc.setText(dataBean.getTitle());
        tv_money.setText(dataBean.getMoney());
        tv_num.setText(dataBean.getNum());
        switch (dataBean.getFlag()) {
            case "0":
                tv_is_fahuo.setText("待发货");
                tv_check.setVisibility(View.GONE);
                break;
            case "1":
                tv_is_fahuo.setText("已发货");
                tv_check.setVisibility(View.VISIBLE);
                break;
        }


    }
}
