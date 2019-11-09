package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.RollInputRecordBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.widget.CircleImageView;

/**
 * 描述： 转入记录适配器
 * 日期： 2018/11/13 0013 15:56
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class RollInputRecordAdapter extends ListBaseAdapter<RollInputRecordBean.DataBean> {

    public RollInputRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.roll_input_record_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        RollInputRecordBean.DataBean dataBean = mDataList.get(position);
        TextView tv_nike_name = holder.getView(R.id.tv_nike_name);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_balance_money = holder.getView(R.id.tv_balance_money);
        CircleImageView img_head = holder.getView(R.id.img_head);
        tv_nike_name.setText(dataBean.getNickname());
        tv_time.setText(dataBean.getAdd_time());
        tv_balance_money.setText("+"+dataBean.getMoney());
        ImageLoadUtil.GlideImageLoad(mContext,dataBean.getAvatar(),img_head);
    }


}
