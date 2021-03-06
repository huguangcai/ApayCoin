package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.TicketRecordBean;

/**
 * 描述： 余额记录的适配器
 * 日期： 2018/11/14 0014 10:56
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BalanceMoneyRecordAdapter extends ListBaseAdapter<TicketRecordBean.DataBean> {
    public BalanceMoneyRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.balance_money_record_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TicketRecordBean.DataBean dataBean = mDataList.get(position);
        TextView tv_input_out = holder.getView(R.id.tv_input_out);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_balance_ticket = holder.getView(R.id.tv_balance_ticket);
        tv_time.setText(dataBean.getAdd_time());
        tv_balance_ticket.setText(dataBean.getMoney());
        /**
         * 	1转入 2转出 3兑换的 4释放 5购买gold 6 购物product 7确认收货
         */
        switch (dataBean.getFlag()) {
            case "1":
                tv_input_out.setText("转入");
                tv_balance_ticket.setTextColor(mContext.getResources().getColor(R.color.black));
                break;
            case "2":
                tv_input_out.setText("转出");
                tv_balance_ticket.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                break;
            case "3":
                tv_input_out.setText("兑换获得");
                tv_balance_ticket.setTextColor(mContext.getResources().getColor(R.color.black));
                break;
            case "4":
                tv_input_out.setText("释放减少");
                tv_balance_ticket.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                break;
            case "5":
                tv_input_out.setText("购买金币");
                tv_balance_ticket.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                break;
            case "6":
                tv_input_out.setText("购买商品");
                tv_balance_ticket.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                break;
            case "7":
                tv_input_out.setText("确认收货");
                tv_balance_ticket.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                break;
        }
    }
}
