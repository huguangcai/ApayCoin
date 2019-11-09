package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.MallGoodsDetialHeaderBean;

/**
 * 描述： TODO ListBaseAdapter
 * 日期： 2018/11/26 0026 11:11
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class MallGoodsDetialAdapter extends ListBaseAdapter<MallGoodsDetialHeaderBean.DataBean> {

    public MallGoodsDetialAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.goods_detail_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MallGoodsDetialHeaderBean.DataBean dataBean = mDataList.get(position);
        TextView tv_content = holder.getView(R.id.tv_content);
        ImageView img_goods = holder.getView(R.id.img_goods);
    }
}
