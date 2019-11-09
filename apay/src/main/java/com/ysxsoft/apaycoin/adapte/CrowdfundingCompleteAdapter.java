package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.CrowdingFundingBean;

/**
 * 描述：众筹完成适配器
 * 日期： 2018/11/21 0021 09:38
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class CrowdfundingCompleteAdapter extends ListBaseAdapter<CrowdingFundingBean.DataBean> {
    public CrowdfundingCompleteAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.crowd_funding_complete_fragment_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        CrowdingFundingBean.DataBean dataBean = mDataList.get(position);
        TextView tv_end_time = holder.getView(R.id.tv_end_time);
        TextView tv_balance_money = holder.getView(R.id.tv_balance_money);
        TextView tv_apay_price = holder.getView(R.id.tv_apay_price);
        TextView tv_num = holder.getView(R.id.tv_num);
        TextView tv_surplus_num = holder.getView(R.id.tv_surplus_num);
        TextView tv_limit_num = holder.getView(R.id.tv_limit_num);
        TextView tv_money_type = holder.getView(R.id.tv_money_type);

        tv_end_time.setText(dataBean.getEnd_date());
        tv_balance_money.setText(dataBean.getMoney());
        tv_apay_price.setText(dataBean.getMoney());
        tv_num.setText(dataBean.getFx_num());
        tv_surplus_num.setText(dataBean.getSy_num());
        tv_limit_num.setText(dataBean.getXiangou());

    }
}
