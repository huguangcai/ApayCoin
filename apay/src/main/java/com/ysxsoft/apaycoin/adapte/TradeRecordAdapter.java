package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.TradeRecordBean;

/**
 * 描述： 交易记录适配器
 * 日期： 2018/11/19 0019 17:40
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class TradeRecordAdapter extends ListBaseAdapter<TradeRecordBean.DataBean> {
    public TradeRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.transaction_record_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TradeRecordBean.DataBean dataBean = mDataList.get(position);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_num = holder.getView(R.id.tv_num);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_sale_buy = holder.getView(R.id.tv_sale_buy);
        tv_name.setText(dataBean.getNickname());
        tv_num.setText(dataBean.getGold());
        tv_time.setText(dataBean.getAdd_time());
        if ("购买".equals(dataBean.getType())){
            tv_sale_buy.setText("购买");
        }else {
            tv_sale_buy.setText("出售");
        }
    }
}
