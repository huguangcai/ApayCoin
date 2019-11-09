package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.ChangeTicketRecordBean;

import java.util.ArrayList;

/**
 * 描述： 兑换券的Adapter
 * 日期： 2018/11/5 0005 16:13
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ExchangeRecordAdapter extends ListBaseAdapter<ChangeTicketRecordBean.DataBean> {

    private Context mContext;
    private ArrayList<Object> objects;

    public ExchangeRecordAdapter(Context context) {
        super(context);
    }


    @Override
    public int getLayoutId() {
        return R.layout.exchange_record_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView  tv_balance_money = holder.getView(R.id.tv_balance_money);
        TextView  tv_change_ticket = holder.getView(R.id.tv_change_ticket);
        TextView  tv_time = holder.getView(R.id.tv_time);

    }

}
