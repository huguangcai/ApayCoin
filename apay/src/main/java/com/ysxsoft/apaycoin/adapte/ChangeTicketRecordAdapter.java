package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.ChangeTicketRecordBean;

/**
 * 描述： 兑换券记录适配器
 * 日期： 2018/11/14 0014 11:47
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ChangeTicketRecordAdapter extends ListBaseAdapter<ChangeTicketRecordBean.DataBean> {
    public ChangeTicketRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.exchange_record_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ChangeTicketRecordBean.DataBean dataBean = mDataList.get(position);
        TextView tv_balance_money = holder.getView(R.id.tv_balance_money);
        TextView tv_change_ticket = holder.getView(R.id.tv_change_ticket);
        TextView tv_time = holder.getView(R.id.tv_time);
        tv_balance_money.setText(dataBean.getMoney());
        tv_change_ticket.setText(dataBean.getQuans());
        tv_time.setText(dataBean.getAdd_time());
    }
}
